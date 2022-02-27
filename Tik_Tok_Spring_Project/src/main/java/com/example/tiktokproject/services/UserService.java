package com.example.tiktokproject.services;

import com.example.tiktokproject.controller.SessionManager;
import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.postDTO.PostLikedDTO;
import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import com.example.tiktokproject.model.dto.userDTO.*;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.Token;
import com.example.tiktokproject.model.pojo.TypeMapperClass;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.PostRepository;
import com.example.tiktokproject.model.repository.TokenRepository;
import com.example.tiktokproject.model.repository.UserRepository;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    public static final int CREATOR_ROLE_ID = 2;
    public static final int DEFAULT_ROLE_ID = 1;
    private static final long MAX_UPLOAD_SIZE = 250 * 1024 * 1024;
    private static final String UPLOAD_PHOTO_FOLDER = "photos";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenRepository tokenRepository;

    public UserLoginResponseWithEmailDTO loginWithEmail(UserLoginWithEmailDTO user) {
        String email = user.getEmail();
        String password = user.getPassword();
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new NotFoundException("Wrong email or password!");
        }
        if (!passwordEncoder.matches(password, userRepository.findByEmail(email).get().getPassword())) {
            throw new NotFoundException("Wrong email or password!");
        }
        User u = userRepository.findByEmail(email).get();
        return modelMapper.map(u, UserLoginResponseWithEmailDTO.class);
    }

    public UserRegisterResponseWithEmailDTO registerWithEmail(UserRegisterRequestWithEmailDTO userEmailDTO) {
        if (userRepository.findByEmail(userEmailDTO.getEmail()).isPresent()) {
            throw new BadRequestException("User with this email already exist");
        }
        checkForInValidPasswordAndDateOfBirth(userEmailDTO.getPassword(),
                userEmailDTO.getConfirmPassword(),
                userEmailDTO.getDateOfBirth());
        User user = modelMapper.map(userEmailDTO, User.class);
        user.setPassword(passwordEncoder.encode(userEmailDTO.getPassword()));
        user.setRoleId(1);
        user.setRegisterDate(LocalDateTime.now());
        userRepository.save(user);
        new Thread(() -> emailService.sendSimpleMessage(user, EmailService.REGISTRATION_BODY, EmailService.REGISTRATION_TOPIC)).start();
        return modelMapper.map(user, UserRegisterResponseWithEmailDTO.class);
    }

    @Transactional(rollbackForClassName = "SQLException.class")
    public void verifyEmail(String token, User user) {
        Token t = tokenRepository.getByToken(token).orElseThrow(() -> new NotFoundException("Token not found."));
        if (user.getId() != t.getOwner().getId()) {
            throw new BadRequestException("You are not the owner of this token.");
        }
        if (t.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token expiry date is expired.");
        }
        user.setVerified(true);
        userRepository.save(user);
        tokenRepository.delete(t);
    }

    public void sendEmailForForgottenPassword(UserForgottenPasswordRequestDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new NotFoundException("User not found."));
        new Thread(() -> emailService.sendSimpleMessage(user, EmailService.PASSWORD_BODY, EmailService.PASSWORD_TOPIC)).start();
    }

    public UserEditResponseDTO editUser(UserEditRequestDTO userEditDTO) {
        boolean hasChangePassword = false;
        User oldUser = userRepository.findById(userEditDTO.getId()).orElseThrow(() -> new NotFoundException("User not found."));
        if (userEditDTO.getEmail() != null) {
            if (checkForInvalidEmail(userEditDTO.getEmail())) {
                throw new BadRequestException("Invalid email address");
            }
            if (userRepository.findByEmail(userEditDTO.getEmail()).isPresent()) {
                throw new BadRequestException("User with that email already exist");
            }
        }
        if (userEditDTO.getUsername() != null) {
            if (userRepository.findByUsername(userEditDTO.getUsername()).isPresent()) {
                throw new BadRequestException("Username already exist");
            }
        }
        if (userEditDTO.getName() != null) {
            if (userEditDTO.getName().isBlank()) {
                throw new BadRequestException("Name is mandatory");
            }
        }
        if (userEditDTO.getPhoneNumber() != null) {
            if (checkForInvalidPhoneNumber(userEditDTO.getPhoneNumber())) {
                throw new BadRequestException("Invalid phone number");
            }
        }
        if (userEditDTO.getPassword() != null) {
            if (userEditDTO.getNewPassword() != null) {
                if (!passwordEncoder.matches(userEditDTO.getPassword(), oldUser.getPassword())) {
                    throw new BadRequestException("Wrong old password");
                }
                if (checkForInvalidPassword(userEditDTO.getNewPassword())) {
                    throw new BadRequestException("Wrong password credentials");
                }
                if (passwordEncoder.matches(userEditDTO.getNewPassword(), oldUser.getPassword())) {
                    throw new BadRequestException("New password must be different");
                }
                if (!userEditDTO.getNewPassword().equals(userEditDTO.getConfirmNewPassword())) {
                    throw new BadRequestException("Password miss match");
                }
                hasChangePassword = true;
            }
        }
        TypeMapperClass.getInstance().map(userEditDTO, oldUser);
        if (hasChangePassword) {
            oldUser.setPassword(passwordEncoder.encode(userEditDTO.getNewPassword()));
        }
        userRepository.save(oldUser);
        return modelMapper.map(oldUser, UserEditResponseDTO.class);
    }


    public UserEditProfilePictureResponseDTO editProfilePicture(MultipartFile file, int userId) {
        Tika tika = new Tika();
        String realFileExtension = null;
        try {
            realFileExtension = tika.detect(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file.getSize() > MAX_UPLOAD_SIZE) {
            throw new BadRequestException("Too big photo size. The maximum photo size is 50MB.");
        }
        if (!("image/png".equalsIgnoreCase(realFileExtension)) && !("image/jpeg".equalsIgnoreCase(realFileExtension))) {
            throw new BadRequestException("Wrong photo format.You can upload only .png or .jpg.");
        }
        String extension;
        if ("image/png".equalsIgnoreCase(realFileExtension)) {
            extension = "png";
        } else {
            extension = "jpg";
        }
        String fileName = UUID.randomUUID() + "." + extension;
        User oldUser;
        try {
            oldUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
            String userPhotoName = oldUser.getPhotoUrl();
            if (userPhotoName != null) {
                String photoPath = UPLOAD_PHOTO_FOLDER + File.separator + userPhotoName;
                Files.delete(Path.of(photoPath));
            }
            Files.copy(file.getInputStream(), new File(UPLOAD_PHOTO_FOLDER + File.separator + fileName).toPath());
        } catch (IOException e) {
            throw new com.example.tiktokproject.exceptions.IOException("Server file system error.");
        }
        oldUser.setPhotoUrl(fileName);
        userRepository.save(oldUser);
        return modelMapper.map(oldUser, UserEditProfilePictureResponseDTO.class);
    }

    public UserInformationDTO getUserByUsername(String username, int pageNumber, int rowsNumber) {
        if (username.trim().isEmpty()) {
            throw new BadRequestException("Username is mandatory");
        }
        User u = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Wrong username"));
        UserInformationDTO userInformationDTO = modelMapper.map(u, UserInformationDTO.class);
        userInformationDTO.setNumberOfFollowers(u.getFollowers().size());
        userInformationDTO.setNumberOfFollowerTo(u.getFollowerTo().size());

        Pageable page = PageRequest.of(pageNumber, rowsNumber);
        List<Post> userPosts = postRepository.findAllPostsByOwnerId(u.getId(), page);
        for (Post p : userPosts) {
            PostWithoutOwnerDTO postDTO = modelMapper.map(p, PostWithoutOwnerDTO.class);
            postDTO.setPostHaveComments(p.getPostComments().size());
            postDTO.setPostHaveLikes(p.getPostLikes().size());
            userInformationDTO.addPost(postDTO);
        }
        return userInformationDTO;
    }

    public void follow(User follower, int id) {
        User star = userRepository.findById(id).orElseThrow(() -> new NotFoundException("No such user to follow"));
        if (follower.getId() == star.getId()) {
            throw new BadRequestException("You can't follow yourself");
        }
        if (star.getFollowers().contains(follower)) {
            throw new BadRequestException("You are already follow that person");
        }
        star.addFollower(follower);
        userRepository.save(star);
    }

    public void unfollow(User userWhoWantToUnfollow, int unfollowedUserId) {
        User unfollowedUser = userRepository.findById(unfollowedUserId).orElseThrow(() -> new NotFoundException("No such user to unfollow"));
        if (userWhoWantToUnfollow.getId() == unfollowedUser.getId()) {
            throw new BadRequestException("You can't unfollow yourself");
        }
        if (!unfollowedUser.getFollowers().contains(userWhoWantToUnfollow)) {
            throw new BadRequestException("You are already not follow that person");
        }
        unfollowedUser.removeFollower(userWhoWantToUnfollow);
        userRepository.save(unfollowedUser);
    }

    public List<PostLikedDTO> getAllLikedPosts(int id, int pageNumber, int rowsNumber) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found user"));
        Pageable page = PageRequest.of(pageNumber, rowsNumber, Sort.by("views").descending());
        List<Post> postsLiked = postRepository.findPostsByPostLikesContains(user, page);
        List<PostLikedDTO> postsLikedList = new ArrayList<>();
        for (Post post : postsLiked) {
            PostLikedDTO postLiked = modelMapper.map(post, PostLikedDTO.class);
            postLiked.setViews(post.getViews());
            postsLikedList.add(postLiked);
        }
        return postsLikedList;
    }

    public UserSetUsernameDTO setUsername(int id, String username, String name) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found user"));
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BadRequestException("This username is already used");
        }
        if (username.trim().isEmpty() || username.contains(" ")) {
            throw new BadRequestException("Wrong username format");
        }
        user.setUsername(username);
        if (name == null || name.trim().isEmpty()) {
            user.setName(username);
        } else {
            user.setName(name);
        }
        userRepository.save(user);
        return modelMapper.map(user, UserSetUsernameDTO.class);
    }

    public List<UserUsernameDTO> getAllUsersByUsername(String search, int pageNumber, int rowsNumber) {
        if (search.trim().isEmpty()) {
            throw new BadRequestException("Search field is mandatory");
        }
        search = "%" + search + "%";
        Pageable page = PageRequest.of(pageNumber, rowsNumber);
        List<User> users = userRepository.findBySearch(search, page);
        List<UserUsernameDTO> responseUsers = new ArrayList<>();
        for (User u : users) {
            UserUsernameDTO user = modelMapper.map(u, UserUsernameDTO.class);
            responseUsers.add(user);
        }
        return responseUsers;
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }


    public void forgottenPassword(String token) {
        Token t = tokenRepository.getByToken(token).orElseThrow(() -> new NotFoundException("Token not found."));
        if (t.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token expiry date is expired");
        }
        tokenRepository.delete(t);
    }

    public UserEditResponseDTO validateNewPassword(UserForgottenPasswordDTO userDto, User user) {
        if (!userDto.getNewPassword().equals(userDto.getConfirmNewPassword())) {
            throw new BadRequestException("Password and confirm password should be equals");
        }
        user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
        userRepository.save(user);
        return modelMapper.map(user, UserEditResponseDTO.class);
    }

    public void changeUserRole(User user) {
        user.setRoleId(CREATOR_ROLE_ID);
        userRepository.save(user);
    }

    public void setLastLoginAttempt(int userId) {
        User u = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        u.setLastLoginAttempt(LocalDateTime.now());
        userRepository.save(u);
    }

    public int getUserIdByToken(String token) {
        return tokenRepository.getByToken(token).orElseThrow(() -> new NotFoundException("Token not found")).getOwner().getId();
    }

    private boolean checkForInvalidPassword(String password) {
        return !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}");
    }

    private boolean checkForInvalidEmail(String email) {
        return !email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+.[a-zA-Z]+$");
    }

    private boolean checkForInvalidPhoneNumber(String phoneNumber) {
        return !phoneNumber.matches("^[+]3598[7-9][0-9]{7}$");
    }

    private void checkForInValidPasswordAndDateOfBirth(String password, String confirmPassword, LocalDate localDate) {
        if (!password.equals(confirmPassword)) {
            throw new BadRequestException("Password and confirm password should be equals");
        }
        if (localDate.isAfter(LocalDate.now().minusYears(13))) {
            throw new UnauthorizedException("You should be at least 13 years old");
        }
    }
}


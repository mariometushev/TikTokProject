package com.example.tiktokproject.services;

import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.postDTO.PostLikedDTO;
import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import com.example.tiktokproject.model.dto.userDTO.*;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.TypeMapperClass;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.PostRepository;
import com.example.tiktokproject.model.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

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


    public UserLoginResponseWithPhoneDTO loginWithPhone(UserLoginWithPhoneDTO user) {
        String phone = user.getPhoneNumber();
        String password = user.getPassword();
        if (userRepository.findByPhoneNumber(phone).isEmpty()) {
            throw new NotFoundException("Wrong phone number or password!");
        }
        if (!passwordEncoder.matches(password, userRepository.findByPhoneNumber(phone).get().getPassword())) {
            throw new NotFoundException("Wrong phone number or password!");
        }
        User u = userRepository.findByPhoneNumber(phone).get();
        return modelMapper.map(u, UserLoginResponseWithPhoneDTO.class);
    }

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
        emailService.sendSimpleMessage(user.getEmail(), user.getRegisterDate());
        return modelMapper.map(user, UserRegisterResponseWithEmailDTO.class);
    }

    public UserRegisterResponseWithPhoneDTO registerWithPhone(UserRegisterRequestWithPhoneDTO userPhoneDTO) {
        if (userRepository.findByPhoneNumber(userPhoneDTO.getPhoneNumber()).isPresent()) {
            throw new BadRequestException("User with this phone already exist");
        }
        checkForInValidPasswordAndDateOfBirth(userPhoneDTO.getPassword(),
                userPhoneDTO.getConfirmPassword(),
                userPhoneDTO.getDateOfBirth());
        User u = modelMapper.map(userPhoneDTO, User.class);
        u.setPassword(passwordEncoder.encode(userPhoneDTO.getPassword()));
        u.setRoleId(1);
        u.setRegisterDate(LocalDateTime.now());
        userRepository.save(u);
        emailService.sendSimpleMessage(u.getEmail(), u.getRegisterDate());
        return modelMapper.map(u, UserRegisterResponseWithPhoneDTO.class);
    }

    public String verifyEmail(String token, User user) {
        String checkToken = emailService.generateToken(user.getEmail(), user.getRegisterDate());
        if (checkToken.equals(token)) {
            user.setVerified(true);
            userRepository.save(user);
            return "Email verified.";
        } else throw new BadRequestException("Invalid validation token");
    }

    public UserEditResponseDTO editUser(UserEditRequestDTO userEditDTO) {
        boolean hasChangePassword = false;
        User oldUser = userRepository.findById(userEditDTO.getId()).orElseThrow(() -> new NotFoundException("Not found user"));
        if (userEditDTO.getRoleId() < 1 || userEditDTO.getRoleId() > 3) {
            throw new BadRequestException("No such role");
        }
        if (userEditDTO.getEmail() != null) {
            if (checkForInvalidEmail(userEditDTO.getEmail())) {
                throw new BadRequestException("Invalid email address");
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
        if (userEditDTO.getDescription() != null) {
            if (userEditDTO.getDescription().length() > 150) {
                throw new BadRequestException("Description is too long");
            }
        }
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
        TypeMapperClass.getInstance().map(userEditDTO, oldUser);
        if (hasChangePassword) {
            oldUser.setPassword(passwordEncoder.encode(userEditDTO.getNewPassword()));
        }
        userRepository.save(oldUser);
        return modelMapper.map(oldUser, UserEditResponseDTO.class);
    }

    public UserEditProfilePictureResponseDTO editProfilePicture(MultipartFile file, int userId) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = System.nanoTime() + "." + extension;
        if (file.getSize() > MAX_UPLOAD_SIZE) {
            throw new BadRequestException("Too big photo size. The maximum photo size is 250MB.");
        }
        if (!("jpg".equals(extension)) && !("png".equals(extension))) {
            throw new BadRequestException("Wrong photo format.You can upload only .png or .jpg.");
        }
        try {
            Files.copy(file.getInputStream(), new File(UPLOAD_PHOTO_FOLDER + File.separator + fileName).toPath());
        } catch (IOException e) {
            throw new com.example.tiktokproject.exceptions.IOException("Server file system error.");
        }
        User oldUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Cannot find user with that id"));
        oldUser.setPhotoUrl(fileName);
        userRepository.save(oldUser);
        return modelMapper.map(oldUser, UserEditProfilePictureResponseDTO.class);
    }

    public UserInformationDTO getUserByUsername(String username) {
        User u = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Wrong username"));
        UserInformationDTO userInformationDTO = modelMapper.map(u, UserInformationDTO.class);
        userInformationDTO.setNumberOfFollowers(u.getFollowers().size());
        userInformationDTO.setNumberOfFollowerTo(u.getFollowerTo().size());
        for (Post p : u.getPosts()) {
            PostWithoutOwnerDTO postDTO = modelMapper.map(p, PostWithoutOwnerDTO.class);
            postDTO.setComments(p.getComments().size());
            postDTO.setPostLikes(p.getPostLikes().size());
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
            throw new BadRequestException("You already subscribe to that person");
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

    public List<PostLikedDTO> getAllLikedPosts(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found user"));
        List<Post> postsLiked = postRepository.findPostsByPostLikesContains(user);
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
        user.setUsername(username);
        if (name == null) {
            user.setName(username);
        } else {
            user.setName(name);
        }
        userRepository.save(user);
        return modelMapper.map(user, UserSetUsernameDTO.class);
    }

    public List<UserUsernameDTO> getAllUsersByUsername(String search) {
        search = "%" + search + "%";
        List<User> users = userRepository
                .findByUsernameLike(search)
                .stream()
                .sorted((u1, u2) -> u2.getFollowers().size() - u1.getFollowers().size())
                .collect(Collectors.toList()).subList(0, 5);
        List<UserUsernameDTO> responseUsers = new ArrayList<>();
        for (User u : users) {
            UserUsernameDTO user = modelMapper.map(u, UserUsernameDTO.class);
            responseUsers.add(user);
        }
        return responseUsers;
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


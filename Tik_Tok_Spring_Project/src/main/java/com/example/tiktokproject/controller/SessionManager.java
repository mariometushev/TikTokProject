package com.example.tiktokproject.controller;

import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.UserRepository;
import com.example.tiktokproject.services.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SessionManager {

    public static final String LOGGED = "logged";
    public static final String LOGGED_FROM = "logged_from";
    public static final String USER_ID = "user_id";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public void validateLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        boolean newSession = session.isNew();
        boolean logged = session.getAttribute(LOGGED) != null && ((Boolean) session.getAttribute(LOGGED));
        boolean sameIP = request.getRemoteAddr().equals(session.getAttribute(LOGGED_FROM));
        boolean userIdIsNull = session.getAttribute(USER_ID) == null;
        boolean isVerified = getSessionUser(session).isVerified();
        if (userIdIsNull || newSession || !logged || !sameIP) {
            throw new UnauthorizedException("You have to log in");
        }
        if (!isVerified) {
            throw new UnauthorizedException("You have to verify your account.");
        }
    }

    public void validateUserId(HttpSession session, int userId) {
        if ((Integer) session.getAttribute(USER_ID) != userId) {
            throw new UnauthorizedException("You are not owner of this account.");
        }
    }

    public boolean isUserLogged(HttpSession session){
        return session.getAttribute(LOGGED) != null;
    }

    public User getSessionUser(HttpSession session) {
        if(session.getAttribute(USER_ID) == null){
            throw new UnauthorizedException("You have to log in");
        }
        int sessionUserId = (int) session.getAttribute(USER_ID);
        return userRepository.findById(sessionUserId).orElseThrow(() -> new NotFoundException("Wrong user id"));
    }

    public void setSession(HttpServletRequest request, int userId) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionManager.LOGGED, true);
        session.setAttribute(SessionManager.LOGGED_FROM, request.getRemoteAddr());
        session.setAttribute(SessionManager.USER_ID, userId);
        userService.setLastLoginAttempt(userId);
    }

    public void setSessionUserId(HttpServletRequest request, int userId){
        HttpSession session = request.getSession();
        session.setAttribute(SessionManager.USER_ID, userId);
    }

}

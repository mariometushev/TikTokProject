package com.example.tiktokproject.controller;

import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.UserRepository;
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

    public void validateLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        boolean newSession = session.isNew();
        boolean logged = session.getAttribute(LOGGED) != null && ((Boolean) session.getAttribute(LOGGED));
        boolean sameIP = request.getRemoteAddr().equals(session.getAttribute(LOGGED_FROM));
        if (newSession || !logged || !sameIP) {
            throw new UnauthorizedException("You have to log in");
        }
    }

    public void validateUserId(HttpSession session, int userId) {
        if ((Integer) session.getAttribute(USER_ID) != userId) {
            throw new UnauthorizedException("You are not owner of this account.");
        }
    }

    public User getSessionUser(HttpSession session) {
        int sessionUserId = (int) session.getAttribute(USER_ID);
        User u = userRepository.findById(sessionUserId).orElseThrow(() -> new NotFoundException("Wrong user id"));
        return u;
    }

    public void setSession(HttpServletRequest request, int userId) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionManager.LOGGED, true);
        session.setAttribute(SessionManager.LOGGED_FROM, request.getRemoteAddr());
        session.setAttribute(SessionManager.USER_ID, userId);
    }
}

package com.example.tiktokproject.controller;

import com.example.tiktokproject.exceptions.UnauthorizedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SessionManager {

    public static final String LOGGED = "logged";
    public static final String LOGGED_FROM = "logged_from";
    public static final String USER_ID = "user_id";

    public void validateLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        boolean newSession = session.isNew();
        boolean logged = session.getAttribute(LOGGED) != null && ((Boolean) session.getAttribute(LOGGED));
        boolean sameIP = request.getRemoteAddr().equals(session.getAttribute(LOGGED_FROM));
        if (newSession || !logged || !sameIP) {
            throw new UnauthorizedException("You have to log");
        }
    }

    public void setSession(HttpServletRequest request, int userId){
        HttpSession session = request.getSession();
        session.setAttribute(SessionManager.LOGGED, true);
        session.setAttribute(SessionManager.LOGGED_FROM, request.getRemoteAddr());
        session.setAttribute(SessionManager.USER_ID, userId);
    }
}

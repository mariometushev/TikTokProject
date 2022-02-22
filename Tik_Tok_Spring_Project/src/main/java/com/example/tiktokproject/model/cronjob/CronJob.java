package com.example.tiktokproject.model.cronjob;

import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.UserRepository;
import com.example.tiktokproject.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CronJob {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 12 * * FRI")//every friday in 12:00
    public void sendEmailsToUsersWhoHaveBeenInactiveLastWeek(){
        LocalDateTime ldt = LocalDateTime.now().minusDays(7);
        List<User> inactiveUsers = userRepository.findAllWhereLastLoginAttemptIsBefore(ldt);
        for(User u : inactiveUsers){
            emailService.sendMessageForInactivity(u);
        }
    }

}

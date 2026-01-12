package com.example.store.fundamentals1;

import org.springframework.stereotype.Service;

@Service("fundumentals1UserService")
public class UserService {
    private UserRepository userRepository;
    private NotificationService notificationService;

    public UserService(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public void registerUser(User user){
        if(this.userRepository.findByEmail(user.getEmail()) != null){
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }

        this.userRepository.save(user);
        this.notificationService.send("You registered successfully!", user.getEmail());
    }

}

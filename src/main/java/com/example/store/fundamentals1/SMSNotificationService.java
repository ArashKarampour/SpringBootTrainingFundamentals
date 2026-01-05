package com.example.store.fundamentals1;

import org.springframework.stereotype.Service;

@Service("sms")
public class SMSNotificationService implements NotificationService{
    @Override
    public void send(String message, String recipientPhone){
        System.out.println("Sending SMS notification: " + message + "to" + recipientPhone);
    }
}

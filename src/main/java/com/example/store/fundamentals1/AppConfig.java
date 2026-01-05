package com.example.store.fundamentals1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${payment-service:stripe}") // default to stripe if not set
    private String paymentService;

    @Bean
    public PaymentService stripe(){
        return new StripePaymentService();
    }

    @Bean
    public PaymentService paypal(){
        return new PaypalPaymentService();
    }

    @Bean
//    @Lazy
    public OrderService orderService(){
        if(paymentService.equals("stripe")){
            return new OrderService(stripe());
        }
        return new OrderService(paypal());
    }


}

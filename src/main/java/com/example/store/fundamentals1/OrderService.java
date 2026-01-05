package com.example.store.fundamentals1;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

//@Service
public class OrderService {
    private PaymentService paymentService;

// @Autowired is optional on constructor in new versions of spring(check the version) if the class has only one constructor
//    @Autowired
//    public OrderService(@Qualifier("paypal") PaymentService paymentService) {
//        this.paymentService = paymentService;
//    }
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // this will be executed after the constructor and after all the dependencies are injected
    @PostConstruct
    public void init(){
        System.out.println("orderService PostConstruct");
    }

    // this will be executed before the bean is destroyed or before the application is shut down
    @PreDestroy
    public void destroy() {
        System.out.println("orderService PreDestroy");
    }

    public void placeOrder(){
        this.paymentService.processPayment(20);
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}

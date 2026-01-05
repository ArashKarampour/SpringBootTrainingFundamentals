package com.example.store.fundamentals1;

//@Service("stripe")
//@Primary
public class StripePaymentService implements PaymentService {
    @Override
    public void processPayment(double amount){
        System.out.println("STRIPE");
        System.out.println("Amount" + amount);
    }
}

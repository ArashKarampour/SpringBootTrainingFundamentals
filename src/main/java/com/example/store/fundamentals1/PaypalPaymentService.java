package com.example.store.fundamentals1;

//@Service("paypal")
public class PaypalPaymentService implements PaymentService {
    @Override
    public void processPayment(double amount){
        System.out.println("PAYPAL");
        System.out.println("Amount" + amount);
    }
}
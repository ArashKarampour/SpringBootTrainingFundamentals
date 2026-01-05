package com.example.store.fundamentals1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    private final OrderService orderService;
    private final NotificationManager notificationManager;
    private final UserService userService;
    private HeavyResource heavyResource;
    @Value("${app.value}") // accessing the application.properties values
    private int appValue;

    @Autowired
    public HomeController(OrderService orderService, NotificationManager notificationManager, UserService userService) {
        this.notificationManager = notificationManager;
        this.orderService = orderService;
        this.userService = userService;
    }

    @RequestMapping("/")
    public String index(){
        return "index.html"; // in order for this to work the file should be under \src\main\resources\static folder
    }
    @RequestMapping("/value")
    public String checkValue(){
        System.out.println(appValue);
        return "index.html";
    }

    @RequestMapping("/order")
    public String order(){
        this.orderService.placeOrder();
        return "index.html";
    }

    @RequestMapping("/notify")
    public String notifyUser(){
        this.notificationManager.sendNotification("Your order has been placed successfully!","recipient");
        return "index.html";
    }

    @RequestMapping("/heavy")
    public String heavy(){
        this.heavyResource = new HeavyResource();
        return "index.html";
    }

    @RequestMapping("/register/{id}/{name}/{email}/{password}")
    public String registerUser(@PathVariable Long id,@PathVariable String name,@PathVariable String email,@PathVariable String password){
        userService.registerUser(new User(id, email, password, name));
        return "redirect:/";
    }
}

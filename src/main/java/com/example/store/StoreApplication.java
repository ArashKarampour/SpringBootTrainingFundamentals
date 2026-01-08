package com.example.store;

import com.example.store.entities.Address;
import com.example.store.entities.Profile;
import com.example.store.entities.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;

@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		var user = new User("arash karampour", "arash.karampour@example.com", "securepassword");
		user.addTag("tag1");
		var address = new Address(1L, "123 Main St", "Springfield", "12345", null);
		user.addAddress(address);
		var profile = Profile.builder() // using builder pattern instead of constructor because there are many optional fields in profile we can use builder pattern to set only the fields we want
						.bio("bio")
						.build();
		profile.setUser(user);
		user.setProfile(profile);

		System.out.println(user);


//		ApplicationContext context =
		SpringApplication.run(StoreApplication.class, args);
//		var orderService = context.getBean(OrderService.class);
//		orderService.placeOrder();
	}

}

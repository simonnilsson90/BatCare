package com.simme.lektion_5_java_ee;

import com.simme.lektion_5_java_ee.models.user.Roles;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.simme.lektion_5_java_ee")
public class Lektion5JavaEeApplication {

	public static void main(String[] args) {

		SpringApplication.run(Lektion5JavaEeApplication.class, args);

		System.out.println(Roles.ADMIN.getAuthorities());
	}

}

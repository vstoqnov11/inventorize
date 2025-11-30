package bg.softuni.My_Inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableMethodSecurity
@EnableWebSecurity
public class MyInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyInventoryApplication.class, args);
	}

}

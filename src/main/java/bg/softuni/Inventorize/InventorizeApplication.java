package bg.softuni.Inventorize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableMethodSecurity
@EnableWebSecurity
public class InventorizeApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorizeApplication.class, args);
	}

}

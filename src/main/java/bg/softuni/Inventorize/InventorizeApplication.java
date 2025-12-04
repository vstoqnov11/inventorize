package bg.softuni.Inventorize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
@EnableFeignClients
public class InventorizeApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorizeApplication.class, args);
	}
}

package bg.softuni.Inventorize.user.property;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.user.model.UserRole;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Data
@Configuration
@ConfigurationProperties(prefix = "users")
public class UserProperties {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private UserRole role;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private Business business;
}

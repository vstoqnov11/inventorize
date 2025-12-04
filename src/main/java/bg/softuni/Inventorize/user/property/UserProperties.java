package bg.softuni.Inventorize.user.property;

import bg.softuni.Inventorize.user.model.UserRole;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "users")
public class UserProperties {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole role;
}

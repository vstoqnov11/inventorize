package bg.softuni.Inventorize.web.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Size(min = 5, max = 16, message = "Incorrect username or password")
    private String username;

    @Size(min = 5, max = 16, message = "Incorrect username or password")
    private String password;
}

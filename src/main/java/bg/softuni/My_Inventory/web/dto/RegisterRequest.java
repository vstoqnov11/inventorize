package bg.softuni.My_Inventory.web.dto;

import bg.softuni.My_Inventory.business.model.BusinessType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 5, max = 16, message = "Username should be between 5 and 16 characters")
    @NotBlank
    private String username;

    @Size(min = 5, max = 16, message = "Password should be between 5 and 16 characters")
    @NotBlank
    private String password;

    @Size(min = 3, max = 20, message = "First name should be between 3 and 20 characters")
    @NotBlank
    private String firstName;

    @Size(min = 3, max = 20, message = "Last name should be between 3 and 3 characters")
    @NotBlank
    private String lastName;

    @Email(message = "Incorrect email address")
    @NotBlank
    private String email;

}

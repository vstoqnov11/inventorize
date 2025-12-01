package bg.softuni.Inventorize.web.dto;

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
public class EditProfileRequest {

    @Size(min = 3, max = 20, message = "First name should be between 3 and 20 characters")
    @NotBlank
    private String firstName;

    @Size(min = 3, max = 20, message = "Last name should be between 3 and 20 characters")
    @NotBlank
    private String lastName;

    @Email(message = "Incorrect email address")
    @NotBlank
    private String email;

    @Size(min = 10, max = 10, message = "Phone number should be 10 digits")
    private String phoneNumber;
}

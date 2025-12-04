package bg.softuni.Inventorize.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditBusinessRequest {

    @Size(min = 5, max = 50, message = "Address should be between 5 and 50 characters long")
    private String address;

    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Email
    private String email;
}

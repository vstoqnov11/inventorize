package bg.softuni.Inventorize.web.dto;

import bg.softuni.Inventorize.business.model.BusinessType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewBusinessRequest {

    @Size(min = 4, max = 30, message = "Name should be between 4 and 30 characters")
    @NotBlank
    private String name;

    @NotNull
    private BusinessType type;

    @Size(min = 5, max = 50, message = "address should be between 5 and 50 characters")
    private String address;

    @Size(min = 10, max = 10, message = "Name should be 10 characters")
    private String phoneNumber;

    @Email(message = "Incorrect email address")
    @NotBlank
    private String email;
}

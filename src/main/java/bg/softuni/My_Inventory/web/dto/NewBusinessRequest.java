package bg.softuni.My_Inventory.web.dto;

import bg.softuni.My_Inventory.business.model.BusinessType;
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

    @Size(min = 4, max = 30)
    @NotBlank
    private String name;

    @NotNull
    private BusinessType type;

    @Size(min = 5, max = 50)
    private String address;

    @Size(min = 10, max = 14)
    private String phoneNumber;

    @Email
    private String email;
}

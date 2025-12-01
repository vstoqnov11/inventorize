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

    @Size(min = 5, max = 50)
    private String address;

    @Size(min = 10, max = 14)
    private String phoneNumber;

    @Email
    private String email;
}

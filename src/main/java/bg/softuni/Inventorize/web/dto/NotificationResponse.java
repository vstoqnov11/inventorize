package bg.softuni.Inventorize.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private UUID id;
    private String subject;
    private String body;
    private LocalDateTime createdOn;
    private String status;
    private String type;
    private UUID businessId;
}

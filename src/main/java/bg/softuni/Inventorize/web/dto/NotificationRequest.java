package bg.softuni.Inventorize.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private UUID businessId;
    private String businessName;
    private String businessEmail;
    private UUID productId;
    private String productName;
    private String productBrand;
    private int currentQuantity;
    private int minStockThreshold;
}

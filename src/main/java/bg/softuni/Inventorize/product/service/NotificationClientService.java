package bg.softuni.Inventorize.product.service;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.product.model.Product;
import bg.softuni.Inventorize.web.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationClientService {

    private final NotificationServiceClient notificationServiceClient;

    @Autowired
    public NotificationClientService(NotificationServiceClient notificationServiceClient) {
        this.notificationServiceClient = notificationServiceClient;
    }

    public void sendLowStockNotification(Product product, Business business) {
        try {
            NotificationRequest request = NotificationRequest.builder()
                    .businessId(business.getId())
                    .businessName(business.getName())
                    .businessEmail(business.getEmail())
                    .productId(product.getId())
                    .productName(product.getName())
                    .productBrand(product.getBrand())
                    .currentQuantity(product.getQuantity())
                    .minStockThreshold(product.getMinStockThreshold())
                    .build();

            ResponseEntity<String> response = notificationServiceClient.sendLowStockNotification(request);
            log.info("Low stock notification sent for product: {} - Response: {}",
                    product.getName(), response.getBody());
        } catch (Exception e) {
            log.error("Failed to send low stock notification for product {}: {}",
                    product.getName(), e.getMessage(), e);
        }
    }
}


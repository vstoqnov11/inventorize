package bg.softuni.Inventorize.web;

import bg.softuni.Inventorize.product.service.NotificationServiceClient;
import bg.softuni.Inventorize.web.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationServiceClient notificationServiceClient;

    @Autowired
    public NotificationController(NotificationServiceClient notificationServiceClient) {
        this.notificationServiceClient = notificationServiceClient;
    }

    @PostMapping("/low-stock")
    public ResponseEntity<Map<String, String>> sendLowStockNotification(@RequestBody NotificationRequest request) {
        try {
            ResponseEntity<String> response = notificationServiceClient.sendLowStockNotification(request);

            Map<String, String> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", response.getBody() != null ? response.getBody() : "Low stock notification sent successfully");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to send low stock notification: {}", e.getMessage(), e);
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send notification: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}


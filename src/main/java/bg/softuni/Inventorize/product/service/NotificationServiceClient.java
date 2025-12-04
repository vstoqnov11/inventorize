package bg.softuni.Inventorize.product.service;

import bg.softuni.Inventorize.web.dto.NotificationRequest;
import bg.softuni.Inventorize.web.dto.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "email-service", url = "http://localhost:8081/api/v1/notification-svc")
public interface NotificationServiceClient {

    @PostMapping("/notifications/low-stock")
    ResponseEntity<String> sendLowStockNotification(@RequestBody NotificationRequest request);

    @GetMapping("/notifications/business/{businessId}")
    ResponseEntity<List<NotificationResponse>> getNotificationHistory(@PathVariable UUID businessId);
}


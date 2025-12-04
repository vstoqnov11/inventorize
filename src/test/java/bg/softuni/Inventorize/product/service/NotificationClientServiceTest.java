package bg.softuni.Inventorize.product.service;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.model.BusinessType;
import bg.softuni.Inventorize.product.model.Product;
import bg.softuni.Inventorize.product.model.ProductCategory;
import bg.softuni.Inventorize.web.dto.NotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationClientServiceTest {

    @Mock
    private NotificationServiceClient notificationServiceClient;

    @InjectMocks
    private NotificationClientService notificationClientService;

    private UUID productId;
    private UUID businessId;
    private Product product;
    private Business business;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        businessId = UUID.randomUUID();

        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .email("test@example.com")
                .build();

        product = Product.builder()
                .id(productId)
                .name("Test Product")
                .brand("Test Brand")
                .quantity(5)
                .minStockThreshold(10)
                .build();
    }

    @Test
    void testSendLowStockNotification_ShouldSendNotificationSuccessfully() {
        ResponseEntity<String> response = new ResponseEntity<>("Notification sent", HttpStatus.OK);
        when(notificationServiceClient.sendLowStockNotification(any(NotificationRequest.class)))
                .thenReturn(response);

        notificationClientService.sendLowStockNotification(product, business);

        ArgumentCaptor<NotificationRequest> requestCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(notificationServiceClient).sendLowStockNotification(requestCaptor.capture());

        NotificationRequest capturedRequest = requestCaptor.getValue();
        assertEquals(businessId, capturedRequest.getBusinessId());
        assertEquals("Test Business", capturedRequest.getBusinessName());
        assertEquals("test@example.com", capturedRequest.getBusinessEmail());
        assertEquals(productId, capturedRequest.getProductId());
        assertEquals("Test Product", capturedRequest.getProductName());
        assertEquals("Test Brand", capturedRequest.getProductBrand());
        assertEquals(5, capturedRequest.getCurrentQuantity());
        assertEquals(10, capturedRequest.getMinStockThreshold());
    }

    @Test
    void testSendLowStockNotification_WhenExceptionOccurs_ShouldHandleGracefully() {
        when(notificationServiceClient.sendLowStockNotification(any(NotificationRequest.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        assertDoesNotThrow(() -> notificationClientService.sendLowStockNotification(product, business));
        verify(notificationServiceClient).sendLowStockNotification(any(NotificationRequest.class));
    }

    @Test
    void testSendLowStockNotification_WithNullResponse_ShouldHandleGracefully() {
        ResponseEntity<String> response = new ResponseEntity<>(null, HttpStatus.OK);
        when(notificationServiceClient.sendLowStockNotification(any(NotificationRequest.class)))
                .thenReturn(response);

        assertDoesNotThrow(() -> notificationClientService.sendLowStockNotification(product, business));
        verify(notificationServiceClient).sendLowStockNotification(any(NotificationRequest.class));
    }
}


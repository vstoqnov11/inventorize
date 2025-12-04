package bg.softuni.Inventorize.product.service;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.model.BusinessType;
import bg.softuni.Inventorize.business.service.BusinessService;
import bg.softuni.Inventorize.exception.ProductNotFoundException;
import bg.softuni.Inventorize.product.model.Product;
import bg.softuni.Inventorize.product.model.ProductCategory;
import bg.softuni.Inventorize.product.model.ProductUnit;
import bg.softuni.Inventorize.product.repository.ProductRepository;
import bg.softuni.Inventorize.web.dto.UpsertProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BusinessService businessService;

    @Mock
    private NotificationClientService notificationClientService;

    @InjectMocks
    private ProductService productService;

    private UUID productId;
    private UUID businessId;
    private Business business;
    private Product product;
    private UpsertProductRequest upsertProductRequest;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        businessId = UUID.randomUUID();

        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .businessType(BusinessType.RETAIL)
                .email("test@example.com")
                .notificationsEnabled(true)
                .products(new ArrayList<>())
                .build();

        product = Product.builder()
                .id(productId)
                .name("Test Product")
                .brand("Test Brand")
                .category(ProductCategory.ELECTRONICS)
                .quantity(100)
                .minStockThreshold(10)
                .maxStockThreshold(200)
                .unit(ProductUnit.PIECE)
                .price(29.99)
                .business(business)
                .build();

        upsertProductRequest = UpsertProductRequest.builder()
                .name("New Product")
                .brand("New Brand")
                .category(ProductCategory.FOOD_AND_BEVERAGES)
                .quantity(50)
                .minThreshold(5)
                .maxThreshold(150)
                .unit(ProductUnit.KG)
                .price(19.99)
                .arrivalDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(1))
                .build();
    }

    @Test
    void testCreate_ShouldCreateAndSaveProduct() {
        when(businessService.getById(businessId)).thenReturn(business);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            savedProduct.setId(productId);
            return savedProduct;
        });
        doNothing().when(businessService).save(any(Business.class));

        productService.create(upsertProductRequest, businessId);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(businessService).getById(businessId);
        verify(productRepository).save(productCaptor.capture());
        verify(businessService).save(business);

        Product capturedProduct = productCaptor.getValue();
        assertEquals("New Product", capturedProduct.getName());
        assertEquals("New Brand", capturedProduct.getBrand());
        assertEquals(ProductCategory.FOOD_AND_BEVERAGES, capturedProduct.getCategory());
        assertEquals(50, capturedProduct.getQuantity());
        assertEquals(5, capturedProduct.getMinStockThreshold());
        assertEquals(150, capturedProduct.getMaxStockThreshold());
        assertEquals(ProductUnit.KG, capturedProduct.getUnit());
        assertEquals(19.99, capturedProduct.getPrice(), 0.01);
        assertEquals(business, capturedProduct.getBusiness());
    }

    @Test
    void testCreate_WithLowStock_ShouldSendNotification() {
        upsertProductRequest.setQuantity(5);
        upsertProductRequest.setMinThreshold(10);
        when(businessService.getById(businessId)).thenReturn(business);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            savedProduct.setId(productId);
            return savedProduct;
        });
        doNothing().when(businessService).save(any(Business.class));
        doNothing().when(notificationClientService).sendLowStockNotification(any(), any());

        productService.create(upsertProductRequest, businessId);

        verify(notificationClientService).sendLowStockNotification(any(), eq(business));
    }

    @Test
    void testFindById_WhenProductExists_ShouldReturnProduct() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.findById(productId);

        assertNotNull(result);
        assertEquals(product, result);
        verify(productRepository).findById(productId);
    }

    @Test
    void testFindById_WhenProductNotFound_ShouldThrowException() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(productId));
        verify(productRepository).findById(productId);
    }

    @Test
    void testEdit_ShouldUpdateProduct() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.edit(upsertProductRequest, businessId, productId);

        verify(productRepository).findById(productId);
        verify(productRepository).save(product);
        assertEquals("New Product", product.getName());
        assertEquals("New Brand", product.getBrand());
        assertEquals(ProductCategory.FOOD_AND_BEVERAGES, product.getCategory());
        assertEquals(50, product.getQuantity());
    }

    @Test
    void testEdit_WhenStockFallsBelowThreshold_ShouldSendNotification() {
        product.setQuantity(20);
        product.setMinStockThreshold(10);
        upsertProductRequest.setQuantity(5);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        doNothing().when(notificationClientService).sendLowStockNotification(any(), any());

        productService.edit(upsertProductRequest, businessId, productId);

        verify(notificationClientService).sendLowStockNotification(any(), eq(business));
    }

    @Test
    void testEdit_WhenStockAboveThreshold_ShouldNotSendNotification() {
        product.setQuantity(20);
        product.setMinStockThreshold(10);
        upsertProductRequest.setQuantity(15);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.edit(upsertProductRequest, businessId, productId);

        verify(notificationClientService, never()).sendLowStockNotification(any(), any());
    }

    @Test
    void testDelete_ShouldDeleteProduct() {
        doNothing().when(productRepository).deleteById(productId);

        productService.delete(productId);

        verify(productRepository).deleteById(productId);
    }

    @Test
    void testGetAllProducts_ShouldReturnProductsByBusinessId() {
        List<Product> products = new ArrayList<>();
        products.add(product);

        when(productRepository.findAllByBusinessId(businessId)).thenReturn(products);

        List<Product> result = productService.getAllProducts(businessId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
        verify(productRepository).findAllByBusinessId(businessId);
    }
}


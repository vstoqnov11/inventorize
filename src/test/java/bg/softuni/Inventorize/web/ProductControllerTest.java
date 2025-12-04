package bg.softuni.Inventorize.web;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.model.BusinessType;
import bg.softuni.Inventorize.business.service.BusinessService;
import bg.softuni.Inventorize.product.model.Product;
import bg.softuni.Inventorize.product.model.ProductCategory;
import bg.softuni.Inventorize.product.model.ProductUnit;
import bg.softuni.Inventorize.product.service.ProductService;
import bg.softuni.Inventorize.security.UserData;
import bg.softuni.Inventorize.user.model.User;
import bg.softuni.Inventorize.user.model.UserRole;
import bg.softuni.Inventorize.user.service.UserService;
import bg.softuni.Inventorize.web.dto.UpsertProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private BusinessService businessService;

    @InjectMocks
    private ProductController productController;

    private UserData userData;
    private Business business;
    private User user;
    private UUID businessId;
    private UUID productId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        businessId = UUID.randomUUID();
        productId = UUID.randomUUID();
        userId = UUID.randomUUID();

        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .businessType(BusinessType.RETAIL)
                .build();

        userData = new UserData(userId, "testuser", "password", "test@example.com", UserRole.MANAGER, business);

        user = User.builder()
                .id(userId)
                .username("testuser")
                .build();
    }

    @Test
    void testGetProductsPage_ShouldReturnProductsView() {
        when(userService.getById(userId)).thenReturn(user);
        when(businessService.getById(businessId)).thenReturn(business);
        when(productService.getAllProducts(businessId)).thenReturn(new ArrayList<>());

        ModelAndView result = productController.getProductsPage(userData, businessId);

        assertNotNull(result);
        assertEquals("products", result.getViewName());
        assertTrue(result.getModel().containsKey("user"));
        assertTrue(result.getModel().containsKey("business"));
        assertTrue(result.getModel().containsKey("upsertProductRequest"));
        assertTrue(result.getModel().containsKey("products"));
        verify(userService).getById(userId);
        verify(businessService).getById(businessId);
        verify(productService).getAllProducts(businessId);
    }

    @Test
    void testAddNewProduct_WithValidRequest_ShouldCreateProductAndRedirect() {
        UpsertProductRequest request = UpsertProductRequest.builder()
                .name("Test Product")
                .brand("Test Brand")
                .category(ProductCategory.ELECTRONICS)
                .quantity(100)
                .minThreshold(10)
                .maxThreshold(200)
                .price(29.99)
                .unit(ProductUnit.PIECE)
                .arrivalDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(1))
                .build();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(productService).create(any(UpsertProductRequest.class), eq(businessId));

        ModelAndView result = productController.addNewProduct(request, bindingResult, businessId);

        assertNotNull(result);
        assertEquals("redirect:/businesses/" + businessId + "/products", result.getViewName());
        verify(productService).create(request, businessId);
        verify(bindingResult).hasErrors();
    }

    @Test
    void testAddNewProduct_WithInvalidRequest_ShouldReturnFormWithErrors() {
        UpsertProductRequest request = UpsertProductRequest.builder()
                .name("")
                .brand("")
                .quantity(-1)
                .build();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ModelAndView result = productController.addNewProduct(request, bindingResult, businessId);

        assertNotNull(result);
        assertEquals("products", result.getViewName());
        assertTrue(result.getModel().containsKey("upsertProductRequest"));
        verify(productService, never()).create(any(), any());
        verify(bindingResult).hasErrors();
    }

    @Test
    void testEditProduct_WithValidRequest_ShouldUpdateProductAndRedirect() {
        UpsertProductRequest request = UpsertProductRequest.builder()
                .name("Updated Product")
                .brand("Updated Brand")
                .category(ProductCategory.ELECTRONICS)
                .quantity(150)
                .minThreshold(20)
                .maxThreshold(250)
                .price(39.99)
                .unit(ProductUnit.PIECE)
                .build();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(productService).edit(any(UpsertProductRequest.class), eq(businessId), eq(productId));

        ModelAndView result = productController.editProduct(request, bindingResult, businessId, productId);

        assertNotNull(result);
        assertEquals("redirect:/businesses/" + businessId + "/products", result.getViewName());
        verify(productService).edit(request, businessId, productId);
        verify(bindingResult).hasErrors();
    }

    @Test
    void testEditProduct_WithInvalidRequest_ShouldReturnFormWithErrors() {
        UpsertProductRequest request = UpsertProductRequest.builder()
                .name("")
                .brand("")
                .price(-10.0)
                .build();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ModelAndView result = productController.editProduct(request, bindingResult, businessId, productId);

        assertNotNull(result);
        assertEquals("products", result.getViewName());
        assertTrue(result.getModel().containsKey("upsertProductRequest"));
        verify(productService, never()).edit(any(), any(), any());
        verify(bindingResult).hasErrors();
    }

    @Test
    void testDeleteProduct_ShouldDeleteProductAndRedirect() {
        doNothing().when(productService).delete(productId);

        ModelAndView result = productController.deleteProduct(businessId, productId);

        assertNotNull(result);
        assertEquals("redirect:/businesses/" + businessId + "/products", result.getViewName());
        verify(productService).delete(productId);
    }
}


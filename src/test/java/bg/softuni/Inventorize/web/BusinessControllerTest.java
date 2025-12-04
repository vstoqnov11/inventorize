package bg.softuni.Inventorize.web;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.model.BusinessType;
import bg.softuni.Inventorize.business.service.BusinessService;
import bg.softuni.Inventorize.security.UserData;
import bg.softuni.Inventorize.user.model.User;
import bg.softuni.Inventorize.user.model.UserRole;
import bg.softuni.Inventorize.user.service.UserService;
import bg.softuni.Inventorize.web.dto.EditBusinessRequest;
import bg.softuni.Inventorize.web.dto.NewBusinessRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BusinessService businessService;

    @InjectMocks
    private BusinessController businessController;

    private UserData userData;
    private Business business;
    private UUID businessId;

    @BeforeEach
    void setUp() {
        businessId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .businessType(BusinessType.RETAIL)
                .email("test@example.com")
                .address("lulin, sofia")
                .phoneNumber("1234567890")
                .build();

        userData = new UserData(userId, "testuser", "password", "test@example.com", UserRole.MANAGER, business);
    }

    @Test
    void testGetCreateBusinessPage_ShouldReturnNewBusinessView() {
        when(userService.userHasBusiness("testuser")).thenReturn(false);

        ModelAndView result = businessController.getCreateBusinessPage(userData);

        assertNotNull(result);
        assertEquals("new-business", result.getViewName());
        assertTrue(result.getModel().containsKey("newBusinessRequest"));
        assertTrue(result.getModel().containsKey("hasBusiness"));
        assertFalse((Boolean) result.getModel().get("hasBusiness"));
        verify(userService).userHasBusiness("testuser");
    }

    @Test
    void testCreateBusiness_WithValidRequest_ShouldCreateBusinessAndRedirect() {
        NewBusinessRequest request = NewBusinessRequest.builder()
                .name("New Business")
                .type(BusinessType.RETAIL)
                .email("new@example.com")
                .address("mladost, sofia")
                .phoneNumber("1234567890")
                .build();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(businessService).createBusiness(any(NewBusinessRequest.class), eq("testuser"));

        ModelAndView result = businessController.createBusiness(request, bindingResult, userData);

        assertNotNull(result);
        assertEquals("redirect:/home", result.getViewName());
        verify(businessService).createBusiness(request, "testuser");
        verify(bindingResult).hasErrors();
    }

    @Test
    void testCreateBusiness_WithInvalidRequest_ShouldReturnFormWithErrors() {
        NewBusinessRequest request = NewBusinessRequest.builder()
                .name("AB") // Too short
                .type(BusinessType.RETAIL)
                .email("invalid-email")
                .build();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userService.userHasBusiness("testuser")).thenReturn(false);

        ModelAndView result = businessController.createBusiness(request, bindingResult, userData);

        assertNotNull(result);
        assertEquals("new-business", result.getViewName());
        assertTrue(result.getModel().containsKey("newBusinessRequest"));
        assertTrue(result.getModel().containsKey("hasBusiness"));
        verify(businessService, never()).createBusiness(any(), any());
        verify(userService).userHasBusiness("testuser");
    }

    @Test
    void testGetEditPage_ShouldReturnEditBusinessView() {
        when(businessService.getById(businessId)).thenReturn(business);

        ModelAndView result = businessController.getEditPage(businessId);

        assertNotNull(result);
        assertEquals("edit-business", result.getViewName());
        assertTrue(result.getModel().containsKey("editBusinessRequest"));
        assertTrue(result.getModel().containsKey("business"));
        verify(businessService, times(2)).getById(businessId);
    }

    @Test
    void testEditBusiness_WithValidRequest_ShouldUpdateBusinessAndRedirect() {
        EditBusinessRequest request = EditBusinessRequest.builder()
                .email("updated@example.com")
                .address("banishora, sofia")
                .phoneNumber("1111111111")
                .build();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(businessService).updateBusiness(any(EditBusinessRequest.class), eq(businessId));

        ModelAndView result = businessController.editBusiness(request, bindingResult, businessId);

        assertNotNull(result);
        assertEquals("redirect:/home", result.getViewName());
        verify(businessService).updateBusiness(request, businessId);
        verify(bindingResult).hasErrors();
    }

    @Test
    void testEditBusiness_WithInvalidRequest_ShouldReturnFormWithErrors() {
        EditBusinessRequest request = EditBusinessRequest.builder()
                .email("invalid-email")
                .phoneNumber("123") // Too short
                .build();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(businessService.getById(businessId)).thenReturn(business);

        ModelAndView result = businessController.editBusiness(request, bindingResult, businessId);

        assertNotNull(result);
        assertEquals("edit-business", result.getViewName());
        assertTrue(result.getModel().containsKey("editBusinessRequest"));
        assertTrue(result.getModel().containsKey("business"));
        verify(businessService).getById(businessId);
        verify(businessService, never()).updateBusiness(any(), any());
    }
}


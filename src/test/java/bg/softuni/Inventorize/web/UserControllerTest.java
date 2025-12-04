package bg.softuni.Inventorize.web;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.model.BusinessType;
import bg.softuni.Inventorize.security.UserData;
import bg.softuni.Inventorize.user.model.User;
import bg.softuni.Inventorize.user.model.UserRole;
import bg.softuni.Inventorize.user.service.UserService;
import bg.softuni.Inventorize.web.dto.EditProfileRequest;
import bg.softuni.Inventorize.web.dto.NewEmployeeRequest;
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
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserData userData;
    private User user;
    private Business business;
    private UUID userId;
    private UUID businessId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        businessId = UUID.randomUUID();

        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .businessType(BusinessType.RETAIL)
                .build();

        userData = new UserData(userId, "testuser", "password", "test@example.com", UserRole.MANAGER, business);

        user = User.builder()
                .id(userId)
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .phoneNumber("1234567890")
                .business(business)
                .build();
    }

    @Test
    void testGetEditProfilePage_ShouldReturnEditProfileView() {
        when(userService.getById(userId)).thenReturn(user);

        ModelAndView result = userController.getEditProfilePage(userId);

        assertNotNull(result);
        assertEquals("edit-profile", result.getViewName());
        assertTrue(result.getModel().containsKey("editRequest"));
        assertTrue(result.getModel().containsKey("user"));
        assertTrue(result.getModel().containsKey("business"));
        verify(userService).getById(userId);
    }

    @Test
    void testEditProfile_WithValidRequest_ShouldUpdateProfileAndRedirect() {
        EditProfileRequest request = EditProfileRequest.builder()
                .firstName("Updated")
                .lastName("Name")
                .email("updated@example.com")
                .phoneNumber("0987654321")
                .build();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(userService).updateProfile(any(EditProfileRequest.class), eq(userId));

        ModelAndView result = userController.editProfile(request, bindingResult, userId);

        assertNotNull(result);
        assertEquals("redirect:/home", result.getViewName());
        verify(userService).updateProfile(request, userId);
        verify(bindingResult).hasErrors();
    }

    @Test
    void testEditProfile_WithInvalidRequest_ShouldReturnFormWithErrors() {
        EditProfileRequest request = EditProfileRequest.builder()
                .firstName("AB")
                .lastName("")
                .email("invalid-email")
                .phoneNumber("123")
                .build();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userService.getById(userId)).thenReturn(user);

        ModelAndView result = userController.editProfile(request, bindingResult, userId);

        assertNotNull(result);
        assertEquals("edit-profile", result.getViewName());
        assertTrue(result.getModel().containsKey("editRequest"));
        assertTrue(result.getModel().containsKey("user"));
        assertTrue(result.getModel().containsKey("business"));
        verify(userService).getById(userId);
        verify(userService, never()).updateProfile(any(), any());
    }

    @Test
    void testGetNewEmployeePage_ShouldReturnNewEmployeeView() {
        when(userService.getById(userId)).thenReturn(user);

        ModelAndView result = userController.getNewEmployeePage(userData);

        assertNotNull(result);
        assertEquals("new-employee", result.getViewName());
        assertTrue(result.getModel().containsKey("newEmployeeRequest"));
        assertTrue(result.getModel().containsKey("user"));
        verify(userService).getById(userId);
    }

    @Test
    void testNewEmployee_WithValidRequest_ShouldCreateEmployeeAndRedirect() {
        NewEmployeeRequest request = NewEmployeeRequest.builder()
                .username("newemployee")
                .password("password123")
                .firstName("New")
                .lastName("Employee")
                .email("newemployee@example.com")
                .phoneNumber("1111111111")
                .build();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(userService).createEmployee(any(NewEmployeeRequest.class), eq(business));

        ModelAndView result = userController.newEmployee(request, bindingResult, userData);

        assertNotNull(result);
        assertEquals("redirect:/businesses/" + businessId + "/employees", result.getViewName());
        verify(userService).createEmployee(request, business);
        verify(bindingResult).hasErrors();
    }

    @Test
    void testNewEmployee_WithInvalidRequest_ShouldReturnFormWithErrors() {
        NewEmployeeRequest request = NewEmployeeRequest.builder()
                .username("ab")
                .password("123")
                .firstName("")
                .lastName("")
                .email("invalid-email")
                .build();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userService.getById(userId)).thenReturn(user);

        ModelAndView result = userController.newEmployee(request, bindingResult, userData);

        assertNotNull(result);
        assertEquals("new-employee", result.getViewName());
        assertTrue(result.getModel().containsKey("newEmployeeRequest"));
        assertTrue(result.getModel().containsKey("user"));
        verify(userService).getById(userId);
        verify(userService, never()).createEmployee(any(), any());
    }
}


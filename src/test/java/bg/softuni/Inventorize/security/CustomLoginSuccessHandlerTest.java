package bg.softuni.Inventorize.security;

import bg.softuni.Inventorize.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomLoginSuccessHandlerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CustomLoginSuccessHandler customLoginSuccessHandler;

    private String username;

    @BeforeEach
    void setUp() {
        username = "testuser";
    }

    @Test
    void testOnAuthenticationSuccess_WhenUserHasBusiness_ShouldRedirectToHome() throws IOException, ServletException {
        when(authentication.getName()).thenReturn(username);
        when(userService.userHasBusiness(username)).thenReturn(true);

        customLoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(authentication).getName();
        verify(userService).userHasBusiness(username);
        verify(response).sendRedirect("/home");
        verify(response, never()).sendRedirect("/businesses/new");
    }

    @Test
    void testOnAuthenticationSuccess_WhenUserDoesNotHaveBusiness_ShouldRedirectToNewBusiness() throws IOException, ServletException {
        when(authentication.getName()).thenReturn(username);
        when(userService.userHasBusiness(username)).thenReturn(false);

        customLoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(authentication).getName();
        verify(userService).userHasBusiness(username);
        verify(response).sendRedirect("/businesses/new");
        verify(response, never()).sendRedirect("/home");
    }

    @Test
    void testOnAuthenticationSuccess_ShouldExtractUsernameFromAuthentication() throws IOException, ServletException {
        String differentUsername = "anotheruser";
        when(authentication.getName()).thenReturn(differentUsername);
        when(userService.userHasBusiness(differentUsername)).thenReturn(true);

        customLoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(authentication).getName();
        verify(userService).userHasBusiness(differentUsername);
        verify(response).sendRedirect("/home");
    }
}


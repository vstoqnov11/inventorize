package bg.softuni.Inventorize.web;

import bg.softuni.Inventorize.exception.BusinessNotFoundException;
import bg.softuni.Inventorize.exception.ProductNotFoundException;
import bg.softuni.Inventorize.exception.UserNotFoundException;
import bg.softuni.Inventorize.exception.UsernameAlreadyTakenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public String handleUsernameAlreadyTaken(RedirectAttributes redirectAttributes, UsernameAlreadyTakenException exception) {
        redirectAttributes.addFlashAttribute("usernameError", exception.getMessage() != null ? exception.getMessage() : "Username already taken");
        return "redirect:/register";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolation(HttpServletRequest request, RedirectAttributes redirectAttributes, DataIntegrityViolationException exception) {
        String errorMessage = "An error occurred. ";
        String requestPath = request.getRequestURI();
        String fieldName = null;

        if (exception.getMessage() != null && exception.getMessage().contains("username")) {
            errorMessage = "Username already taken";
            fieldName = "usernameError";
        } else if (exception.getMessage() != null && exception.getMessage().contains("email")) {
            errorMessage = "Email already taken";
            fieldName = "emailError";
        } else if (exception.getMessage() != null && exception.getMessage().contains("name")) {
            if (requestPath != null && requestPath.contains("business")) {
                errorMessage = "Business name already taken";
                fieldName = "nameError";
            } else {
                errorMessage = "Product name already exists";
                fieldName = "nameError";
            }
        } else {
            errorMessage = "A record with this information already exists";
        }

        if (fieldName != null) {
            redirectAttributes.addFlashAttribute(fieldName, errorMessage);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        if (requestPath != null) {
            if (requestPath.contains("/register")) {
                return "redirect:/register";
            } else if (requestPath.contains("/users/new")) {
                return "redirect:/users/new";
            } else if (requestPath.contains("/businesses/new")) {
                return "redirect:/businesses/new";
            } else if (requestPath.contains("/profile")) {
                String[] parts = requestPath.split("/");
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals("users") && i + 1 < parts.length) {
                        return "redirect:/users/" + parts[i + 1] + "/profile";
                    }
                }
            } else if (requestPath.contains("/businesses/") && requestPath.contains("/edit")) {
                String[] parts = requestPath.split("/");
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals("businesses") && i + 1 < parts.length && !parts[i + 1].equals("new")) {
                        return "redirect:/businesses/" + parts[i + 1] + "/edit";
                    }
                }
            } else if (requestPath.contains("/products")) {
                String[] parts = requestPath.split("/");
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals("businesses") && i + 1 < parts.length) {
                        return "redirect:/businesses/" + parts[i + 1] + "/products";
                    }
                }
            }
        }

        return "redirect:/home";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            UserNotFoundException.class,
            BusinessNotFoundException.class,
            ProductNotFoundException.class,
            NoHandlerFoundException.class
    })
    public ModelAndView handleNotFoundException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("not-found");
        String errorMessage = exception.getMessage();
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = "The requested resource was not found.";
        }
        modelAndView.addObject("errorMessage", errorMessage);
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            IllegalArgumentException.class
    })
    public ModelAndView handleBadRequestException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("not-found");
        String errorMessage = exception.getMessage();
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = "Invalid request parameters.";
        }
        modelAndView.addObject("errorMessage", errorMessage);
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("internal-server-error");
        String errorMessage = exception.getClass().getSimpleName();
        if (exception.getMessage() != null && !exception.getMessage().isEmpty()) {
            errorMessage = exception.getMessage();
        }
        modelAndView.addObject("errorMessage", errorMessage);
        return modelAndView;
    }
}

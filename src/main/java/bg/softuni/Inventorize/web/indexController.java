package bg.softuni.Inventorize.web;

import bg.softuni.Inventorize.business.service.BusinessService;
import bg.softuni.Inventorize.security.UserData;
import bg.softuni.Inventorize.user.model.User;
import bg.softuni.Inventorize.user.service.UserService;
import bg.softuni.Inventorize.web.dto.LoginRequest;
import bg.softuni.Inventorize.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class indexController {

    private final UserService userService;
    private final BusinessService businessService;

    @Autowired
    public indexController (UserService userService, BusinessService businessService) {
        this.userService = userService;
        this.businessService = businessService;
    }

    @GetMapping
    public String index () {

        return "index";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage (@RequestParam(name = "loginAttemptMessage", required = false) String message) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("loginRequest", new LoginRequest());
        mav.addObject("loginAttemptMessage", message);

        return mav;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage () {

        ModelAndView mav = new ModelAndView("register");
        mav.addObject("registerRequest", new RegisterRequest());

        return mav;
    }

    @PostMapping("/register")
    public ModelAndView processRegister (@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("register");
            mav.addObject("registerRequest", registerRequest);
            return mav;
        }

        userService.register(registerRequest);

        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/home")
    public ModelAndView getHomePage (@AuthenticationPrincipal UserData userData) {

        ModelAndView mav = new ModelAndView("home");
        User user = userService.getById(userData.getId());
        mav.addObject("user", user);
        mav.addObject("business", businessService.getById(user.getBusiness().getId()));

        return mav;
    }
}

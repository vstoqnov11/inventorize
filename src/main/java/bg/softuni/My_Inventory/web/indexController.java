package bg.softuni.My_Inventory.web;

import bg.softuni.My_Inventory.business.service.BusinessService;
import bg.softuni.My_Inventory.security.UserData;
import bg.softuni.My_Inventory.user.service.UserService;
import bg.softuni.My_Inventory.web.dto.LoginRequest;
import bg.softuni.My_Inventory.web.dto.RegisterRequest;
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
    public String index() {

        return "index";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(name = "loginAttemptMessage", required = false) String message) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("loginRequest", new LoginRequest());
        mav.addObject("loginAttemptMessage", message);

        return mav;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {

        ModelAndView mav = new ModelAndView("register");
        mav.addObject("registerRequest", new RegisterRequest());

        return mav;
    }

    @PostMapping("/register")
    public ModelAndView processRegister(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("register");
            mav.addObject("registerRequest", registerRequest);
            return mav;
        }

        userService.register(registerRequest);

        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(@AuthenticationPrincipal UserData userData) {

        ModelAndView mav = new ModelAndView("home");
        mav.addObject("business", businessService.getById(userData.getBusiness().getId()));
        mav.addObject("user", userService.getById(userData.getId()));

        return mav;
    }
}

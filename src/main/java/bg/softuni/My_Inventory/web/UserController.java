package bg.softuni.My_Inventory.web;

import bg.softuni.My_Inventory.security.UserData;
import bg.softuni.My_Inventory.user.model.User;
import bg.softuni.My_Inventory.user.service.UserService;
import bg.softuni.My_Inventory.utils.DtoMapper;
import bg.softuni.My_Inventory.utils.PaginationUtils;
import bg.softuni.My_Inventory.web.dto.EditProfileRequest;
import bg.softuni.My_Inventory.web.dto.NewEmployeeRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ModelAndView getUsersPage(@RequestParam(defaultValue = "1") int page, @AuthenticationPrincipal UserData userData) {

        Page<User> userPage = userService.getAllUsers(page - 1);

        int totalPages = userPage.getTotalPages();
        page = PaginationUtils.clampPage(page, totalPages);

        ModelAndView mav = new ModelAndView("users");
        mav.addObject("user", userService.getById(userData.getId()));
        mav.addObject("business", userData.getBusiness());
        mav.addObject("users", userPage.getContent());
        mav.addObject("page", page);
        mav.addObject("totalPages", totalPages);
        mav.addObject("pageNumbers", PaginationUtils.pageSequence(userPage.getTotalPages()));

        return mav;
    }

    @GetMapping("/{id}/profile")
    public ModelAndView getEditProfilePage(@PathVariable UUID id) {

        User user = userService.getById(id);

        EditProfileRequest editProfileRequest = DtoMapper.fromUser(user);

        ModelAndView mav = new ModelAndView("edit-profile");
        mav.addObject("editRequest", editProfileRequest);
        mav.addObject("user", user);
        mav.addObject("business", user.getBusiness());

        return mav;
    }

    @PutMapping("/{id}/profile")
    public ModelAndView editProfile(@Valid EditProfileRequest editProfileRequest, BindingResult bindingResult, @PathVariable UUID id) {

        if (bindingResult.hasErrors()) {
            User user = userService.getById(id);
            ModelAndView mav = new ModelAndView("edit-profile");
            mav.addObject("user", user);
            return mav;
        }

        userService.updateProfile(editProfileRequest, id);

        return new ModelAndView("redirect:/home");
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/new")
    public ModelAndView getNewEmployeePage(@AuthenticationPrincipal UserData userData) {

        ModelAndView mav = new ModelAndView("new-employee");
        mav.addObject("newEmployeeRequest", new NewEmployeeRequest());
        mav.addObject("user", userService.getById(userData.getId()));

        return mav;
    }

    @PostMapping("/new")
    public ModelAndView newEmployee(@Valid NewEmployeeRequest newEmployeeRequest, BindingResult bindingResult, @AuthenticationPrincipal UserData userData) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("new-employee");
            mav.addObject("newEmployeeRequest", newEmployeeRequest);
            return mav;
        }

        userService.createEmployee(newEmployeeRequest, userData.getBusiness());

        return new ModelAndView(String.format("redirect:/businesses/%s/employees", userData.getBusiness().getId()));
    }
}

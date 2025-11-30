package bg.softuni.My_Inventory.web;

import bg.softuni.My_Inventory.user.model.User;
import bg.softuni.My_Inventory.user.service.UserService;
import bg.softuni.My_Inventory.utils.DtoMapper;
import bg.softuni.My_Inventory.utils.PaginationUtils;
import bg.softuni.My_Inventory.web.dto.EditProfileRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping
    public ModelAndView getUsersPage(@RequestParam(defaultValue = "1") int page) {

        Page<User> userPage = userService.getAllUsers(page - 1);

        int totalPages = userPage.getTotalPages();
        page = PaginationUtils.clampPage(page, totalPages);

        ModelAndView mav = new ModelAndView("users");
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
}

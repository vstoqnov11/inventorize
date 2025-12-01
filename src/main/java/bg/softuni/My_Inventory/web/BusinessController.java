package bg.softuni.My_Inventory.web;

import bg.softuni.My_Inventory.business.model.Business;
import bg.softuni.My_Inventory.business.service.BusinessService;
import bg.softuni.My_Inventory.security.UserData;
import bg.softuni.My_Inventory.user.model.User;
import bg.softuni.My_Inventory.user.service.UserService;
import bg.softuni.My_Inventory.utils.DtoMapper;
import bg.softuni.My_Inventory.utils.PaginationUtils;
import bg.softuni.My_Inventory.web.dto.EditBusinessRequest;
import bg.softuni.My_Inventory.web.dto.NewBusinessRequest;
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
@RequestMapping("/businesses")
public class BusinessController {

    private final UserService userService;
    private final BusinessService businessService;

    @Autowired
    public BusinessController (UserService userService, BusinessService businessService) {
        this.userService = userService;
        this.businessService = businessService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ModelAndView getBusinessesPage(@RequestParam(defaultValue = "1") int page, @AuthenticationPrincipal UserData userData) {

        Page<Business> businessPage = businessService.getAllBusinesses(page - 1);

        int totalPages = businessPage.getTotalPages();
        page = PaginationUtils.clampPage(page, totalPages);

        ModelAndView mav = new ModelAndView("businesses");
        mav.addObject("user", userService.getById(userData.getId()));
        mav.addObject("business", userData.getBusiness());
        mav.addObject("businesses", businessPage.getContent());
        mav.addObject("page", page);
        mav.addObject("totalPages", totalPages);
        mav.addObject("pageNumber", PaginationUtils.pageSequence(businessPage.getTotalPages()));

        return mav;
    }

    @GetMapping("/{id}/products")
    public ModelAndView getBusinessPage(@AuthenticationPrincipal UserData userData, @PathVariable UUID id) {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("products");
        mav.addObject("user", userService.getById(userData.getId()));
        mav.addObject("business", businessService.getById(id));

        return mav;
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/new")
    public ModelAndView getCreateBusinessPage(@AuthenticationPrincipal UserData userData) {
        ModelAndView mav = new ModelAndView("new-business");
        mav.addObject("newBusinessRequest", new NewBusinessRequest());
        mav.addObject("hasBusiness", userService.userHasBusiness(userData.getUsername()));
        return mav;
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public ModelAndView getEditPage(@AuthenticationPrincipal UserData userData, @PathVariable UUID id) {

        ModelAndView mav = new ModelAndView("edit-business");
        mav.addObject("editBusinessRequest", DtoMapper.fromBusiness(businessService.getById(id)));
        mav.addObject("business", businessService.getById(id));

        return mav;
    }

    @PostMapping("/new")
    public ModelAndView createBusiness(@Valid NewBusinessRequest newBusinessRequest, BindingResult bindingResult, @AuthenticationPrincipal UserData userData) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("new-business");
            mav.addObject("businessFormDto", newBusinessRequest);
            return mav;
        }

        businessService.createBusiness(newBusinessRequest, userData.getUsername());

        return new ModelAndView("redirect:/home");
    }

    @PutMapping("/{id}/edit")
    public ModelAndView editBusiness(@Valid EditBusinessRequest editBusinessRequest, BindingResult bindingResult, @AuthenticationPrincipal UserData userData, @PathVariable UUID id) {

        if (bindingResult.hasErrors()) {
            Business business = businessService.getById(id);
            ModelAndView mav = new ModelAndView("edit-business");
            mav.addObject("businessFormDto", editBusinessRequest);
            mav.addObject("business", business);

            return mav;
        }

        businessService.updateBusiness(editBusinessRequest, id);

        return new ModelAndView("redirect:/home");
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/{id}/employees")
    public ModelAndView getEmployeesPage(@AuthenticationPrincipal UserData userData, @PathVariable UUID id, @RequestParam(defaultValue = "1") int page) {

        Page<User> businessPage = userService.getAllEmployees(page - 1, id);

        int totalPages = businessPage.getTotalPages();
        page = PaginationUtils.clampPage(page, totalPages);

        ModelAndView mav = new ModelAndView("employees");
        mav.addObject("user", userService.getById(userData.getId()));
        mav.addObject("business", userData.getBusiness());
        mav.addObject("employees", businessPage.getContent());
        mav.addObject("page", page);
        mav.addObject("totalPages", totalPages);
        mav.addObject("pageNumber", PaginationUtils.pageSequence(businessPage.getTotalPages()));

        return mav;
    }
}

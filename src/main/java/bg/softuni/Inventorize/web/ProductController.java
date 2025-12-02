package bg.softuni.Inventorize.web;

import bg.softuni.Inventorize.business.service.BusinessService;
import bg.softuni.Inventorize.product.service.ProductService;
import bg.softuni.Inventorize.security.UserData;
import bg.softuni.Inventorize.user.service.UserService;
import bg.softuni.Inventorize.web.dto.UpsertProductRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/businesses/{businessId}/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final BusinessService businessService;

    @Autowired
    public ProductController (ProductService productService, UserService userService, BusinessService businessService) {
        this.productService = productService;
        this.userService = userService;
        this.businessService = businessService;
    }

    @GetMapping
    public ModelAndView getProductsPage (@AuthenticationPrincipal UserData userData, @PathVariable UUID businessId) {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("products");
        mav.addObject("user", userService.getById(userData.getId()));
        mav.addObject("business", businessService.getById(businessId));
        mav.addObject("upsertProductRequest", new UpsertProductRequest());
        mav.addObject("products", productService.getAllProducts(businessId));

        return mav;
    }

    @PostMapping("/new")
    public ModelAndView addNewProduct(@Valid @ModelAttribute("upsertProductRequest") UpsertProductRequest upsertProductRequest, BindingResult bindingResult, @PathVariable UUID businessId) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("products");
            mav.addObject("upsertProductRequest", upsertProductRequest);
            return mav;
        }

        productService.create(upsertProductRequest, businessId);

         return new ModelAndView("redirect:/businesses/" + businessId + "/products");
    }

    @PutMapping("/{productId}/edit")
    public ModelAndView editProduct(@Valid @ModelAttribute("upsertProductRequest") UpsertProductRequest upsertProductRequest, BindingResult bindingResult, @PathVariable UUID businessId, @PathVariable UUID productId) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("products");
            mav.addObject("upsertProductRequest", upsertProductRequest);
            return mav;
        }

        productService.edit(upsertProductRequest, businessId, productId);

        return new ModelAndView("redirect:/businesses/" + businessId + "/products");
    }

    @DeleteMapping("/{productId}/delete")
    public ModelAndView deleteProduct(@PathVariable UUID businessId, @PathVariable UUID productId) {

        productService.delete(productId);

        return new ModelAndView("redirect:/businesses/" + businessId + "/products");
    }
}

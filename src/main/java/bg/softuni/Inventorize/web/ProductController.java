package bg.softuni.Inventorize.web;

import bg.softuni.Inventorize.business.service.BusinessService;
import bg.softuni.Inventorize.product.service.ProductService;
import bg.softuni.Inventorize.security.UserData;
import bg.softuni.Inventorize.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/businesses/{businessId}/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final BusinessService businessService;

    @Autowired
    public ProductController(ProductService productService, UserService userService, BusinessService businessService) {
        this.productService = productService;
        this.userService = userService;
        this.businessService = businessService;
    }

    @GetMapping
    public ModelAndView getProductsPage (@AuthenticationPrincipal UserData userData, @PathVariable UUID id) {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("products");
        mav.addObject("user", userService.getById(userData.getId()));
        mav.addObject("business", businessService.getById(id));
        // mav.addObject("products", businessService.getAllProducts(id));

        return mav;
    }

    @GetMapping("/new")
    public ModelAndView showAddForm(@PathVariable UUID businessId) {
        ModelAndView mav = new ModelAndView("product-add"); // new template
        mav.addObject("businessId", businessId);
        mav.addObject("productForm", new ProductDto());
        return mav;
    }

    @PostMapping
    public ModelAndView createProduct(@Valid ProductDto productForm, BindingResult bindingResult, @PathVariable UUID id) {
        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("product-add");
            mav.addObject("businessId", businessId);
            mav.addObject("productForm", productForm);
            return mav;
        }

        productService.create(ProductDto ,id);
        return new ModelAndView("redirect:/businesses/" + id + "/products");
    }

    @GetMapping("/{productId}/edit")
    public ModelAndView showEditForm(@PathVariable UUID businessId,
                                     @PathVariable UUID productId) {
        ModelAndView mav = new ModelAndView("product-edit"); // new template
        mav.addObject("businessId", businessId);
        mav.addObject("productForm", productService.getForEdit(productId));
        return mav;
    }

    @PostMapping("/{productId}")
    public ModelAndView updateProduct(@PathVariable UUID businessId,
                                      @PathVariable UUID productId,
                                      @Valid ProductDto productForm,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("product-edit");
            mav.addObject("businessId", businessId);
            mav.addObject("productForm", productForm);
            return mav;
        }

        productService.updateForBusiness(businessId, productId, productForm);
        return new ModelAndView("redirect:/businesses/" + businessId + "/products");
    }

    @PostMapping("/{productId}/delete")
    public ModelAndView deleteProduct(@PathVariable UUID businessId, @PathVariable UUID productId) {
        productService.deleteFromBusiness(businessId, productId);
        return new ModelAndView("redirect:/businesses/" + businessId + "/products");
    }
}

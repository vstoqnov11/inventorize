package bg.softuni.Inventorize.business.service;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.repository.BusinessRepository;
import bg.softuni.Inventorize.product.model.Product;
import bg.softuni.Inventorize.product.service.ProductService;
import bg.softuni.Inventorize.user.service.UserService;
import bg.softuni.Inventorize.web.dto.EditBusinessRequest;
import bg.softuni.Inventorize.web.dto.NewBusinessRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public BusinessService (BusinessRepository businessRepository, UserService userService, ProductService productService) {
        this.businessRepository = businessRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @Transactional
    public void createBusiness (NewBusinessRequest newBusinessRequest, String username) {

        Business business = Business.builder()
                .name(newBusinessRequest.getName())
                .businessType(newBusinessRequest.getType())
                .address(newBusinessRequest.getAddress())
                .phoneNumber(newBusinessRequest.getPhoneNumber())
                .email(newBusinessRequest.getEmail())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        businessRepository.save(business);
        userService.addBusiness(business, username);
    }

    public void updateBusiness (EditBusinessRequest editBusinessRequest, UUID id) {

        Business business = getById(id);

        business.setAddress(editBusinessRequest.getAddress());
        business.setPhoneNumber(editBusinessRequest.getPhoneNumber());
        business.setEmail(editBusinessRequest.getEmail());
        business.setUpdatedOn(LocalDateTime.now());

        businessRepository.save(business);
    }

    public Page<Business> getAllBusinesses(int page) {

        Pageable pageable = PageRequest.of(page, 10);

        return businessRepository.findAll(pageable);
    }

    public Business getById (UUID id) {
        return businessRepository.findById(id).orElseThrow(() -> new RuntimeException("Business with id %s not found".formatted(id)));
    }

//    public List<Product> getAllProducts (UUID id) {
//        return productService;
//    }
}

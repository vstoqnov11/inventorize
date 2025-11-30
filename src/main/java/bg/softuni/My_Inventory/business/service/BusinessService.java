package bg.softuni.My_Inventory.business.service;

import bg.softuni.My_Inventory.business.model.Business;
import bg.softuni.My_Inventory.business.repository.BusinessRepository;
import bg.softuni.My_Inventory.user.service.UserService;
import bg.softuni.My_Inventory.web.dto.EditBusinessRequest;
import bg.softuni.My_Inventory.web.dto.NewBusinessRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final UserService userService;

    @Autowired
    public BusinessService (BusinessRepository businessRepository, UserService userService) {
        this.businessRepository = businessRepository;
        this.userService = userService;
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
}

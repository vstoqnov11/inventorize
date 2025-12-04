package bg.softuni.Inventorize.business.service;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.repository.BusinessRepository;
import bg.softuni.Inventorize.exception.BusinessNotFoundException;
import bg.softuni.Inventorize.user.service.UserService;
import bg.softuni.Inventorize.web.dto.EditBusinessRequest;
import bg.softuni.Inventorize.web.dto.NewBusinessRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final UserService userService;

    @Autowired
    public BusinessService(BusinessRepository businessRepository, UserService userService) {
        this.businessRepository = businessRepository;
        this.userService = userService;
    }

    @Transactional
    public void createBusiness(NewBusinessRequest newBusinessRequest, String username) {
        Business business = Business.builder()
                .name(newBusinessRequest.getName())
                .businessType(newBusinessRequest.getType())
                .address(newBusinessRequest.getAddress())
                .phoneNumber(newBusinessRequest.getPhoneNumber())
                .email(newBusinessRequest.getEmail())
                .notificationsEnabled(true)
                .restockEnabled(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        businessRepository.save(business);
        userService.addBusiness(business, username);
        log.info("Business created: {} with id: {}", business.getName(), business.getId());
    }

    public void updateBusiness(EditBusinessRequest editBusinessRequest, UUID id) {
        Business business = getById(id);
        business.setAddress(editBusinessRequest.getAddress());
        business.setPhoneNumber(editBusinessRequest.getPhoneNumber());
        business.setEmail(editBusinessRequest.getEmail());
        business.setUpdatedOn(LocalDateTime.now());
        businessRepository.save(business);
        log.info("Business updated with id: {}", id);
    }

    public Page<Business> getAllBusinesses(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return businessRepository.findAll(pageable);
    }

    public Business getById(UUID id) {
        return businessRepository.findById(id).orElseThrow(() -> {
            log.error("Business not found with id: {}", id);
            return new BusinessNotFoundException("Business with id %s not found".formatted(id));
        });
    }

    public void save(Business business) {
        businessRepository.save(business);
    }

    public void toggleNotifications(UUID id) {
        Business business = getById(id);
        business.setNotificationsEnabled(!business.getNotificationsEnabled());
        business.setUpdatedOn(LocalDateTime.now());
        businessRepository.save(business);
        log.info("Notifications for business id: {} set to {}", id, business.getNotificationsEnabled());
    }

    public void toggleRestock(UUID id) {
        Business business = getById(id);
        business.setRestockEnabled(!business.getRestockEnabled());
        business.setUpdatedOn(LocalDateTime.now());
        businessRepository.save(business);
        log.info("Restock for business id: {} set to {}", id, business.getRestockEnabled());
    }

}

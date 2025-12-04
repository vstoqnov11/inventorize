package bg.softuni.Inventorize.business.service;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.model.BusinessType;
import bg.softuni.Inventorize.business.repository.BusinessRepository;
import bg.softuni.Inventorize.exception.BusinessNotFoundException;
import bg.softuni.Inventorize.user.service.UserService;
import bg.softuni.Inventorize.web.dto.EditBusinessRequest;
import bg.softuni.Inventorize.web.dto.NewBusinessRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessServiceTest {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BusinessService businessService;

    private UUID businessId;
    private Business business;
    private NewBusinessRequest newBusinessRequest;
    private EditBusinessRequest editBusinessRequest;

    @BeforeEach
    void setUp() {
        businessId = UUID.randomUUID();

        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .businessType(BusinessType.RETAIL)
                .address("123 Test St")
                .phoneNumber("1234567890")
                .email("test@example.com")
                .notificationsEnabled(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        newBusinessRequest = NewBusinessRequest.builder()
                .name("New Business")
                .type(BusinessType.SUPERMARKET)
                .address("456 New St")
                .phoneNumber("0987654321")
                .email("new@example.com")
                .build();

        editBusinessRequest = EditBusinessRequest.builder()
                .address("789 Updated St")
                .phoneNumber("1111111111")
                .email("updated@example.com")
                .build();
    }

    @Test
    void testCreateBusiness_ShouldCreateAndSaveBusiness() {
        when(businessRepository.save(any(Business.class))).thenAnswer(invocation -> {
            Business savedBusiness = invocation.getArgument(0);
            savedBusiness.setId(businessId);
            return savedBusiness;
        });
        doNothing().when(userService).addBusiness(any(Business.class), eq("testuser"));

        businessService.createBusiness(newBusinessRequest, "testuser");

        ArgumentCaptor<Business> businessCaptor = ArgumentCaptor.forClass(Business.class);
        verify(businessRepository).save(businessCaptor.capture());
        verify(userService).addBusiness(any(Business.class), eq("testuser"));

        Business capturedBusiness = businessCaptor.getValue();
        assertEquals("New Business", capturedBusiness.getName());
        assertEquals(BusinessType.SUPERMARKET, capturedBusiness.getBusinessType());
        assertEquals("456 New St", capturedBusiness.getAddress());
        assertEquals("0987654321", capturedBusiness.getPhoneNumber());
        assertEquals("new@example.com", capturedBusiness.getEmail());
        assertTrue(capturedBusiness.getNotificationsEnabled());
        assertNotNull(capturedBusiness.getCreatedOn());
        assertNotNull(capturedBusiness.getUpdatedOn());
    }

    @Test
    void testUpdateBusiness_ShouldUpdateBusinessFields() {
        LocalDateTime originalUpdatedOn = business.getUpdatedOn();
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));
        when(businessRepository.save(any(Business.class))).thenReturn(business);

        businessService.updateBusiness(editBusinessRequest, businessId);

        verify(businessRepository).findById(businessId);
        verify(businessRepository).save(business);
        assertEquals("789 Updated St", business.getAddress());
        assertEquals("1111111111", business.getPhoneNumber());
        assertEquals("updated@example.com", business.getEmail());
        assertNotNull(business.getUpdatedOn());
        assertNotEquals(originalUpdatedOn, business.getUpdatedOn());
    }

    @Test
    void testUpdateBusiness_WhenBusinessNotFound_ShouldThrowException() {
        when(businessRepository.findById(businessId)).thenReturn(Optional.empty());

        assertThrows(BusinessNotFoundException.class, 
                () -> businessService.updateBusiness(editBusinessRequest, businessId));
        verify(businessRepository).findById(businessId);
        verify(businessRepository, never()).save(any(Business.class));
    }

    @Test
    void testGetAllBusinesses_ShouldReturnPageOfBusinesses() {
        List<Business> businesses = new ArrayList<>();
        businesses.add(business);
        Page<Business> businessPage = new PageImpl<>(businesses);
        Pageable pageable = PageRequest.of(0, 10);

        when(businessRepository.findAll(pageable)).thenReturn(businessPage);

        Page<Business> result = businessService.getAllBusinesses(0);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(business, result.getContent().get(0));
        verify(businessRepository).findAll(pageable);
    }

    @Test
    void testGetAllBusinesses_WithDifferentPageNumber_ShouldReturnCorrectPage() {
        List<Business> businesses = new ArrayList<>();
        Business business2 = Business.builder()
                .id(UUID.randomUUID())
                .name("Second Business")
                .businessType(BusinessType.RESTAURANT)
                .address("999 Second St")
                .phoneNumber("9999999999")
                .email("second@example.com")
                .notificationsEnabled(false)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        businesses.add(business2);
        Page<Business> businessPage = new PageImpl<>(businesses);
        Pageable pageable = PageRequest.of(1, 10);

        when(businessRepository.findAll(pageable)).thenReturn(businessPage);

        Page<Business> result = businessService.getAllBusinesses(1);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Second Business", result.getContent().get(0).getName());
        verify(businessRepository).findAll(pageable);
    }

    @Test
    void testGetAllBusinesses_WhenEmpty_ShouldReturnEmptyPage() {
        List<Business> emptyList = new ArrayList<>();
        Page<Business> emptyPage = new PageImpl<>(emptyList);
        Pageable pageable = PageRequest.of(0, 10);

        when(businessRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<Business> result = businessService.getAllBusinesses(0);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        verify(businessRepository).findAll(pageable);
    }

    @Test
    void testGetById_WhenBusinessExists_ShouldReturnBusiness() {
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));

        Business result = businessService.getById(businessId);

        assertNotNull(result);
        assertEquals(businessId, result.getId());
        assertEquals("Test Business", result.getName());
        verify(businessRepository).findById(businessId);
    }

    @Test
    void testGetById_WhenBusinessNotFound_ShouldThrowException() {
        when(businessRepository.findById(businessId)).thenReturn(Optional.empty());

        assertThrows(BusinessNotFoundException.class, () -> businessService.getById(businessId));
        verify(businessRepository).findById(businessId);
    }

    @Test
    void testSave_ShouldSaveBusiness() {
        when(businessRepository.save(business)).thenReturn(business);

        businessService.save(business);

        verify(businessRepository).save(business);
    }

    @Test
    void testSave_WithNewBusiness_ShouldSaveAndReturnBusiness() {
        Business newBusiness = Business.builder()
                .name("New Business to Save")
                .businessType(BusinessType.RETAIL)
                .address("Save Address")
                .phoneNumber("5555555555")
                .email("save@example.com")
                .notificationsEnabled(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        when(businessRepository.save(newBusiness)).thenReturn(newBusiness);

        businessService.save(newBusiness);

        verify(businessRepository).save(newBusiness);
    }

    @Test
    void testToggleNotifications_WhenEnabled_ShouldDisable() {
        business.setNotificationsEnabled(true);
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));
        when(businessRepository.save(any(Business.class))).thenReturn(business);

        businessService.toggleNotifications(businessId);

        verify(businessRepository).findById(businessId);
        verify(businessRepository).save(business);
        assertFalse(business.getNotificationsEnabled());
        assertNotNull(business.getUpdatedOn());
    }

    @Test
    void testToggleNotifications_WhenDisabled_ShouldEnable() {
        business.setNotificationsEnabled(false);
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));
        when(businessRepository.save(any(Business.class))).thenReturn(business);

        businessService.toggleNotifications(businessId);

        verify(businessRepository).findById(businessId);
        verify(businessRepository).save(business);
        assertTrue(business.getNotificationsEnabled());
    }

    @Test
    void testToggleNotifications_WhenNull_ShouldSetToTrue() {
        business.setNotificationsEnabled(null);
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));
        when(businessRepository.save(any(Business.class))).thenReturn(business);

        businessService.toggleNotifications(businessId);

        assertTrue(business.getNotificationsEnabled());
    }
}


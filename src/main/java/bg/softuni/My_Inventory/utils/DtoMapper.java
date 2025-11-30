package bg.softuni.My_Inventory.utils;

import bg.softuni.My_Inventory.business.model.Business;
import bg.softuni.My_Inventory.user.model.User;
import bg.softuni.My_Inventory.web.dto.EditBusinessRequest;
import bg.softuni.My_Inventory.web.dto.EditProfileRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static EditProfileRequest fromUser(User user) {

        return EditProfileRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public static EditBusinessRequest fromBusiness(Business business) {

        return EditBusinessRequest.builder()
                .address(business.getAddress())
                .email(business.getEmail())
                .phoneNumber(business.getPhoneNumber())
                .build();
    }
}

package bg.softuni.Inventorize.utils;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.user.model.User;
import bg.softuni.Inventorize.web.dto.EditBusinessRequest;
import bg.softuni.Inventorize.web.dto.EditProfileRequest;
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

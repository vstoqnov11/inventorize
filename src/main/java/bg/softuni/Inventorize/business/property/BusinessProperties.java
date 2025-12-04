package bg.softuni.Inventorize.business.property;

import bg.softuni.Inventorize.business.model.BusinessType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "business")
public class BusinessProperties {

    private String name;
    private BusinessType type;
}


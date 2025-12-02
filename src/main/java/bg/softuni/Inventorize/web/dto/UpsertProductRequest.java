package bg.softuni.Inventorize.web.dto;

import bg.softuni.Inventorize.product.model.ProductCategory;
import bg.softuni.Inventorize.product.model.ProductUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertProductRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Brand is required")
    private String brand;

    private ProductCategory category;

    @Min(value = 0, message = "Min threshold cannot be negative")
    private Integer minThreshold;

    @Min(value = 0, message = "Max threshold cannot be negative")
    private Integer maxThreshold;

    private LocalDate arrivalDate;
    private LocalDate expiryDate;

    @Min(value = 0, message = "Price cannot be negative")
    private double price;

    private ProductUnit unit;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
}

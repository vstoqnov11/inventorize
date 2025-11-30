package bg.softuni.My_Inventory.product.model;

import bg.softuni.My_Inventory.business.model.Business;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column
    private int quantity;

    @Column
    private int minStockThreshold;

    @Column
    private int maxStockThreshold;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductUnit unit;

    @Column
    private LocalDateTime arrivalDate;

    @Column
    private LocalDateTime expiryDate;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;
}
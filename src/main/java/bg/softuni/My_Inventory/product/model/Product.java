package bg.softuni.My_Inventory.product.model;

import bg.softuni.My_Inventory.business.model.Business;
import bg.softuni.My_Inventory.transaction.model.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
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
    private BusinessCategory category;

    @Column
    private int quantity;

    @Column
    private int minStockThreshold;

    @Column
    private String unit;

    @Column
    private double purchasePrice;

    @Column
    private double sellingPrice;

    @Column
    private String supplier;

    @Column
    private LocalDateTime arrivalDate;

    @Column
    private LocalDateTime expiryDate;

    @Column
    private String batchNumber;

    @Column
    private LocalDateTime createdOn;

    @Column
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;
}
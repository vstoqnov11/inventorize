package bg.softuni.My_Inventory.business.model;

import bg.softuni.My_Inventory.product.model.Product;
import bg.softuni.My_Inventory.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "businesses")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private BusinessType businessType;

    @Column
    private String address;

    @Column
    private String phoneNumber;

    @Column
    private String email;

    @Column
    private String websiteUrl;

    @OneToMany(mappedBy = "business")
    private List<User> users;

    @OneToMany(mappedBy = "business")
    private List<Product> products;
}

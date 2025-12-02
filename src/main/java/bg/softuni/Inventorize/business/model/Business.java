package bg.softuni.Inventorize.business.model;

import bg.softuni.Inventorize.product.model.Product;
import bg.softuni.Inventorize.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
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

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    @Column
    private String address;

    @Column
    private String phoneNumber;

    @Column
    private String email;

    @Column
    private LocalDateTime createdOn;

    @Column
    private LocalDateTime updatedOn;

    @OneToMany(mappedBy = "business")
    private List<User> employees;

    @OneToMany(mappedBy = "business")
    private List<Product> products;
}

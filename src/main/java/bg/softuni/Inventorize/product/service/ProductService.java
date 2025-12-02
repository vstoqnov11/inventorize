package bg.softuni.Inventorize.product.service;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.service.BusinessService;
import bg.softuni.Inventorize.product.model.Product;
import bg.softuni.Inventorize.product.repository.ProductRepository;
import bg.softuni.Inventorize.web.dto.UpsertProductRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BusinessService businessService;

    @Autowired
    public ProductService (ProductRepository productRepository, BusinessService businessService) {
        this.productRepository = productRepository;
        this.businessService = businessService;
    }

    @Transactional
    public void create (UpsertProductRequest upsertProductRequest, UUID id) {

        Business business = businessService.getById(id);

        Product product = Product.builder()
                .name(upsertProductRequest.getName())
                .brand(upsertProductRequest.getBrand())
                .category(upsertProductRequest.getCategory())
                .minStockThreshold(upsertProductRequest.getMinThreshold())
                .maxStockThreshold(upsertProductRequest.getMaxThreshold())
                .arrivalDate(upsertProductRequest.getArrivalDate())
                .expiryDate(upsertProductRequest.getExpiryDate())
                .price(upsertProductRequest.getPrice())
                .unit(upsertProductRequest.getUnit())
                .quantity(upsertProductRequest.getQuantity())
                .business(business)
                .build();

        productRepository.save(product);

        business.getProducts().add(product);
        businessService.save(business);
    }

    public Product findById (UUID productId) {
        return productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void edit (UpsertProductRequest upsertProductRequest, UUID businessId, UUID productId) {

        Product product = findById(productId);

        product.setName(upsertProductRequest.getName());
        product.setBrand(upsertProductRequest.getBrand());
        product.setCategory(upsertProductRequest.getCategory());
        product.setMinStockThreshold(upsertProductRequest.getMinThreshold());
        product.setMaxStockThreshold(upsertProductRequest.getMaxThreshold());
        product.setArrivalDate(upsertProductRequest.getArrivalDate());
        product.setExpiryDate(upsertProductRequest.getExpiryDate());
        product.setPrice(upsertProductRequest.getPrice());
        product.setUnit(upsertProductRequest.getUnit());
        product.setQuantity(upsertProductRequest.getQuantity());

        productRepository.save(product);
    }

    public void delete (UUID productId) {
        productRepository.deleteById(productId);
    }

    public List<Product> getAllProducts (UUID businessId) {
        return productRepository.findAllByBusinessId(businessId);
    }
}

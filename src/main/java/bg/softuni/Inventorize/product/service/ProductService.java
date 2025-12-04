package bg.softuni.Inventorize.product.service;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.service.BusinessService;
import bg.softuni.Inventorize.exception.ProductNotFoundException;
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
    private final NotificationClientService notificationClientService;

    @Autowired
    public ProductService(ProductRepository productRepository, BusinessService businessService, NotificationClientService notificationClientService) {
        this.productRepository = productRepository;
        this.businessService = businessService;
        this.notificationClientService = notificationClientService;
    }

    @Transactional
    public void create(UpsertProductRequest upsertProductRequest, UUID id) {
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
        log.info("Product created successfully: {}", product.getName());
        checkAndNotifyLowStock(product, business);
    }

    public Product findById(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() -> {
            log.error("Product not found with id: {}", productId);
            return new ProductNotFoundException("Product with id %s not found".formatted(productId));
        });
    }

    public void edit(UpsertProductRequest upsertProductRequest, UUID businessId, UUID productId) {
        Product product = findById(productId);
        int oldQuantity = product.getQuantity();
        Business business = product.getBusiness();

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
        log.info("Product updated successfully: {} - Quantity changed from {} to {}", product.getName(), oldQuantity, product.getQuantity());

        if (oldQuantity > product.getMinStockThreshold() &&
            product.getQuantity() <= product.getMinStockThreshold()) {
            log.warn("Product {} stock fell below threshold: {} (was: {})", product.getName(), product.getQuantity(), oldQuantity);
            checkAndNotifyLowStock(product, business);
        }
    }

    private void checkAndNotifyLowStock(Product product, Business business) {
        if (product.getQuantity() <= product.getMinStockThreshold() &&
            product.getMinStockThreshold() > 0 &&
            business.getEmail() != null && !business.getEmail().isEmpty()) {
            log.info("Low stock detected for product: {} - Quantity: {}, Threshold: {}",
                    product.getName(), product.getQuantity(), product.getMinStockThreshold());
            notificationClientService.sendLowStockNotification(product, business);
        } else {
            log.debug("Low stock notification skipped for product: {} - Quantity: {}, Threshold: {}, Email: {}",
                    product.getName(), product.getQuantity(), product.getMinStockThreshold(),
                    business.getEmail() != null ? business.getEmail() : "not set");
        }
    }

    public void delete(UUID productId) {
        productRepository.deleteById(productId);
        log.info("Product deleted with id: {}", productId);
    }

    public List<Product> getAllProducts(UUID businessId) {
        return productRepository.findAllByBusinessId(businessId);
    }
}

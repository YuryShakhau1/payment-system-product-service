package by.shakhau.ps.product.repository;

import by.shakhau.ps.product.repository.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {

    Page<ProductEntity> findByNameStartingWith(String name, Pageable pageable);

    @Query("UPDATE ProductEntity SET deleted = :deleted WHERE id = :id")
    @Modifying
    void updateDeleted(UUID id, Boolean deleted);
}

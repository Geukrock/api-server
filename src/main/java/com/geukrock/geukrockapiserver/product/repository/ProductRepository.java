package com.geukrock.geukrockapiserver.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.geukrock.geukrockapiserver.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
        
} 

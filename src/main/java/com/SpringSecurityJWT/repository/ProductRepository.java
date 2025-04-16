package com.SpringSecurityJWT.repository;

import com.SpringSecurityJWT.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {


}

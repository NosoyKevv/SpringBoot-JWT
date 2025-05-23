package com.SpringSecurityJWT.domain.repository;
import com.SpringSecurityJWT.domain.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {}
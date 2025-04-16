package com.SpringSecurityJWT.auth.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public enum Role {

    CUSTOMER(List.of(Permission.READ_ALL_PRODUCTS)),

    ADMINISTRATOR(List.of(Permission.SAVE_ONE_PRODUCT, Permission.READ_ALL_PRODUCTS));

    private final List<Permission> permissions;

}

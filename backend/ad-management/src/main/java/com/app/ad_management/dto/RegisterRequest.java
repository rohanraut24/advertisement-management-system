package com.app.ad_management.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String companyName;
    private String platformName;
    private String platformUrl;
    private String Role;
}

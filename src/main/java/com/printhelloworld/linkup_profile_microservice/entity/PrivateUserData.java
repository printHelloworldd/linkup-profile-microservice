package com.printhelloworld.linkup_profile_microservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "PrivateData")
@Table(name = "private_user_data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrivateUserData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "pin", nullable = false)
    private String pin;
    @Column(name = "encrypted_mnemonic", nullable = true)
    private String encryptedMnemonic;
    @Column(name = "fcm_token", nullable = false)
    private String fcmToken;
}

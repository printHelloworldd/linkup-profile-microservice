package com.printhelloworld.linkup_profile_microservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "RestrictedData")
@Table(name = "restricted_user_data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestrictedUserData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "public_x25519_key", nullable = false)
    private String publicX25519Key;
    @Column(name = "public_ed25519_key", nullable = false)
    private String publicEd25519Key;
}

package com.printhelloworld.linkup_profile_microservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hobbies")
public class Hobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

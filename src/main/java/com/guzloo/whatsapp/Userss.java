package com.guzloo.whatsapp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="user_dtls")
@Data
public class Userss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String phone;
    private String otp;
    private String email;
    private String otpStatus;
}

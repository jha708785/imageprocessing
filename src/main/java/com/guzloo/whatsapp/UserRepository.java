package com.guzloo.whatsapp;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Userss, Integer> {

    public Userss findByEmailAndOtp(String email, String otp);
}

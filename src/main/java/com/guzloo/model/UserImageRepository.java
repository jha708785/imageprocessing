package com.guzloo.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage,Long> {

    Optional<UserImage> findByUsername(String username);
}

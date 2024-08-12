package com.finstack.projectlog.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finstack.projectlog.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, UUID>{

    Optional<Resource> findByEmailAddress(String email);
}

package com.example.springreactive.repositories;

import com.example.springreactive.models.Role;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RoleRepository extends R2dbcRepository<Role, Long> {
}

package com.example.springreactive.controllers;

import com.example.springreactive.models.Role;
import com.example.springreactive.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/role")
public class RoleController {
  @Autowired
  RoleRepository roleRepository;

  @GetMapping("/{role}")
  public Mono<Role> createRole(@PathVariable String role) {
    return roleRepository.save(Role.of(role));
  }
}

package com.example.springreactive.repositories;

import com.example.springreactive.models.User;
import io.r2dbc.spi.Parameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<User, Long> {
  Mono<User> findUserByName(String name);

  Mono<User> findUserById(Long id);

  @Query("SELECT * FROM users LIMIT :elements OFFSET :page")
  Flux<User> findAll(Integer page, Integer elements);
}

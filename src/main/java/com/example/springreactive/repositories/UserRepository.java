package com.example.springreactive.repositories;

import com.example.springreactive.models.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<User, Long> {
  Mono<User> findUserByName(String name);

  Mono<User> findUserById(Long id);

  @Query("SELECT * FROM users LIMIT :elements OFFSET :page * :elements")
  Flux<User> findAll(Integer page, Integer elements);

  @Query(""
      + "SELECT * FROM users "
      + "WHERE users.name LIKE CONCAT('%', :keyword, '%') "
      + "ORDER BY users.name ASC "
      + "LIMIT :elements OFFSET :page * :elements")
  Flux<User> findFilterByName(String keyword, Integer elements, Integer page);

  @Query(""
      + "SELECT * FROM users "
      + "WHERE users.name LIKE CONCAT('%', :keyword, '%') "
      + "ORDER BY users.name DESC "
      + "LIMIT :elements OFFSET :page * :elements")
  Flux<User> findFilterByNameDesc(String keyword, Integer elements, Integer page);

  @Query(""
      + "SELECT * FROM users JOIN roles ON users.role_id = roles.id "
      + "WHERE roles.role LIKE CONCAT('%', :keyword, '%') "
      + "ORDER BY users.name ASC "
      + "LIMIT :elements OFFSET :page * :elements")
  Flux<User> findFilterByRoleName(String keyword, Integer elements, Integer page);

  @Query(""
      + "SELECT * FROM users JOIN roles ON users.role_id = roles.id "
      + "WHERE roles.role LIKE CONCAT('%', :keyword, '%') "
      + "ORDER BY users.name DESC "
      + "LIMIT :elements OFFSET :page * :elements")
  Flux<User> findFilterByRoleNameDesc(String keyword, Integer elements, Integer page);
}

package com.example.springreactive.services;

import com.example.springreactive.commands.FilterCommandImpl;
import com.example.springreactive.models.User;
import com.example.springreactive.repositories.UserRepository;
import com.example.springreactive.requests.FilterCommandRequest;
import com.example.springreactive.responses.FilterCommandResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {
  @Autowired UserRepository repository;

  @Autowired FilterCommandImpl command;

  public Mono<FilterCommandResponse> getAllByFilter(FilterCommandRequest request) {
    return command.execute(request);
  }

  public Mono<? extends Object> getAll(Integer page, Integer elements) {
    try {
      return repository.findAll(page, elements)
          .collectList()
          .map(users -> ResponseEntity.ok().body(users));
    } catch (Exception e) {
      log.error(e.toString());
      return Mono.just(ResponseEntity.badRequest().body(e.toString()));
    }
  }

  public Mono<? extends Object> getUserByName(String name) {
    try {
      return repository.findUserByName(name).map(user -> ResponseEntity.ok().body(user));

    } catch (Exception e) {
      log.error(e.toString());
      return Mono.just(ResponseEntity.badRequest().body(e.toString()));
    }
  }

  public Mono<? extends Object> createUser(User user) {
    try {
      return repository.save(user)
          .map(u -> ResponseEntity.ok().body("Success created user: " + user));

    } catch (Exception e) {
      log.error(e.toString());
      return Mono.just(ResponseEntity.badRequest().body(e.toString()));
    }
  }

  public Mono<? extends Object> updateUser(Long id, Mono<User> userMono) {

    try {
      return repository.findUserById(id).flatMap(u -> userMono.map(uM -> {
        u.setRole_id(uM.getRole_id());
        u.setName(uM.getName());
        return u;
      })).flatMap(u -> {
        repository.save(u);
        return Mono.just(ResponseEntity.ok().body("Success updated user id: " + u.getId()));
      });
    } catch (Exception e) {
      log.error(e.toString());
      return Mono.just(ResponseEntity.badRequest().body(e.toString()));
    }

  }

  public Mono<? extends Object> deleteUser(Long id) {
    try {
      log.info("Deleting user with id: " + id);
      repository.deleteById(id);
      return Mono.just(ResponseEntity.ok().body("Deleted user with id: " + id));
    } catch (Exception e) {
      log.error(e.toString());
      return Mono.just(ResponseEntity.ok().body(e.toString()));
    }
  }
}

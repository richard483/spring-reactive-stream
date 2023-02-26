package com.example.springreactive.services;

import com.example.springreactive.models.User;
import com.example.springreactive.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class UserService {
  @Autowired
  UserRepository repository;

  public Flux<User> getAll() {
    try {
      return repository.findAll();

    } catch (Exception e) {
      log.error(e.toString());
      return null;
    }
  }

  public Flux<User> getAll(Integer page, Integer elements) {
    try {
      return repository.findAll(page, elements);

    } catch (Exception e) {
      log.error(e.toString());
      return null;
    }
  }

  public Mono<User> getUserByName(String name) {
    try {
      return repository.findUserByName(name);

    } catch (Exception e) {
      log.error(e.toString());
      return null;
    }
  }

  public Mono<User> createUser(User user) {
    try {
      return repository.save(user);

    } catch (Exception e) {
      log.error(e.toString());
      return null;
    }
  }

  public Mono<User> updateUser(Long id, Mono<User> userMono) {

    try {
      return repository.findUserById(id).flatMap(u -> userMono.map(uM -> {
        u.setRole_id(uM.getRole_id());
        u.setName(uM.getName());
        return u;
      })).flatMap(u -> repository.save(u));
    } catch (Exception e) {
      log.error(e.toString());
      return null;
    }

  }

  public Mono<Void> deleteUser(Long id) {
    try {
      if(repository.findUserById(id)==null)
        throw new RuntimeException();
      log.info("Deleting user with id: " + id);
      return repository.deleteById(id);
    } catch (Exception e) {
      log.error(e.toString());
      return null;
    }
  }
}
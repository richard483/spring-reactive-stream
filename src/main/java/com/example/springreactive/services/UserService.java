package com.example.springreactive.services;

import com.example.springreactive.commands.FilterCommandImpl;
import com.example.springreactive.models.User;
import com.example.springreactive.repositories.UserRepository;
import com.example.springreactive.requests.FilterCommandRequest;
import com.example.springreactive.responses.DefaultResponse;
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

  public Mono<ResponseEntity<?>> getAll(Integer page, Integer elements) {
    try {
      return repository.findAll(page, elements).collectList().map(users -> {
        if (users.size() == 0)
          return ResponseEntity.badRequest()
              .body(DefaultResponse.builder().message("There are no item(s) in this page").build());
        return ResponseEntity.ok()
            .body(DefaultResponse.builder().data(users).message("success").build());
      });
    } catch (Exception e) {
      log.error(e.toString());
      return Mono.just(ResponseEntity.badRequest().body(DefaultResponse.builder()
          .message("error")
          .data(e.toString())
          .build()));
    }
  }

  public Mono<ResponseEntity<?>> getUserByName(String name) {
    try {
      return repository.findUserByName(name)
          .map(user -> ResponseEntity.ok()
              .body(DefaultResponse.builder()
                  .message("success")
                  .data(user)
                  .build()));

    } catch (Exception e) {
      log.error(e.toString());
      return Mono.just(ResponseEntity.badRequest().body(DefaultResponse.builder()
          .message("error")
          .data(e.toString())
          .build()));
    }
  }

  public Mono<ResponseEntity<?>> createUser(User user) {
    try {
      return repository.save(user)
          .map(u -> ResponseEntity.ok().body(DefaultResponse.builder()
              .message("Success created user: " + user)
              .data(user)
              .build()));

    } catch (Exception e) {
      log.error(e.toString());
      return Mono.just(ResponseEntity.badRequest().body(DefaultResponse.builder()
          .message("error")
          .data(e.toString())
          .build()));
    }
  }

  public Mono<ResponseEntity<?>> updateUser(User userMono) {

    try {
      return repository.save(userMono).then(Mono.just(ResponseEntity.badRequest().body(DefaultResponse.builder()
          .message("success")
          .data(repository.save(userMono))
          .build())));
    } catch (Exception e) {
      log.error(e.toString());
      return Mono.just(ResponseEntity.badRequest().body(DefaultResponse.builder()
          .message("error")
          .data(e.toString())
          .build()));
    }

  }

  public Mono<ResponseEntity<?>> deleteUser(Long id) {
    try {
      log.info("Deleting user with id: " + id);
      repository.deleteById(id);
      return Mono.just(ResponseEntity.ok().body(DefaultResponse.builder()
          .message("Deleted user with id: " + id)
          .data(id)
          .build()));
    } catch (Exception e) {
      log.error(e.toString());
      return Mono.just(ResponseEntity.badRequest().body(DefaultResponse.builder()
          .message("error")
          .data(e.toString())
          .build()));
    }
  }
}

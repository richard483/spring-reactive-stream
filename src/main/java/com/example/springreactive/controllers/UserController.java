package com.example.springreactive.controllers;

import com.example.springreactive.commands.FilterCommandImpl;
import com.example.springreactive.models.User;
import com.example.springreactive.requests.FilterCommandRequest;
import com.example.springreactive.responses.FilterCommandResponse;
import com.example.springreactive.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
public class UserController {


  @Autowired FilterCommandImpl command;
  @Autowired private UserService service;

  @GetMapping
  public Mono<ResponseEntity<?>> getAll(@RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "5") Integer elements) {
    return service.getAll(page, elements);
  }

  @GetMapping(value = "/{name}")
  public Mono<ResponseEntity<?>> getUserByName(@PathVariable String name) {
    return service.getUserByName(name);
  }

  @PostMapping
  public Mono<ResponseEntity<?>> createUser(@RequestBody User user) {
    return service.createUser(user);
  }

  @PatchMapping("update")
  public Mono<ResponseEntity<?>> updateUser(@RequestBody User user) {
    return service.updateUser(user);
  }

  @DeleteMapping("{id}")
  public Mono<ResponseEntity<?>> deleteUser(@PathVariable Long id) {
    return service.deleteUser(id);
  }

  @PostMapping("/filter")
  public Mono<FilterCommandResponse> filterUser(@RequestBody FilterCommandRequest request) {
    return command.execute(request);
  }

}

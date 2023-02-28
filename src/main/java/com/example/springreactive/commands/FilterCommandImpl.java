package com.example.springreactive.commands;

import com.example.springreactive.constants.EFilterBy;
import com.example.springreactive.constants.ESort;
import com.example.springreactive.models.User;
import com.example.springreactive.repositories.UserRepository;
import com.example.springreactive.requests.FilterCommandRequest;
import com.example.springreactive.responses.FilterCommandResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class FilterCommandImpl implements IFilterCommand {
  @Autowired
  UserRepository userRepository;

  @Override
  public Mono<FilterCommandResponse> execute(FilterCommandRequest request) {
    Flux<User> userFlux;

    if (request.getFilterBy() == EFilterBy.USER_NAME) {
      userFlux = request.getSort() == ESort.ASCENDING ?
          userRepository.findFilterByName(request.getKeyword(), request.getItemsPerPage(), request.getPageNumber()) :
          userRepository.findFilterByNameDesc(request.getKeyword(), request.getItemsPerPage(), request.getPageNumber());
    }else{
      userFlux = request.getSort() == ESort.ASCENDING ?
          userRepository.findFilterByRoleName(request.getKeyword(), request.getItemsPerPage(), request.getPageNumber()) :
          userRepository.findFilterByRoleNameDesc(request.getKeyword(), request.getItemsPerPage(), request.getPageNumber());
    }

    userFlux.subscribe(all -> log.info(String.valueOf(all)));
    Mono<List<User>> userMono = userFlux.collectList();

    return userMono.flatMap(userList -> {
      Mono<FilterCommandResponse> listOfUser = Mono.just(FilterCommandResponse.builder()
          .data(userList)
          .responseMessage("List of user")
          .build());
      return listOfUser;
    });

  }

}

package com.example.springreactive;

import com.example.springreactive.models.Role;
import com.example.springreactive.models.User;
import com.example.springreactive.repositories.RoleRepository;
import com.example.springreactive.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Slf4j
class SpringReactiveApplicationTests {
  @Autowired private WebTestClient webTestClient;
  @Autowired private UserRepository userRepository;
  @Autowired private RoleRepository roleRepository;
  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void beforeEach() {
    this.roleRepository.save(Role.builder().id(1L).role(Role.ERole.MEMBER_ROLE).build());
    this.roleRepository.save(Role.builder().id(2L).role(Role.ERole.SUPER_ROLE).build());
    this.roleRepository.save(Role.builder().id(3L).role(Role.ERole.ADMIN_ROLE).build());
    this.roleRepository.save(Role.builder().id(4L).role(Role.ERole.GIGA_ROLE).build());


    this.userRepository.save(User.builder().id(1L).name("member").role_id(2L).build());
    this.userRepository.save(User.builder().id(2L).name("Kotoka Torahime").role_id(2L).build());
    this.userRepository.save(User.builder().id(3L).name("Zaion Lanza").role_id(2L).build());
    this.userRepository.save(User.builder().id(4L).name("Layla Alstromeria").role_id(2L).build());
    this.userRepository.save(User.builder().id(5L).name("Finana Ryugu").role_id(2L).build());

    this.userRepository.save(User.builder().id(6L).name("admin").role_id(1L).build());
  }

  @AfterEach
  void afterEach() {
    this.userRepository.deleteAll();
    this.roleRepository.deleteAll();
  }

  @Test
  void getAllUserData_byDefaultPaging_success() {
    Integer expectedObjectLength = 5;

    webTestClient.get()
        .uri("/api/user")
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.length()")
        .isEqualTo(expectedObjectLength);
  }

  @Test
  void getAllUserData_withNoItemsOnPage_fail() {
    Integer page = 10;
    Integer elements = 10;
    Integer expectedObjectLength = 0;

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/api/user")
            .queryParam("page", page)
            .queryParam("elements", elements)
            .build())
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$")
        .isEqualTo("There are no user(s) in this page");
  }

  @Test
  void name() {
  }
}

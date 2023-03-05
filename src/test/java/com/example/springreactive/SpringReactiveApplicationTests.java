package com.example.springreactive;

import com.example.springreactive.constants.EFilterBy;
import com.example.springreactive.constants.ERole;
import com.example.springreactive.constants.ESort;
import com.example.springreactive.models.Role;
import com.example.springreactive.models.User;
import com.example.springreactive.repositories.RoleRepository;
import com.example.springreactive.repositories.UserRepository;
import com.example.springreactive.requests.FilterCommandRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.AfterTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "urlspring.r2dbc.url=r2dbc:postgresql://localhost:49153/User-test")
//@Sql(scripts = {"classpath:init.sql"},
//    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureWebTestClient
@Slf4j
class SpringReactiveApplicationTests {
  @Autowired private WebTestClient webTestClient;
  @Autowired private UserRepository userRepository;
  @Autowired private RoleRepository roleRepository;
  @Autowired private ObjectMapper objectMapper;

  @BeforeTestExecution
  void beforeTestExe() {
    this.roleRepository.save(Role.builder().id(1L).role(ERole.MEMBER_ROLE).build()).subscribe();
    this.roleRepository.save(Role.builder().id(2L).role(ERole.SUPER_ROLE).build()).subscribe();
    this.roleRepository.save(Role.builder().id(3L).role(ERole.ADMIN_ROLE).build()).subscribe();
    this.roleRepository.save(Role.builder().id(4L).role(ERole.GIGA_ROLE).build()).subscribe();


    this.userRepository.save(User.builder().id(1L).name("member").role_id(2L).build()).subscribe();
    this.userRepository.save(User.builder().id(2L).name("Kotoka Torahime").role_id(2L).build())
        .subscribe();
    this.userRepository.save(User.builder().id(3L).name("Zaion Lanza").role_id(2L).build())
        .subscribe();
    this.userRepository.save(User.builder().id(4L).name("Layla Alstromeria").role_id(2L).build())
        .subscribe();
    this.userRepository.save(User.builder().id(5L).name("Finana Ryugu").role_id(2L).build())
        .subscribe();

    this.userRepository.save(User.builder().id(6L).name("admin").role_id(1L).build()).subscribe();
  }

  @AfterTestExecution
  void afterTestExe() throws InterruptedException {
    this.userRepository.deleteAll().subscribe();
    this.roleRepository.deleteAll().subscribe();
  }

  @Test
  @Order(1)
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
  @Order(2)
  void getAllUserData_withNoItemsOnPage_success() {
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
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.length()")
        .isEqualTo(0);
  }

  @Test
  @Order(3)
  void getUserByName_withExactlySameUserNameOnDB_success() {
    String pathVariable = "admin";

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/api/user/" + pathVariable).build())
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$['name']")
        .isEqualTo(pathVariable);
  }

  @Test
  @Order(4)
  void getUserByName_withExactlySameUserNameOnDBToUppercase_fail() {
    String pathVariable = "ADMIN";

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/api/user/" + pathVariable).build())
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .isEmpty();
  }

  @Test
  @Order(5)
  void createUser_withAllGivenField_success() throws Exception {
    User request = User.builder().name("Selen Tatsuki").role_id(2L).build();

    String requestJson = objectMapper.writeValueAsString(request);

    webTestClient.post()
        .uri("/api/user")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(requestJson))
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$['name']")
        .isEqualTo(request.getName())
        .jsonPath("$['role_id']")
        .isEqualTo(request.getRole_id());

    userRepository.findUserByName("Selen Tatsuki").subscribe(Assertions::assertNotNull);
  }

  @Test
  @Order(6)
  void createUser_withNoRoleId_success() throws Exception {
    User request = User.builder().name("Selen Tatsuki").build();

    String requestJson = objectMapper.writeValueAsString(request);

    webTestClient.post()
        .uri("/api/user")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(requestJson))
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$['name']")
        .isEqualTo(request.getName())
        .jsonPath("$['role_id']")
        .isEqualTo(request.getRole_id());

    userRepository.findUserByName("Selen Tatsuki").subscribe(Assertions::assertNotNull);
  }

  @Test
  @Order(7)
  void createUser_withNoName_fail() throws Exception {
    User request = User.builder().role_id(1L).build();

    String requestJson = objectMapper.writeValueAsString(request);

    webTestClient.post()
        .uri("/api/user")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(requestJson))
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON);

    userRepository.findUserByName("Selen Tatsuki").subscribe(Assertions::assertNull);
  }

  @Test
  @Order(8)
  void updateUser_withFieldGiven_success() throws Exception {
    User request = User.builder().name("Takina Sakana").role_id(1L).build();
    Integer requestPath = 5;

    String requestJson = objectMapper.writeValueAsString(request);

    webTestClient.patch()
        .uri("/api/user/update/" + requestPath)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(requestJson))
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$['name']")
        .isEqualTo(request.getName())
        .jsonPath("$['role_id']")
        .isEqualTo(request.getRole_id());

    userRepository.findUserById(request.getId()).subscribe(user -> {
      Assertions.assertEquals(user.getName(), request.getName());
      Assertions.assertEquals(user.getRole_id(), request.getRole_id());
    });
  }

  @Test
  @Order(9)
  void updateUser_withOnlyNameGiven_success() throws Exception {
    User request = User.builder().name("Takina Sakana").build();
    Integer requestPath = 5;

    String requestJson = objectMapper.writeValueAsString(request);

    webTestClient.patch()
        .uri("/api/user/update/" + requestPath)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(requestJson))
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$['name']")
        .isEqualTo(request.getName());

    userRepository.findUserById(request.getId()).subscribe(user -> {
      Assertions.assertEquals(user.getName(), request.getName());
    });
  }

  @Test
  @Order(10)
  void updateUser_withOnlyRoleIdGiven_success() throws Exception {
    User request = User.builder().role_id(3L).build();
    Integer requestPath = 5;

    String requestJson = objectMapper.writeValueAsString(request);

    webTestClient.patch()
        .uri("/api/user/update/" + requestPath)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(requestJson))
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$['role_id']")
        .isEqualTo(request.getRole_id());

    userRepository.findUserById(request.getId()).subscribe(user -> {
      Assertions.assertEquals(user.getRole_id(), request.getRole_id());
    });
  }

  @Test
  @Order(11)
  void updateUser_withNoFieldGiven_fail() throws Exception {
    Integer requestPath = 5;

    webTestClient.patch()
        .uri("/api/user/update/" + requestPath)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$['error']")
        .isEqualTo("Bad Request");
  }

  @Test
  @Order(12)
  void deleteUser_withIDContainedOnDB_success() throws Exception {
    Integer requestPath = 5;

    webTestClient.delete()
        .uri("/api/user/" + requestPath)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk();

    userRepository.findUserById(5L).subscribe(user -> Assertions.assertNull(user));
  }

  @Test
  @Order(13)
  void deleteUser_withIDNotContainedOnDB_success() throws Exception {
    Integer requestPath = 10;

    webTestClient.delete()
        .uri("/api/user/" + requestPath)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk();
  }

  @Test
  @Order(14)
  void filterUser_withFieldGivenExKeyword_success() throws Exception {
    FilterCommandRequest request = FilterCommandRequest.builder()
        .pageNumber(1)
        .itemsPerPage(2)
        .sort(ESort.ASCENDING)
        .keyword("")
        .filterBy(EFilterBy.USER_NAME)
        .build();

    String requestJson = objectMapper.writeValueAsString(request);

    webTestClient.post()
        .uri("/api/user/filter")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(requestJson))
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$['responseMessage']")
        .isEqualTo("List of user")
        .jsonPath("$['data'].length()")
        .isEqualTo(request.getItemsPerPage());
  }

  @Test
  @Order(15)
  void filterUser_withFieldGiven_success() throws Exception {
    FilterCommandRequest request = FilterCommandRequest.builder()
        .pageNumber(1)
        .itemsPerPage(2)
        .sort(ESort.ASCENDING)
        .keyword("an")
        .filterBy(EFilterBy.USER_NAME)
        .build();

    String requestJson = objectMapper.writeValueAsString(request);

    JSONObject json = new JSONObject(new String(Objects.requireNonNull(webTestClient.post()
        .uri("/api/user/filter")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(requestJson))
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$['responseMessage']")
        .isEqualTo("List of user")
        .returnResult()
        .getResponseBody())));

    for (int i = 0; i < json.getJSONArray("data").length(); i++) {
      Assertions.assertTrue(json.getJSONArray("data")
          .getJSONObject(i)
          .getString("name")
          .contains(request.getKeyword()));
    }
  }

  @Test
  @Order(16)
  void filterUser_withFieldGivenDescending_success() throws Exception {
    FilterCommandRequest request = FilterCommandRequest.builder()
        .pageNumber(1)
        .itemsPerPage(5)
        .sort(ESort.DESCENDING)
        .keyword("")
        .filterBy(EFilterBy.USER_NAME)
        .build();

    String requestJson = objectMapper.writeValueAsString(request);

    JSONObject json = new JSONObject(new String(Objects.requireNonNull(webTestClient.post()
        .uri("/api/user/filter")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(requestJson))
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$['responseMessage']")
        .isEqualTo("List of user")
        .returnResult()
        .getResponseBody())));

    Assertions.assertTrue(json.getJSONArray("data")
        .getJSONObject(0)
        .getString("name")
        .compareTo(json.getJSONArray("data").getJSONObject(4).getString("name")) > 0);
  }

}
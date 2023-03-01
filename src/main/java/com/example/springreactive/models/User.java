package com.example.springreactive.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table(value = "users")
@AllArgsConstructor
public class User {
  @Id
  @Setter(AccessLevel.PRIVATE)
  Long id;

  String name;
  Long role_id;

}

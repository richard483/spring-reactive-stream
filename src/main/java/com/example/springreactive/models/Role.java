package com.example.springreactive.models;

import com.example.springreactive.constants.ERole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table(value = "roles")
@AllArgsConstructor
public class Role {

  @Id
  @Setter(AccessLevel.PRIVATE)
  private Long id;

  private ERole role;

  public static Role of(String role) {
    return new Role(null, ERole.valueOf(role));
  }
}

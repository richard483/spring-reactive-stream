package com.example.springreactive.responses;

import com.example.springreactive.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterCommandResponse {
  private List<User> data;

  private String responseMessage;
}

package com.example.springreactive.requests;

import com.example.springreactive.constants.EFilterBy;
import com.example.springreactive.constants.ESort;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterCommandRequest {
  private Integer pageNumber;
  private Integer itemsPerPage;
  private ESort sort;
  private String keyword;
  @NotBlank private EFilterBy filterBy;

}

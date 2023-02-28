package com.example.springreactive.requests;

import com.example.springreactive.constants.EFilterBy;
import com.example.springreactive.constants.ESort;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterCommandRequest {
  private Integer pageNumber;
  private Integer itemsPerPage;
  private ESort sort;
  private String keyword;
  @NonNull
  private EFilterBy filterBy;

}

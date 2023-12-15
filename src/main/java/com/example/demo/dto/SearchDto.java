package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class SearchDto <T> {
    private String startDate;
    private String endDate;
    private String orderDate;
    private List<T> result;
    private String[] messages;
    private boolean isValid;
    private Integer pageIndex;
    private Integer pageSize;
    private Integer totalPages;
}

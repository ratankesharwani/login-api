package com.ratan.loginapi.actor.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class GenericSearchRequest {
    private Map<String, Object> filters;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id"; // default sort column
    private String direction = "asc";
}

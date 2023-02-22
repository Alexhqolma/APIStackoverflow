package com.example.apistackoverflow.model;

import lombok.Data;

@Data
public class ApiResponseTagDto {
    private ApiTagDto[] items;
    private boolean has_more;
    private int page;
    private int page_size;
}

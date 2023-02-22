package com.example.apistackoverflow.model;

import lombok.Data;

@Data
public class ApiUserDto {
    private int answer_count;
    private int question_count;
    private int reputation;
    private int user_id;
    private String location;
    private String link;
    private String profile_image;
    private String display_name;
    private String tags;
}

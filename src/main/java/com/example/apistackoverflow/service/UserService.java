package com.example.apistackoverflow.service;

import com.example.apistackoverflow.model.ApiUserDto;
import java.util.List;

public interface UserService {
    List<ApiUserDto> getAllUsers();
}

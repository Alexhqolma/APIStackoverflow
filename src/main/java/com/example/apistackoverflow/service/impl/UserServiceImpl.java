package com.example.apistackoverflow.service.impl;

import com.example.apistackoverflow.model.ApiResponseTagDto;
import com.example.apistackoverflow.model.ApiResponseUserDto;
import com.example.apistackoverflow.model.ApiTagDto;
import com.example.apistackoverflow.model.ApiUserDto;
import com.example.apistackoverflow.service.HttpClient;
import com.example.apistackoverflow.service.UserService;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final static String MAIN_URL = "https://api.stackexchange.com/2.3/";
    private final static String BODY_USER_URL = "users?page=1&pagesize=100&order=desc&sort=reputation&site=stackoverflow&";
    private final static String FILTER_USER_URL = "filter=!4VV9AqHItXUX)LNNM6gGscGKM2n";
    private final static String BODY_TAG_URL_PREV = "users/";
    private final static String BODY_TAG_URL_NEXT = "/tags?";
    private final static String PAGE_TAG = "page=";
    private final static String LAST_TAG = "&pagesize=100&order=desc&sort=popular&site=stackoverflow&";
    private final static String FILTER_TAG_URL = "filter=!T-Rln0Rryl.qCi4miM";
    private final HttpClient httpClient;

    public UserServiceImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public List<ApiUserDto> getAllUsers() {
        List<ApiUserDto> userList = Arrays.stream(getApiUsers().getItems())
                .collect(Collectors.toList());
        List<ApiTagDto> tagList = Arrays.stream(getApiTags(collectIds(userList)).getItems())
                .collect(Collectors.toList());
        List<ApiUserDto> result = addTagsToUsers(userList, tagList);
        return result.stream()
                .filter(r -> r.getLocation() != null && r.getLocation().contains("United"))
                .filter(r -> r.getReputation() > 223)
                .filter(r -> r.getAnswer_count() > 1)
                .filter(r -> r.getTags().contains("java")
                        || r.getTags().contains(".net")
                        || r.getTags().contains("docker")
                        || r.getTags().contains("c#"))
                .collect(Collectors.toList());
    }

    private ApiResponseUserDto getApiUsers() {
        return httpClient.get(MAIN_URL
                + BODY_USER_URL
                + FILTER_USER_URL, ApiResponseUserDto.class);
    }

    private ApiResponseTagDto getApiTags(String apiIds) {
        return httpClient.get(MAIN_URL
                + BODY_TAG_URL_PREV
                + apiIds
                + BODY_TAG_URL_NEXT
                + PAGE_TAG
                + 1
                + LAST_TAG
                + FILTER_TAG_URL, ApiResponseTagDto.class);
    }

    private String collectIds(List<ApiUserDto> userList) {
        StringBuilder apiIdsPack = new StringBuilder();
        for (int i = 0; i < userList.size(); i++) {
            if ((userList.size() - 1) == i) {
                apiIdsPack.append(userList.get(i).getUser_id());
            } else {
                apiIdsPack.append(userList.get(i).getUser_id()).append(";");
            }
        }
        return apiIdsPack.toString();
    }

    private List<ApiUserDto> addTagsToUsers(List<ApiUserDto> userList, List<ApiTagDto> tagList) {
        String tags = "";
        for (int i = 0; i < userList.size(); i++) {
            for (int j = 0; j < tagList.size(); j++) {
                if (userList.get(i).getUser_id() == tagList.get(j).getUser_id()) {
                    tags += tagList.get(j).getName() + ", ";
                }
            }
            userList.get(i).setTags(tags);
            tags = "";
        }
        return userList;
    }
}

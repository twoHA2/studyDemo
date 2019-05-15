package com.southintel.zaokin.base.service;


import com.southintel.zaokin.base.entity.*;

public interface UserService {

    String register(RegisterData registerData);

    ServerResponse isregister(String tel);

    ServerResponse sendSMSCode(String tel, int parseInt);

    String login(String tel, String smscode);

    ServerResponse logout(String token);

    ServerResponse queryUserData(String token);

    ServerResponse putPersonData(UserDto userDto);

    ServerResponse addProject(Project project);

    ServerResponse putCollectin(String id, String project_name);

    ServerResponse getProjectCollection(CollectionReq collectionReq);

    ServerResponse delProjectCollection(String id, String projectName);

    ServerResponse getProject(CollectionReq collectionReq);
}

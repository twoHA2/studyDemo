package com.southintel.zaokin.base.mapper;


import com.southintel.zaokin.base.entity.User;

import java.util.List;


public interface UserMapper {

    User getUserById(int id);

    List<User> getUserByName(String userName);
}

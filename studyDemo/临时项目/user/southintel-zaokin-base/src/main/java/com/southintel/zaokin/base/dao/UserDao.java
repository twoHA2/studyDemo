package com.southintel.zaokin.base.dao;

import com.southintel.zaokin.base.entity.HximUser;
import com.southintel.zaokin.base.entity.RegisterData;
import com.southintel.zaokin.base.entity.User;

import java.util.List;

public interface UserDao {

    //通过id查找user信息
    User getUserById(int id);

    List<User> getUserByName(String userName);

    int insertUser(RegisterData registerData);
    int insertUserNaturePerson(RegisterData registerData);
    int insertCompanyName(RegisterData registerData);
    int insertHximUser(HximUser hximUser);
    int insertZaokinUser(RegisterData registerData);
}

package com.southintel.zaokin.base.dao;

import com.southintel.zaokin.base.entity.HximUser;
import com.southintel.zaokin.base.entity.RegisterData;
import com.southintel.zaokin.base.entity.User;
import com.southintel.zaokin.base.entity.UserDto;

import java.util.List;
import java.util.Map;

public interface UserDao {

    //通过id查找user信息
    User getUserById(int id);

    List<User> getUserByName(String userName);

    int insertUser(RegisterData registerData);

    int insertCompanyName(RegisterData registerData);
    int insertHximUser(HximUser hximUser);
    int insertZaokinUser(RegisterData registerData);

    int isregister(String tel);

    List< UserDto >  queryUserByTel(String tel);

    Map queryUserByUsre_name(String user_name);

    int updateUser(UserDto userDto);
}

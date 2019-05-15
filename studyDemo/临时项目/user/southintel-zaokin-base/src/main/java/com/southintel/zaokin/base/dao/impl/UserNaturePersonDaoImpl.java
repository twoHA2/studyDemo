package com.southintel.zaokin.base.dao.impl;

import com.southintel.zaokin.base.dao.UserNaturePersonDao;
import com.southintel.zaokin.base.entity.RegisterData;
import com.southintel.zaokin.base.entity.UserDto;
import com.southintel.zaokin.base.mapper.UserMapper;
import com.southintel.zaokin.base.mapper.UserNaturePersonMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class UserNaturePersonDaoImpl implements UserNaturePersonDao {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserNaturePersonMapper userNaturePersonMapper;

    @Override
    public int insertUserNaturePerson(RegisterData registerData) {
        return userMapper.insertUserNaturePerson(registerData);
    }

    @Override
    public int updateNaturePerson(UserDto userDto) {
        return userNaturePersonMapper.updateNaturePerson(userDto);
    }
}

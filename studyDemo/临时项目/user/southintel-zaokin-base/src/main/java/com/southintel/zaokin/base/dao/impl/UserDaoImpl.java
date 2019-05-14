package com.southintel.zaokin.base.dao.impl;

import com.southintel.zaokin.base.dao.UserDao;
import com.southintel.zaokin.base.entity.HximUser;
import com.southintel.zaokin.base.entity.RegisterData;
import com.southintel.zaokin.base.entity.User;
import com.southintel.zaokin.base.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{

    @Autowired
    UserMapper userMapper;

    @Override
    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public List<User> getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }

    @Override
    public int insertUser(RegisterData registerData) {
        return userMapper.insertUser(registerData
        );
    }

    @Override
    public int insertUserNaturePerson(RegisterData registerData) {
        return userMapper.insertUserNaturePerson(registerData);
    }

    @Override
    public int insertCompanyName(RegisterData registerData) {
        return userMapper.insertCompanyName(registerData);
    }

    @Override
    public int insertHximUser(HximUser hximUser) {
        return userMapper.insertHximUser(hximUser);
    }

    @Override
    public int insertZaokinUser(RegisterData registerData) {
        return userMapper.insertZaokinUser(registerData);
    }
}

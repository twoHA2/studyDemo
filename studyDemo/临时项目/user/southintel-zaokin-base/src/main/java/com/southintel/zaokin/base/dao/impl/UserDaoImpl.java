package com.southintel.zaokin.base.dao.impl;

import com.southintel.zaokin.base.dao.UserDao;
import com.southintel.zaokin.base.entity.HximUser;
import com.southintel.zaokin.base.entity.RegisterData;
import com.southintel.zaokin.base.entity.User;
import com.southintel.zaokin.base.entity.UserDto;
import com.southintel.zaokin.base.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao{

    @Autowired
    UserMapper userMapper;

    @Override
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    @Override
    public int insertHximUser(HximUser hximUser) {
        return userMapper.insertHximUser(hximUser);
    }

    @Override
    public int isregister(String tel) {
        return userMapper.isregister(tel);
    }

    @Override
    public List< UserDto >  queryUserByTel(String tel) {
        return userMapper.queryUserByTel(tel);
    }

    @Override
    public List< UserDto > queryUserByUser_name(String user_name) {
        return userMapper.queryUserByUser_name(user_name);
    }

    @Override
    public int queryHXUserByAccount(String account) {
        return userMapper.queryHXUserByAccount(account);
    }

    @Override
    public int updateUser(UserDto userDto) {
        return userMapper.updateUser(userDto);
    }
}

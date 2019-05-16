package com.southintel.zaokin.base.dao.impl;

import com.southintel.zaokin.base.dao.UserNaturePersonDao;
import com.southintel.zaokin.base.entity.UserDto;
import com.southintel.zaokin.base.entity.UserNaturePerson;
import com.southintel.zaokin.base.mapper.UserNaturePersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserNaturePersonDaoImpl implements UserNaturePersonDao {
    @Autowired
    UserNaturePersonMapper userNaturePersonMapper;

    @Override
    public int insertUserNaturePerson(UserNaturePerson userNaturePerson) {
        return userNaturePersonMapper.insertUserNaturePerson(userNaturePerson);
    }

    @Override
    public int updateNaturePerson(UserDto userDto) {
        return userNaturePersonMapper.updateNaturePerson(userDto);
    }
}

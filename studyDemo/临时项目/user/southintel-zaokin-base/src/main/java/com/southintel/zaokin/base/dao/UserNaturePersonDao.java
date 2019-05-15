package com.southintel.zaokin.base.dao;

import com.southintel.zaokin.base.entity.RegisterData;
import com.southintel.zaokin.base.entity.UserDto;

public interface UserNaturePersonDao {

    int insertUserNaturePerson(RegisterData registerData);

    int updateNaturePerson(UserDto userDto);
}

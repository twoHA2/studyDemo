package com.southintel.zaokin.base.dao;

import com.southintel.zaokin.base.entity.RegisterData;
import com.southintel.zaokin.base.entity.UserDto;
import com.southintel.zaokin.base.entity.UserNaturePerson;

public interface UserNaturePersonDao {

    int insertUserNaturePerson(UserNaturePerson userNaturePerson);

    int updateNaturePerson(UserDto userDto);
}

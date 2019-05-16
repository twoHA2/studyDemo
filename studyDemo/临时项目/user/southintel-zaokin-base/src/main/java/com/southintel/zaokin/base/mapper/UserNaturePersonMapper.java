package com.southintel.zaokin.base.mapper;

import com.southintel.zaokin.base.entity.UserDto;
import com.southintel.zaokin.base.entity.UserNaturePerson;
import org.mapstruct.Mapper;

@Mapper
public interface UserNaturePersonMapper {
    /**
     * 用户自然人信息表
     * @param userNaturePerson
     * @return
     */
    int insertUserNaturePerson(UserNaturePerson userNaturePerson);
    int updateNaturePerson(UserDto userDto);
}

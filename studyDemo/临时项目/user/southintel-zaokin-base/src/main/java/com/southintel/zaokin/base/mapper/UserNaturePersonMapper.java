package com.southintel.zaokin.base.mapper;

import com.southintel.zaokin.base.entity.UserDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserNaturePersonMapper {

    int updateNaturePerson(UserDto userDto);
}

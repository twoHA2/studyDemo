package com.southintel.zaokin.base.mapper;

import com.southintel.zaokin.base.entity.Company;
import com.southintel.zaokin.base.entity.UserDto;
import org.mapstruct.Mapper;

@Mapper
public interface CompanyMapper {

    int updateCompany(UserDto userDto);

    int insertCompanyName(Company company);
}

package com.southintel.zaokin.base.dao;

import com.southintel.zaokin.base.entity.Company;
import com.southintel.zaokin.base.entity.UserDto;

public interface CompanyDao {
    int insertCompanyName(Company company);
    int updateCompany(UserDto userDto);
}

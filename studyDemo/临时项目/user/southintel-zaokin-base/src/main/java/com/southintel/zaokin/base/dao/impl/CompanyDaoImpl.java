package com.southintel.zaokin.base.dao.impl;

import com.southintel.zaokin.base.dao.CompanyDao;
import com.southintel.zaokin.base.entity.Company;
import com.southintel.zaokin.base.entity.RegisterData;
import com.southintel.zaokin.base.entity.UserDto;
import com.southintel.zaokin.base.mapper.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDaoImpl implements CompanyDao {
    @Autowired
    CompanyMapper companyMapper;

    @Override
    public int insertCompanyName(Company company) {
        return companyMapper.insertCompanyName(company);
    }

    @Override
    public int updateCompany(UserDto userDto) {
        return companyMapper.updateCompany(userDto);
    }
}

package com.southintel.zaokin.base.mapper;


import com.southintel.zaokin.base.entity.HximUser;
import com.southintel.zaokin.base.entity.RegisterData;
import com.southintel.zaokin.base.entity.User;
import com.southintel.zaokin.base.entity.UserDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserMapper {

    User getUserById(int id);

    List<User> getUserByName(String userName);

    /**
     * 用户基础信息表
     * @param registerData
     * @return
     */
    int insertUser(RegisterData registerData);

    /**
     * 用户自然人信息表
     * @param registerData
     * @return
     */
    int insertUserNaturePerson(RegisterData registerData);

    /**
     * 公司信息表
     * @param registerData
     * @return
     */
    int insertCompanyName(RegisterData registerData);

    /**
     * 环信用户表
     * @param hximUser
     * @return
     */
    int insertHximUser(HximUser hximUser);

    /**
     * 招金汇用户信息表
     * @param registerData
     * @return
     */
    int insertZaokinUser(RegisterData registerData);

    /**
     * 查询电话是否已注册
     * @param tel
     * @return
     */
    int isregister(@Param("tel") String tel);

    /**
     * 查询user
     * @param tel
     * @return
     */
    List< UserDto >  queryUserByTel(@Param("tel") String tel);

    int updateUser(UserDto userDto);

}

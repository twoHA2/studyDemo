package com.southintel.zaokin.base.mapper;


import com.southintel.zaokin.base.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserMapper {

    User getUserById(int id);

    List<User> getUserByName(String userName);

    /**
     * 用户基础信息表
     * @param user
     * @return
     */
    int insertUser(User user);

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

    List< UserDto> queryUserByUser_name(@Param("user_name") String user_name);

    int queryHXUserByAccount(@Param("account") String account);
}

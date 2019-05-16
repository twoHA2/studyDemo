package com.southintel.zaokin.base.dao;

import com.southintel.zaokin.base.entity.*;

import java.util.List;
import java.util.Map;

public interface UserDao {
    /**
     * 新增用户基础表信息
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 是否已注册
     * @param tel
     * @return
     */
    int isregister(String tel);

    /**
     * 更新用户基础表
     * @param userDto
     * @return
     */
    int updateUser(UserDto userDto);

    //*********************分隔符**************************

    /**
     * 新增环信IM信息表
     * @param hximUser
     * @return
     */
    int insertHximUser(HximUser hximUser);

    /**
     * 根据电话查询用户信息 （涉及多张表）
     * @param tel
     * @return
     */
    List< UserDto >  queryUserByTel(String tel);

    /**
     * 根据user_name查询用户信息 （多张表）
     * @param user_name
     * @return
     */
    List< UserDto> queryUserByUser_name(String user_name);

    int queryHXUserByAccount(String account);
}

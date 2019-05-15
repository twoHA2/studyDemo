package com.southintel.zaokin.base.controller;

import com.southintel.zaokin.base.entity.*;
import com.southintel.zaokin.base.enums.ResultEnum;
import com.southintel.zaokin.base.exception.BusinessException;
import com.southintel.zaokin.base.service.UserService;
import com.southintel.zaokin.base.util.BeanUtil;
import com.southintel.zaokin.base.util.JsonUtil;
import com.southintel.zaokin.base.util.PatternUtil;
import com.southintel.zaokin.base.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 注册
     * @param registerData
     * @return
     */
    @RequestMapping(value = "/register")
    public String register(@RequestBody RegisterData registerData){
        return userService.register(registerData);
    }

    /**
     * 是否已注册
     * @param request
     * @return
     */
    @RequestMapping(value = "/isregister",method = RequestMethod.POST)
    public ServerResponse isregister(HttpServletRequest request){
        return userService.isregister(request.getParameter("tel"));
    }

    /**
     * 发送验证码
     * @param request
     * @return
     */
    @RequestMapping(value = "/SMSCode",method = RequestMethod.POST)
    public ServerResponse sendSMSCode(HttpServletRequest request){
        String tel = request.getParameter("tel");
        String type = request.getParameter("type");
        if(!PatternUtil.isMobile(tel)){
            throw new BusinessException(ResultEnum.TEL_ERROR.getCode(),ResultEnum.TEL_ERROR.getMsg());
        }else if(StringUtils.isEmpty(type)){
            throw new BusinessException(ResultEnum.PARAMS_ERROR.getCode(),ResultEnum.PARAMS_ERROR.getMsg());
        }
        return userService.sendSMSCode(tel,Integer.parseInt(type));
    }

    /**
     * 登陆
     * @param request
     * @return
     */
    @RequestMapping("/login")
    public String login(HttpServletRequest request){
        String tel = request.getParameter("tel");
        String smscode = request.getParameter("smscode");
        if(!PatternUtil.isMobile(tel)){
            throw new BusinessException(ResultEnum.TEL_ERROR.getCode(),ResultEnum.TEL_ERROR.getMsg());
        }else if(StringUtils.isEmpty(smscode)){
            throw new BusinessException(ResultEnum.VERIFCODE_ISEMPTY.getCode(),ResultEnum.VERIFCODE_ISEMPTY.getMsg());
        }
        return userService.login(tel,smscode);
    }

    /**
     * 登出
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout")
    public ServerResponse logout(HttpServletRequest request){
        String token = RequestUtil.getToken(request);
        return userService.logout(token);
    }

    /**
     * 认证身份
     * @param request
     * @return
     */
    @RequestMapping(value = "/attestation/person")
    public ServerResponse attestationPerson(HttpServletRequest request){
        String front_url = request.getParameter("front_url");
        String side_url = request.getParameter("side_url");
        if(StringUtils.isEmpty(front_url)){
            throw new BusinessException(ResultEnum.FRONT_URL_NOFIND.getCode(),ResultEnum.FRONT_URL_NOFIND.getMsg());
        }else if(StringUtils.isEmpty(side_url)){
            throw new BusinessException(ResultEnum.SIDE_URL_NOFIND.getCode(),ResultEnum.SIDE_URL_NOFIND.getMsg());
        }
//        return userService.attestation();
        return null;
    }

    /**
     * 个人信息查询
     * @param request
     * @return
     */
    @RequestMapping(value = "/get_person_data")
    public ServerResponse getpersondata(HttpServletRequest request){
        String token = RequestUtil.getToken(request);
        return userService.queryUserData(token);
    }

    /**
     * 修改个人信息
     * @param userDto
     * @return
     */
    @RequestMapping(value = "/put_person_data")
    public ServerResponse putPersonData(@RequestBody UserDto userDto){
        return userService.putPersonData(userDto);
    }

    /**
     * 项目发布
     * @param project
     * @return
     */
    @RequestMapping(value = "/add_project")
    public ServerResponse addProject(@RequestBody Project project){
        return userService.addProject(project);
    }

    /**
     * 资产查询
     * @param collectionReq
     * @return
     */
    @RequestMapping(value = "/get_project")
    public ServerResponse getProject(@RequestBody CollectionReq collectionReq){
        if(StringUtils.isEmpty(collectionReq.getStartIndex()) || StringUtils.isEmpty(collectionReq.getPageSize())){
            throw new BusinessException(ResultEnum.PARAMS_ERROR.getCode(),ResultEnum.PARAMS_ERROR.getMsg());
        }
        return userService.getProject(collectionReq);
    }
    /**
     * 项目收藏  需转招金服务
     * @param request
     * @return
     */
    @RequestMapping(value = "/put_project_collection")
    public ServerResponse putCollection(HttpServletRequest request){
        String id = request.getParameter("id");
        String project_name = request.getParameter("project_name");
        if(StringUtils.isEmpty(id)){
            throw new BusinessException(ResultEnum.PARAMS_ERROR.getCode(),ResultEnum.PARAMS_ERROR.getMsg());
        }
        return userService.putCollectin(id,project_name);
    }

    /**
     * 项目收藏查询
     * @param request
     * @return
     */
    @RequestMapping(value = "/get_project_collection")
    public ServerResponse getProjectCollection(HttpServletRequest request){
        String startIndex = request.getParameter("startIndex");
        String pageSize = request.getParameter("pageSize");
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        if(StringUtils.isEmpty(startIndex) || StringUtils.isEmpty(pageSize)){
            throw new BusinessException(ResultEnum.PARAMS_ERROR.getCode(),ResultEnum.PARAMS_ERROR.getMsg());
        }
        CollectionReq collectionReq = new CollectionReq();
        collectionReq.setStartIndex(Integer.parseInt(startIndex));
        collectionReq.setPageSize(Integer.parseInt(pageSize));
        collectionReq.setBeginDate(beginDate);
        collectionReq.setEndDate(endDate);
        return userService.getProjectCollection(collectionReq);
    }

    /**
     * 取消项目收藏
     * @param request
     * @return
     */
    @RequestMapping(value = "/del_project_collection")
    public ServerResponse delProjectCollection(HttpServletRequest request){
        String id = request.getParameter("id");
        String projectName = request.getParameter("project_name");
        if(StringUtils.isEmpty(id)){
            throw new BusinessException(ResultEnum.PARAMS_ERROR.getCode(),ResultEnum.PARAMS_ERROR.getMsg());
        }
        return userService.delProjectCollection(id,projectName);
    }
}

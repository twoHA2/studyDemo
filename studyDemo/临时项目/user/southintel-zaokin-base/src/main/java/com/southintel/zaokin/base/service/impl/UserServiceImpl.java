package com.southintel.zaokin.base.service.impl;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.southintel.zaokin.base.constant.Constant;
import com.southintel.zaokin.base.dao.*;
import com.southintel.zaokin.base.entity.*;
import com.southintel.zaokin.base.enums.BaseResult;
import com.southintel.zaokin.base.enums.BaseResultEnum;
import com.southintel.zaokin.base.enums.ResultEnum;
import com.southintel.zaokin.base.exception.BusinessException;
import com.southintel.zaokin.base.service.UserService;
import com.southintel.zaokin.base.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    UserNaturePersonDao userNaturePersonDao;
    @Autowired
    CompanyDao companyDao;

    @Value("${gatewayService.host}")
    private String HXUrl;
    @Value("${SMSService.host}")
    private String SMSService;
    @Value("${ZaoKinService.host}")
    private String zaokinService;

    @Transactional
    @Override
    public String register(RegisterData registerData) {
//        Assert.isAllFieldNull(registerData);
        //生成唯一ID
        String idt = (Constant.ONLYID + UUID.randomUUID());
         registerData.setUser_name( idt);
        //将注册信息分发各张表中
        //基础信息表
        List< UserDto > users = userDao.queryUserByTel(registerData.getTel());
        if(users!=null && users.size()>0 ){
            throw new BusinessException(ResultEnum.INCORRECT_CREDENTIALS_ERROR.getCode(),ResultEnum.INCORRECT_CREDENTIALS_ERROR.getMsg());
        }
        int i1 = userDao.insertUser(registerData);
        int i2 = userNaturePersonDao.insertUserNaturePerson(registerData);//自然人信息
        int i3 = userDao.insertCompanyName(registerData);//公司
        //招金汇用户
        ZaokinUser zaokinUser = new ZaokinUser();
        zaokinUser.setAvatar_url(registerData.getAvatar_url());
        zaokinUser.setCard_url(registerData.getCard_url());
        zaokinUser.setType(registerData.getType());
        zaokinUser.setUser_name(registerData.getUser_name());
        String s = HttpUtils.doPost(HXUrl + "/zaokin/register", JsonUtil.toJson(zaokinUser));
        if(s==null) throw new BusinessException(new BaseResult(BaseResultEnum.HXZH_ERROR));
        ServerResponse serverResponse = JsonUtil.toObject(s, ServerResponse.class);
        String data1 = serverResponse.getData().toString();
        Map<String,Integer> map = JsonUtil.toObject(data1, Map.class);
        int size = map.get("size");
        if(size<=0){
            throw new BusinessException(new BaseResult(BaseResultEnum.HXZH_ERROR));
        }
        //注册环信
        JsonObject jso = new JsonObject();
        jso.addProperty("tel",registerData.getTel());
        String s1 = HttpUtils.doPost(HXUrl + "/policy/hx/register", jso.getAsString());
        if(s==null) throw new BusinessException(new BaseResult(BaseResultEnum.HXZH_ERROR));
        JsonObject result = new JsonParser().parse(s).getAsJsonObject();
        if(!result.isJsonNull() && result.get("retcode").getAsInt()== 0){
            JsonObject data = result.get("data").getAsJsonObject();
            HximUser hximUser = new HximUser();
            hximUser.setUser_name(idt);
            hximUser.setAccount(data.get("account").getAsString());
            hximUser.setPassword(data.get("password").getAsString());
            hximUser.setNickname(data.get("name").getAsString());
            int i = userDao.insertHximUser(hximUser);
            if(i<=0){
                throw new BusinessException(new BaseResult(BaseResultEnum.HXZH_ERROR));
            }
        }else{
            throw new BusinessException(new BaseResult(BaseResultEnum.HXZH_ERROR));
        }
        //登陆
        UserDto user = new UserDto();
        user.setTel(registerData.getTel());
        user.setUser_name(idt);
        user.setAvatar_url(registerData.getAvatar_url());
        user.setCompany(registerData.getCompany());
        user.setTel(registerData.getTel());
        user.setPosition(registerData.getPosition());
        user.setDepartment(registerData.getDepartment());
        return createToken(user);
    }

    @Override
    public ServerResponse isregister(String tel) {
        if(!PatternUtil.isMobile(tel)){
            throw new BusinessException(ResultEnum.TEL_ERROR.getCode(),ResultEnum.TEL_ERROR.getMsg());
        }
        int isregister = userDao.isregister(tel);
        Map< String, Boolean > map = new HashMap();
        if(isregister >0){
            map.put("exists",true);
        }else{
            map.put("exists",false);
        }
        return ServerResponse.successWithData(map);
    }

    @Override
    public ServerResponse sendSMSCode(String tel, int type) {
        String pries="";
        if(type == 0){
            //注册
            pries =Constant.REGISTER_SMSCODE_PREFIX;
        }else if(type == 1){
            //登陆
            pries =Constant.LOGIN_SMSCODE_PREFIX;
        }
        String code =  CommentUtil.vcode();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phone",tel);
        jsonObject.addProperty("message",code);
        String s = HttpUtils.doPost(SMSService + "/sms/zaoSend", jsonObject.getAsString());
        if(s==null) throw new BusinessException(new BaseResult(BaseResultEnum.VERIFCODE_CREATE_ERROR));
        TokenUtil.setValue(pries+tel,code,Constant.SMSCODE_EXPIRE);
        return ServerResponse.success();
    }

    @Override
    public String login(String tel, String smscode) {
        List< UserDto > list = userDao.queryUserByTel(tel);
        //用户不存在
        if(list == null || list.size()<=0)  throw new BusinessException(new BaseResult(BaseResultEnum.INVALID_CUSTOMER));
        UserDto user = list.get(0);
        //是否禁用
        if(user.getStatus() ==1){
            throw new BusinessException(new BaseResult(BaseResultEnum.USER_FORBIDDEN));
        }
        //验证码失效
        if(!TokenUtil.isValid(Constant.LOGIN_SMSCODE_PREFIX+tel))
            throw new BusinessException(new BaseResult(BaseResultEnum.VERIFCODE_EXPIRE));
        //验证验证码
        if(!smscode.equals(TokenUtil.get(Constant.LOGIN_SMSCODE_PREFIX+tel))){
            throw new BusinessException(new BaseResult(BaseResultEnum.VERIFCODE_ERROR));
        }
        return createToken(user);
    }

    @Override
    public ServerResponse logout(String token) {
        TokenUtil.deleteToken(Constant.AUTH_TOKEN_PREFIX +token);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse queryUserData(String token) {
        UserDto user = JsonUtil.toObject(TokenUtil.get(Constant.AUTH_TOKEN_PREFIX + token), UserDto.class);
        return ServerResponse.successWithData(user);
    }

    @Override
    public ServerResponse putPersonData(UserDto userDto) {
        UserDto user = RequestUtil.currentUser();
        userDto.setUser_name(user.getUser_name());
        //更新用户基础表
        if(!StringUtils.isEmpty(userDto.getTel())){
            int userCount = userDao.updateUser(userDto);
            Assert.sqlIsSuccess(userCount,ResultEnum.UPDATE_FAIL);
        }
        //更新自然人信息表
        if(!StringUtils.isEmpty(userDto.getPerson_name())){
            //更新姓名
            int i = userNaturePersonDao.updateNaturePerson(userDto);
            Assert.sqlIsSuccess(i,ResultEnum.UPDATE_FAIL);
        }
        //更新公司
        String company = user.getCompany();//公司名不可修改
        userDto.setCompany(company);
        if(!StringUtils.isEmpty(userDto.getDepartment()) || !StringUtils.isEmpty(userDto.getPosition()) || !StringUtils.isEmpty(userDto.getBrief_introduction())){
            int companyCount = companyDao.updateCompany(userDto);
            Assert.sqlIsSuccess(companyCount,ResultEnum.UPDATE_FAIL);
        }
        //更新用户头像
        if(!StringUtils.isEmpty(userDto.getAvatar_url())){
            ZaokinUser zaokinUser = new ZaokinUser();
            zaokinUser.setUser_name(user.getUser_name());
            zaokinUser.setAvatar_url(userDto.getAvatar_url());
            String s = HttpUtils.doPost(zaokinService + "/xx", JsonUtil.toJson(zaokinUser));
            if(s == null){
                throw new BusinessException(ResultEnum.UPDATE_FAIL.getCode(),ResultEnum.UPDATE_FAIL.getMsg());
            }
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse addProject(Project project) {
        UserDto user = RequestUtil.currentUser();
        project.setCompany_id(user.getCompany());
        project.setUser_name(user.getUser_name());
        if(project == null )throw new BusinessException(ResultEnum.PROJECT_PUBLISH_FAILY.getCode(),ResultEnum.PROJECT_PUBLISH_FAILY.getMsg());
        String s = HttpUtils.doPost(zaokinService + "/xx", JsonUtil.toJson(project));
        if(s == null){
            throw new BusinessException(ResultEnum.PROJECT_PUBLISH_FAILY.getCode(),ResultEnum.PROJECT_PUBLISH_FAILY.getMsg());
        }
        createErrorObject(s);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse putCollectin(String id, String project_name) {
        UserDto user = RequestUtil.currentUser();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id",id);
        jsonObject.addProperty("project_name",project_name);
        jsonObject.addProperty("user_name",user.getUser_name());
        String s = HttpUtils.doPost(zaokinService + "/xx", JsonUtil.toJson(jsonObject));
        if(s == null){
            throw new BusinessException(ResultEnum.PROJECT_COLLECTION_FAILY.getCode(),ResultEnum.PROJECT_COLLECTION_FAILY.getMsg());
        }
        createErrorObject(s);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse getProjectCollection(CollectionReq collectionReq) {
        UserDto user = RequestUtil.currentUser();
        collectionReq.setUser_name(user.getUser_name());
        String s = HttpUtils.doPost(zaokinService + "/xx", JsonUtil.toJson(collectionReq));
        if(s == null){
            throw new BusinessException(ResultEnum.PROJECT_COLLECTIN_QUERY_FAILY.getCode(),ResultEnum.PROJECT_COLLECTIN_QUERY_FAILY.getMsg());
        }
        createErrorObject(s);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse delProjectCollection(String id, String projectName) {
        UserDto user = RequestUtil.currentUser();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id",id);;
        jsonObject.addProperty("projectName",projectName);
        jsonObject.addProperty("user_name",user.getUser_name());
        String s = HttpUtils.doPost(zaokinService + "/xxeee", JsonUtil.toJson(jsonObject));
        if(s == null){
            throw new BusinessException(ResultEnum.PROJECT_COLLECTIN_QUERY_FAILY.getCode(),ResultEnum.PROJECT_COLLECTIN_QUERY_FAILY.getMsg());
        }
        createErrorObject(s);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse getProject(CollectionReq collectionReq) {
        UserDto user = RequestUtil.currentUser();
        collectionReq.setUser_name(user.getUser_name());
        String s = HttpUtils.doPost(zaokinService + "/xeex", JsonUtil.toJson(collectionReq));
        if(s == null){
            throw new BusinessException(ResultEnum.PROJECT_QUERY_FAILY.getCode(),ResultEnum.PROJECT_QUERY_FAILY.getMsg());
        }
        createErrorObject(s);
        return ServerResponse.success();
    }

    private void createErrorObject(String s) {
        if(StringUtils.isEmpty(s)){
            throw new BusinessException(new BaseResult(BaseResultEnum.INTERFACE_ERROR));
        }
        JsonObject jso = new JsonParser().parse(s).getAsJsonObject();
        int retcode = jso.get("retcode").getAsInt();
        if (retcode != 1) {
            throw new BusinessException(jso.get("retcode").getAsInt(), jso.get("errmsg").getAsString());
        }
    }

    private String createToken(UserDto user) {
        //存储redis，返回token
        String s = TokenUtil.obtainRefreshToken(user);
        Map< String, String > map = new HashMap<>();
        map.put("token",s);
        ServerResponse serverResponse = ServerResponse.successWithData(map);
        log.info("登陆成功："+user.toString());
        return JsonUtil.toJson(serverResponse);
    }


}

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

    @Value("${SMSService.host}")
    private String SMSService;
    @Value("${ZaoKinService.host}")
    private String zaokinService;
    @Value("${HXService.host}")
    private String HXService;

    @Transactional
    @Override
    public String register(RegisterData registerData) {
        Assert.isAllFieldNull(registerData);
        String s = TokenUtil.get(Constant.REGISTER_SMSCODE_PREFIX + registerData.getTel());
        if(StringUtils.isEmpty(s)){
            throw new BusinessException(new BaseResult(BaseResultEnum.VERIFCODE_EXPIRE));
        }
        if(!registerData.getSms_code().equals(s)){
            throw new BusinessException(new BaseResult(BaseResultEnum.VERIFCODE_ERROR));
        }
        //生成唯一ID
        String idt = (Constant.ONLYID + UUID.randomUUID());
        log.info(String.format("开始注册:[ %s ]", registerData.toString()));
        //将注册信息分发各张表中
        //基础信息表
        List< UserDto > users = userDao.queryUserByTel(registerData.getTel());
        if(users!=null && users.size()>0 ){
            throw new BusinessException(ResultEnum.INCORRECT_CREDENTIALS_ERROR);
        }
        insertUser(registerData, idt);
        insertNaturePerson(registerData,idt);
//        insertCompany(registerData);//认证后新增
       insertZaokinUser(registerData, idt);
        insetHXUser(registerData, idt);
        //登陆
        UserDto user = new UserDto();
        user.setTel(registerData.getTel());
        user.setUser_name(idt);
        user.setAvatar_url(registerData.getAvatar_url());
        user.setCompany(registerData.getCompany());
        user.setTel(registerData.getTel());
        user.setPosition(registerData.getPosition());
        user.setDepartment(registerData.getDepartment());
        user.setBrief_introduction(registerData.getBrief_introduction());
        return createToken(user);
    }

    /**
     * 新增环信用户
     * @param registerData
     * @param idt user_name
     */
    private void insetHXUser(RegisterData registerData, String idt ) {
        //注册环信
        JsonObject jso = new JsonObject();
        jso.addProperty("tel",registerData.getTel());
        jso.addProperty("name","");
        jso.addProperty("appType","zaok");
        String s =null;
        try {
           s= HttpUtils.doPost(HXService + "/api/hx/register", jso.toString());
        }catch (Exception e){
            if (e instanceof BusinessException) {
                throw (BusinessException) e;
            }
            throw new BusinessException(BaseResultEnum.HXZH_ERROR.getCode(),BaseResultEnum.HXZH_ERROR.getMsg());
        }
        if(s==null) throw new BusinessException(new BaseResult(BaseResultEnum.HXZH_ERROR));
        JsonObject result = new JsonParser().parse(s).getAsJsonObject();
        if(!result.isJsonNull()){
            JsonObject data = result.get("data").getAsJsonObject();
            if(data.get("account") ==null || StringUtils.isEmpty(data.get("account").getAsString()))throw new BusinessException(new BaseResult(BaseResultEnum.HXZH_ERROR));
            int account = userDao.queryHXUserByAccount(data.get("account").getAsString());
            if(account < 1){
                HximUser hximUser = new HximUser();
                hximUser.setUser_name(idt);
                hximUser.setAccount(data.get("account").getAsString());
                hximUser.setPassword(data.get("password").getAsString());
                hximUser.setNickname(data.get("name").getAsString());
                int i = userDao.insertHximUser(hximUser);
                if(i<=0){
                    throw new BusinessException(new BaseResult(BaseResultEnum.HXZH_ERROR));
                }
            }
        }else{
            throw new BusinessException(new BaseResult(BaseResultEnum.HXZH_ERROR));
        }
    }

    /**
     * 新增招金用户
     * @param registerData
     * @param idt
     * @return
     */
    private String insertZaokinUser(RegisterData registerData, String idt) {
        //招金汇用户
        ZaokinUser zaokinUser = new ZaokinUser();
        zaokinUser.setAvatarUrl(registerData.getAvatar_url());
        zaokinUser.setCardUrl(registerData.getCard_url());
        zaokinUser.setUserType(registerData.getType());
        zaokinUser.setUserName(idt);
        String s =null;
        try {
              s = HttpUtils.doPost(zaokinService + "/zaok/user/register", JsonUtil.toJson(zaokinUser));
        }catch (Exception e){
            if( e instanceof BusinessException)throw e;
            throw  new BusinessException(ResultEnum.REGISTER_FAILY);
        }
        if(s==null) throw new BusinessException(ResultEnum.REGISTER_FAILY);
        ServerResponse serverResponse = JsonUtil.toObject(s, ServerResponse.class);
        int retcode = serverResponse.getRetcode();
        if(retcode !=0){
            throw new BusinessException(ResultEnum.REGISTER_FAILY);
        }
        return s;
    }

    /**
     * 新增公司信息
     * @param registerData
     */
    private void insertCompany(RegisterData registerData) {
        Company company = new Company();
        company.setCompany_name(registerData.getCompany());
        int i = companyDao.insertCompanyName(company);//公司
        Assert.sqlIsSuccess(i,ResultEnum.REGISTER_FAILY);
        log.info("新增公司信息完成!");
    }

    /**
     * 新增自然人信息表
     * @param registerData 注册信息
     * @param user_name user_nmae
     */
    private void insertNaturePerson(RegisterData registerData,String user_name) {
        UserNaturePerson usernature = new UserNaturePerson();
        usernature.setCompany(registerData.getCompany());
        usernature.setDepartment(registerData.getDepartment());
        usernature.setPserson_name(registerData.getName());
        usernature.setPosition(registerData.getPosition());
        usernature.setUser_name(user_name);
        usernature.setBrief_introduction(registerData.getBrief_introduction());
        int i = userNaturePersonDao.insertUserNaturePerson(usernature);//自然人信息
        Assert.sqlIsSuccess(i,ResultEnum.REGISTER_FAILY);
        log.info("新增自然人信息完成!");
    }

    /**
     * 新增user
     * @param registerData 注册信息
     * @param idt user_nmae
     */
    private void insertUser(RegisterData registerData, String idt) {
        User user1 = new User();
        user1.setUser_name(idt);
        user1.setTel(registerData.getTel());
        int i = userDao.insertUser(user1);
        Assert.sqlIsSuccess(i,ResultEnum.REGISTER_FAILY);
        log.info("新增用户基础信息完成!");
    }

    @Override
    public ServerResponse isregister(String tel) {
        if(!PatternUtil.isMobile(tel)){
            throw new BusinessException(ResultEnum.TEL_ERROR);
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

    /**
     * 发送短信
     * @param tel
     * @param type 0：注册 1：登陆
     * @return
     */
    @Override
    public ServerResponse sendSMSCode(String tel, int type) {
        String pries="";
        String m = "";
        if(type == 0){
            //注册
            pries =Constant.REGISTER_SMSCODE_PREFIX;
            m="注册";
        }else if(type == 1){
            //登陆
            pries =Constant.LOGIN_SMSCODE_PREFIX;
            m="登陆";
        }
        if(StringUtils.isEmpty(m)){
            throw new BusinessException(ResultEnum.NOT_TYPE);
        }
        String code =  CommentUtil.vcode();
        JsonObject jsonObject = new JsonObject();
        String msg =  String.format(Constant.MESSAGE_MB, m,code);
        jsonObject.addProperty("phone",tel);
        jsonObject.addProperty("message",msg);
        String s = HttpUtils.doPost(SMSService + "/api/sms/zaok/send", jsonObject.toString());
        if(s==null) throw new BusinessException(new BaseResult(BaseResultEnum.VERIFCODE_CREATE_ERROR));
        TokenUtil.setValue(pries+tel,code,Constant.SMSCODE_EXPIRE);
        return ServerResponse.success();
    }

    /**
     * 登陆
     * @param tel
     * @param smscode
     * @return
     */
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

        getZaokUser(user);
        return createToken(user);
    }

    private void getZaokUser(UserDto user) {
        ZaokinUser zaokinUser = new ZaokinUser();
        zaokinUser.setUserName(user.getUser_name());
        String s = null;
        try {
            s = HttpUtils.doPost(zaokinService + "/zaok/user/get", JsonUtil.toJson(zaokinUser));
        }catch (Exception e){
            if( e instanceof BusinessException)throw e;
            throw new BusinessException(ResultEnum.LOGIN_FAILY);
        }
        if(s==null)  throw new BusinessException(ResultEnum.LOGIN_FAILY);
        JsonObject jso = new JsonParser().parse(s).getAsJsonObject();
        int retcode = jso.get("retcode").getAsInt();
        if(retcode !=0){
            throw new BusinessException(ResultEnum.LOGIN_FAILY);
        }
        JsonObject data =  jso.get("data").getAsJsonObject();
        if(data!=null){
            user.setType(data.get("userType").getAsInt());
            user.setAvatar_url(data.get("avatarUrl").getAsString());
            Integer attestationPerson = data.get("attestationPerson").getAsInt();
            Integer attestationCompany = data.get("attestationCompany").getAsInt();
            if(attestationCompany ==1 && attestationPerson ==1){
                user.setStatus(1);
            }else{
                user.setStatus(0);
            }
        }
    }

    /**
     * 登出
     * @param token
     * @return
     */
    @Override
    public ServerResponse logout(String token) {
        TokenUtil.deleteToken(Constant.AUTH_TOKEN_PREFIX +token);
        return ServerResponse.success();
    }

    /**
     * 获取当前用户信息
     * @param token
     * @return
     */
    @Override
    public ServerResponse queryUserData(String token) {
        UserDto user = JsonUtil.toObject(TokenUtil.get(Constant.AUTH_TOKEN_PREFIX + token), UserDto.class);
        return ServerResponse.successWithData(user);
    }

    /**
     * 更新用户信息
     * @param userDto
     * @return
     */
    @Transactional
    @Override
    public ServerResponse putPersonData(UserDto userDto) {
        UserDto user = RequestUtil.currentUser();
        userDto.setUser_name(user.getUser_name());
        //更新用户基础表
        List< UserDto > userDtos = userDao.queryUserByTel(userDto.getTel());
        if(userDtos != null && userDtos.size()>0) throw new BusinessException(ResultEnum.INCORRECT_CREDENTIALS_ERROR);
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

        //更新用户头像
        if(!StringUtils.isEmpty(userDto.getAvatar_url())){
            ZaokinUser zaokinUser = new ZaokinUser();
            zaokinUser.setUserName(user.getUser_name());
            zaokinUser.setAvatarUrl(userDto.getAvatar_url());
            String s = null;
            try {
                s = HttpUtils.doPost(zaokinService + "/zaok/user/update", JsonUtil.toJson(zaokinUser));
            }catch (Exception e){
                if( e instanceof BusinessException)throw e;
                throw new BusinessException(ResultEnum.UPDATE_FAIL);
            }
            if(s==null)  throw new BusinessException(ResultEnum.UPDATE_FAIL);
            ServerResponse serverResponse = JsonUtil.toObject(s, ServerResponse.class);
            int retcode = serverResponse.getRetcode();
            if(retcode !=0){
                throw new BusinessException(ResultEnum.UPDATE_FAIL);
            }
        }
        //重新更新redis中的userDto
        List< UserDto > list = userDao.queryUserByUser_name(user.getUser_name());
        //用户不存在
        if(list == null || list.size()<=0)  throw new BusinessException(new BaseResult(BaseResultEnum.INVALID_CUSTOMER));
        UserDto u = list.get(0);
        getZaokUser(u);
        String token = RequestUtil.getToken();
        TokenUtil.setValue(Constant.AUTH_TOKEN_PREFIX+token,u,-1);
        return ServerResponse.success();
    }

    /**
     * 项目发布
     * @param project
     * @return
     */
    @Override
    public ServerResponse addProject(Project project) {
        Assert.isAllFieldNull(project);
        UserDto user = RequestUtil.currentUser();
        project.setCompany_id(user.getCompany());
        project.setUser_name(user.getUser_name());
        String s = HttpUtils.doPost(zaokinService + "/xx", JsonUtil.toJson(project));
        if(s == null){
            throw new BusinessException(ResultEnum.PROJECT_PUBLISH_FAILY);
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
            throw new BusinessException(ResultEnum.PROJECT_COLLECTION_FAILY);
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
            throw new BusinessException(ResultEnum.PROJECT_COLLECTIN_QUERY_FAILY);
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
            throw new BusinessException(ResultEnum.PROJECT_COLLECTIN_QUERY_FAILY);
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
            throw new BusinessException(ResultEnum.PROJECT_QUERY_FAILY);
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

package com.southintel.zaokin.base.advice;

import com.southintel.zaokin.base.constant.Constant;
import com.southintel.zaokin.base.entity.User;
import com.southintel.zaokin.base.enums.BaseResult;
import com.southintel.zaokin.base.enums.BaseResultEnum;
import com.southintel.zaokin.base.exception.BusinessException;
import com.southintel.zaokin.base.util.JsonUtil;
import com.southintel.zaokin.base.util.RequestUtil;
import com.southintel.zaokin.base.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@Slf4j
@Component
@Aspect
@Order(1)
public class TokenInterceptorAdvice {
    @Value("${ignoreUrl}")
    private String[] ignoreUrl;

    @Pointcut("execution(* com.southintel.zaokin.base.controller.*Controller.*(..)))")
    public void execute(){
    }

    @Before("execute()")
    public void deBefore(JoinPoint joinPoint) throws Throwable{
        // 获取当前请求
        HttpServletRequest request = RequestUtil.currentRequest();
        //记录用户操作
//        recordUserOperate(request);

        Signature signature = joinPoint.getSignature();// 获取切入点
        String name = signature.getName();// 获取方法名称
        String servletPath = request.getServletPath();
        if( Arrays.asList(ignoreUrl).contains(servletPath)){
            return;
        }
        if (Constant.OPEN_PERMISSION_METHOD.contains(name)) { // 对开放权限的方法不需要拦截
            return;
        }
        String token = RequestUtil.getToken(request);// 获取token
        //验证token是否为空
        if(!StringUtils.isNoneEmpty(token)) {
            throw new BusinessException(new BaseResult(BaseResultEnum.TOKEN_ORERTIME));
        }
        boolean valid = TokenUtil.isValid(Constant.AUTH_TOKEN_PREFIX+token);// 校验token是否有效
        if (!valid) { // token 失效
            throw new BusinessException(new BaseResult(BaseResultEnum.TOKEN_ORERTIME));
        }
        return;
    }

//    private void recordUserOperate(HttpServletRequest request){
//        UserOperateLog userOperateLog = new UserOperateLog();
//        userOperateLog.setUri(request.getRequestURI());
//        userOperateLog.setBody(RequestUtil.getBody(request));
//        userOperateLog.setParams(request.getQueryString());
//        try{
//            //通过token获取用户名
//            String token = RequestUtil.getToken(request);
//            //获取token
//            if(token != null){
//                token = Constant.AUTH_TOKEN_PREFIX + token;
//                User user = JsonUtil.toObject(TokenUtil.get(token), User.class);
//                userOperateLog.setUserName(user.getUserName());
//            }
//        }catch (Exception e){
//        }
//        userOperateLogDao.insert(userOperateLog);
//    }
}

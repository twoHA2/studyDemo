package com.southintel.zaokin.base.util;


import com.southintel.zaokin.base.enums.BaseResult;
import com.southintel.zaokin.base.enums.BaseResultEnum;
import com.southintel.zaokin.base.enums.ResultEnum;
import com.southintel.zaokin.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;


@Slf4j
public class Assert {

	public static void isNotNull(Object src, ResultEnum msg) {
		if(src == null) {
			throw new BusinessException(msg.getCode(), msg.getMsg());
		}
	}

	public static void sqlIsSuccess(int src, ResultEnum msg) {
		if(src != 1) {
			throw new BusinessException(msg.getCode(), msg.getMsg());
		}
	}


	public static void isNotBlank(String src, ResultEnum msg) {
		if(StringUtils.isBlank(src)){
			throw new BusinessException(msg.getCode(), msg.getMsg());
		}
	}

	/**
	 * @Desinition：校验数据不为空和''
	 * @author:qufei
	 * @return:ServerResponse
	 */
	public static void isNotBlank(Object src) {
		ResultEnum msg = ResultEnum.PARAMETER_ERROR;
		if(src == null) {
			throw new BusinessException(msg.getCode(), msg.getMsg());
		}
		if(src.getClass().getName().equals(String.class.getName())) {
			if(StringUtils.isBlank(src.toString())){
				throw new BusinessException(msg.getCode(), msg.getMsg());
			}
		}
	}
	//判断该对象 是否为空
	public static boolean isAllFieldNull(Object obj){
		if(obj == null )throw new BusinessException(new BaseResult(BaseResultEnum.PARAMETER_ERROR));
		Class stuCla = (Class) obj.getClass();
		Field[] fs = stuCla.getDeclaredFields();
		boolean flag = true;
		for (Field f : fs) {
			try {
				f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
				String name = f.getName();
				Object val =  f.get(obj);
				if(val==null) {
					throw new BusinessException(new BaseResult(BaseResultEnum.PARAMETER_ERROR));
				}
			} catch (IllegalAccessException e) {
				//没有访问权限
				e.printStackTrace();
			}
		}
		return flag;
	}
}

package com.southintel.zaokin.base.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.southintel.zaokin.base.enums.BaseResult;
import com.southintel.zaokin.base.enums.BaseResultEnum;
import com.southintel.zaokin.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JsonUtil {

    public static String toJson(Object object){
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("对象转json失败",e);
            throw new BusinessException(new BaseResult(BaseResultEnum.TO_JSON_STRING_ERROR));
        }
    }

    public static  <T> T toObject(String json,Class<T> clazz){
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            return objectMapper.readValue(json,clazz);
        } catch (IOException e) {
            log.error("json转对象失败",e);
            throw  new BusinessException(new BaseResult(BaseResultEnum.JSON_TO_OBJECT_ERROR));
        }
    }

    public static  <T> T forceToObject(String json,Class<T> clazz){
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(json,clazz);
        } catch (IOException e) {
            log.error("json转对象失败",e);
            throw  new BusinessException(new BaseResult(BaseResultEnum.JSON_TO_OBJECT_ERROR));
        }
    }


    public static <T> List<T> toList(String josn,Class<T> clazz){
        ObjectMapper objectMapper=new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
        try {
          return   objectMapper.readValue(josn,javaType);
        } catch (IOException e) {
            log.error("json转对象失败",e);
            throw  new BusinessException(new BaseResult(BaseResultEnum.JSON_TO_OBJECT_ERROR));
        }
    }
    

	public static String readJsonFile(String file) {
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
			BufferedReader in = new BufferedReader(inputStreamReader);

			String str;
			while ((str = in.readLine()) != null) {
				sb.append(str); // new String(str,"UTF-8")
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
		return sb.toString();
	}
}

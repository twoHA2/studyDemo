package com.southintel.zaokin.base.util;

import com.southintel.zaokin.base.enums.BaseResult;
import com.southintel.zaokin.base.enums.BaseResultEnum;
import com.southintel.zaokin.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BeanUtil {

    public static <T> T copy(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }
        try {
            T t = target.newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception e) {
            log.error("对象转换异常", e);
            throw new BusinessException(new BaseResult(BaseResultEnum.COPY_OBJECT_ERROR));
        }
    }

    public static <T> List<T> copyList(List source, Class<T> target) {
        return (List<T>) source.stream().map(e -> copy(source, target)).collect(Collectors.toList());
    }
}

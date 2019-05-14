package com.southintel.zaokin.base.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.southintel.zaokin.base.util.JsonUtil.toJson;
import static com.southintel.zaokin.base.util.JsonUtil.toList;
import static com.southintel.zaokin.base.util.JsonUtil.toObject;

@Component
@Slf4j
public class RedisCacheTempleImpl implements ICache {

	/**
	 * 专门操作字符串的简化模板
	 */
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public void set(String key, Object object, Long expire) {
		ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
		String value = null;
		if (object instanceof String) {
			value = (String) object;
		} else {
			value = toJson(object);
		}
		// String escapeJsonvalue= StringEscapeUtils.escapeJson(value);
		operations.set(key, value, expire, TimeUnit.SECONDS);

	}

	@Override
	public void set(String key, Object object) {
		ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
		String value = null;
		if (object instanceof String) {
			value = (String) object;
		} else {
			value = toJson(object);
		}
		operations.set(key, value);

	}

	@Override
	public String get(String key) {
		ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
		return operations.get(key);
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		String value = get(key);
		if (value == null) {
			return null;
		}
		// String escapeJsonvalue= StringEscapeUtils.unescapeJson(value);
		return toObject(value, clazz);
	}

	@Override
	public <T> List<T> getList(String key, Class<T> clazz) {
		String value = get(key);
		if (value == null) {
			return null;
		}
		return toList(value, clazz);
	}

	@Override
	public void delete(String key) {
		stringRedisTemplate.delete(key);
	}

	@Override
	public void hset(String key, String hkey, Object value) {
		HashOperations<String, Object, Object> opsForHash = stringRedisTemplate.opsForHash();
		String hvalue = "";
		if (value instanceof String) {
			hvalue = (String) value;
		} else {
			hvalue = toJson(value);
		}
		opsForHash.put(key, hkey, hvalue);
	}

	@Override
	public String hget(String key, String hkey) {
		HashOperations<String, Object, Object> opsForHash = stringRedisTemplate.opsForHash();
		Object json = opsForHash.get(key, hkey);
		return json == null ? null : json.toString();
	}

	@Override
	public boolean lock(String key, String hkey) {
		HashOperations<String, Object, Object> opsForHash = stringRedisTemplate.opsForHash();
		return !opsForHash.putIfAbsent(key + "lock", hkey, "1");
	}

	@Override
	public void unlock(String key, String hkey) {
		HashOperations<String, Object, Object> opsForHash = stringRedisTemplate.opsForHash();
		opsForHash.delete(key + "lock", hkey);
	}

}

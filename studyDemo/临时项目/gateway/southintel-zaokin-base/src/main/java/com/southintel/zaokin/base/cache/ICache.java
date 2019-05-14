package com.southintel.zaokin.base.cache;

import java.util.List;

public interface ICache {

	/**
	 * @param key
	 * @param value
	 * @param expire
	 *            设置key的超时时间 单位是秒
	 */
	void set(String key, Object value, Long expire);

	/**
	 * @param key
	 * @return
	 */
	String get(String key);

	/**
	 *
	 * @param key
	 * @param clazz
	 *            对应的对象的class
	 * @param <T>
	 * @return
	 */
	<T> T get(String key, Class<T> clazz);

	/**
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> List<T> getList(String key, Class<T> clazz);

	/**
	 * 删除key
	 * 
	 * @param key
	 */
	void delete(String key);

	void set(String key, Object object);

	boolean lock(String key, String hkey);

	void unlock(String key, String hkey);

	void hset(String key, String hkey, Object value);

	String hget(String key, String hkey);
}

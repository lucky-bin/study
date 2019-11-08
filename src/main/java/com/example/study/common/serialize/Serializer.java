package com.example.study.common.serialize;

/**
 * 序列化器
 * Serializer
 */
public interface Serializer {
	
	/**
	 * 序列化
	 * @param obj
	 * @return
	 */
	Object serialize(Object obj);
	
	/**
	 * 反序列化
	 * @param <T>
	 * @param obj
	 * @param clazz
	 * @return
	 */
	<T> T deserialize(Object obj, Class<T> clazz);
}

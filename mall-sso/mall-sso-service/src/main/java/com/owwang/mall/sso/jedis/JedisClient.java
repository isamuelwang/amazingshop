package com.owwang.mall.sso.jedis;


public interface JedisClient {

	String set(String key, String value);
	String get(String key);
	Boolean exists(String key);
	Long expire(String key, int seconds);
	Long ttl(String key);
	Long incr(String key);
	Long hset(String key, String field, String value);
	String hget(String key, String field);
	Long del(String key);
	Long hdel(String key, String... field);//删除hkey
	
}

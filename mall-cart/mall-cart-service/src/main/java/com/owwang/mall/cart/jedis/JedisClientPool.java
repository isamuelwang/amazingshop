package com.owwang.mall.cart.jedis;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *	单机版本Redis实现
 * @Description TODO
 * @Date 2019-12-22
 * @auther Samuel
 */
public class JedisClientPool implements JedisClient {

	@Autowired
	private JedisPool jedisPool;

	//访问密码
	private String auth;

	public void setAuth(String auth) {
		this.auth = auth;
	}

	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.auth(auth);
		String result = jedis.set(key, value);
		jedis.close();
		return result;
	}

	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.auth(auth);
		String result = jedis.get(key);
		jedis.close();
		return result;
	}

	@Override
	public Boolean exists(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.auth(auth);
		Boolean result = jedis.exists(key);
		jedis.close();
		return result;
	}

	@Override
	public Long expire(String key, int seconds) {
		Jedis jedis = jedisPool.getResource();
		jedis.auth(auth);
		Long result = jedis.expire(key, seconds);
		jedis.close();
		return result;
	}

	@Override
	public Long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.auth(auth);
		Long result = jedis.ttl(key);
		jedis.close();
		return result;
	}

	@Override
	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.auth(auth);
		Long result = jedis.incr(key);
		jedis.close();
		return result;
	}

	@Override
	public Long hset(String key, String field, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.auth(auth);
		Long result = jedis.hset(key, field, value);
		jedis.close();
		return result;
	}

	@Override
	public String hget(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		jedis.auth(auth);
		String result = jedis.hget(key, field);
		jedis.close();
		return result;
	}

	@Override
	public Long hdel(String key, String... field) {
		Jedis jedis = jedisPool.getResource();
		jedis.auth(auth);
		Long hdel = jedis.hdel(key, field);
		jedis.close();
		return hdel;
	}	

}

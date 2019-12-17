package com.owwang.mall.service.impl;

import com.owwang.mall.pojo.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.owwang.mall.mapper.TestMapper;
import com.owwang.mall.service.TestService;

@Service
public class TestServiceImpl implements TestService {

	@Autowired
	private TestMapper testMapper;
	
	@Override
	public String queryNow() {
		return testMapper.queryNow();
	}
}

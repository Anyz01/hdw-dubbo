package com.hdw.dubbo.upms.rpc.service.impl;

import com.hdw.dubbo.upms.rpc.api.ITestService;

public class TestServiceImpl implements ITestService{

	@Override
	public String print() {
		
		return "测试";
	}

}

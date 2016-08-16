package com.inspur.cloud.lb.base.inter;

import java.util.Map;

import com.inspur.cloud.lb.base.object.LbParaObject;
import com.inspur.cloud.lb.base.object.ReturnMessage;

public abstract interface ILbBaseInterface {
	public abstract ReturnMessage updateConfig(LbParaObject paramLbParaObject,
			String paramString);

	public abstract ReturnMessage operLb(LbParaObject paramLbParaObject,
			String paramString);

	public abstract ReturnMessage updateConfig(String ip, String path);

	public abstract ReturnMessage operLb(String ip, String command);

	public abstract ReturnMessage updateConfig(String ip, String path,
			String privateKey);

	public abstract ReturnMessage operLb(String ip, String command,
			String privateKey);
	public abstract ReturnMessage updateRsysConfig(String ip, String path,
			String privateKey);
	
	public abstract ReturnMessage updateIpconfig(Map<String, String> paramMap);
}
package com.inspur.cloud.lb.test;

import com.inspur.cloud.lb.base.impl.LbBaseImpl;
import com.inspur.cloud.lb.base.inter.ILbBaseInterface;
import com.inspur.cloud.lb.base.object.ReturnMessage;

public class LbTest {
	public static void main(String[] args) {
		ILbBaseInterface lb = new LbBaseImpl();
		ReturnMessage rm = null;
		for (int i = 0; i < 100000; i++) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rm = lb.updateConfig("200.0.4.25", Thread.currentThread().getContextClassLoader().getResource("haproxy.cfg").getPath(),
					Thread.currentThread().getContextClassLoader().getResource("id_rsa").getPath());
			System.out.println(rm.getMessage());
		}
		// rm = lb.updateConfig(
		// "172.23.10.223",
		// Thread.currentThread().getContextClassLoader()
		// .getResource("haproxy.cfg").getPath(),
		// Thread.currentThread().getContextClassLoader()
		// .getResource("id_rsa").getPath());
		// for(int i=0;i<100000;i++)
		// {
		// rm = lb.operLb("172.23.10.224",
		// "restart",Thread.currentThread().getContextClassLoader()
		// .getResource("id_rsa").getPath());
		// }

		// rm = lb.operLb("172.23.10.223", "restart");

		// System.out.println(rm.getMessage());
	}
}
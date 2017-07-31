package mtcservers.cn.hzcr.monitor.message;

import ctd.annotation.RpcService;

/**
 * 发送数据核心调用方法
 * 
 * @author Administrator
 * 
 */
public class ProxySendMessage implements SendMsg {
	@Override
	@RpcService
	public Boolean sendMessage(String mobileNumber, String message) {
		final MessageOperation mo = new MessageOperation();
		System.out.println("------------------------init-------------");
		int connectRe = mo.jBtnInitActionPerformed();// 初始化服务
		System.out.println("------------------------sssss-------------");
		int status = 0;
		Boolean flag = false;
		// StringBuffer msg = new StringBuffer();
		// int key = (int)((Math.random()*9+1)*100000);//返回的随机数值
		try {
			if (connectRe == 0) {
				// msg.append("尊敬的用户,您注册的杭州市市属医院检验、体检报告查询系统验证码： ").append(key).append(
				// "。请勿将验证码告知他人，并在5分钟内完成注册。");
				if (message.length() > 0 && !"".equals(mobileNumber)) {
					status = mo.sendActionPerformed(mobileNumber, null,
							message, 10); // 用户注册短信
					if (status == 0) {
						System.out.println("发送成功");
						flag =true;
					} else {
						System.out.println("发送失败");
						flag = false;
						// key = 0;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("短信平台服务启用出错");
			// key = 0;
			return false;
		} finally {
			mo.jBtnReleaseActionPerformed();// 释放服务
		}
		return flag;
	}
}

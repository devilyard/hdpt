package mtcservers.cn.hzcr.monitor.message;

import ctd.annotation.RpcService;

public interface SendMsg {
	@RpcService
	public Boolean sendMessage(String mobileNumber, String message);

}

package mtcservers.cn.hzcr.monitor.message;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mtcservers.cn.hzcr.monitor.message.APIClient;

public class MessageOperation {
	
	private static Log logger = LogFactory.getLog(MessageOperation.class);
	
	private String ip = "192.26.7.142";
	private String name = "wsjsms";
	private String apiId = "wsjsms";
	private String pwd = "wsjsms";
	private String dbName = "mas";
	
	private APIClient apiClient = new APIClient();
	
	public MessageOperation(){}
	
	public void setMessageOperation(String ip,String name,String apiId,String pwd,String dbName ){
		this.ip = ip;
		this.name = name;
		this.apiId = apiId;
		this.pwd = pwd;
		this.dbName = dbName;
	}

	public void jBtnReleaseActionPerformed() {
		apiClient.release();
		logger.info("Release成功");
	}

	public int jBtnInitActionPerformed() {
		int connectRe = apiClient.init(ip, name, pwd, apiId, dbName);
		if (connectRe == APIClient.IMAPI_SUCC)
			logger.info("初始化成功");
		else if (connectRe == APIClient.IMAPI_CONN_ERR)
			logger.error("数据库连接失败");
		else if (connectRe == APIClient.IMAPI_API_ERR)
			logger.error("apiID不存在");
		
		return connectRe;
	}
	
	/**
	 * 
	* @Title: sendActionPerformed
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param   @param mobileStrs	手机号字符串，以逗号隔开
	* @param   @param sendTime		发送时间
	* @param   @param content		发送内容
	* @param   @param smId 			
	* @return void    返回类型
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public int sendActionPerformed(String mobileStrs,String sendTime,String content,long smId){
		// 改成支持JDK1.3
		ArrayList mobileList = new ArrayList();
		StringTokenizer st = new StringTokenizer(mobileStrs, ",");
		String str = "";
		while (st.hasMoreElements()) {
			String tmp = (String) st.nextElement();
			mobileList.add(tmp);
		}

		String[] mobiles = new String[0];
		mobiles = (String[]) mobileList.toArray(mobiles);

		long srcID = 0;

		try {
			srcID = Long.parseLong("10");
		} catch (NumberFormatException e) {
			logger.error("SrcID只能为整数");
		}
		int result = 0;
		
		try {
			result = apiClient.sendSM(mobiles, content, sendTime, smId, srcID);
		} catch (RuntimeException e) {
			result = -12;
		}
		if (result == APIClient.IMAPI_SUCC) 
			str = "发送成功";
		else if (result == APIClient.IMAPI_INIT_ERR)
			str = "未初始化";
		else if (result == APIClient.IMAPI_CONN_ERR)
			str = "数据库连接失败";
		else if (result == APIClient.IMAPI_DATA_ERR)
			str = "参数错误";
		else if (result == APIClient.IMAPI_DATA_TOOLONG)
			str = "消息内容太长";
		else if (result == APIClient.IMAPI_INS_ERR)
			str = "数据库插入错误";
		else if (result == APIClient.IMAPI_IFSTATUS_INVALID)
			str = "接口处于暂停或失效状态";
		else if (result == APIClient.IMAPI_GATEWAY_CONN_ERR)
			str = "短信网关未连接";
		else
			str = "出现其他错误";
		
		return result;
	}
	
}

package mtcservers.cn.hzcr.monitor.message;

public class RPTItem 
{
	/*MT短信所发送的手机号码。*/
	private String mobile_;
	/*MT短信的短信编号。*/
	private long smID_;
	/*回执编码。*/
	private int code_;
	/*回执的描述信息。*/
	private String desc_;
	/** 回执时间 */
	private String rptTime;
	
	
	/**
	 * 获得回执编码。
	 * 
	 * @return int 回执编码。
	 */
	public int getCode() 
	{
		return this.code_;
	}
	
	
	/**
	 * 设置回执编码。
	 * 
	 * @param code 回执编码。
	 */
	public void setCode(int code) 
	{   	
		this.code_ = code;
	}
	
	
	/**
	 * 获得回执的描述信息。
	 * 
	 * @return String 回执的描述信息。
	 */
	public String getDesc() 
	{
		return this.desc_;
	}
	
	
	/**
	 * 设置回执的描述信息。
	 * 
	 * @param desc 回执的描述信息。
	 */
	public void setDesc(String desc) 
	{  
		this.desc_ =  desc;
	}
	
	
	/**
	 * 获得MT短信所发送的手机号码。
	 * 
	 * @return String MT短信所发送的手机号码。
	 */
	public String getMobile() 
	{
		return this.mobile_;
	}
	
	
	/**
	 * 设置MT短信所发送的手机号码。 
	 * 
	 * @param mobile MT短信所发送的手机号码。 
	 */
	public void setMobile(String mobile) 
	{
		this.mobile_ = mobile;
	}
	
	
	/**
	 * 获得MT短信的短信编号。
	 * 
	 * @return long MT短信的短信编号。
	 */
	public long getSmID() 
	{
		return this.smID_;
	}
	
	
	/**
	 * 设置MT短信的短信编号。
	 * 
	 * @param smID MT短信的短信编号。
	 */
	public void setSmID(long smID) 
	{
		this.smID_ = smID;
	}


	/**
	 * 获得回执时间
	 * @return rptTime 回执时间 （格式：yyyy-MM-dd HH:mm:ss）
	 */
	public String getRptTime()
	{
		return rptTime;
	}


	/**
	 * 设置回执时间
	 * @param rptTime
	 */
	public void setRptTime(String rptTime)
	{
		this.rptTime = rptTime;
	}
}
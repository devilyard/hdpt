package mtcservers.cn.hzcr.monitor.message;



public class MOItem 
{
	/*发送MO短信的手机号码。*/
	private String mobile_;
	/*MO短信的短信编号。*/
	private long smID_;
	/*MO短信的短信内容。*/
	private String content_;
	/* MO短信时间 */
	private String moTime;
	/* MO短信的编码格式 */
	private int msgFmt;
	
	/**
	 * 获得MO短信的短信内容。
	 * 
	 * @return String MO短信的短信内容。
	 */
	public String getContent() 
	{
		return this.content_;
	}
	
	
	/**
	 * 设置MO短信的短信内容。
	 * 
	 * @param content MO短信的短信内容.
	 */
	public void setContent(String content) 
	{
		this.content_ = content;
	}
	
	
	/**
	 * 获得发送MO短信的手机号码。
	 * 
	 * @return String 发送MO短信的手机号码。
	 */
	public String getMobile() 
	{
		return this.mobile_;
	}
	
	
	/**
	 * 设置发送MO短信的手机号码。
	 * 
	 * @param mobile 发送MO短信的手机号码。
	 */
	public void setMobile(String mobile) 
	{
		this.mobile_ = mobile;
	}
	
	
	/**
	 * 获得MO短信的短信编号。
	 * 
	 * @return long MO短信的短信编号。
	 */
	public long getSmID() 
	{
		return this.smID_;
	}
	
	
	/**
	 * 设置MO短信的短信编号。
	 * 
	 * @param smID MO短信的短信编号。
	 */
	public void setSmID(long smID) 
	{
		this.smID_ = smID;
	}


	/**
	 * 获得MO时间
	 * @return
	 */
	public String getMoTime()
	{
		return moTime;
	}


	/**
	 * 设置MO时间
	 * @param moTime
	 */
	public void setMoTime(String moTime)
	{
		this.moTime = moTime;
	}

   /**
    * 获得MO短信的编码格式
    * @return msgFmt MO短信的编码格式
    */
	public int getMsgFmt() {
		return msgFmt;
	}

    /**
     *  设置MO短信的编码格式
     * @param msgFmt MO短信的编码格式
     */
	public void setMsgFmt(int msgFmt) {
		this.msgFmt = msgFmt;
	}
}

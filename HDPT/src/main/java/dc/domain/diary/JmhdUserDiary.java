package dc.domain.diary;

import java.util.Date;



public class JmhdUserDiary implements java.io.Serializable {

	// Fields

	private Long id;
	private Long userId;
	private String username;
	private String source;
	private Date createTime;
	private String title;
	private String message;
	private String cacheJson;
	private String ip;
	private Integer replyNum;
	private Short mood;
	private String mpiId;

	// Constructors

	/** default constructor */
	public JmhdUserDiary() {
	}

	/** minimal constructor */
	public JmhdUserDiary(Long id) {
		this.id = id;
	}

	/** full constructor */
	public JmhdUserDiary(Long id, Long userId, String username, String source,
			Date createTime, String title, String message, String cacheJson,
			String ip, Integer replyNum, Short mood) {
		this.id = id;
		this.userId = userId;
		this.username = username;
		this.source = source;
		this.createTime = createTime;
		this.title = title;
		this.message = message;
		this.cacheJson = cacheJson;
		this.ip = ip;
		this.replyNum = replyNum;
		this.mood = mood;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCacheJson() {
		return this.cacheJson;
	}

	public void setCacheJson(String cacheJson) {
		this.cacheJson = cacheJson;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getReplyNum() {
		return this.replyNum;
	}

	public void setReplyNum(Integer replyNum) {
		this.replyNum = replyNum;
	}

	public Short getMood() {
		return this.mood;
	}

	public void setMood(Short mood) {
		this.mood = mood;
	}

	public String getMpiId() {
		return mpiId;
	}

	public void setMpiId(String mpiId) {
		this.mpiId = mpiId;
	}
	
}
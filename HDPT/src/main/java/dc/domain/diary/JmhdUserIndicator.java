package dc.domain.diary;

import java.util.Date;



public class JmhdUserIndicator implements java.io.Serializable {

	// Fields

	private Long id;
	private String uuid;
	private Long userId;
	private String indicatorName;
	private Integer indicatorId;
	private String val;
	private String groupNo;
	private String isFactor;
	private String isAbnormity;
	private String abnormityLevel2;
	private Long abnormityLevelId;
	private String abnormityValue;
	private Date createDate;
	private String source;
	private String sourceType;
	private Long pid;
	private String status;
	private String mpiId;
	private String type;
	private String newuuid;

	// Constructors

	public String getType() {
		return type;
	}

	public String getNewuuid() {
		return newuuid;
	}

	public void setNewuuid(String newuuid) {
		this.newuuid = newuuid;
	}

	public void setType(String type) {
		this.type = type;
	}

	/** default constructor */
	public JmhdUserIndicator() {
	}

	/** minimal constructor */
	public JmhdUserIndicator(Long id) {
		this.id = id;
	}

	/** full constructor */
	public JmhdUserIndicator(Long id, String uuid, Long userId,
			String indicatorName, Integer indicatorId, String val,
			String groupNo, String isFactor, String isAbnormity,
			String abnormityLevel2, Long abnormityLevelId,
			String abnormityValue, Date createDate, String source,
			String sourceType, Long pid, String status) {
		this.id = id;
		this.uuid = uuid;
		this.userId = userId;
		this.indicatorName = indicatorName;
		this.indicatorId = indicatorId;
		this.val = val;
		this.groupNo = groupNo;
		this.isFactor = isFactor;
		this.isAbnormity = isAbnormity;
		this.abnormityLevel2 = abnormityLevel2;
		this.abnormityLevelId = abnormityLevelId;
		this.abnormityValue = abnormityValue;
		this.createDate = createDate;
		this.source = source;
		this.sourceType = sourceType;
		this.pid = pid;
		this.status = status;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIndicatorName() {
		return this.indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public Integer getIndicatorId() {
		return this.indicatorId;
	}

	public void setIndicatorId(Integer indicatorId) {
		this.indicatorId = indicatorId;
	}

	public String getVal() {
		return this.val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getGroupNo() {
		return this.groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public String getIsFactor() {
		return this.isFactor;
	}

	public void setIsFactor(String isFactor) {
		this.isFactor = isFactor;
	}

	public String getIsAbnormity() {
		return this.isAbnormity;
	}

	public void setIsAbnormity(String isAbnormity) {
		this.isAbnormity = isAbnormity;
	}

	public String getAbnormityLevel2() {
		return this.abnormityLevel2;
	}

	public void setAbnormityLevel2(String abnormityLevel2) {
		this.abnormityLevel2 = abnormityLevel2;
	}

	public Long getAbnormityLevelId() {
		return this.abnormityLevelId;
	}

	public void setAbnormityLevelId(Long abnormityLevelId) {
		this.abnormityLevelId = abnormityLevelId;
	}

	public String getAbnormityValue() {
		return this.abnormityValue;
	}

	public void setAbnormityValue(String abnormityValue) {
		this.abnormityValue = abnormityValue;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceType() {
		return this.sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Long getPid() {
		return this.pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMpiId() {
		return mpiId;
	}

	public void setMpiId(String mpiId) {
		this.mpiId = mpiId;
	}
	
}
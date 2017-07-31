package dc.domain.contact;

import java.util.Date;



public class JmhdUserRecontactView implements java.io.Serializable {

	// Fields

	private Long id;
	private Long userId;
	private String empiid;
	private String doctorId;
	private String remessage;
	private Date createTime;
	private Long reId;
	private Long contactId;
	private String delStatus;
	private String mpiid;
	private String recontacterId;
	private String  name;
	// Constructors

	/** default constructor */
	public JmhdUserRecontactView() {
	}

	/** minimal constructor */
	public JmhdUserRecontactView(Long id) {
		this.id = id;
	}

	/** full constructor */
	public JmhdUserRecontactView(Long id, Long userId, String empiid,
			String doctorId, String remessage, Date createTime, Long reId,
			Long contactId, String delStatus) {
		this.id = id;
		this.userId = userId;
		this.empiid = empiid;
		this.doctorId = doctorId;
		this.remessage = remessage;
		this.createTime = createTime;
		this.reId = reId;
		this.contactId = contactId;
		this.delStatus = delStatus;
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

	public String getEmpiid() {
		return this.empiid;
	}

	public void setEmpiid(String empiid) {
		this.empiid = empiid;
	}

	public String getDoctorId() {
		return this.doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getRemessage() {
		return this.remessage;
	}

	public void setRemessage(String remessage) {
		this.remessage = remessage;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getReId() {
		return this.reId;
	}

	public void setReId(Long reId) {
		this.reId = reId;
	}

	public Long getContactId() {
		return this.contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public String getDelStatus() {
		return this.delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	public String getMpiid() {
		return mpiid;
	}

	public void setMpiid(String mpiid) {
		this.mpiid = mpiid;
	}

	public String getRecontacterId() {
		return recontacterId;
	}

	public void setRecontacterId(String recontacterId) {
		this.recontacterId = recontacterId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
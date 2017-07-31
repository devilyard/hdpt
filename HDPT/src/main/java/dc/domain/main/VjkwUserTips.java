package dc.domain.main;

import java.util.Date;

/**
 * VjkwUserTipsId entity. @author MyEclipse Persistence Tools
 */

public class VjkwUserTips implements java.io.Serializable {

	// Fields

	private String ptype;
	private Date ptime;
	private String id;
	private String empiid;
	private Long userId;
	private String remarks;
	private String message;
	private String recordid;
	private String status;

	// Constructors

	/** default constructor */
	public VjkwUserTips() {
	}

	/** minimal constructor */
	public VjkwUserTips(Date ptime) {
		this.ptime = ptime;
	}

	/** full constructor */
	public VjkwUserTips(String ptype, Date ptime, String id, String empiid,
			Long userId, String remarks, String message, String recordid,
			String status) {
		this.ptype = ptype;
		this.ptime = ptime;
		this.id = id;
		this.empiid = empiid;
		this.userId = userId;
		this.remarks = remarks;
		this.message = message;
		this.recordid = recordid;
		this.status = status;
	}

	// Property accessors

	public String getPtype() {
		return this.ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public Date getPtime() {
		return this.ptime;
	}

	public void setPtime(Date ptime) {
		this.ptime = ptime;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmpiid() {
		return this.empiid;
	}

	public void setEmpiid(String empiid) {
		this.empiid = empiid;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRecordid() {
		return this.recordid;
	}

	public void setRecordid(String recordid) {
		this.recordid = recordid;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VjkwUserTips))
			return false;
		VjkwUserTips castOther = (VjkwUserTips) other;

		return ((this.getPtype() == castOther.getPtype()) || (this.getPtype() != null
				&& castOther.getPtype() != null && this.getPtype().equals(
				castOther.getPtype())))
				&& ((this.getPtime() == castOther.getPtime()) || (this
						.getPtime() != null && castOther.getPtime() != null && this
						.getPtime().equals(castOther.getPtime())))
				&& ((this.getId() == castOther.getId()) || (this.getId() != null
						&& castOther.getId() != null && this.getId().equals(
						castOther.getId())))
				&& ((this.getEmpiid() == castOther.getEmpiid()) || (this
						.getEmpiid() != null && castOther.getEmpiid() != null && this
						.getEmpiid().equals(castOther.getEmpiid())))
				&& ((this.getUserId() == castOther.getUserId()) || (this
						.getUserId() != null && castOther.getUserId() != null && this
						.getUserId().equals(castOther.getUserId())))
				&& ((this.getRemarks() == castOther.getRemarks()) || (this
						.getRemarks() != null && castOther.getRemarks() != null && this
						.getRemarks().equals(castOther.getRemarks())))
				&& ((this.getMessage() == castOther.getMessage()) || (this
						.getMessage() != null && castOther.getMessage() != null && this
						.getMessage().equals(castOther.getMessage())))
				&& ((this.getRecordid() == castOther.getRecordid()) || (this
						.getRecordid() != null
						&& castOther.getRecordid() != null && this
						.getRecordid().equals(castOther.getRecordid())))
				&& ((this.getStatus() == castOther.getStatus()) || (this
						.getStatus() != null && castOther.getStatus() != null && this
						.getStatus().equals(castOther.getStatus())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getPtype() == null ? 0 : this.getPtype().hashCode());
		result = 37 * result
				+ (getPtime() == null ? 0 : this.getPtime().hashCode());
		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result
				+ (getEmpiid() == null ? 0 : this.getEmpiid().hashCode());
		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getRemarks() == null ? 0 : this.getRemarks().hashCode());
		result = 37 * result
				+ (getMessage() == null ? 0 : this.getMessage().hashCode());
		result = 37 * result
				+ (getRecordid() == null ? 0 : this.getRecordid().hashCode());
		result = 37 * result
				+ (getStatus() == null ? 0 : this.getStatus().hashCode());
		return result;
	}

}
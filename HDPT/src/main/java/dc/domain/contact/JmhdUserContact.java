package dc.domain.contact;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;



public class JmhdUserContact implements java.io.Serializable {

	// Fields

	private Long id;
	private Long userId;
	private String empiid;
	private String message;
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date qtime;
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date atime;
	private String replyStatus;
	private String doctorId;
	private String delStatus;
	private String endStatus;
	private String mpiid;

	// Constructors

	/** default constructor */
	public JmhdUserContact() {
	}

	/** minimal constructor */
	public JmhdUserContact(Long id) {
		this.id = id;
	}

	/** full constructor */
	public JmhdUserContact(Long id, Long userId, String empiid, String message,
			Date qtime, Date atime, String replyStatus, String doctorId,
			String delStatus, String endStatus) {
		this.id = id;
		this.userId = userId;
		this.empiid = empiid;
		this.message = message;
		this.qtime = qtime;
		this.atime = atime;
		this.replyStatus = replyStatus;
		this.doctorId = doctorId;
		this.delStatus = delStatus;
		this.endStatus = endStatus;
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

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getQtime() {
		return this.qtime;
	}

	public void setQtime(Date qtime) {
		this.qtime = qtime;
	}

	public Date getAtime() {
		return this.atime;
	}

	public void setAtime(Date atime) {
		this.atime = atime;
	}

	public String getReplyStatus() {
		return this.replyStatus;
	}

	public void setReplyStatus(String replyStatus) {
		this.replyStatus = replyStatus;
	}

	public String getDoctorId() {
		return this.doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDelStatus() {
		return this.delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	public String getEndStatus() {
		return this.endStatus;
	}

	public void setEndStatus(String endStatus) {
		this.endStatus = endStatus;
	}

	public String getMpiid() {
		return mpiid;
	}

	public void setMpiid(String mpiid) {
		this.mpiid = mpiid;
	}

}
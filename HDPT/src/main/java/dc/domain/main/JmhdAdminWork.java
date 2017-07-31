package dc.domain.main;



public class JmhdAdminWork implements java.io.Serializable {

	// Fields

	private Long wid;
	private String doctorId;
	private String status;
	private String taskid;
	private String wtype;
	private String wurl;
	private String wtypename;
	private String sqtaskid;

	// Constructors

	/** default constructor */
	public JmhdAdminWork() {
	}

	/** minimal constructor */
	public JmhdAdminWork(Long wid) {
		this.wid = wid;
	}

	/** full constructor */
	public JmhdAdminWork(Long wid, String doctorId, String status,
			String taskid, String wtype, String wurl, String wtypename,
			String sqtaskid) {
		this.wid = wid;
		this.doctorId = doctorId;
		this.status = status;
		this.taskid = taskid;
		this.wtype = wtype;
		this.wurl = wurl;
		this.wtypename = wtypename;
		this.sqtaskid = sqtaskid;
	}

	// Property accessors

	public Long getWid() {
		return this.wid;
	}

	public void setWid(Long wid) {
		this.wid = wid;
	}

	public String getDoctorId() {
		return this.doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskid() {
		return this.taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getWtype() {
		return this.wtype;
	}

	public void setWtype(String wtype) {
		this.wtype = wtype;
	}

	public String getWurl() {
		return this.wurl;
	}

	public void setWurl(String wurl) {
		this.wurl = wurl;
	}

	public String getWtypename() {
		return this.wtypename;
	}

	public void setWtypename(String wtypename) {
		this.wtypename = wtypename;
	}

	public String getSqtaskid() {
		return this.sqtaskid;
	}

	public void setSqtaskid(String sqtaskid) {
		this.sqtaskid = sqtaskid;
	}

}
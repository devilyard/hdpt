package dc.domain.adminuser;

import java.util.List;

import dc.domain.main.VjmhdSysAdminrole;

/**
 * JmhdSysAdminuser entity. @author MyEclipse Persistence Tools
 */

public class JmhdSysAdminuser implements java.io.Serializable {

	// Fields

	private Integer adminId;
	private String adminName;
	private String loginName;
	private String password;
	private String orgId;
	private String remarks;
	private String status;
	private String type;
	private String datafield;
	private int page;
	private List<VjmhdSysAdminrole> roles;

	// Constructors

	/** default constructor */
	public JmhdSysAdminuser() {
	}

	/** minimal constructor */
	public JmhdSysAdminuser(Integer adminId) {
		this.adminId = adminId;
	}

	/** full constructor */
	public JmhdSysAdminuser(Integer adminId, String adminName,
			String loginName, String password, String orgId, String remarks,
			String status, String type, String datafield) {
		this.adminId = adminId;
		this.adminName = adminName;
		this.loginName = loginName;
		this.password = password;
		this.orgId = orgId;
		this.remarks = remarks;
		this.status = status;
		this.type = type;
		this.datafield = datafield;
	}

	// Property accessors

	public Integer getAdminId() {
		return this.adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return this.adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDatafield() {
		return this.datafield;
	}

	public void setDatafield(String datafield) {
		this.datafield = datafield;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<VjmhdSysAdminrole> getRoles() {
		return roles;
	}

	public void setRoles(List<VjmhdSysAdminrole> roles) {
		this.roles = roles;
	}
	
}
package dc.domain.role;

import java.util.Date;

/**
 * JmhdSysRole entity. @author MyEclipse Persistence Tools
 */

public class JmhdSysRole implements java.io.Serializable {

	// Fields

	private Integer roleId;
	private String roleName;
	private String roleType;
	private String remarks;
	private String status;
	private String sqrole;
	private Date createtime;
	private int page;
	private Integer enabled = 1;

	// Constructors

	/** default constructor */
	public JmhdSysRole() {
	}

	/** minimal constructor */
	public JmhdSysRole(Integer roleId, String roleName, String roleType,
			String status) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleType = roleType;
		this.status = status;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	/** full constructor */
	public JmhdSysRole(Integer roleId, String roleName, String roleType,
			String remarks, String status, String sqrole) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleType = roleType;
		this.remarks = remarks;
		this.status = status;
		this.sqrole = sqrole;
	}

	// Property accessors

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleType() {
		return this.roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
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

	public String getSqrole() {
		return this.sqrole;
	}

	public void setSqrole(String sqrole) {
		this.sqrole = sqrole;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

}
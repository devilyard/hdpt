package dc.domain.main;

/**
 * VjmhdSysAdminroleId entity. @author MyEclipse Persistence Tools
 */

public class VjmhdSysAdminrole implements java.io.Serializable {

	// Fields

	private Integer adminId;
	private Integer id;
	private String moduleName;
	private String actionMethod;
	private String actionName;
	private String roleType;

	// Constructors

	/** default constructor */
	public VjmhdSysAdminrole() {
	}

	/** minimal constructor */
	public VjmhdSysAdminrole(Integer adminId) {
		this.adminId = adminId;
	}

	/** full constructor */
	public VjmhdSysAdminrole(Integer adminId, Integer id, String moduleName,
			String actionMethod, String actionName) {
		this.adminId = adminId;
		this.id = id;
		this.moduleName = moduleName;
		this.actionMethod = actionMethod;
		this.actionName = actionName;
	}

	// Property accessors

	public Integer getAdminId() {
		return this.adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getActionMethod() {
		return this.actionMethod;
	}

	public void setActionMethod(String actionMethod) {
		this.actionMethod = actionMethod;
	}

	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VjmhdSysAdminrole))
			return false;
		VjmhdSysAdminrole castOther = (VjmhdSysAdminrole) other;

		return ((this.getAdminId() == castOther.getAdminId()) || (this
				.getAdminId() != null && castOther.getAdminId() != null && this
				.getAdminId().equals(castOther.getAdminId())))
				&& ((this.getId() == castOther.getId()) || (this.getId() != null
						&& castOther.getId() != null && this.getId().equals(
						castOther.getId())))
				&& ((this.getModuleName() == castOther.getModuleName()) || (this
						.getModuleName() != null
						&& castOther.getModuleName() != null && this
						.getModuleName().equals(castOther.getModuleName())))
				&& ((this.getActionMethod() == castOther.getActionMethod()) || (this
						.getActionMethod() != null
						&& castOther.getActionMethod() != null && this
						.getActionMethod().equals(castOther.getActionMethod())))
				&& ((this.getActionName() == castOther.getActionName()) || (this
						.getActionName() != null
						&& castOther.getActionName() != null && this
						.getActionName().equals(castOther.getActionName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getAdminId() == null ? 0 : this.getAdminId().hashCode());
		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37
				* result
				+ (getModuleName() == null ? 0 : this.getModuleName()
						.hashCode());
		result = 37
				* result
				+ (getActionMethod() == null ? 0 : this.getActionMethod()
						.hashCode());
		result = 37
				* result
				+ (getActionName() == null ? 0 : this.getActionName()
						.hashCode());
		return result;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

}
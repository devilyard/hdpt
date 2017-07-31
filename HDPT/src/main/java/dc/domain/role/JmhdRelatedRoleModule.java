package dc.domain.role;

/**
 * JkwRelatedRoleModuleId entity. @author MyEclipse Persistence Tools
 */

public class JmhdRelatedRoleModule implements java.io.Serializable {

	// Fields

	private Integer roleId;
	private Integer moduleId;
	private Integer enabled = 1;

	// Constructors

	/** default constructor */
	public JmhdRelatedRoleModule() {
	}

	/** full constructor */
	public JmhdRelatedRoleModule(Integer roleId, Integer moduleId) {
		this.roleId = roleId;
		this.moduleId = moduleId;
	}

	// Property accessors

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof JmhdRelatedRoleModule))
			return false;
		JmhdRelatedRoleModule castOther = (JmhdRelatedRoleModule) other;

		return ((this.getRoleId() == castOther.getRoleId()) || (this
				.getRoleId() != null && castOther.getRoleId() != null && this
				.getRoleId().equals(castOther.getRoleId())))
				&& ((this.getModuleId() == castOther.getModuleId()) || (this
						.getModuleId() != null
						&& castOther.getModuleId() != null && this
						.getModuleId().equals(castOther.getModuleId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		result = 37 * result
				+ (getModuleId() == null ? 0 : this.getModuleId().hashCode());
		return result;
	}

}
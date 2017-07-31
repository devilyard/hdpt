package dc.domain.adminuser;

/**
 * JmhdRelatedAdminRoleId entity. @author MyEclipse Persistence Tools
 */

public class JmhdRelatedAdminRole implements java.io.Serializable {

	// Fields

	private Integer adminId;
	private Integer roleId;
	private Integer enabled = 1;

	// Constructors

	/** default constructor */
	public JmhdRelatedAdminRole() {
	}

	/** full constructor */
	public JmhdRelatedAdminRole(Integer adminId, Integer roleId) {
		this.adminId = adminId;
		this.roleId = roleId;
	}

	// Property accessors

	public Integer getAdminId() {
		return this.adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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
		if (!(other instanceof JmhdRelatedAdminRole))
			return false;
		JmhdRelatedAdminRole castOther = (JmhdRelatedAdminRole) other;

		return ((this.getAdminId() == castOther.getAdminId()) || (this
				.getAdminId() != null && castOther.getAdminId() != null && this
				.getAdminId().equals(castOther.getAdminId())))
				&& ((this.getRoleId() == castOther.getRoleId()) || (this
						.getRoleId() != null && castOther.getRoleId() != null && this
						.getRoleId().equals(castOther.getRoleId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getAdminId() == null ? 0 : this.getAdminId().hashCode());
		result = 37 * result
				+ (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		return result;
	}

}
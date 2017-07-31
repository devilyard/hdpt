package dc.domain.role;

/**
 * JmhdSysRoleViewId entity. @author MyEclipse Persistence Tools
 */

public class JmhdSysRoleView implements java.io.Serializable {

	// Fields

	private Integer mid;
	private String mcode;
	private Long mlev;
	private String mname;
	private Integer rid;

	// Constructors

	/** default constructor */
	public JmhdSysRoleView() {
	}

	/** minimal constructor */
	public JmhdSysRoleView(Integer mid) {
		this.mid = mid;
	}

	/** full constructor */
	public JmhdSysRoleView(Integer mid, String mcode, Long mlev,
			String mname, Integer rid) {
		this.mid = mid;
		this.mcode = mcode;
		this.mlev = mlev;
		this.mname = mname;
		this.rid = rid;
	}

	// Property accessors

	public Integer getMid() {
		return this.mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public String getMcode() {
		return this.mcode;
	}

	public void setMcode(String mcode) {
		this.mcode = mcode;
	}

	public Long getMlev() {
		return this.mlev;
	}

	public void setMlev(Long mlev) {
		this.mlev = mlev;
	}

	public String getMname() {
		return this.mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public Integer getRid() {
		return this.rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof JmhdSysRoleView))
			return false;
		JmhdSysRoleView castOther = (JmhdSysRoleView) other;

		return ((this.getMid() == castOther.getMid()) || (this.getMid() != null
				&& castOther.getMid() != null && this.getMid().equals(
				castOther.getMid())))
				&& ((this.getMcode() == castOther.getMcode()) || (this
						.getMcode() != null && castOther.getMcode() != null && this
						.getMcode().equals(castOther.getMcode())))
				&& ((this.getMlev() == castOther.getMlev()) || (this.getMlev() != null
						&& castOther.getMlev() != null && this.getMlev()
						.equals(castOther.getMlev())))
				&& ((this.getMname() == castOther.getMname()) || (this
						.getMname() != null && castOther.getMname() != null && this
						.getMname().equals(castOther.getMname())))
				&& ((this.getRid() == castOther.getRid()) || (this.getRid() != null
						&& castOther.getRid() != null && this.getRid().equals(
						castOther.getRid())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getMid() == null ? 0 : this.getMid().hashCode());
		result = 37 * result
				+ (getMcode() == null ? 0 : this.getMcode().hashCode());
		result = 37 * result
				+ (getMlev() == null ? 0 : this.getMlev().hashCode());
		result = 37 * result
				+ (getMname() == null ? 0 : this.getMname().hashCode());
		result = 37 * result
				+ (getRid() == null ? 0 : this.getRid().hashCode());
		return result;
	}

}
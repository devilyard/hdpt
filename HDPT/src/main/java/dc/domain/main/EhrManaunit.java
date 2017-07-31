package dc.domain.main;

/**
 * EhrManaunit entity. @author MyEclipse Persistence Tools
 */

public class EhrManaunit implements java.io.Serializable {

	// Fields

	private String manaunitid;
	private String manaunitname;
	private String pycode;

	// Constructors

	/** default constructor */
	public EhrManaunit() {
	}

	/** minimal constructor */
	public EhrManaunit(String manaunitid, String manaunitname) {
		this.manaunitid = manaunitid;
		this.manaunitname = manaunitname;
	}

	/** full constructor */
	public EhrManaunit(String manaunitid, String manaunitname, String pycode) {
		this.manaunitid = manaunitid;
		this.manaunitname = manaunitname;
		this.pycode = pycode;
	}

	// Property accessors

	public String getManaunitid() {
		return this.manaunitid;
	}

	public void setManaunitid(String manaunitid) {
		this.manaunitid = manaunitid;
	}

	public String getManaunitname() {
		return this.manaunitname;
	}

	public void setManaunitname(String manaunitname) {
		this.manaunitname = manaunitname;
	}

	public String getPycode() {
		return this.pycode;
	}

	public void setPycode(String pycode) {
		this.pycode = pycode;
	}

}
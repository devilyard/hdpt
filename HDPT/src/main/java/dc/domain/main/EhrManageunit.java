package dc.domain.main;

import java.math.BigDecimal;
import java.util.Date;

/**
 * EhrManageunit entity. @author MyEclipse Persistence Tools
 */

public class EhrManageunit implements java.io.Serializable {

	// Fields

	private String manaunitid;
	private String manaunitname;
	private String introduction;
	private BigDecimal orderno;
	private String createuser;
	private Date createdate;
	private String lastmodifyuser;
	private Date lastmodifydate;

	// Constructors

	/** default constructor */
	public EhrManageunit() {
	}

	/** minimal constructor */
	public EhrManageunit(String manaunitid) {
		this.manaunitid = manaunitid;
	}

	/** full constructor */
	public EhrManageunit(String manaunitid, String manaunitname,
			String introduction, BigDecimal orderno, String createuser,
			Date createdate, String lastmodifyuser, Date lastmodifydate) {
		this.manaunitid = manaunitid;
		this.manaunitname = manaunitname;
		this.introduction = introduction;
		this.orderno = orderno;
		this.createuser = createuser;
		this.createdate = createdate;
		this.lastmodifyuser = lastmodifyuser;
		this.lastmodifydate = lastmodifydate;
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

	public String getIntroduction() {
		return this.introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public BigDecimal getOrderno() {
		return this.orderno;
	}

	public void setOrderno(BigDecimal orderno) {
		this.orderno = orderno;
	}

	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getLastmodifyuser() {
		return this.lastmodifyuser;
	}

	public void setLastmodifyuser(String lastmodifyuser) {
		this.lastmodifyuser = lastmodifyuser;
	}

	public Date getLastmodifydate() {
		return this.lastmodifydate;
	}

	public void setLastmodifydate(Date lastmodifydate) {
		this.lastmodifydate = lastmodifydate;
	}

}
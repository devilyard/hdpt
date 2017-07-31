package dc.domain.volunteer;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


public class JmhdVolunteerDonation implements java.io.Serializable {

	// Fields

	private Long donateid;
	private String empiid;
	private String personname;
	private String sexcode;
	private String nationcode;
	@DateTimeFormat(iso=ISO.DATE)
	private Date birthday;
	private String workcode;
	private String bloodtypecode;
	private String rhbloodcode;
	private String certificatetypecode;
	private String zipcode;
	private String email;
	private String address;
	private String certificateno;
	private String workplace;
	private String mobilenumber;
	private String bloodflag;
	@DateTimeFormat(iso=ISO.DATE)
	private Date lastdonateblooddate;
	private BigDecimal tdonatebloodcount;
	private BigDecimal allbloodcount;
	private Date createdate;
	private String createunit;
	private String createuser;
	private BigDecimal commponenbloodcount;
	private String cancellationuser;
	private Date cancellationdate;
	private String cancellationunit;
	private String cancellationreason;
	private String status;
	private String lastmodifyunit;
	private Date lastmodifydate;
	private String lastmodifyuser;
	private String workplacenumber;
	private String qqnumber;
	private String notes;
	private String deadreason;
	private String cardtype;

	// Constructors

	/** default constructor */
	public JmhdVolunteerDonation() {
	}

	/** minimal constructor */
	public JmhdVolunteerDonation(Long donateid) {
		this.donateid = donateid;
	}

	/** full constructor */
	public JmhdVolunteerDonation(Long donateid, String empiid,
			String personname, String sexcode, String nationcode,
			Date birthday, String workcode, String bloodtypecode,
			String rhbloodcode, String certificatetypecode, String zipcode,
			String email, String address, String certificateno,
			String workplace, String mobilenumber, String bloodflag,
			Date lastdonateblooddate, BigDecimal tdonatebloodcount,
			BigDecimal allbloodcount, Date createdate, String createunit,
			String createuser, BigDecimal commponenbloodcount,
			String cancellationuser, Date cancellationdate,
			String cancellationunit, String cancellationreason, String status,
			String lastmodifyunit, Date lastmodifydate, String lastmodifyuser,
			String workplacenumber, String qqnumber, String notes,
			String deadreason, String cardtype) {
		this.donateid = donateid;
		this.empiid = empiid;
		this.personname = personname;
		this.sexcode = sexcode;
		this.nationcode = nationcode;
		this.birthday = birthday;
		this.workcode = workcode;
		this.bloodtypecode = bloodtypecode;
		this.rhbloodcode = rhbloodcode;
		this.certificatetypecode = certificatetypecode;
		this.zipcode = zipcode;
		this.email = email;
		this.address = address;
		this.certificateno = certificateno;
		this.workplace = workplace;
		this.mobilenumber = mobilenumber;
		this.bloodflag = bloodflag;
		this.lastdonateblooddate = lastdonateblooddate;
		this.tdonatebloodcount = tdonatebloodcount;
		this.allbloodcount = allbloodcount;
		this.createdate = createdate;
		this.createunit = createunit;
		this.createuser = createuser;
		this.commponenbloodcount = commponenbloodcount;
		this.cancellationuser = cancellationuser;
		this.cancellationdate = cancellationdate;
		this.cancellationunit = cancellationunit;
		this.cancellationreason = cancellationreason;
		this.status = status;
		this.lastmodifyunit = lastmodifyunit;
		this.lastmodifydate = lastmodifydate;
		this.lastmodifyuser = lastmodifyuser;
		this.workplacenumber = workplacenumber;
		this.qqnumber = qqnumber;
		this.notes = notes;
		this.deadreason = deadreason;
		this.cardtype = cardtype;
	}

	// Property accessors


	public String getEmpiid() {
		return this.empiid;
	}

	public Long getDonateid() {
		return donateid;
	}

	public void setDonateid(Long donateid) {
		this.donateid = donateid;
	}

	public void setEmpiid(String empiid) {
		this.empiid = empiid;
	}

	public String getPersonname() {
		return this.personname;
	}

	public void setPersonname(String personname) {
		this.personname = personname;
	}

	public String getSexcode() {
		return this.sexcode;
	}

	public void setSexcode(String sexcode) {
		this.sexcode = sexcode;
	}

	public String getNationcode() {
		return this.nationcode;
	}

	public void setNationcode(String nationcode) {
		this.nationcode = nationcode;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getWorkcode() {
		return this.workcode;
	}

	public void setWorkcode(String workcode) {
		this.workcode = workcode;
	}

	public String getBloodtypecode() {
		return this.bloodtypecode;
	}

	public void setBloodtypecode(String bloodtypecode) {
		this.bloodtypecode = bloodtypecode;
	}

	public String getRhbloodcode() {
		return this.rhbloodcode;
	}

	public void setRhbloodcode(String rhbloodcode) {
		this.rhbloodcode = rhbloodcode;
	}

	public String getCertificatetypecode() {
		return this.certificatetypecode;
	}

	public void setCertificatetypecode(String certificatetypecode) {
		this.certificatetypecode = certificatetypecode;
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCertificateno() {
		return this.certificateno;
	}

	public void setCertificateno(String certificateno) {
		this.certificateno = certificateno;
	}

	public String getWorkplace() {
		return this.workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	public String getMobilenumber() {
		return this.mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getBloodflag() {
		return this.bloodflag;
	}

	public void setBloodflag(String bloodflag) {
		this.bloodflag = bloodflag;
	}

	public Date getLastdonateblooddate() {
		return this.lastdonateblooddate;
	}

	public void setLastdonateblooddate(Date lastdonateblooddate) {
		this.lastdonateblooddate = lastdonateblooddate;
	}

	public BigDecimal getTdonatebloodcount() {
		return this.tdonatebloodcount;
	}

	public void setTdonatebloodcount(BigDecimal tdonatebloodcount) {
		this.tdonatebloodcount = tdonatebloodcount;
	}

	public BigDecimal getAllbloodcount() {
		return this.allbloodcount;
	}

	public void setAllbloodcount(BigDecimal allbloodcount) {
		this.allbloodcount = allbloodcount;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getCreateunit() {
		return this.createunit;
	}

	public void setCreateunit(String createunit) {
		this.createunit = createunit;
	}

	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public BigDecimal getCommponenbloodcount() {
		return this.commponenbloodcount;
	}

	public void setCommponenbloodcount(BigDecimal commponenbloodcount) {
		this.commponenbloodcount = commponenbloodcount;
	}

	public String getCancellationuser() {
		return this.cancellationuser;
	}

	public void setCancellationuser(String cancellationuser) {
		this.cancellationuser = cancellationuser;
	}

	public Date getCancellationdate() {
		return this.cancellationdate;
	}

	public void setCancellationdate(Date cancellationdate) {
		this.cancellationdate = cancellationdate;
	}

	public String getCancellationunit() {
		return this.cancellationunit;
	}

	public void setCancellationunit(String cancellationunit) {
		this.cancellationunit = cancellationunit;
	}

	public String getCancellationreason() {
		return this.cancellationreason;
	}

	public void setCancellationreason(String cancellationreason) {
		this.cancellationreason = cancellationreason;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastmodifyunit() {
		return this.lastmodifyunit;
	}

	public void setLastmodifyunit(String lastmodifyunit) {
		this.lastmodifyunit = lastmodifyunit;
	}

	public Date getLastmodifydate() {
		return this.lastmodifydate;
	}

	public void setLastmodifydate(Date lastmodifydate) {
		this.lastmodifydate = lastmodifydate;
	}

	public String getLastmodifyuser() {
		return this.lastmodifyuser;
	}

	public void setLastmodifyuser(String lastmodifyuser) {
		this.lastmodifyuser = lastmodifyuser;
	}

	public String getWorkplacenumber() {
		return this.workplacenumber;
	}

	public void setWorkplacenumber(String workplacenumber) {
		this.workplacenumber = workplacenumber;
	}

	public String getQqnumber() {
		return this.qqnumber;
	}

	public void setQqnumber(String qqnumber) {
		this.qqnumber = qqnumber;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getDeadreason() {
		return this.deadreason;
	}

	public void setDeadreason(String deadreason) {
		this.deadreason = deadreason;
	}

	public String getCardtype() {
		return this.cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

}
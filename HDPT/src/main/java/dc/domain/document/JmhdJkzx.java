package dc.domain.document;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * JmhdJkzx entity. @author MyEclipse Persistence Tools
 */

public class JmhdJkzx implements java.io.Serializable {

	// Fields

	private Long zxid;
	private Long type;
	private String subject;
	private String titlefirst;
	private String titlesecond;
	private String author;
	private String fromw;
	private String mark;
	private Date createdate;
	private int page;
	private String status;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date beginDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	private String url;
	private String orgId;

	// Constructors

	/** default constructor */
	public JmhdJkzx() {
	}

	/** minimal constructor */
	public JmhdJkzx(Long zxid) {
		this.zxid = zxid;
	}

	/** full constructor */
	public JmhdJkzx(Long zxid, Long type, String subject, String titlefirst,
			String titlesecond, String author, String fromw, String mark,
			Date createdate) {
		this.zxid = zxid;
		this.type = type;
		this.subject = subject;
		this.titlefirst = titlefirst;
		this.titlesecond = titlesecond;
		this.author = author;
		this.fromw = fromw;
		this.mark = mark;
		this.createdate = createdate;
	}

	// Property accessors

	public Long getZxid() {
		return this.zxid;
	}

	public void setZxid(Long zxid) {
		this.zxid = zxid;
	}

	public Long getType() {
		return this.type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTitlefirst() {
		return this.titlefirst;
	}

	public void setTitlefirst(String titlefirst) {
		this.titlefirst = titlefirst;
	}

	public String getTitlesecond() {
		return this.titlesecond;
	}

	public void setTitlesecond(String titlesecond) {
		this.titlesecond = titlesecond;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getFromw() {
		return this.fromw;
	}

	public void setFromw(String fromw) {
		this.fromw = fromw;
	}

	public String getMark() {
		return this.mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
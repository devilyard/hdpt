package dc.domain.document;

import java.util.Date;

/**
 * JmhdWszw entity. @author MyEclipse Persistence Tools
 */

public class JmhdWszw implements java.io.Serializable {

	// Fields

	private Long zwid;
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
	private String url;

	// Constructors

	/** default constructor */
	public JmhdWszw() {
	}

	/** minimal constructor */
	public JmhdWszw(Long zwid) {
		this.zwid = zwid;
	}

	/** full constructor */
	public JmhdWszw(Long zwid, Long type, String subject, String titlefirst,
			String titlesecond, String author, String fromw, String mark,
			Date createdate) {
		this.zwid = zwid;
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

	public Long getZwid() {
		return this.zwid;
	}

	public void setZwid(Long zwid) {
		this.zwid = zwid;
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
	
	
}
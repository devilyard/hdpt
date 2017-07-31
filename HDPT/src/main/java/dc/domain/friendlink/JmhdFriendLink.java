package dc.domain.friendlink;

import java.util.Date;

/**
 * JmhdFriendLink entity. @author MyEclipse Persistence Tools
 */

public class JmhdFriendLink implements java.io.Serializable {

	// Fields

	private Long id;
	private String linkname;
	private String linkurl;
	private Date createdate;
	private String status;
	private String sort;
	private String org;
	private String orgCode;
	private int page;

	// Constructors

	/** default constructor */
	public JmhdFriendLink() {
	}

	/** minimal constructor */
	public JmhdFriendLink(Long id) {
		this.id = id;
	}

	/** full constructor */
	public JmhdFriendLink(Long id, String linkname, String linkurl,
			Date createdate, String status, String sort, String org,
			String orgCode) {
		this.id = id;
		this.linkname = linkname;
		this.linkurl = linkurl;
		this.createdate = createdate;
		this.status = status;
		this.sort = sort;
		this.org = org;
		this.orgCode = orgCode;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLinkname() {
		return this.linkname;
	}

	public void setLinkname(String linkname) {
		this.linkname = linkname;
	}

	public String getLinkurl() {
		return this.linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSort() {
		return this.sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrg() {
		return this.org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

}
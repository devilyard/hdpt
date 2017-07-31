package dc.domain.document;

import java.util.Date;



public class JmhdCmsDocument implements java.io.Serializable {

	// Fields

	private Long id;
	private String title;
	private String subtitle;
	private String content;
	private String category;
	private String author;
	private String source;
	private String creator;
	private Date filldate;
	private String status;
	private String tags;
	private String photonews;
	private String orgId;
	private String url;

	// Constructors

	/** default constructor */
	public JmhdCmsDocument() {
	}

	/** minimal constructor */
	public JmhdCmsDocument(Long id) {
		this.id = id;
	}

	/** full constructor */
	public JmhdCmsDocument(Long id, String title, String subtitle,
			String content, String category, String author, String source,
			String creator, Date filldate, String status, String tags,
			String photonews, String orgId) {
		this.id = id;
		this.title = title;
		this.subtitle = subtitle;
		this.content = content;
		this.category = category;
		this.author = author;
		this.source = source;
		this.creator = creator;
		this.filldate = filldate;
		this.status = status;
		this.tags = tags;
		this.photonews = photonews;
		this.orgId = orgId;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return this.subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getFilldate() {
		return this.filldate;
	}

	public void setFilldate(Date filldate) {
		this.filldate = filldate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTags() {
		return this.tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getPhotonews() {
		return this.photonews;
	}

	public void setPhotonews(String photonews) {
		this.photonews = photonews;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
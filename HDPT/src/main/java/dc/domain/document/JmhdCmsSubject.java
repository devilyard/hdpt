package dc.domain.document;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * JmhdCmsSubject entity. @author MyEclipse Persistence Tools
 */

public class JmhdCmsSubject implements java.io.Serializable {

	// Fields

	private Long id;
	private String subjectname;
	private String description;
	private String creator;
	private Date createdate;
	private String moduleCode;
	private String moduleName;
	private int page;
	@DateTimeFormat(pattern="yyyy-MM-dd")  
	private Date beginDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")  
	private Date endDate;

	// Constructors

	/** default constructor */
	public JmhdCmsSubject() {
	}

	/** minimal constructor */
	public JmhdCmsSubject(Long id) {
		this.id = id;
	}


	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubjectname() {
		return this.subjectname;
	}

	public void setSubjectname(String subjectname) {
		this.subjectname = subjectname;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
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
	
}
package dc.domain.main;

/**
 * JmhdSysModule entity. @author MyEclipse Persistence Tools
 */

public class JmhdSysModule implements java.io.Serializable {

	// Fields

	private Integer id;
	private String moduleName;
	private String actionName;
	private String actionMethod;
	private String moduleCode;
	private Long moduleLev;
	private String isleaf;
	private String url;

	// Constructors

	/** default constructor */
	public JmhdSysModule() {
	}

	/** minimal constructor */
	public JmhdSysModule(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public JmhdSysModule(Integer id, String moduleName, String actionName,
			String actionMethod, String moduleCode, Long moduleLev,
			String isleaf, String url) {
		this.id = id;
		this.moduleName = moduleName;
		this.actionName = actionName;
		this.actionMethod = actionMethod;
		this.moduleCode = moduleCode;
		this.moduleLev = moduleLev;
		this.isleaf = isleaf;
		this.url = url;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionMethod() {
		return this.actionMethod;
	}

	public void setActionMethod(String actionMethod) {
		this.actionMethod = actionMethod;
	}

	public String getModuleCode() {
		return this.moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public Long getModuleLev() {
		return this.moduleLev;
	}

	public void setModuleLev(Long moduleLev) {
		this.moduleLev = moduleLev;
	}

	public String getIsleaf() {
		return this.isleaf;
	}

	public void setIsleaf(String isleaf) {
		this.isleaf = isleaf;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
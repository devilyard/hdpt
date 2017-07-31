package dc.domain.diary;



public class JmhdLibIndicator implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String exp;
	private String status;
	private Integer pid;
	private String units;
	private String assemble;
	private String formula;

	// Constructors

	/** default constructor */
	public JmhdLibIndicator() {
	}

	/** minimal constructor */
	public JmhdLibIndicator(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public JmhdLibIndicator(Integer id, String name, String exp, String status,
			Integer pid, String units, String assemble, String formula) {
		this.id = id;
		this.name = name;
		this.exp = exp;
		this.status = status;
		this.pid = pid;
		this.units = units;
		this.assemble = assemble;
		this.formula = formula;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExp() {
		return this.exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPid() {
		return this.pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getUnits() {
		return this.units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getAssemble() {
		return assemble;
	}

	public void setAssemble(String assemble) {
		this.assemble = assemble;
	}

	public String getFormula() {
		return this.formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

}
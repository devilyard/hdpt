package dc.domain.diary;



public class JmhdRelatedDiaryIndicator implements java.io.Serializable {

	// Fields

	private JmhdRelatedDiaryIndicatorId id;

	// Constructors

	/** default constructor */
	public JmhdRelatedDiaryIndicator() {
	}

	/** full constructor */
	public JmhdRelatedDiaryIndicator(JmhdRelatedDiaryIndicatorId id) {
		this.id = id;
	}

	// Property accessors

	public JmhdRelatedDiaryIndicatorId getId() {
		return this.id;
	}

	public void setId(JmhdRelatedDiaryIndicatorId id) {
		this.id = id;
	}

}
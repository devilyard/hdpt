package dc.domain.diary;

/**
 * JkwRelatedDiaryIndicatorId entity. @author MyEclipse Persistence Tools
 */

public class JmhdRelatedDiaryIndicatorId implements java.io.Serializable {

	// Fields

	private Long diaryId;
	private String indicatorId;

	// Constructors

	/** default constructor */
	public JmhdRelatedDiaryIndicatorId() {
	}

	/** full constructor */
	public JmhdRelatedDiaryIndicatorId(Long diaryId, String indicatorId) {
		this.diaryId = diaryId;
		this.indicatorId = indicatorId;
	}

	// Property accessors

	public Long getDiaryId() {
		return this.diaryId;
	}

	public void setDiaryId(Long diaryId) {
		this.diaryId = diaryId;
	}

	public String getIndicatorId() {
		return this.indicatorId;
	}

	public void setIndicatorId(String indicatorId) {
		this.indicatorId = indicatorId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof JmhdRelatedDiaryIndicatorId))
			return false;
		JmhdRelatedDiaryIndicatorId castOther = (JmhdRelatedDiaryIndicatorId) other;

		return ((this.getDiaryId() == castOther.getDiaryId()) || (this
				.getDiaryId() != null && castOther.getDiaryId() != null && this
				.getDiaryId().equals(castOther.getDiaryId())))
				&& ((this.getIndicatorId() == castOther.getIndicatorId()) || (this
						.getIndicatorId() != null
						&& castOther.getIndicatorId() != null && this
						.getIndicatorId().equals(castOther.getIndicatorId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getDiaryId() == null ? 0 : this.getDiaryId().hashCode());
		result = 37
				* result
				+ (getIndicatorId() == null ? 0 : this.getIndicatorId()
						.hashCode());
		return result;
	}

}
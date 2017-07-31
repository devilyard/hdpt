package dc.domain.main;

import java.io.Serializable;
import java.util.List;

import dc.domain.diary.JmhdUserDiary;
import dc.domain.diary.JmhdUserIndicator;

public class JmhdInfo
  implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<JmhdUserIndicator> indicators;
	
	private JmhdUserDiary diary;
	
	private String isend;
	
	private String title;
	
	private int page;
	
	private String beginDate;
	
	private String endDate;
	
    private String cardtype;
	
	private String status;
	
	private String extendtitlefir;
	
	private Long orderNum;
	
	public List<JmhdUserIndicator> getIndicators() {
		return indicators;
	}
	
	public void setIndicators(List<JmhdUserIndicator> indicators) {
		this.indicators = indicators;
	}

	public JmhdUserDiary getDiary() {
		return diary;
	}

	public void setDiary(JmhdUserDiary diary) {
		this.diary = diary;
	}

	public String getIsend() {
		return isend;
	}

	public void setIsend(String isend) {
		this.isend = isend;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExtendtitlefir() {
		return extendtitlefir;
	}

	public void setExtendtitlefir(String extendtitlefir) {
		this.extendtitlefir = extendtitlefir;
	}

	public Long getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Long orderNum) {
		this.orderNum = orderNum;
	}
	
}
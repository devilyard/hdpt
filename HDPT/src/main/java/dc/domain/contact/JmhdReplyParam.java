package dc.domain.contact;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 回复参数
 * Created by WYI on 2016/4/26.
 */
public class JmhdReplyParam implements Serializable {
    private String doctorId;
    private String doctorName;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date replyDate;
    private String replyContent;

    public String getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    public Date getReplyDate() {
        return replyDate;
    }
    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }
    public String getReplyContent() {
        return replyContent;
    }
    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }
}

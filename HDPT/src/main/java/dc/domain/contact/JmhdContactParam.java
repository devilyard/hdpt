package dc.domain.contact;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资讯互动接口参数类
 * Created by WYI on 2016/4/26.
 */
public class JmhdContactParam implements Serializable {
    private Long questionId;
    private String questionContent;
    private Map<String, byte[]> pictures;
    private String userId;
    private String userName;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date questionTime;
    private String status;
    private List<JmhdReplyParam> replies;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public Map<String, byte[]> getPictures() {
        return pictures;
    }

    public void setPictures(Map<String, byte[]> pictures) {
        this.pictures = pictures;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(Date questionTime) {
        this.questionTime = questionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<JmhdReplyParam> getReplies() {
        return replies;
    }

    public void setReplies(List<JmhdReplyParam> replies) {
        this.replies = replies;
    }
}

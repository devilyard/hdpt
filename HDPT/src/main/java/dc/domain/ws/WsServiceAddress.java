package dc.domain.ws;

import java.util.Date;

public class WsServiceAddress {

	private long id;
	private long wstype;
	private String wsname;
	private String url;
	private Date createtime;
	private long enabled;
	private String appid;
	private String sectrekey;
	
	public WsServiceAddress() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getWstype() {
		return wstype;
	}

	public void setWstype(long wstype) {
		this.wstype = wstype;
	}

	public String getWsname() {
		return wsname;
	}

	public void setWsname(String wsname) {
		this.wsname = wsname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getEnabled() {
		return enabled;
	}

	public void setEnabled(long enabled) {
		this.enabled = enabled;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSectrekey() {
		return sectrekey;
	}

	public void setSectrekey(String sectrekey) {
		this.sectrekey = sectrekey;
	}
	
}

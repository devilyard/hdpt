package dc.domain.ondemand;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Request") 
public class OndemandResponse {

	private String Header;

	public String getHeader() {
		return Header;
	}

	public void setHeader(String header) {
		Header = header;
	}
	
}

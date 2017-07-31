package dc.job;

import java.util.HashMap;
import java.util.List;

import org.dom4j.DocumentException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import dc.util.ConfigUtil;
import dc.util.Util;

public class RssJob extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(RssJob.class);
	
	public static List<HashMap<String,Object>>  list;
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		try {
			list = Util.RSSTest(ConfigUtil.getValue("healthe.ducation"));
			
		} catch (DocumentException e) {
			LOGGER.error("RssJob is error:"+e);
		}
	}

	public static void setList(List<HashMap<String, Object>> list) {
		RssJob.list = list;
	}
	
	
	

}

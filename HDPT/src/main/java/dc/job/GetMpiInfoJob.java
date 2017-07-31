package dc.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.bsoft.mpi.Constants;
import com.bsoft.mpi.server.rpc.IMPIProvider;
import com.bsoft.xds.support.dao.IDAO;


public class GetMpiInfoJob extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetMpiInfoJob.class);
	
	private IMPIProvider mpiService;
	
	private IDAO serviceDao;
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		try {
			
			List<Map<String, Object>> meberlist = serviceDao.queryForList("JMHD_MEMBER", "STATUS is null order by LASTMODIFYDATE desc", new Object[]{});
			
			if(meberlist !=null && meberlist.size()>0){
				
				Map<String, Object> map = meberlist.get(0);
				
				if(map != null && map.get("CERTIFICATETYPECODE") != null){
					
					Map<String, Object> mpiArgs = new HashMap<String, Object>();
					List<Map<String, Object>> list = null;
					//身份证
					if("01".equals(map.get("CERTIFICATETYPECODE"))){
						mpiArgs.put(Constants.F_IDCARD, map.get("CERTIFICATENO"));
						list = mpiService.getMPIDetail(mpiArgs); 
					}else{
						Map<String, Object> certificatesmap = new HashMap<String, Object>();
						certificatesmap.put("certificateNo", map.get("CERTIFICATENO"));
						certificatesmap.put("certificateTypeCode", map.get("CERTIFICATETYPECODE"));
						List<Map<String, Object>> certificateslist = new ArrayList<Map<String, Object>>();
						certificateslist.add(certificatesmap);
						mpiArgs.put("certificates", certificateslist);
						list = mpiService.getMPIDetail(mpiArgs); 
					}
					
					if(list!= null && list.size()>0){
						Map<String,Object> m = list.get(0);
						
						map.put("MPIID", m.get("mpiId"));
						map.put("STATUS", "0");
						map.put("HOMEADDRESS", m.get("address"));
						map.put("CONTACT", m.get("contactName"));
						map.put("CONTACTPHONE", m.get("contactPhone"));
						map.put("LASTMODIFYDATE", new Date());
						
						serviceDao.update("JMHD_MEMBER", map);
					}
					
				}
				
				
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("GetMpiInfoJob is error:"+e);
		}
	}

	public void setMpiService(IMPIProvider mpiService) {
		this.mpiService = mpiService;
	}

	public void setServiceDao(IDAO serviceDao) {
		this.serviceDao = serviceDao;
	}

}

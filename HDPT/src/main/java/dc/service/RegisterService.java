package dc.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsoft.xds.support.dao.IDAO;

import dc.domain.main.YyUserAccount;

@Service
public class RegisterService implements IRegisterService {

	@Autowired
	private IDAO serviceDao;
	
	
	public void save(YyUserAccount account,Map<String, Object> JMHD_MEMBER){
		
		serviceDao.save("YyUserAccount", account);
		serviceDao.save("JMHD_MEMBER", JMHD_MEMBER);
	}
	
	public void saveUpdate(YyUserAccount account,Map<String, Object> JMHD_MEMBER){
		
		serviceDao.update("YyUserAccount", account);
		serviceDao.save("JMHD_MEMBER", JMHD_MEMBER);
	}
	
	public Map<String, Object> queryForMap(String classifying ,String where, Object[] params){
		return  serviceDao.queryForMap(classifying, where, params);
	}
	
	
	public <T> T queryT(String classifying ,String where, Object[] params){
		return serviceDao.queryT(classifying, where, params);
	}
}

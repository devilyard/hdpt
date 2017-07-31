package dc.service;

import java.util.Map;


import dc.domain.main.YyUserAccount;

public interface IRegisterService {


	public void save(YyUserAccount account,Map<String, Object> JMHD_MEMBER);
	
	public void saveUpdate(YyUserAccount account,Map<String, Object> JMHD_MEMBER);
	
	public Map<String, Object> queryForMap(String classifying ,String where, Object[] params);
	
	public <T> T queryT(String classifying ,String where, Object[] params);
}

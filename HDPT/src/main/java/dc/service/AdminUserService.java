package dc.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsoft.xds.support.dao.IDAO;



@Service
public class AdminUserService implements IRoleService {

	@Autowired
	private IDAO serviceDao;

	@Override
	public boolean delete(int id) {
		try{
			/*同时也删除角色和后台用户管理中的信息*/
			serviceDao.delete("JmhdRelatedAdminRole", "adminId=?", new Object[]{id});
			
			serviceDao.delete("JmhdSysAdminuser", " adminId=? ", new Object[]{id});
		}catch(Exception e){
			return false;
		}
		return true;
		
	}

	@Override
	public boolean setModule(int id, String moduleString) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteByUpdate(int id) {
		// TODO Auto-generated method stub
		return false;
	}


	
	
	
}

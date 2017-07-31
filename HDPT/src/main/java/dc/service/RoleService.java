package dc.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsoft.xds.support.dao.IDAO;

import dc.domain.adminuser.JmhdRelatedAdminRole;
import dc.domain.role.JmhdRelatedRoleModule;
import dc.domain.role.JmhdSysRole;


@Service
public class RoleService implements IRoleService {

	@Autowired
	private IDAO serviceDao;

	@Override
	public boolean delete(int id) {
		try{
			/*先删除 角色和模块关联表中的信息*/
			serviceDao.delete("JmhdRelatedRoleModule", "roleId=?", new Object[]{id});
			
			/*同时也删除角色和后台用户管理中的信息*/
			serviceDao.delete("JmhdRelatedAdminRole", "roleId=?", new Object[]{id});
			
			serviceDao.delete("JmhdSysRole", " roleId=? ", new Object[]{id});
		}catch(Exception e){
			return false;
		}
		return true;
		
	}
	
	@Override
	public boolean deleteByUpdate(int id) {
		try {
			/* 先删除 角色和模块关联表中的信息 */
			serviceDao.update("JmhdRelatedRoleModule", " set enabled = 0 ", "roleId=?", new Object[]{id});

			/* 同时也删除角色和后台用户管理中的信息 */
			serviceDao.update("JmhdRelatedAdminRole", " set enabled = 0 ", "roleId=?", new Object[]{id});

			JmhdSysRole jmhdSysRole = serviceDao.queryT("JmhdSysRole", "roleId=?", new Object[] { id });
			if (jmhdSysRole != null) {
				jmhdSysRole.setEnabled(new Integer(0));
				serviceDao.update("JmhdSysRole", jmhdSysRole);
			}
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	@Override
	public boolean setModule(int roleId, String moduleString) {
		try{
			serviceDao.delete("JmhdRelatedRoleModule", "roleId=?", new Object[]{roleId});
			
			if(moduleString!=null&&!"".equals(moduleString)){
				String[] moduleIdArray=moduleString.split(",");
				for(String moduleId:moduleIdArray){
					JmhdRelatedRoleModule roleModule=new JmhdRelatedRoleModule();
					roleModule.setModuleId(Integer.parseInt(moduleId.trim()));
					roleModule.setRoleId(roleId);
					serviceDao.save("JmhdRelatedRoleModule",roleModule);
				}
			}
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	
	
}

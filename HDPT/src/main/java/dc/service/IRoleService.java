package dc.service;


public interface IRoleService {


	public boolean delete(int id);
	
	public boolean deleteByUpdate(int id);
	
	public boolean setModule(int id,String moduleString);
}

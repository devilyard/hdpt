package dc.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;

public interface IEHRViewService extends IService {

	public Map<String, List<Map<String, Object>>> findFamilyHistory(HttpServletRequest request, String mpiId)
			throws HibernateException;

	public String findContent(String entryName, String cnd)
			throws HibernateException;

}

package dc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import wl.zs.utils.DateUtils;

import com.bsoft.mpi.server.MPIServiceException;
import com.bsoft.mpi.server.rpc.IMPIProvider;

import ctd.util.context.Context;
import ctd.util.exp.ExpException;
import ctd.util.exp.ExpRunner;
import dc.dictionary.instance.Population;
import dc.util.MPIProxy;

public class EHRViewService extends BaseService implements IEHRViewService {

//	@Autowired
	private IMPIProvider mpiProvider;

	public IMPIProvider getMpiProvider() {
		return mpiProvider;
	}

	public void setMpiProvider(IMPIProvider mpiProvider) {
		this.mpiProvider = mpiProvider;
	}

	public static void removeContent(List<Map<String, Object>> list) {
		for (Map<String, Object> m : list) {
			m.remove("DOCCONTENT");
		}
	}

	@Override
	public String findContent(String entryName, String cnd)
			throws HibernateException {
		String where;
		try {
			where = ExpRunner.toString(cnd, new Context());
		} catch (ExpException e) {
			throw new HibernateException("Make where crouse failed.", e);
		}
		List<Object> list = find(entryName, "DocContent", where);
		if (list.size() > 0) {
			byte[] content = (byte[]) list.get(0);
			return new String(content);
		}
		return null;
	}

	@Override
	public Map<String, List<Map<String, Object>>> findFamilyHistory(HttpServletRequest request, String mpiId)
			throws HibernateException {
		List<Map<String, Object>> list = find("EHR_HealthRecord", "mpiId=?",
				(Object) mpiId);
		if (list.size() < 1) {
			return null;
		}
		
		String sexCode = null;
		try {
			Map<String, Object> baseInfo = MPIProxy.getBaseMPI(request, mpiId, mpiProvider);
			if(baseInfo != null){
				sexCode = baseInfo.get("sexCode").toString();
			}else {
				return null;
			}
		} catch (MPIServiceException e) {
			e.printStackTrace();
		}
		
		Map<String, Object> r = list.get(0);
		String MOTHERID = (String) r.get("MOTHERID");
//		System.out.println(MOTHERID);
		String PARTNERID = (String) r.get("PARTNERID");
//		System.out.println(PARTNERID);
		String FATHERID = (String) r.get("FATHERID");
//		System.out.println(FATHERID);
		HashMap<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		map.put("MOTHERID",
				find("SUMMARY_Hist_Sick", "mpiId=?", (Object) MOTHERID));
		if(sexCode.equals("2")){
			map.put("HUSBAND",
					find("SUMMARY_Hist_Sick", "mpiId=?", (Object) PARTNERID));
		}else {
			map.put("WIFE",
					find("SUMMARY_Hist_Sick", "mpiId=?", (Object) PARTNERID));
		}
		map.put("FATHERID",
				find("SUMMARY_Hist_Sick", "mpiId=?", (Object) FATHERID));
		return map;
	}

	/**
	 * �жϵ�ǰ��Ա��������Ⱥ����Ⱥ����Ϊ��ͯ�������ˣ���Ѫѹ���ˣ����򲡲��ˣ��и���
	 * 
	 * @param mpiId
	 * @return
	 * @throws HibernateException
	 * @throws MPIServiceException
	 * @throws DocumentException
	 */
	public List<String> getPopulations(String mpiId) throws HibernateException,
			MPIServiceException, DocumentException {
		Map<String, Object> mpi = mpiProvider.getMPI(mpiId);
		if (mpi == null || mpi.size() == 0) {
			return null;
		}
		Date birthDate = (Date) mpi.get("birthday");
		List<String> list = new ArrayList<String>(5);
		if (birthDate != null) {
			int age = DateUtils.getAnniversary(birthDate, new Date());
			if (age <= 14) {
				list.add(Population.Child);
			}
			if (age >= 60) {
				list.add(Population.OldPeople);
			}
		}
		List<Map<String, Object>> prs = find("MHC_PregnantRecord", "mpiId=?",
				(Object) mpiId);
		for (Map<String, Object> m : prs) {
			Document doc = DocumentHelper.parseText((String) m
					.get("DOCCONTENT"));
			Element root = doc.getRootElement();
			String status = root.elementText("Status");
			Date lastMenstrualPeriod = DateUtils.toDate(root
					.elementText("LastMenstrualPeriod"));
			int period = DateUtils.getPeriod(lastMenstrualPeriod, new Date());
			if (status.equals("0") && period <= 300) {
				list.add(Population.Pregnant);
				break;
			}
		}
		List<Map<String, Object>> hyperRec = getBaseDao().find(
				"MDC_HypertensionRecord", false, "mpiId=?", (Object) mpiId);
		if (hyperRec.size() > 0) {
			list.add(Population.Hypertension);
		}
		List<Map<String, Object>> diaRec = getBaseDao().find("MDC_DiabetesRecord",
				false, "mpiId=?", (Object) mpiId);
		if (diaRec.size() > 0) {
			list.add(Population.Diabetes);
		}
		return list;
	}
}

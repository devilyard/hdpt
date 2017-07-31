package dc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LifecycelUtil {

	/**
	 * �������������ڡ�������
	 * 
	 * @param list
	 * @return
	 */
	public static List<Object> parseLifecycle(List<Map<String, Object>> list) {
		List<Object> result = new ArrayList<Object>();
		// �����ڼ�¼
		List<Object> old = new ArrayList<Object>();
		// �����ڼ�¼
		List<Object> mid = new ArrayList<Object>();
		// �����ڼ�¼
		List<Object> qn = new ArrayList<Object>();
		// �����ڼ�¼
		List<Object> yon = new ArrayList<Object>();
		// ��ͯ�ڼ�¼
		List<Object> ert = new ArrayList<Object>();
		// Ӥ���ڼ�¼
		List<Object> yer = new ArrayList<Object>();

		for (Map<String, Object> map : list) {
			String timeslot = map.get("TIMESLOT").toString();
			if (timeslot.equals("gerontic")) {
				old.add(map);
			} else if ("middleAge".equals(timeslot)) {
				mid.add(map);
			} else if ("adolescence".equals(timeslot)) {
				qn.add(map);
			} else if ("boyhood".equals(timeslot)) {
				yon.add(map);
			} else if ("childhood".equals(timeslot)) {
				ert.add(map);
			} else {
				yer.add(map);
			}
		}
		old = parseMap(old);
		mid = parseMap(mid);
		yon = parseMap(yon);
		qn = parseMap(qn);
		ert = parseMap(ert);
		yer = parseMap(yer);

		result.add(old);
		result.add(mid);
		result.add(qn);
		result.add(yon);
		result.add(ert);
		result.add(yer);
		return result;
	}

	/**
	 * ���ݡ��������ڡ���¼�����ͣ�����¼���ࡣ
	 * 
	 * @param objs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<Object> parseMap(List<Object> objs) {
		// ��������¼
		List<Map<String, Object>> mzjz = new ArrayList<Map<String, Object>>();
		// סԺ�����¼
		List<Map<String, Object>> zyjz = new ArrayList<Map<String, Object>>();
		// ��鱨��
		List<Map<String, Object>> jcbg = new ArrayList<Map<String, Object>>();
		// ������¼
		List<Map<String, Object>> ssjl = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		String contTable = null;
		for (Object o : objs) {
			map = (Map<String, Object>) o;
			// ������������
			contTable = map.get("CONTINGENCYTABLE").toString();
			if ("EMR_INPATIENTRECORDHOME".equalsIgnoreCase(contTable)) {
				zyjz.add(map);
			} else if ("OPT_Record".equalsIgnoreCase(contTable)) {
				mzjz.add(map);
			} else if ("PT_Operation".equalsIgnoreCase(contTable)) {
				ssjl.add(map);
			} else {
				jcbg.add(map);
			}
		}
		objs = new ArrayList<Object>();
		objs.add(zyjz);
		objs.add(mzjz);
		objs.add(jcbg);
		objs.add(ssjl);
		if (zyjz.size() == 0 && mzjz.size() == 0 && jcbg.size() == 0
				&& ssjl.size() == 0) {
			objs = new ArrayList<Object>();
		}
		return objs;
	}

}

package dc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LifecycelUtil {

	/**
	 * 解析“生命周期”表数据
	 * 
	 * @param list
	 * @return
	 */
	public static List<Object> parseLifecycle(List<Map<String, Object>> list) {
		List<Object> result = new ArrayList<Object>();
		// 老年期记录
		List<Object> old = new ArrayList<Object>();
		// 中年期记录
		List<Object> mid = new ArrayList<Object>();
		// 青年期记录
		List<Object> qn = new ArrayList<Object>();
		// 少年期记录
		List<Object> yon = new ArrayList<Object>();
		// 儿童期记录
		List<Object> ert = new ArrayList<Object>();
		// 婴儿期记录
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
	 * 根据“生命周期”记录的类型，将记录分类。
	 * 
	 * @param objs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<Object> parseMap(List<Object> objs) {
		// 门诊就诊记录
		List<Map<String, Object>> mzjz = new ArrayList<Map<String, Object>>();
		// 住院就诊记录
		List<Map<String, Object>> zyjz = new ArrayList<Map<String, Object>>();
		// 检查报告
		List<Map<String, Object>> jcbg = new ArrayList<Map<String, Object>>();
		// 手术记录
		List<Map<String, Object>> ssjl = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		String contTable = null;
		for (Object o : objs) {
			map = (Map<String, Object>) o;
			// 就诊服务关联表
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

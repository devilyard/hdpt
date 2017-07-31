/*
 * @(#)MPIQueryService.java Created on Nov 9, 2012 12:18:05 PM
 *
 * ��Ȩ����Ȩ���� B-Soft ��������Ȩ����
 */
package dc.service.ssdev;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bsoft.mpi.Constants;
import com.bsoft.mpi.exp.CExpRunner;
import com.bsoft.mpi.server.MPIServiceException;
import com.bsoft.mpi.server.rpc.IMPIProvider;
import com.bsoft.mpi.service.ServiceCode;

import ctd.accredit.User;
import ctd.accredit.condition.Condition;
import ctd.accredit.result.AuthorizeResult;
import ctd.config.schema.Schema;
import ctd.config.schema.SchemaController;
import ctd.config.schema.SchemaItem;
import ctd.service.core.Service;
import ctd.service.core.ServiceException;
import ctd.util.context.Context;
import ctd.util.exp.ExpException;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class MPIQueryService implements Service {

	private static final Logger logger = LoggerFactory
			.getLogger(MPIQueryService.class);

	private static final int DEFAULT_PAGESIZE = 25;
	private static final int DEFAULT_PAGENO = 1;
	public final static String DEFAULT_QUERYCNDSTYPE = "filter";
	private String timeZone = "Asia/Shanghai";
	@Autowired
	private IMPIProvider mpiProvider;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ctd.service.core.Service#execute(java.util.Map, java.util.Map,
	 * ctd.util.context.Context)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Map<String, Object> req, Map<String, Object> res,
			Context ctx) throws ServiceException {
		int code = 200;
		String msg = "Success";
		String schemaId = (String) req.get("schema");
		if (StringUtils.isEmpty(schemaId)) {
			code = 401;
			msg = "SchemaIdMissed";
			res.put(Service.RES_CODE, code);
			res.put(Service.RES_MESSAGE, msg);
			return;
		}
		Schema sc = SchemaController.instance().getSchema(schemaId);
		if (sc == null) {
			code = 402;
			msg = "NoSuchSchema";
			res.put(Service.RES_CODE, code);
			res.put(Service.RES_MESSAGE, msg);
			return;
		}
		if (StringUtils.isEmpty(schemaId)) {
			code = 401;
			msg = "solrCoreMissed";
			res.put(Service.RES_CODE, code);
			res.put(Service.RES_MESSAGE, msg);
			return;
		}
		List<String> queryCnd = null;
		if (req.containsKey("cnd")) {
			queryCnd = (List<String>) req.get("cnd");
		}
		String queryCndsType = null;
		if (req.containsKey("queryCndsType")) {
			queryCndsType = (String) req.get("queryCndsType");
		}
		String sortInfo = null;
		if (req.containsKey("sortInfo")) {
			sortInfo = (String) req.get("sortInfo");
		} else {
			sortInfo = sc.getSortInfo();
		}
		QueryContext qc = initQueryContext(sc, queryCnd, queryCndsType,
				sortInfo, ctx);
		String q = qc.buildQueryString();
		if (q.isEmpty()) {
			q = "*:*";
		}
		String fl = qc.buildQueryField();
		String sort = qc.buildSortString();

		int pageSize = DEFAULT_PAGESIZE;
		if (req.containsKey("pageSize")) {
			pageSize = (Integer) req.get("pageSize");
		}
		int pageNo = DEFAULT_PAGENO;
		if (req.containsKey("pageNo")) {
			pageNo = (Integer) req.get("pageNo");
		}
		int first = (pageNo - 1) * pageSize;

		Map<String, Object> rst;
		try {
			rst = mpiProvider.getMPIList(fl, q, sort, first, pageSize);
		} catch (MPIServiceException e) {
			logger.error(e.getMessage(), e);
			res.put(Service.RES_CODE, ServiceCode.CODE_SERVICE_ERROR);
			res.put(Service.RES_MESSAGE,
					"SolrServerException:" + e.getMessage());
			return;
		}
		if (rst == null || rst.isEmpty()) {
			logger.warn("No record is found.");
			res.put(Service.RES_CODE, ServiceCode.CODE_OK);
			res.put("totalCount", 0);
			return;
		}
		res.put("totalCount", rst.get("totalCount"));
		List<Map<String, Object>> list = (List<Map<String, Object>>) rst.get(Constants.P_BODY);
		List<Map<String, Object>> body = new ArrayList<Map<String, Object>>(list.size());
		res.put("body", body);
		for (Map<String, Object> map : list) {
			Map<String, Object> o = new HashMap<String, Object>();
			for (String key : map.keySet()) {
				Object value = map.get(key);
				String type = qc.getFieldType(key);
				if (type != null && (value instanceof Date)) {
					value = formatDateType((Date) value, type);
				}
				if (value instanceof List) {
					List<String> l = (List<String>) value;
					if (l.size() > 0) {
						value = l.get(0);
					}
				}
				o.put(key, value);
			}
			body.add(o);
		}
		// System.out.println(body);
		res.put(Service.RES_CODE, 200);
		res.put(Service.RES_MESSAGE, "QuerySuccess");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private QueryContext initQueryContext(Schema sc, List queryCnd,
			String queryCndsType, String sortInfo, Context ctx) {
		User user = (User) ctx.get("user.instance");
		String entryName = sc.getId();

		List<List> cnds = new ArrayList<List>();
		List<SchemaItem> items = sc.getAllItemsList();
		QueryContext qc = new QueryContext();
		AuthorizeResult r = null;
		for (SchemaItem it : items) {
			// if (it.isVirtual()) {
			// continue;
			// }
			String fid = it.getId();
			r = user.authorize("storage", entryName + "." + fid);
			if (r.getResult() == AuthorizeResult.NO) {
				continue;
			}
			String av = r.getAuthorizeValue();
			if (av.charAt(3) == '0'
					|| it.displayMode() == -1) {
				continue;
			}
			qc.addField(fid, it.getType());
			if (it.isCodedValue()) {
				qc.addField(fid + "_text");
			}
		}

		if (queryCnd != null) {
			cnds.add(queryCnd);
		}
		// init where
		AuthorizeResult result = user.authorize("storage", entryName);
		// add role query condition
		Condition c = result.getCondition(queryCndsType);
		if (c != null) {
			List exp = (List) c.data().get("exp");
			if (exp != null) {
				cnds.add(exp);
			}
		}
		// start
		List searchCnds = null;
		int cndCount = cnds.size();
		if (cndCount == 0) {
			searchCnds = queryCnd;
		} else {
			if (cndCount == 1) {
				searchCnds = cnds.get(0);
			} else {
				searchCnds = new ArrayList();
				searchCnds.add("and");
				for (List cd : cnds) {
					searchCnds.add(cd);
				}
			}
		}
		if (searchCnds != null) {
			try {
				qc.setQueryFactor(CExpRunner.toString(searchCnds, ctx));
			} catch (ExpException e) {
				e.printStackTrace();
			}
		}
		// set sortinfo
		qc.setSortInfo(sortInfo);
		return qc;
	}

	private String formatDateType(Date date, String type) {
		SimpleDateFormat sdf = null;
		if (type.equals("date")) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		} else if (type.equals("timestamp")) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if (type.equals("time")) {
			sdf = new SimpleDateFormat("HH:mm:ss");
		} else {
			return "";
		}
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		return sdf.format(date);
	}
}

class QueryContext {
	private List<String> fields = new ArrayList<String>();
	private Map<String, String> fieldTypes = new HashMap<String, String>();
	private String queryFactor = "";
	private String sortInfo = "";

	public void addField(String f) {
		fields.add(f);
		fieldTypes.put(f, "string");
	}

	public void addField(String f, String t) {
		fieldTypes.put(f, t);
		fields.add(f);
	}

	public String getFieldName(int i) {
		return fields.get(i);
	}

	public List<String> getAllFields() {
		return fields;
	}

	public String getFieldType(String f) {
		return fieldTypes.get(f);
	}

	public void setSortInfo(String sortInfo) {
		this.sortInfo = sortInfo;
	}

	public int getFieldCount() {
		return fields.size();
	}

	public void setQueryFactor(String queryFactor) {
		this.queryFactor = queryFactor;
	}

	public String buildQueryField() {
		StringBuffer q = new StringBuffer();
		for (String f : fields) {
			q.append(",").append(f);
		}
		if (fields.size() > 0) {
			q.deleteCharAt(0);
		}
		return q.toString();
	}

	public String buildQueryString() {
		if (queryFactor.trim().length() == 0) {
			return "";
		}
		queryFactor = queryFactor.replaceAll("[a-z]\\.", "");
		return queryFactor;
	}

	public String buildSortString() {
		StringBuffer q = new StringBuffer();
		if (sortInfo != null && !sortInfo.equals("")) {
			StringTokenizer st = new StringTokenizer(sortInfo, ",");
			while (st.hasMoreTokens()) {
				String str = st.nextToken();
				int i = str.indexOf(".");
				if (i > 0) {
					str = str.substring(i + 1);
				}
				if (str.endsWith(" asc") || str.endsWith(" desc")) {
					q.append(str);
				} else {
					q.append(str).append(" asc");
				}
			}
		}
		return q.toString();
	}
}

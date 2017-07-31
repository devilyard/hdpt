package dc.service.ssdev;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.impl.SessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bsoft.mpi.service.ServiceCode;

import ctd.accredit.User;
import ctd.accredit.UsersController;
import ctd.config.schema.Schema;
import ctd.config.schema.SchemaController;
import ctd.service.core.Service;
import ctd.service.core.ServiceException;
import ctd.util.AppContextHolder;
import ctd.util.context.Context;
import dc.dao.QueryTemplate;

public class BrowsingHistService implements Service {

	private static final Logger logger = LoggerFactory
			.getLogger(MPIQueryService.class);
	private static final int DEFAULT_PAGESIZE = 25;
	private static final int DEFAULT_PAGENO = 1;
	public static String dialect = null;
	
	@Override
	public void execute(Map<String, Object> req, Map<String, Object> res,
			Context ctx) throws ServiceException {
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(Context.HTTP_REQUEST);

		List<Map<String, Object>> list;
		String uid = (String) request.getSession().getAttribute("uid");
		if (uid == null) {
			return;
		}

		int code = 200;
		String msg = "Success";
		String schemaId = (String) req.get("schema");
		Schema sc = SchemaController.instance().getSchema(schemaId);
		if (sc == null) {
			code = 402;
			msg = "NoSuchSchema";
			res.put(Service.RES_CODE, code);
			res.put(Service.RES_MESSAGE, msg);
			return;
		}
		int pageSize = DEFAULT_PAGESIZE;
		if (req.containsKey("pageSize")) {
			pageSize = (Integer) req.get("pageSize");
		}
		int pageNo = DEFAULT_PAGENO;
		if (req.containsKey("pageNo")) {
			pageNo = (Integer) req.get("pageNo");
		}
		int first = (pageNo - 1) * pageSize;

		User user = UsersController.instance().getUser(uid);
		String userID = user.getId();
		Map<String, Object> args = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		Session session = null;
		 if(dialect==null){
			SessionFactory sessionFactory = AppContextHolder.get().getBean(SessionFactory.class);
			session = sessionFactory.openSession();
			dialect = ((SessionImpl)session).getFactory().getDialect().getClass().getName();
		 }
		 if(dialect.equals("org.hibernate.dialect.DB2Dialect")){
			sb.append("select * from (");
			sb.append("select rownumber() over() as rownum,");
			sb.append("a.bhid         as BHID,");
			sb.append("       a.userid       as USERID,");
			sb.append("       a.mpiid        as MPIID,");
			sb.append("       a.systemtime   as SYSTEMTIME,");
			sb.append("       a.rolelist     as ROLELIST,");
			sb.append("       a.personname   as PERSONNAME,");
			sb.append("       a.sexcode_text as SEXCODE_TEXT,");
			sb.append("       a.birthday     as BIRTHDAY,");
			sb.append("       a.idcard       as IDCARD,");
			sb.append("       a.cardno       as CARDNO");
			sb.append("  from SYS_BROWSINGHIST a");
			sb.append(" inner join (select mpiId, userid, max(systemTime) as maxTime");
			sb.append("               from SYS_BROWSINGHIST");
			sb.append("              group by userId, mpiId) b on a.mpiId = b.mpiId");
			sb.append("                                       and a.userid = b.userid");
			sb.append("                                       and a.systemtime = b.maxtime");
			sb.append(" where a.userid = '");
			sb.append(userID);
			sb.append("' order by b.maxTime desc)");
			sb.append("where rownum > ");
			sb.append(first);
			sb.append(" and rownum <= ");
			sb.append(pageSize);
		}else {
			sb.append("select * from (");
			sb.append("select ");
			sb.append("a.bhid         as BHID,");
			sb.append("       a.userid       as USERID,");
			sb.append("       a.mpiid        as MPIID,");
			sb.append("       a.systemtime   as SYSTEMTIME,");
			sb.append("       a.rolelist     as ROLELIST,");
			sb.append("       a.personname   as PERSONNAME,");
			sb.append("       a.sexcode_text as SEXCODE_TEXT,");
			sb.append("       a.birthday     as BIRTHDAY,");
			sb.append("       a.idcard       as IDCARD,");
			sb.append("       a.cardno       as CARDNO");
			sb.append("  from SYS_BROWSINGHIST a");
			sb.append(" inner join (select mpiId, userid, max(systemTime) as maxTime");
			sb.append("               from SYS_BROWSINGHIST");
			sb.append("              group by userId, mpiId) b on a.mpiId = b.mpiId");
			sb.append("                                       and a.userid = b.userid");
			sb.append("                                       and a.systemtime = b.maxtime");
			sb.append(" where a.userid = '");
			sb.append(userID);
			sb.append("' order by b.maxTime desc)");
			sb.append("where rownum > ");
			sb.append(first);
			sb.append(" and rownum <= ");
			sb.append(pageSize);
		}
		// sb.append("select a.bhid as BHID,a.userid as USERID,a.mpiid as MPIID,a.systemtime as SYSTEMTIME,a.rolelist as ROLELIST,a.personname as PERSONNAME,a.sexcode_text as SEXCODE_TEXT,a.birthday as BIRTHDAY,a.idcard as IDCARD,a.cardno as CARDNO from SYS_BROWSINGHIST a inner join (select mpiId, userid,max(systemTime) as maxTime from SYS_BROWSINGHIST group by userId,mpiId)b on a.mpiId=b.mpiId and a.userid=b.userid and a.systemtime=b.maxtime where a.userid=");
		
		list = QueryTemplate.querySQLForListMap(sb.toString(), args);
		// 计算总条数
		StringBuilder sbs = new StringBuilder();
		sbs.append("select count(*) as totalCount from (");
		sbs.append("select a.bhid         as BHID,");
		sbs.append("       a.userid       as USERID,");
		sbs.append("       a.mpiid        as MPIID,");
		sbs.append("       a.systemtime   as SYSTEMTIME,");
		sbs.append("       a.rolelist     as ROLELIST,");
		sbs.append("       a.personname   as PERSONNAME,");
		sbs.append("       a.sexcode_text as SEXCODE_TEXT,");
		sbs.append("       a.birthday     as BIRTHDAY,");
		sbs.append("       a.idcard       as IDCARD,");
		sbs.append("       a.cardno       as CARDNO");
		sbs.append("  from SYS_BROWSINGHIST a");
		sbs.append(" inner join (select mpiId, userid, max(systemTime) as maxTime");
		sbs.append("               from SYS_BROWSINGHIST");
		sbs.append("              group by userId, mpiId) b on a.mpiId = b.mpiId");
		sbs.append("                                       and a.userid = b.userid");
		sbs.append("                                       and a.systemtime = b.maxtime");
		sbs.append(" where a.userid = '");
		sbs.append(userID);
		sbs.append("' order by b.maxTime desc)");

		List<Map<String, Object>> l = QueryTemplate.querySQLForListMap(
				sbs.toString(), args);

		if (list == null || list.isEmpty()) {
			logger.warn("No record is found.");
			res.put(Service.RES_CODE, ServiceCode.CODE_OK);
			res.put("totalCount", 0);
			return;
		}
		res.put("totalCount", l.get(0).get("TOTALCOUNT"));
		res.put("body", list);
		// System.out.println(l.get(0).get("TOTALCOUNT"));
		res.put(Service.RES_CODE, 200);
		res.put(Service.RES_MESSAGE, "QuerySuccess");
		
		if (session != null && session.isOpen()) {
			session.close();
		}
	}
}

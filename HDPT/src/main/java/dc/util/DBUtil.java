package dc.util;

import java.util.List;

import ctd.util.context.Context;
import ctd.util.exp.ExpException;
import ctd.util.exp.ExpRunner;

public class DBUtil {

	public static String conectWhere(String where1, String where2) {
		return "(" + where1 + ") and (" + where2 + ")";
	}

	public static String createStringQuery(String queryField, String value) {
		// return
		// exp2string("[\"eq\",[\"f\",\""+queryField+"\"],[\"s\",\""+value+"\"]]");
		return queryField + "='" + value + "'";
	}

	public static String exp2string(String exp) {
		try {
			return ExpRunner.toString(exp, new Context());
		} catch (ExpException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String query(String entryName, List<String> fields,
			String where) {
		StringBuilder sql = new StringBuilder("select ");
		StringBuilder fs = new StringBuilder();
		for (String field : fields) {
			fs.append(",").append(field);
		}
		sql.append(fs.length() > 0 ? fs.substring(1) : "*").append(" from ")
				.append(entryName).append(" where ").append(where);
		return sql.toString();
	}

	public static String query(String entryName, String field, String where) {
		StringBuilder sql = new StringBuilder("select ").append(field).append(
				" from ");
		sql.append(entryName).append(" where ").append(where);
		return sql.toString();
	}

	public static String query(String osql, int pageNo, int pageSize) {
		StringBuilder sql = new StringBuilder(
				"select * from (select a.*,rownum rn from (").append(osql)
				.append(") a where rownum <=").append(pageNo * pageSize)
				.append(") where rn >").append((pageNo - 1) * pageSize);
		return sql.toString();
	}

	public static String addOrder(String oSql, String orderField, boolean isDesc) {
		StringBuilder sql = new StringBuilder(oSql);
		sql.append(" order by ").append(orderField);
		if (isDesc) {
			sql.append(" desc");
		}
		return sql.toString();
	}

	public static String query(String entryName, boolean hasDoc, String where) {
		StringBuilder sql = new StringBuilder("select ");
		StringBuilder tables = new StringBuilder(entryName);
		String fields = "*";
		StringBuilder where2 = new StringBuilder(where);
		if (hasDoc) {
			tables.append(" a,").append(entryName).append("_Doc b ");
			fields = "a.*,b.DocContent";
			where2.append(" and a.DCID=b.DCID");
		}
		sql.append(fields).append(" from ").append(tables).append(" where ")
				.append(where2);
		return sql.toString();
	}

	public static String queryById(String entryName, boolean hasDoc,
			String idName) {
		StringBuilder sql = new StringBuilder("select ");
		StringBuilder tables = new StringBuilder(entryName);
		String fields = "*";
		StringBuilder where = new StringBuilder(idName).append("=?");
		if (hasDoc) {
			tables.append(" a,").append(entryName).append("_Doc b ");
			fields = "a.*,b.DocContent";
			where.append(" and a.DCID=b.DCID");
		}
		sql.append(fields).append(" from ").append(tables).append(" where ").append(where);
		return sql.toString();
	}

}

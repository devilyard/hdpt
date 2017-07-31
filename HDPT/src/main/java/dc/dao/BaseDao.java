/*
 * @(#)HibernateDao.java Created on 2012-8-25 ����4:29:14
 *
 * ��Ȩ����Ȩ���� B-Soft ��������Ȩ����
 */
package dc.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;

import dc.util.HqlComposor;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class BaseDao implements IDao {

	@Override
	public Map<String, Object> findById(String entryName, boolean hasDoc,
			String idName, Serializable id) throws HibernateException {
		String sql = HqlComposor.makeQueryById(entryName, hasDoc, idName);
		return normalizeMap(QueryTemplate.queryForMap(sql, id), hasDoc);
	}

	@Override
	public Map<String, Object> findOne(String entryName, boolean hasDoc,
			String where, Object... args) throws HibernateException {
		String sql = HqlComposor.makeQuery(entryName, hasDoc, where);
		return normalizeMap(QueryTemplate.queryForMap(sql, args), hasDoc);
	}

	@Override
	public List<Map<String, Object>> find(String entryName, boolean hasDoc,
			String where, Object... args) throws HibernateException {
		String sql = HqlComposor.makeQuery(entryName, hasDoc, where);
		return normalizeList(QueryTemplate.queryForListMap(sql, args), hasDoc);
	}

	@Override
	public List<Object> findField(String entryName, String field, String where,
			Object... args) throws HibernateException {
		String sql = HqlComposor.makeQuery(entryName, field, where);
		return QueryTemplate.queryForList(sql, args);
	}

	@Override
	public List<Map<String, Object>> findFields(String entryName,
			List<String> fields, String where, Object... args)
			throws HibernateException {
		String sql = HqlComposor.makeQuery(entryName, fields, where);
		return QueryTemplate.queryForListMap(sql, args);
	}

	@Override
	public List<Map<String, Object>> find(String entryName, boolean hasDoc,
			String where, int pageNo, int pageSize, Object... args)
			throws HibernateException {
		String sql = HqlComposor.makeQuery(entryName, hasDoc, where);
		List<Map<String, Object>> list = QueryTemplate.queryForPagedList(sql,
				pageNo, pageSize, args);
		return normalizeList(list, hasDoc);
	}

	@Override
	public List<Map<String, Object>> find(String entryName, boolean hasDoc,
			String where, String orderField, boolean isDesc, Object... args)
			throws HibernateException {
		String sql = HqlComposor.addOrder(
				HqlComposor.makeQuery(entryName, hasDoc, where), orderField,
				isDesc);
		return normalizeList(QueryTemplate.queryForListMap(sql, args), hasDoc);
	}

	@Override
	public List<Map<String, Object>> find(String entryName, boolean hasDoc,
			String where, int pageNo, int pageSize, String orderField,
			boolean isDesc, Object... args) throws HibernateException {
		String sql = HqlComposor.addOrder(
				HqlComposor.makeQuery(entryName, hasDoc, where), orderField,
				isDesc);
		return normalizeList(
				QueryTemplate.queryForPagedList(sql, pageNo, pageSize, args),
				hasDoc);
	}

	@Override
	public List<Object> findField(String entryName, String field, String where,
			String orderField, boolean isDesc, Object... args)
			throws HibernateException {
		String sql = HqlComposor.addOrder(
				HqlComposor.makeQuery(entryName, field, where), orderField,
				isDesc);
		return QueryTemplate.queryForList(sql, args);
	}

	private List<Map<String, Object>> normalizeList(
			List<Map<String, Object>> list, boolean hasDoc) {
		if (hasDoc && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				normalizeMap(list.get(i), hasDoc);
			}
		}
		return list;
	}

	private Map<String, Object> normalizeMap(Map<String, Object> map,
			boolean hasDoc) {
		byte[] content = null;
		if(map!=null){
			content = (byte[]) map.get("DOCCONTENT");
		}
		if (content != null) {
			map.put("DOCCONTENT", new String(content));
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dc.dao.IDao#update(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void update(String hql, Object... args) throws HibernateException {
		UpdateTemplate.update(hql, args);
	}

	public void update(String entryName, String where, Map<String, Object> data)
			throws HibernateException {
		String hql = HqlComposor.makeUpdate(entryName, where, data);
		UpdateTemplate.update(hql, data);
	}

	public void save(String entryName, Map<String, Object> data)
			throws HibernateException {
		UpdateTemplate.save(entryName, data);
	}

}

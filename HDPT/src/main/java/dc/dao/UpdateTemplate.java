/*
 * @(#)UpdateTemplate.java Created on 2012-9-2 下午4:08:43
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.dao;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import dc.util.HibernateSessionFactory;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class UpdateTemplate extends DBTemplate {

	/**
	 * @param sql
	 * @return
	 */
	public static void update(String sql, Map<String, Object> args)
			throws HibernateException {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			Query query = session.createQuery(sql);
			setParameters(query, args);
			query.executeUpdate();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**
	 * @param sql
	 * @param args
	 */
	public static void update(String sql, Object... args)
			throws HibernateException {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			Query query = session.createQuery(sql);
			setParameters(query, args);
			query.executeUpdate();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**
	 * @param entryName
	 * @param data
	 */
	public static void save(String entryName, Map<String, Object> data)
			throws HibernateException {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.save(entryName, data);
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
	}
}

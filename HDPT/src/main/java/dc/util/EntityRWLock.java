/*
 * @(#)RWLockManager.java Created on Nov 28, 2012 10:16:28 AM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class EntityRWLock {

	private ConcurrentHashMap<Object, LockEntry> pool = null;
	private List<LockEventListener> listeners = null;

	public EntityRWLock() {
		listeners = new ArrayList<LockEventListener>();
		pool = new ConcurrentHashMap<Object, LockEntry>();
	}

	public void readlock(Object entity) {
		LockEntry l = pool.get(entity);
		if (l == null) {
			l = new LockEntry(entity);
			LockEntry existsOne = pool.putIfAbsent(entity, l);
			if (existsOne != null) {
				l = existsOne;
			}
		}
		l.readLock();
		for (LockEventListener listener : listeners) {
			listener.onReadLock(entity);
		}
	}

	public void readUnlock(Object entity) {
		LockEntry l = pool.get(entity);
		if (l == null) {
			return;
		}
		l.readUnlock();
		for (LockEventListener listener : listeners) {
			listener.onReadUnlock(entity);
		}
	}

	public void writelock(Object entity) {
		LockEntry l = pool.get(entity);
		if (l == null) {
			l = new LockEntry(entity);
			LockEntry existsOne = pool.putIfAbsent(entity, l);
			if (existsOne != null) {
				l = existsOne;
			}
		}
		l.writeLock();
		for (LockEventListener listener : listeners) {
			listener.onWriteLock(entity);
		}
	}

	public void writeUnlock(Object entity) {
		LockEntry l = pool.get(entity);
		if (l == null) {
			return;
		}
		l.writeUnlock();
		for (LockEventListener listener : listeners) {
			listener.onWriteUnlock(entity);
		}
	}

	public void addLockEventListener(LockEventListener listener) {
		this.listeners.add(listener);
	}

	public void removeLockEventListener(LockEventListener listener) {
		this.listeners.remove(listener);
	}

	private class LockEntry {

		private Object key;
		private final ReadWriteLock lock;
		private AtomicInteger lockThreadCount;

		public LockEntry(Object key) {
			this.key = key;
			lock = new ReentrantReadWriteLock();
			lockThreadCount = new AtomicInteger(0);
		}

		public void readLock() {
			lockThreadCount.incrementAndGet();
			lock.readLock().lock();
		}

		public void readUnlock() {
			lock.readLock().unlock();
			if (lockThreadCount.decrementAndGet() == 0) {
				pool.remove(key);
			}
		}

		public void writeLock() {
			lockThreadCount.incrementAndGet();
			lock.writeLock().lock();
		}

		public void writeUnlock() {
			lock.writeLock().unlock();
			if (lockThreadCount.decrementAndGet() == 0) {
				pool.remove(key);
			}
		}
	}

	interface LockEventListener {

		public void onReadLock(Object entity);

		public void onReadUnlock(Object entity);

		public void onWriteLock(Object entity);

		public void onWriteUnlock(Object entity);
	}
}

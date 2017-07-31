/*
 * @(#)Snippet.java Created on Nov 28, 2012 5:34:21 PM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dc.util.EntityRWLock;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class EntityRWLockTest {
	
	public static void main(String[] args) {
		final Map<String, String> cache = new HashMap<String, String>();
		final EntityRWLock lock = new EntityRWLock();

		int threads = 100;
		final String[] keys = new String[] { "key1", "key2" };

		final CountDownLatch connectLatch = new CountDownLatch(threads);
		ExecutorService exec = Executors.newFixedThreadPool(threads);

		for (int i = 0; i < 200; i++) {
			final int num = i;
			exec.execute(new Runnable() {
				public void run() {
					String key = keys[num % 2];
					lock.readlock(key);
					String value = cache.get(key);
					lock.readUnlock(key);
					if (value == null) {
						lock.writelock(key);
						value = cache.get(key);
						if (value != null) {
							System.out.println("..>Thread [" + num
									+ "] got value: " + value);
						} else {
							value = "value" + (num % 2);
							cache.put(key, value);
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println("===>Thread [" + num
									+ "] set value: " + value);
						}
						lock.writeUnlock(key);
					}
					System.out.println("-->Thread [" + num + "] got value: "
							+ value);
					connectLatch.countDown();
				}
			});
		}
		try {
			connectLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		exec.shutdown();
	}
}

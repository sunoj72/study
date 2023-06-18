package com.lgcns.test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* ----------------------------------------------------------------------------
 * 
 * Worker.java - removeExpiredStoreItems() ����, �� �� ���� ����
 * 
 * ----------------------------------------------------------------------------
 */
public class Worker extends AbstractWorker {
//	private List<String> store = Collections.synchronizedList(new ArrayList<>());

	public Worker(int queueNo) {
		super(queueNo);
	}

	public void removeExpiredStoreItems(long timestamp, List<String> store) {
		Iterator<String> itr = store.iterator();
		
		while(itr.hasNext()) {
			String line = itr.next();
			String[] params = line.split("#");
			
			if (timestamp - Long.parseLong(params[0]) > 3000) {
				itr.remove();
			}
		}
		
//		System.out.println("[STORE] " + Arrays.toString(store.toArray()));
	}
}

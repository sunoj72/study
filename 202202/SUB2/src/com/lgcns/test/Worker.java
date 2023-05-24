package com.lgcns.test;

import java.util.List;

/* ----------------------------------------------------------------------------
 * 
 * Worker.java - removeExpiredStoreItems() ����, �� �� ���� ����
 * 
 * ----------------------------------------------------------------------------
 */
public class Worker extends AbstractWorker {
	
	/*
	 * �� Worker ����
	 * - <Queue ��ȣ>�� �Ķ���ͷ� �Ͽ� Worker �ν��Ͻ� ����
	 */
	public Worker(int queueNo) {
		super(queueNo);
	}

	/*
	 * �� ����� Store Item ����
	 * - �Էµ� Timestamp�� Store Item�� Timestamp���� ���̰� ����ð�(3000)�� �ʰ��ϸ� Store���� ����
	 */
	public void removeExpiredStoreItems(long timestamp, List<String> store) {
		// �Ʒ� ������ ����� ����� Store Item ���� ����� �����ϼ���.
		throw new UnsupportedOperationException("removeExpiredStoreItems()�� �����ϼ���.");
	}
}

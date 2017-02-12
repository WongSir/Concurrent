package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  ����JDK1.5�������ṩ��Lock, Condition�������ط��������߳�.
 * @author Wongsir
 *���������̣߳�������ӡ�������
 */
public class NumberPrint implements Runnable{
	private int state = 1;
	private int n = 1;
	//ʹ��lock����
	private ReentrantLock lock = new ReentrantLock();
	//���lock����3����֧����
	private Condition c1 = lock.newCondition();
	private Condition c2 = lock.newCondition();
	private Condition c3 = lock.newCondition();
	
	@Override
	public void run() {
		new Thread(new Runnable(){
			public void run() {
				for(int i=0;i<5;i++){
					try {
						//�߳�1���lock���������߳̽��޷�������Ҫlock���Ĵ����
						//��lock.lock()��lock.unlock()֮��Ĵ����൱��ʹ����synchronized(lock)
						lock.lock();
						while(state!=1){
							try {
								//�߳�1��������lock�����Ƿ���state��Ϊ1��˵����ʱ��δ�ֵ��߳�1��ӡ
								//����߳�1����c1��wait
								//��ⷨһ��ͬ���ǣ������̲߳�����ͬһ��������wait��Ҳ����ͬһ��������
								c1.await();
								
							} catch (InterruptedException e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
						
						//����߳�1��������lock��Ҳͨ����state�ж�����ִ�д�ӡ����
						for(int j= 0;j<5;j++){
							System.out.println(Thread.currentThread().getName() + ": "+ n++);
						}
						System.out.println("�߳�1");
						//��ӡ��ɺ�state��ֵΪ2����ʾ��һ�εĴ�ӡ�������߳�2ִ��
						state = 2;
						//������c2��֧��wait���߳�2
						c2.signal();
					} finally{
						//��ӡ����ִ����ɺ���Ҫȷ�������ͷţ���˽��ͷ����Ĵ������finally��
						lock.unlock();
					}
				}
			}
			
		},"�߳�1").start();
		
		
		new Thread(new Runnable(){
			public void run() {
				for(int i=0;i<5;i++){
					try {
						lock.lock();
						while(state != 2){
							try {
								c2.await();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						for(int j=0;j<5;j++){
							System.out.println(Thread.currentThread().getName()+": " + n++);
						}
						System.out.println("�߳�2");
						state = 3;
						c3.signal();
					} finally {
						// TODO: handle finally clause
						lock.unlock();
					}
				}
			}
			
		},"�߳�2").start();
		
		 new Thread(new Runnable() {  
	            public void run() {  
	                for (int i = 0; i < 5; i++) {  
	                    try {  
	  
	                        lock.lock();  
	                        while (state != 3){
	                        	try {  
	                                c3.await();  
	                            } catch (InterruptedException e) {  
	                                e.printStackTrace();  
	                            }  
	                        }
	                        for (int j = 0; j < 5; j++) {  
	                            System.out.println(Thread.currentThread().getName()  
	                                    + ": " + n++);  
	                        }  
	                        System.out.println("�߳�3");  
	                        state = 1;  
	                        c1.signal();  
	                    } finally {  
	                        lock.unlock();  
	                    }  
	                }  
	            }  
	        }, "�߳�3").start();  
	}

	public static void main(String[] args){
		new NumberPrint().run();
		System.out.println("sdf");
	}
	
}

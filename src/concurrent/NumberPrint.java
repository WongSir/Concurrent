package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  采用JDK1.5并发包提供的Lock, Condition等类的相关方法控制线程.
 * @author Wongsir
 *启动三个线程，轮流打印输出数字
 */
public class NumberPrint implements Runnable{
	private int state = 1;
	private int n = 1;
	//使用lock做锁
	private ReentrantLock lock = new ReentrantLock();
	//获得lock锁的3个分支条件
	private Condition c1 = lock.newCondition();
	private Condition c2 = lock.newCondition();
	private Condition c3 = lock.newCondition();
	
	@Override
	public void run() {
		new Thread(new Runnable(){
			public void run() {
				for(int i=0;i<5;i++){
					try {
						//线程1获得lock锁后，其他线程将无法进入需要lock锁的代码块
						//在lock.lock()和lock.unlock()之间的代码相当于使用了synchronized(lock)
						lock.lock();
						while(state!=1){
							try {
								//线程1竞争到了lock，但是发现state不为1，说明此时还未轮到线程1打印
								//因此线程1将在c1上wait
								//与解法一不同的是，三个线程并非在同一个对象上wait，也不由同一个对象唤醒
								c1.await();
								
							} catch (InterruptedException e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
						
						//如果线程1竞争到了lock，也通过了state判定，将执行打印任务。
						for(int j= 0;j<5;j++){
							System.out.println(Thread.currentThread().getName() + ": "+ n++);
						}
						System.out.println("线程1");
						//打印完成后将state赋值为2，表示下一次的打印任务将由线程2执行
						state = 2;
						//唤醒在c2分支上wait的线程2
						c2.signal();
					} finally{
						//打印任务执行完成后需要确保锁被释放，因此将释放锁的代码放在finally中
						lock.unlock();
					}
				}
			}
			
		},"线程1").start();
		
		
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
						System.out.println("线程2");
						state = 3;
						c3.signal();
					} finally {
						// TODO: handle finally clause
						lock.unlock();
					}
				}
			}
			
		},"线程2").start();
		
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
	                        System.out.println("线程3");  
	                        state = 1;  
	                        c1.signal();  
	                    } finally {  
	                        lock.unlock();  
	                    }  
	                }  
	            }  
	        }, "线程3").start();  
	}

	public static void main(String[] args){
		new NumberPrint().run();
		System.out.println("sdf");
	}
	
}

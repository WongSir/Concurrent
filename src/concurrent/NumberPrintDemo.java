package concurrent;

/**
 * ����ԭʼ��synchronized, wait(), notify(), notifyAll()�ȷ�ʽ�����߳�.
 * @author Wongsir
 *
 */
public class NumberPrintDemo {
	//nΪ������ӡ������
	private static int n= 1;
	//state=1��ʾ�����߳�1��ӡ���֣�state=2��ʾ�����߳�2��ӡ���֣�state=3�����߳�3��ӡ����
	private static int state = 1;
	
	public static void main(String[] args){
		final NumberPrintDemo pn = new NumberPrintDemo();
		new Thread(new Runnable(){
			public void run(){
				//3���̴߳�ӡ75�����֣������߳�ÿ�δ�ӡ5���������֣����ÿ���߳�ֻ��ִ��5�δ�ӡ����3*5*5=75
				for(int i=0;i<5;i++){
					//3���̶߳�ʹ��pn�����������Ա�֤ÿ�������ڼ�ֻ��һ���߳��ڴ�ӡ
					synchronized(pn){
						//���state!=1,˵����ʱ��δ�ֵ��߳�1��ӡ���߳�1������pn��wait()������֪���´α�����
						while(state!=1){
							try {
								pn.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						//��state=1ʱ���ֵ��߳�1��ӡ5������
						for(int j=0;j<5;j++){
							//��ӡһ�κ�n����
							System.out.println(Thread.currentThread().getName() + ": " + n++);
						}
						System.out.println();
						//�߳�1��ӡ��ɺ󣬽�state��ֵΪ2����ʾ���������ֵ��߳�2��ӡ
						state = 2;
						//nofifyAll()����������pn��wait���߳�2���߳�3��ͬʱ�߳�1���˳�ͬ������飬�ͷ�pn��
						//���3���߳̽��ٴξ���pn��
						//�����߳�1�����߳�3��������Դ������state��Ϊ1����3���߳�1���߳�3���ܿ��ٴ�wait���ͷų��յ��ֵ�pn��
						//ֻ���߳�2����ͨ��state�ж��������߳�2һ����ִ���´δ�ӡ������߳�
						//�����߳�2��˵��ͨ��������ĵ�·Ҳ�������۵ģ���ǰ;һ���ǹ�����
						pn.notifyAll();
					}
				}
			}
		},"�߳�1").start();
		
		new Thread(new Runnable() {  
            public void run() {  
                for (int i = 0; i < 5; i++) {  
                    synchronized (pn) {  
                        while (state != 2)  
                            try {  
                                pn.wait();  
                            } catch (InterruptedException e) {  
                                e.printStackTrace();  
                            }  
                        for (int j = 0; j < 5; j++) {  
                            System.out.println(Thread.currentThread().getName()  
                                    + ": " + n++);  
                        }  
                        System.out.println();  
                        state = 3;  
                        pn.notifyAll();  
                    }  
                }  
            }  
        }, "�߳�2").start();  
  
        new Thread(new Runnable() {  
            public void run() {  
                for (int i = 0; i < 5; i++) {  
                    synchronized (pn) {  
                        while (state != 3)  
                            try {  
                                pn.wait();  
                            } catch (InterruptedException e) {  
                                e.printStackTrace();  
                            }  
                        for (int j = 0; j < 5; j++) {  
                            System.out.println(Thread.currentThread().getName()  
                                    + ": " + n++);  
                        }  
                        System.out.println();  
                        state = 1;  
                        pn.notifyAll();  
                    }  
                }  
            }  
        }, "�߳�3").start();  
    }  
	
}

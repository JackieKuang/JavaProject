package task_test;

import java.util.Timer;
import java.util.TimerTask;

public class TaskTest extends TimerTask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.schedule(new TaskTest(), 1000, 5000);

	}
	
	public void run(){
		System.out.println("任務時間：" + new java.util.Date());
	}
}

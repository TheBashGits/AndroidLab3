package com.example.Servlet_Connection;

//------------------------------------------------------------------------
//Intensive CPU service running its heavy duty task in an 
//AsyncTask object. Uses 'Message handling' for synchronization.
//computing Fibonacci numbers between 20 & 50 [ O(2^n) ]


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyService5Async extends Service {
	boolean isRunning = true;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.e("MyService5Async-Handler", "Handler got from MyService5Async: " + (String) msg.obj);
		}
	};
	ScheduledExecutorService scheduleTaskExecutor;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		scheduleTaskExecutor= Executors.newScheduledThreadPool(5);

		scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if(isRunning==true) {
					new GetNumbers().execute(10);
				}
			}

		},0,1, TimeUnit.SECONDS);

	}
	@Override
	public void onDestroy(){

		isRunning=false;
	}
	//public class GetNumbers extends AsyncTask <Integer, Integer, Integer> {
	public class GetNumbers extends AsyncTask <Integer, Integer, int[]> {

		@Override
		protected int[] doInBackground(Integer... params) {
			int[] array = new int[5];		//create array with 5 ints
		//protected Integer doInBackground(Integer... params) {

				Random rand = new Random();
				int one = rand.nextInt(1000);	//create a random number
				int two = rand.nextInt(1000);	//create a random number
				int three = rand.nextInt(1000);	//create a random number
				int four = rand.nextInt(1000);
				int five = rand.nextInt(1000);

				array[0] = one;					//put random numbers in array
				array[1] = two;					//put random numbers in array
				array[2] = three;				//put random numbers in array
				array[3] = four;
				array[4] = five;
				//Integer numb = rand.nextInt(1000);
//				try{
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}

					//publishProgress(array[0]);
					//publishProgress(array[1]);
					//publishProgress(array[2]);
					//publishProgress(array[3]);
					//publishProgress(array[4]);


			return array;					//return this to onPostExecute

		}
//		@Override
//		protected void onProgressUpdate(Integer... values) {
//			super.onProgressUpdate(values);
//
//			Intent intentFilter5 = new Intent("NameOfIntent");
//			String data = ""+ values[0];
//			intentFilter5.putExtra("KeyForValue", data);
//			sendBroadcast(intentFilter5);
//			Message msg = handler.obtainMessage(5, data);
//			handler.sendMessage(msg);
//		}

		@Override
		protected void onPostExecute(int[] myInts) {		//array gets sent down here
			super.onPostExecute(myInts);

			Intent intentFilter5 = new Intent("NameOfIntent");	//create intent
			intentFilter5.putExtra("array",myInts);				//put numbers into extras
			sendBroadcast(intentFilter5);						//broadcast back results
		}


	}// ComputeFibonacciRecursivelyTask


}//MyService5


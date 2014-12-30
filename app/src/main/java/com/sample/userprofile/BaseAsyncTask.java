package com.sample.userprofile;

import android.content.Context;
import android.os.AsyncTask;

public class BaseAsyncTask extends AsyncTask<Integer, Object, Object> {

	protected TaskListener mListener = null;
	private boolean mIsRunning = false;

	private Context context;
	private WaitDialog dialog;

	public BaseAsyncTask(Context context) {
		super();
		this.context = context;
	}

	public static void run(Context context, TaskListener listener) {
        BaseAsyncTask bTask = new BaseAsyncTask(context);
		bTask.setListener(listener);
		bTask.execute();
	}

	public void setListener(TaskListener listener) {
		mListener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		showWait();
	}

	@Override
	protected Object doInBackground(Integer... params) {
		mIsRunning = true;
		Object result = doRunning();
		mIsRunning = false;
		return result;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();

		hideWait();
		mIsRunning = false;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);

		hideWait();
		if (mListener != null) {
			mListener.onTaskResult(result);
			mListener = null;
		}

		mIsRunning = false;
	}

	public void release() {
		if (mIsRunning) {
			this.cancel(false);
			mIsRunning = false;
		}
	}

	protected Object doRunning() {
		if (mListener != null) {
			return mListener.onTaskRunning();
		}
		return null;
	}

	private void showWait() {
		if (dialog == null) {
			dialog = new WaitDialog(context);
			dialog.show();
		}
	}

	private void hideWait() {
		if (dialog != null) {
			dialog.cancel();
			dialog = null;
		}
	}

	public static interface TaskListener {
		public Object onTaskRunning();
		public void onTaskResult(Object result);
	}

	public static class TaskAdapter implements TaskListener {
		public Object onTaskRunning() {return null;};
		public void onTaskResult(Object result) {};
	}

}

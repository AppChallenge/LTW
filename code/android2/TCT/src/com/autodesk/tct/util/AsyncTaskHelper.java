package com.autodesk.tct.util;

import java.util.concurrent.Executor;

import android.os.AsyncTask;
import android.os.Build;

/***
 * On Android 4.0 and above, all the AsyncTasks are executed on a serial
 * executor. This means our thumbnail downloading task may block other tasks
 * like thumb loading, effect apply,etc. So we use this helper to use thread
 * pool executor to execute the asynctasks.
 */
public class AsyncTaskHelper {
	public static <P> void execute(AsyncTask<P, ?, ?> asyncTask, P... params) {
		if (DeviceUtility.getOsVersion() >= Build.VERSION_CODES.HONEYCOMB) {
            // Using default THREAD_POOL_EXECUTOR may block AsyncTasks, too. The number of threads depends on the number
            // of available processor cores at Runtime.
            // As the document says: If corePoolSize or more threads are running, the Executor always prefers queuing a
            // request rather than adding a new thread.
            // If every thread is occupied by a time-consuming AsyncTask, then AsyncTasks added later have to wait.
            // So some tasks which can not be blocked, such as OpenImageTask, should use their own Executor.
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			asyncTask.execute(params);
		}
	}

    public static <P> void execute(Executor executor, AsyncTask<P, ?, ?> asyncTask, P... params) {
        if (DeviceUtility.getOsVersion() >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(executor, params);
        } else {
            asyncTask.execute(params);
        }
    }
}


package com.example.koltinflowex.presentation.common.dexter;

import android.os.Looper;

/**
 * Factory to create the different thread implementations
 */
final class ThreadFactory {

  /**
   * Create a thread to execute on the main thread
   */
  public static Thread makeMainThread() {
    return new MainThread();
  }

  /**
   * Create a thread to execute on the same thread that this method is executed on
   */
  public static Thread makeSameThread() {
    if (runningMainThread()) {
      return new MainThread();
    } else {
      return new WorkerThread();
    }
  }

  private static boolean runningMainThread() {
    return Looper.getMainLooper().getThread() == java.lang.Thread.currentThread();
  }
}

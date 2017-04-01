package com.amaze.filemanager.exceptions;

import android.content.Intent;

/**
 * Created by vishal on 24/12/16.
 * Exception thrown when root is
 */

public class RootNotPermittedException extends Exception {
  /*  public void onCreate ()
    {
        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                handleUncaughtException (thread, e);
            }
        });
    }

    public void handleUncaughtException (Thread thread, Throwable e)
    {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically

        Intent intent = new Intent ();
        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application


        System.exit(1); // kill off the crashed app
    }*/
}

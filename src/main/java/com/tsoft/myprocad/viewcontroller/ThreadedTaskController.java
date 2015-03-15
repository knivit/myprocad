package com.tsoft.myprocad.viewcontroller;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.ThreadedTaskPanel;

import javax.swing.*;

public class ThreadedTaskController {
    private static ExecutorService tasksExecutor;
    private Callable<Void> threadedTask;
    private final String taskMessage;
    private ExceptionHandler exceptionHandler;
    private ThreadedTaskPanel view;
    private Future<?> task;

    /**
     * Creates a controller that will execute in a separated thread the given task.
     * This task shouldn't modify any model objects and should be able to handle
     * interruptions with <code>Thread</code> methods that the user may provoke
     * when he wants to cancel a threaded task.
     */
    public ThreadedTaskController(String taskMessage) {
        this.taskMessage = taskMessage;

        exceptionHandler = new ExceptionHandler() {
            public void handleException(Exception ex) {
                String cause = threadedTask.toString();
                String message = ex.getMessage();
                if (message != null) {
                    cause += ": " + message;
                }
                if (ex.getCause() != null) {
                    cause += "<br>" + ex.getCause();
                }

                JOptionPane.showMessageDialog(null, cause, L10.get(L10.ERROR_DIALOG), JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        };
    }

    public ThreadedTaskPanel getView() {
        if (view == null) {
            view = new ThreadedTaskPanel(taskMessage, this);
        }
        return view;
    }

    /**
     * Executes in a separated thread the task given in constructor. This task shouldn't
     * modify any model objects shared with other threads.
     */
    public void execute(Callable<Void> threadedTask) {
        this.threadedTask = threadedTask;
        if (tasksExecutor == null) {
            tasksExecutor = Executors.newSingleThreadExecutor();
        }

        task = tasksExecutor.submit(new FutureTask<Void>(this.threadedTask) {
            @Override
            public void run() {
                // Update running status in view
                getView().invokeLater(new Runnable() {
                    public void run() {
                        getView().setTaskRunning(true);
                    }
                });
                super.run();
            }

            @Override
            protected void done() {
                // Update running status in view
                getView().invokeLater(new Runnable() {
                    public void run() {
                        task = null;
                        getView().setTaskRunning(false);
                    }
                });

                try {
                    get();
                } catch (ExecutionException ex) {
                    ex.printStackTrace();

                    // Handle exceptions with handler
                    final Throwable throwable = ex.getCause();
                    if (throwable instanceof Exception) {
                        getView().invokeLater(new Runnable() {
                            public void run() {
                                exceptionHandler.handleException((Exception)throwable);
                            }
                        });
                    }
                } catch (final InterruptedException ex) {
                    // Handle exception with handler
                    getView().invokeLater(new Runnable() {
                        public void run() {
                            exceptionHandler.handleException(ex);
                        }
                    });
                }
            }
        });
    }

    /**
     * Cancels the threaded task if it's running.
     */
    public void cancelTask() {
        if (task != null) {
            task.cancel(true);
        }
    }

    /**
     * Returns <code>true</code> if the threaded task is running.
     */
    public boolean isTaskRunning() {
        return task != null && !task.isDone();
    }

    /**
     * Handles exception that may happen during the execution of a threaded task.
     */
    public static interface ExceptionHandler {
        public void handleException(Exception ex);
    }
}

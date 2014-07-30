package org.codelearn.twitter;

public interface TaskFinishedListener<T> {
	void onTaskFinished(T result);
}

package com.ash.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

import javax.swing.JOptionPane;

public class TaskPrio {
	
	ArrayList<Task> taskList;

	public TaskPrio(boolean showMsgOnError){
		taskList = new ArrayList<Task>();
	}
	
	public boolean perform(Object o){
		Collections.sort(taskList);
		try {
			for(Task t : taskList){
				t.getConsumer().accept(o);
			}
		} catch(Exception e){
			infoBox(e);
			return false;
		}
		return true;
	}
	
	public void addTask(Task t){
		taskList.add(t);
	}
	
	public void addTask(int priority, Consumer<Object> c){
		taskList.add(new Task(priority, c));
	}
	
	public void removeTask(Task t){
		taskList.remove(t);
	}
	
	public ArrayList<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(ArrayList<Task> taskList) {
		this.taskList = taskList;
	}

	private static void infoBox(Exception e){
		infoBox(e.getMessage() + "\n" + e.getStackTrace() + "\nGrund: " + e.getCause(),""+e.getCause());
	}
	
	private static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "Error: " + titleBar, JOptionPane.ERROR_MESSAGE);
    }
	
	public class Task implements Comparable<Task> {
		
		private int priority;
		private Consumer<Object> consumer;

		public Task(int priority, Consumer<Object> consumer) {
			this.priority = priority;
			this.consumer = consumer;
		}

		@Override
		public int compareTo(Task t) {
			return this.priority - t.priority;
		}

		public int getPriority() {
			return priority;
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}

		public Consumer<Object> getConsumer() {
			return consumer;
		}

		public void setConsumer(Consumer<Object> consumer) {
			this.consumer = consumer;
		}
		
	}
}

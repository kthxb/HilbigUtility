package com.ash.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.ash.input.InputInterface;

/**
 * Utility for global process management [Windows]
 * @author Aaron
 *
 */
public class ProcessManager {

	public static void main(String[] args) throws Exception {
		
		System.out.println("isRunning(\"firefox\"): " + isRunning("firefox"));
		
		System.out.println("" + any(p -> p.nameEquals("eclipse")));
		
		any(p -> p.PID==2000);
		
		InputInterface i = new InputInterface();
		i.addStringInput("Processname","@");
		i.addMultiInput("Operation", "isRunning", "PID", "List", "Info", "Kill");
		i.open();
		if(i.result.get("Operation").equals("isRunning")) InputInterface.message("" + isRunning((String)i.result.get("Processname")), "Is \"" + i.result.get("Processname") + "\" running? ", InputInterface.MSGTYPE.INFO);
		else if(i.result.get("Operation").equals("PID")) InputInterface.message("PID = " + getProcessByName((String)i.result.get("Processname")).PID, "PID of " + (String)i.result.get("Processname"), InputInterface.MSGTYPE.INFO);
		else if(i.equals("Operation", "List")){
			String s = "";
			for(EZProcess p : getAllImportantProcesses())
				s += p.name + "\n";
			InputInterface.message(s, "Process List", InputInterface.MSGTYPE.INFO);
		} else if(i.equals("Operation", "Info")){
			EZProcess p = getProcessByName((String) i.result.get("Processname"));
			String s = "Name: " +  p.name + " (PID " + p.PID + ")\n" + p.sessionname + " " + p.sessionID + "\nMemory usage: " + p.memoryUsage/1000 + "MB";
			InputInterface.message(s, "Info", InputInterface.MSGTYPE.INFO);
		} else if(i.equals("Operation","Kill")){
			killProcess(getProcessByName((String)i.result.get("Processname")));
		}
		
		
		/*System.out.println(time((o)->{
			try {
				System.out.println(systemInfo());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, null) + "ms");*/
		
		//executeBatchCommands("@echo off","color 0a","echo hallo","dir","pause");
		
		//System.out.println(getProcessWithTitle("TeamSpeak 3"));
		
		//getProcessWithStatus(Status.SUSPENDED).forEach((p) -> {
		//	System.out.println(p.toString());
		//});;
		
		/*System.out.println(time((o)->{
			try {
				System.out.println(getProcessByName("eclipse").toString());
				//getProcessList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, null) + "ms");*/
		
		//String s = Processes.ALL_PROCESSES.listProcesses(true);
		
		/*for(EZProcess p : getProcessList()){
			if(p.sessionname.equals("Console"))
				System.out.println(p.toString());
		}*/
		
		//killProcess(getProcessByName("steam"));
		
		/*getAllProcessesWith((p) -> {
			return p.sessionname.equals("Console") && filter(p, UNIMPORTANT_LIST);
		}).forEach((p) -> {
			System.out.println(p.toString());
		});*/
		
		/*ArrayList<EZProcess> ezp = new ArrayList<EZProcess>();
		
		ezp = getAllImportantProcesses();
		
		Collections.sort(ezp);
		
		ezp.forEach((p)->{
			System.out.println(p.toString());
		});*/
		
	}
	
	public static long time(Consumer<Object> counsumer, Object o){
		long d = System.currentTimeMillis();
		counsumer.accept(o);
		return System.currentTimeMillis() - d;
	}
	
	public static final String[] UNIMPORTANT_LIST = new String[]{"dllhost","cmd","conhost","csrss","winlogon","dwm","nvxdsync","nvvsvc","taskhostex","taskhost","nvtray","nvbackend","jusched","tasklist","GWX","explorer","jucheck"};
	
	public static void killProcess(EZProcess p) throws IOException {
		Runtime.getRuntime().exec("taskkill /F /PID " + p.PID);
	}
	
	public static boolean isRunning(String name){
		try {
			return getProcessByName(name) != null ? true : false;
		} catch (Exception e) {
			return false;
		}
	}
	
	private static EZProcess lastProcess = null;
	public static EZProcess getProcessByName(String name) throws Exception {
		if(lastProcess!=null && (lastProcess.name.equalsIgnoreCase(name) || lastProcess.name.equalsIgnoreCase(name + ".exe"))){
			return lastProcess;
		}
		for(EZProcess p : getProcessList()){
			if(p.nameEquals(name)){
				lastProcess = p;
				return p;
			}
		}
		return null;
	}

	public static boolean filter(EZProcess p, String... list){
		for(String s : list)
			if(s.equalsIgnoreCase(p.name) || (s + ".exe").equalsIgnoreCase(p.name))
				return false;
		return true;
	}
	
	public static EZProcess getProcessWithTitle(String title) throws IOException, InterruptedException{
		String s = executeBatchCommands("tasklist /FI \"WINDOWTITLE eq " + title + "\"");
		try {
		s = s.split("\n")[3];
		} catch(Exception e){
			return null;
		}
		String name = s.substring(0, 25).trim();
		String PID = s.substring(25, 25 + 9).trim();
		String SName = s.substring(25 + 9, 25 + 9 + 18).trim();
		String SID = s.substring(25 + 9 + 18, 25 + 9 + 18 + 13).trim();
		String Mem = s.substring(25 + 9 + 18 + 13, s.length()-1).trim();
		
		return new EZProcess(name, SName, Integer.parseInt(PID), Integer.parseInt(SID), Integer.parseInt(Mem.replace(".", "").replace(".", "")));
	}
	
	public static enum Status {
		
		RUNNING("RUNNING"), SUSPENDED("SUSPENDED"), NOT_RESPONDING("NOT RESPONDING"), UNKNOWN("UNKNOWN");
		
		String s;
		
		Status(String s){
			this.s = s;
		}
	}
	
	
	public static ArrayList<EZProcess> getProcessWithStatus(Status status) throws IOException, InterruptedException{
		String str = executeBatchCommands("tasklist /FI \"STATUS eq " + status.s + "\" /NH");
		ArrayList<EZProcess> list = new ArrayList<EZProcess>();
		for(String s : str.split("\n")){
			if(s==null || s.equals("") || s.startsWith("===========") || (s.contains("PID")))
				continue;
			int i = 0;
			
			String name = s.substring(0, 25).trim();
			String PID = s.substring(25, 25 + 9).trim();
			String SName = s.substring(25 + 9, 25 + 9 + 18).trim();
			String SID = s.substring(25 + 9 + 18, 25 + 9 + 18 + 13).trim();
			String Mem = s.substring(25 + 9 + 18 + 13, s.length()-1).trim();
			
			EZProcess p = new EZProcess(name, SName, Integer.parseInt(PID), Integer.parseInt(SID), Integer.parseInt(Mem.replace(".", "").replace(".", "")));
			
			list.add(p);
		}
		return list;
	}
	
	public static ArrayList<EZProcess> getAllImportantProcesses() throws Exception {
		return getAllProcessesWith((p) -> {
			return p.sessionname.equals("Console") && filter(p, UNIMPORTANT_LIST);
		});
	}
	
	public static boolean any(Predicate<EZProcess> condition) throws Exception{
		return getAllProcessesWith(condition).size() > 0 ? true : false;
	}
	
	public static ArrayList<EZProcess> getAllProcessesWith(Predicate<EZProcess> condition) throws Exception {
		ArrayList<EZProcess> list = new ArrayList<EZProcess>();
		for(EZProcess p : getProcessList()){
			if(condition.test(p)){
				list.add(p);
			}
		}
		return list;
	}
	
	public static ArrayList<EZProcess> getProcessList() throws Exception{
		ArrayList<EZProcess> list = new ArrayList<EZProcess>();
		String str = Processes.ALL_PROCESSES.listProcesses(false);
		
		for(String s : str.split("\n")){
			if(s==null || s.equals("") || s.startsWith("===========") || (s.contains("PID")))
				continue;
			int i = 0;
			
			String name = s.substring(0, 25).trim();
			String PID = s.substring(25, 25 + 9).trim();
			String SName = s.substring(25 + 9, 25 + 9 + 18).trim();
			String SID = s.substring(25 + 9 + 18, 25 + 9 + 18 + 13).trim();
			String Mem = s.substring(25 + 9 + 18 + 13, s.length()-1).trim();
			
			EZProcess p = new EZProcess(name, SName, Integer.parseInt(PID), Integer.parseInt(SID), Integer.parseInt(Mem.replace(".", "").replace(".", "")));
			
			list.add(p);
		}
		
		return list;
	}
	
	public static class EZProcess implements Comparable<EZProcess> {
		public String name, sessionname;
		public int PID, sessionID, memoryUsage;
		
		public boolean nameEquals(String name){
			return (name.equalsIgnoreCase(this.name) || (name + ".exe").equalsIgnoreCase(this.name) || this.name.startsWith(name));
		}
		
		public EZProcess(String name, String sessionname, int pID, int sessionID, int memoryUsage) {
			this.name = name;
			this.sessionname = sessionname;
			PID = pID;
			this.sessionID = sessionID;
			this.memoryUsage = memoryUsage;
		}

		@Override
		public String toString() {
			return "EZProcess [name=" + name + ", sessionname=" + sessionname + ", PID=" + PID + ", sessionID="
					+ sessionID + ", memoryUsage=" + memoryUsage + "]";
		}

		@Override
		public int compareTo(EZProcess o) {
			return o.memoryUsage - this.memoryUsage;
		}
		
	}
	
	public static String systemInfo() throws IOException, InterruptedException{
		String s = executeBatchCommands("systeminfo");
		s = s.split("Hotfix")[0];
		return s;
	}

	public static enum Processes implements IProcessListingStrategy {
		ALL_PROCESSES;

		private IProcessListingStrategy processListing = selectProcessListingStrategy();

		@Override
		public String listProcesses(boolean debug) throws Exception {
			return processListing.listProcesses(debug);
		}

		private IProcessListingStrategy selectProcessListingStrategy() {
			return isWindows() ? new WinProcessListingStrategy() : new LinuxProcessListingStrategy();
		}

		private static boolean isWindows() {
			return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
		}
	}

	static interface IProcessListingStrategy {
		String listProcesses(boolean b) throws Exception;
	}

	static abstract class AbstractNativeProcessListingStrategy implements IProcessListingStrategy {
		@Override
		public String listProcesses(boolean b) throws Exception {
			Process process = makeProcessListingProcessBuilder().start();
			Scanner scanner = new Scanner(process.getInputStream());
			String ret = "";
			while (scanner.hasNextLine()) {
				String s = scanner.nextLine();
				if(b) System.out.println(s);
				ret += s + "\n";
			}
			scanner.close();
			process.waitFor();
			return ret;
		}

		protected abstract ProcessBuilder makeProcessListingProcessBuilder();
	}
	
	public static String executeBatchCommands(String... com) throws IOException, InterruptedException{
		String c = "";
		for(String s : com)
			c += s + "&";
		c = c.substring(0,c.length()-1);
//		System.out.println(c);
		Process p = new ProcessBuilder("cmd", "/c", c).start();
		Scanner scanner = new Scanner(p.getInputStream());
		String ret = "";
		while (scanner.hasNextLine()) {
			String s = scanner.nextLine();
//			System.out.println(s);
			ret += s + "\n";
		}
		scanner.close();
		p.waitFor();
		return ret;
	}

	static class WinProcessListingStrategy extends AbstractNativeProcessListingStrategy {
		@Override
		protected ProcessBuilder makeProcessListingProcessBuilder() {
			return new ProcessBuilder("cmd", "/c", "tasklist");
		}
	}

	static class LinuxProcessListingStrategy extends AbstractNativeProcessListingStrategy {
		@Override
		protected ProcessBuilder makeProcessListingProcessBuilder() {
			return new ProcessBuilder("ps", "-e");
		}
	}
}

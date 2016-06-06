package com.ash.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;

import com.ash.util.math.GaussianElimination;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

/**
 * Provides multiple functionality to debug objects and classes.
 * 
 * @author Aaron Hilbig
 *
 */
public class DebugHelper {
	
	public static String test = "Hi";
	public String msgPrefix = "[Debug]";
	public int testValue = 10;
	
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, FailingHttpStatusCodeException, MalformedURLException, IOException {
		System.out.println(new DebugHelper().dumpInfo(DebugHelper.class, false, false));
		
		System.out.println(insertAllVariables("X/Y %x%/%y%", new GaussianElimination.Point(2,2)));
		
		System.out.println(insertAllVariables("X/Y %%%", 15));
		
		System.out.println(multiInsert("X/Y %%% %x%", null, 15, new GaussianElimination.Point(2,2)));
		
		System.out.println(toFieldName(DebugHelper.class, 10)); //Prints 'testValue'
	}
	
	/**
	 * Prints all static fields and their values on the given class.
	 * @param c
	 * @param printModifier
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> void staticDumpInfo(Class<T> c, boolean printModifier) throws IllegalArgumentException, IllegalAccessException{
		Field[] fields = c.getDeclaredFields();
        System.out.printf(c.getSimpleName() + " has %d fields:%n", fields.length);
        for (Field field : fields) {
            System.out.printf("%s %s %s = '%s'%n",
                printModifier ? Modifier.toString(field.getModifiers()) : "",
                field.getType().getSimpleName(),
                field.getName(),
                field.get(null)
            );
        }
	}
	
	/**
	 * Returns all static fields and their values on the given class as string for debugging.
	 * @param c
	 * @param printModifier
	 * @param prefix
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public <T> String dumpInfo(Class<T> c, boolean printModifier, boolean prefix) throws IllegalArgumentException, IllegalAccessException{
		Field[] fields = c.getDeclaredFields();
		String s = prefix ? msgPrefix + " " : "";
        s += c.getSimpleName() + " has " + fields.length + " fields:\n";
        for (Field field : fields) {
        	if(field!=null){
        		boolean err = false;
        		try {
        			field.get(null);
        		} catch(Exception e){
        			err = true;
        			s += printModifier ? c(Modifier.toString(field.getModifiers())) : "" + " " + c(field.getType().getSimpleName()) + " " + c(field.getName()) + " = ? (Non-static) \n";
        		} finally {
        			if(!err) s += printModifier ? c(Modifier.toString(field.getModifiers())) : "" + " " + c(field.getType().getSimpleName()) + " " + c(field.getName()) + " = '" + cObj(field.get(null)) + "'\n";
        		}
        	}
        }
        return s;
	}
	
	/**
	 * Returns all non-static field-values and their values on the given object as string for debugging.
	 * @param c
	 * @param printModifier
	 * @param prefix
	 * @param relative
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public <T> String dumpInfo(Class<T> c, boolean printModifier, boolean prefix, Object relative) throws IllegalArgumentException, IllegalAccessException{
		Field[] fields = c.getDeclaredFields();
		String s = prefix ? msgPrefix + " " : "";
        s += c.getSimpleName() + " has " + fields.length + " fields:\n";
        for (Field field : fields) {
        	if(field!=null){
        		s += printModifier ? c(Modifier.toString(field.getModifiers())) : "" + " " + c(field.getType().getSimpleName()) + " " + c(field.getName()) + " = '" + cObj(field.get(relative)) + "'\n";
        	}
        }
        return s;
	}

	public String getPrefix() {
		return msgPrefix;
	}

	public void setPrefix(String msgPrefix) {
		this.msgPrefix = msgPrefix;
	}
	
	private String c(String s){
		return (s == null) ? "null" : s;
	}
	
	private Object cObj(Object s){
		return (s == null) ? "null" : s;
	}
	
	/**
	 * Returns value of the field with the given name or else null.<br>Reflection
	 * @param c
	 * @param s
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getValueOfField(@SuppressWarnings("rawtypes") Class c, String s) throws IllegalArgumentException, IllegalAccessException{
		for(Field f : c.getFields()) {
			if(f.getName().equals(s))
				return f.get(null);
		}
		return null;
	}
	
	/**
	 * Returns integer value of the field with the given name or else -1.<br>Reflection
	 * @param c
	 * @param s
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static int getFieldAsInteger(@SuppressWarnings("rawtypes") Class c, String s) throws IllegalArgumentException, IllegalAccessException{
		for(Field f : c.getFields()) {
			if(f.getName().equals(s))
				return (int) f.get(null);
		}
		return -1;
	}
	
	/**
	 * Returns boolean value of the field with the given name or else false.<br>Reflection
	 * @param c
	 * @param s
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static boolean getFieldAsBoolean(@SuppressWarnings("rawtypes") Class c, String s) throws IllegalArgumentException, IllegalAccessException{
		for(Field f : c.getFields()) {
			if(f.getName().equals(s))
				return (boolean) f.get(null);
		}
		return false;
	}
	
	/**
	 * Returns string value of the field with the given name or else null.<br>Reflection
	 * @param c
	 * @param s
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String getFieldAsString(@SuppressWarnings("rawtypes") Class c, String s) throws IllegalArgumentException, IllegalAccessException{
		for(Field f : c.getFields()) {
			if(f.getName().equals(s))
				return (String) f.get(null);
		}
		return null;
	}
	
	/**
	 * Will replace %variableName% (case-sensitive) with the variable value directly from the object<br>
	 * %%% will insert the given object .toString().
	 * <br>Uses reflection.
	 * @param insertTo
	 * @param getFrom
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String insertAllVariables(String insertTo, Object getFrom) throws IllegalArgumentException, IllegalAccessException{
		if(getFrom!=null)
			for(Field f : getFrom.getClass().getFields()) {
				if(insertTo.contains("%" + f.getName() + "%")){
					insertTo = insertTo.replaceAll("%" + f.getName() + "%", f.get(getFrom).toString());
				}
			}
		if(insertTo.contains("%%%"))
			insertTo = insertTo.replaceAll("%%%", (getFrom==null ? "null" : getFrom.toString()));
		return insertTo;
	}
	
	/**
	 * Will replace %variableName% (case-sensitive) with the variable value directly from the first of the given objects<br>which contains a field with this name<br>
	 * %%% will insert the first given object .toString().
	 * <br>Uses reflection.
	 * @param insertTo
	 * @param getFrom
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String multiInsert(String insertTo, Object... getFrom) throws IllegalArgumentException, IllegalAccessException{
		for(Object o : getFrom){
			if(o==null) continue;
			for(Field f : o.getClass().getFields()) {
				if(insertTo.contains("%" + f.getName() + "%")){
					insertTo = insertTo.replaceAll("%" + f.getName() + "%", f.get(o).toString());
				}
			}
		}
		if(insertTo.contains("%%%"))
			insertTo = insertTo.replaceAll("%%%", (getFrom[0] == null ? "null" : getFrom[0].toString()));
		return insertTo;
	}
	
	/**
	 * Converts any value thats equal to a field value of class c to the name of the field. Only with public fields.
	 * @param c
	 * @param o
	 * @param value
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String toFieldName(Class c, int value) throws IllegalArgumentException, IllegalAccessException{
		for(Field f : c.getFields()){
			try {
				f.getInt(c.newInstance());
			} catch(IllegalArgumentException e){continue;}
			catch(InstantiationException e){break;}
			try {
				if(f.getInt(c.newInstance())==value){
					return f.getName();
				}
			} catch (InstantiationException e) {
				System.err.println("Can't extract value from class with private constructor");
				e.printStackTrace();
			}
		}
		return "" + value;
	}
	
}

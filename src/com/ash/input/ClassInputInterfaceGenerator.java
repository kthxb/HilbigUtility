package com.ash.input;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * With {@code classToInputInterface(Class, name, description)} you can automatically generate an {@link InputInterface} for
 * any class at runtime. It will contain input fields for every supported type (int, boolean, String) and therefor provide an easy-to-use
 * GUI where the user can select all parameters to his liking.<br>The {@link InputInterface} will contain only fields with the {@link UserParameter} annotation.<br>
 * The input can later (after calling "open" on the GUI) be parsed as follows:<br>
 * <pre>
 * variable = (TypeCast) i.get("variable");
 * </pre>
 * or by using {@link ClassInputInterfaceGenerator#marshall(HashMap, Class, Object)} like this:
 * <pre>
 * marshall(i.result, Class.class, object); //object is instanceof Class.class
 * </pre>
 * Full example:
 * <pre>
 * HashMap<String, Object> hm = classToInputInterface(ClassInputInterfaceGenerator.class, "Title", "Description").open(); //Thread sleeps while GUI is open.
 * ClassInputInterfaceGenerator test = new ClassInputInterfaceGenerator(); //Create a new instance to paste the content into
 * marshall(hm, ClassInputInterfaceGenerator.class, test); //Marshalling
 * 
 * System.out.println(test.string); //Prints string
 * System.out.println(test.b); //Prints b ...
 * </pre>
 * 
 * @author Aaron Hilbig
 *
 */
public class ClassInputInterfaceGenerator {
	
	@UserParameter
	public String string = "hallo";
	@UserParameter
	public boolean b;
	
	public static void main(String[] args) {
		try {
			HashMap<String, Object> hm = classToInputInterface(ClassInputInterfaceGenerator.class, "", "").open();
			ClassInputInterfaceGenerator test = new ClassInputInterfaceGenerator();
			marshall(hm, ClassInputInterfaceGenerator.class, test);
			System.out.println(test.string);
			System.out.println(test.b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void classAutoMarshall(@SuppressWarnings("rawtypes") Class c, String name, String description, Object instanceOfClass) throws IllegalArgumentException, IllegalAccessException, Exception{
		marshall(classToInputInterface(c, name, description).open(), c, instanceOfClass);
	}
	
	/**
	 * Please read the javadoc of {@link ClassInputInterfaceGenerator}
	 * @param c
	 * @param name
	 * @param description
	 * @return
	 */
	public static InputInterface classToInputInterface(@SuppressWarnings("rawtypes") Class c, String name, String description){
		InputInterface i = new InputInterface(name, description);
		int addedInputs = 0;
		for(Field f : c.getFields()){
			if(f.isAnnotationPresent(UserParameter.class)){
				if(f.getType().getName().equals("java.lang.String")){
					i.addStringInput(f.getName());
					addedInputs++;
				} else if(f.getType().getName().equals("int")){
					i.addIntegerInput(f.getName());
					addedInputs++;
				} else if(f.getType().getName().equals("boolean")){
					i.addBooleanInput(f.getName());
					addedInputs++;
				}
			}
		}
		if(addedInputs==0) return null;
		return i;
	}
	
	public static void marshall(HashMap<String, Object> result, @SuppressWarnings("rawtypes") Class c, Object instanceOfClass) throws IllegalArgumentException, IllegalAccessException{
		for(Field f : c.getFields()){
			if(f.isAnnotationPresent(UserParameter.class)){
				f.set(instanceOfClass, result.get(f.getName()));
			}
		}
	}
	
}

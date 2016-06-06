package com.ash.input;

import java.io.File;
import java.io.StringReader;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ash.util.files.FileManager;

/**
 * 
 * See {@link InputInterface}
 * 
 * @author Aaron Hilbig
 */
@XmlRootElement(name = "InputUI")
public class InputInterfaceXml {
	
	@XmlElement(name = "integerInput")
	public IntegerInput[] integerInputs;
	@XmlElement(name = "limitedIntegerInput")
	public LimitedIntegerInput[] limitedIntegerInputs;
	@XmlElement(name = "stringInput")
	public StringInput[] stringInputs;
	@XmlElement(name = "formatStringInput")
	public FormatStringInput[] formatStringInputs;
	@XmlElement(name = "multiInput")
	public MultiInput[] multiInputs;
	@XmlElement(name = "booleanInput")
	public BooleanInput[] booleanInputs;
	@XmlAttribute
	public String title;
	//@XmlElement(name = "description")
	@XmlAttribute
	public String desc; 
	@XmlAttribute
	public String buttonText;
	
	public InputInterfaceXml(){}
	
	public InputInterfaceXml(String title, String desc, String buttonText, IntegerInput[] integerInputs, LimitedIntegerInput[] limitedIntegerInputs,
			StringInput[] stringInputs, MultiInput[] multiInputs, BooleanInput[] booleanInputs, FormatStringInput[] formatStringInputs) {
		this.buttonText = buttonText;
		this.title = title;
		this.desc = desc;
		this.integerInputs = integerInputs;
		this.limitedIntegerInputs = limitedIntegerInputs;
		this.stringInputs = stringInputs;
		this.multiInputs = multiInputs;
		this.booleanInputs = booleanInputs;
		this.formatStringInputs = formatStringInputs;
	}
	
	public InputInterfaceXml(String title, String desc){
		this.title = title;
		this.desc = desc;
	}
	
	
	public static InputInterface interfaceFromXml(File f){
		return JAXB.unmarshal(f, InputInterfaceXml.class).toInputInterface();
	}
	
	public static InputInterface interfaceFromXml(String s) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(InputInterfaceXml.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(s);
		return ((InputInterfaceXml)unmarshaller.unmarshal(reader)).toInputInterface();
	}
	
	public InputInterface toInputInterface(){
		InputInterface ret = new InputInterface(title,desc);
		ret.setButtonText(buttonText);
		if(integerInputs!=null)
			for(IntegerInput i : integerInputs){
				if(i!=null)
				ret.addIntegerInput(i.name == null ? "null" : i.name);
			}
		if(formatStringInputs!=null)
			for(FormatStringInput i : formatStringInputs){
				if(i!=null)
				ret.addStringInput(i.name == null ? "null" : i.name, i.format);
			}
		if(stringInputs!=null)
			for(StringInput i : stringInputs){
				if(i!=null)
				ret.addStringInput(i.name == null ? "null" : i.name);
			}
		if(limitedIntegerInputs!=null)
			for(LimitedIntegerInput i : limitedIntegerInputs){
				if(i!=null)
				ret.addIntegerInput(i.name == null ? "null" : i.name, i.min, i.max);
			}
		if(multiInputs!=null)
			for(MultiInput i : multiInputs){
				if(i!=null)
				ret.addMultiInput(i.name == null ? "null" : i.name, i.options);
			}
		if(booleanInputs!=null)
			for(BooleanInput i : booleanInputs){
				if(i!=null)
				ret.addBooleanInput(i.name == null ? "null" : i.name);
			}
		return ret;
	}
	
	public void print(){
		JAXB.marshal(this, System.out);
	}

	public static void main(String... args) {
		InputInterfaceXml iix = new InputInterfaceXml();
		iix.integerInputs = new IntegerInput[]{
				new IntegerInput("Hallo")
		};
		iix.desc = "beschr";
		iix.buttonText = "accept";
		iix.title = "title";
		
		JAXB.marshal(iix, System.out);
	}
	
	public void saveAsFile(){
		JAXB.marshal(this, FileManager.getFileWithDialog("Save InputInterface as XML-File", false, null, ".xml"));
	}

	public static InputInterfaceXml openFromFile(){
		return JAXB.unmarshal(FileManager.getFileWithDialog("Load InputInterface from XML-File", true, null, ".xml"), InputInterfaceXml.class);
	}
	
	public static class IntegerInput {
		public IntegerInput(String name) {
			this.name = name;
		}

		public String name;
		
		public IntegerInput(){}
	}
	
	public static class LimitedIntegerInput {
		public String name;
		public int min, max;
		public LimitedIntegerInput(String name, int min, int max) {
			this.name = name;
			this.min = min;
			this.max = max;
		}
		public LimitedIntegerInput(){}
	}
	
	public static class StringInput {
		public String name;

		public StringInput(String name) {
			this.name = name;
		}
		public StringInput(){}
	}
	
	public static class FormatStringInput {
		public String name, format;

		public FormatStringInput(String name, String format) {
			this.name = name;
			this.format = format;
		}
		public FormatStringInput(){}
	}
	
	public static class MultiInput {
		public String name;
		public MultiInput(String name, String[] options) {
			this.name = name;
			this.options = options;
		}
		@XmlElement(name = "option")
		public String[] options;
		public MultiInput(){}
	}
	
	public static class BooleanInput {
		public String name;

		public BooleanInput(String name) {
			this.name = name;
		}
		public BooleanInput(){}
	}
	
}

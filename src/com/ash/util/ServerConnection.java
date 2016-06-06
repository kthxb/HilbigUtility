package com.ash.util;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.ash.util.files.FileManager;

/**
 * 
 * ServerConnection: Establishes connection to the server<br>
 * Known keys: name, passwordHash, premium, email
 * @author Aaron Hilbig
 *
 */
public class ServerConnection {
	
	public static void main(String[] args) throws Exception {
		
		ServerConnection s = new ServerConnection();
		
		System.out.println(FileManager.getCurrentPath());
		
		
		//System.out.println(FileManager.getTreeVision(FileManager.getDirectoryWithDialog("", "Select directory", null, null)));
		
		//System.out.println(s.getValueStartsWith(admin, "te")[0]);
		
		//s.getAllUsers();
		
		//How 2 Cipher/*
		/*
		Object[] cr = cipherCrypt("String");
		
		System.out.println("cipherCrypt: " + (String) cr[0]);
		
		System.out.println("cipherResolve: " + cipherResolve((String) cr[0], (SecretKey) cr[1]));
		
		System.out.println("cipherResolve Locked: " + cipherResolve("bveCUmWyyw0roWiEABCBtg==", stringToKey("sqKMWp/Umb/AfoAaDdGLqw==")));
		
		System.out.println(keyToString((SecretKey) cr[1]));
		*/
		/*System.out.println(s.getValue(admin, "testvalue"));
		s.setValue(admin, "zahl", "25");
		Thread.sleep(250);
		System.out.println(s.getValue(admin, "zahl"));*/
		/*
		try {
			s.setValue(admin, "test0", "test");
			
			System.out.println(s.getValue(admin, "test0"));
		} catch(Exception e){
			e.printStackTrace();
		}
		*/
	}
	
	/**
	 * User admin (für globale Werte)
	 */
	public static User admin;
	
	public static boolean debugPostMethod = false;
	
	public static String cryptAddr = "5Cf7YogHWH0obo+l3L7deCVYksC1IKaMhsqLXq6Cl4Ai9mlPxPgc5qR55ZSHHZgA";
	public static String crypt = "4MS1Y78zL6LAoaKmsZWV9w==";
	
	//public String getURL = "http://android.winbert.selfhost.eu/getvalue.php";
	//public String newURL = "http://android.winbert.selfhost.eu/newvalue.php";
	//public String setURL = "http://android.winbert.selfhost.eu/setvalue.php";
	//public String getAllUsersURL = "http://android.winbert.selfhost.eu/getallusers.php";
	
	/**
	 * Sets the request method (Http GET, Http SET, NEW, ALL, ALLKEYS)
	 * @author Aaron Hilbig
	 */
	public static enum Method {
		GET("getvalue.php"), SET("setvalue.php"), NEW("newvalue.php"), ALL("getallusers.php"), ALLKEYS("getallkeys.php");
		
		public String url;
		
		Method(String url){
			this.url = url;
		}
		
		public String resolve() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException{
			return cipherResolve(cryptAddr, stringToKey(crypt));
		}
	}
	
	/**
	 * User
	 * @author Aaron Hilbig
	 *
	 */
	public static class User {
		
		/**
		 * Liste aller angemeldeten User
		 */
		public static ArrayList<User> users = new ArrayList<User>();
		
		public String name;
		public int id;
		public String key;
		
		/**
		 * 
		 * @param name
		 * @param id
		 * @param key
		 * @return
		 */
		public static User authenticateUser(String name, int id, String key){
			//TODO
			return new User(name, id, key);
		}
		
		@Deprecated
		public User(String username){
			this.name = username;
			this.id = 0;
			this.key = "nokey";
			users.add(this);
		}
		
		private User(String name, int id, String key){
			this.name = name;
			this.id = id;
			this.key = key;
			users.add(this);
		}
		
		public static User getUserById(int id){
			for(User u : users)
				if(u.id==id)
					return u;
			return null;
		}
		
		public static User getUserByName(String name){
			for(User u : users)
				if(u.name.equals(name))
					return u;
			return null;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
		
	}
	
	public static class ServerConnectionException extends Exception {
		public ServerConnectionException(String s){
			super(s);
		}
	}
	
	/**
	 * Verbindung aufbauen
	 */
	public ServerConnection(){
		admin = User.authenticateUser("admin", 00000000, "adminpw");
		//TODO Verbindung aufbauen
	}
	
	
	/*
	 * Setter
	 */
	
	public void setGlobalValue(String key, boolean value) throws Exception{
		this.setGlobalValue(key, value ? "true" : "false");
	}
	
	public void setGlobalValue(String key, int value) throws Exception{
		this.setGlobalValue(key, "" + value);
	}
	
	public void setGlobalValue(String key, String value) throws Exception{
		this.setValue(admin, key, value);
	}
	
	public void setValue(User u, String key, boolean value) throws Exception{
		this.setValue(u, key, value ? "true" : "false");
	}
	
	public void setValue(User u, String key, int value) throws Exception{
		this.setValue(u, key, "" + value);
	}
	
	public void setValue(User u, String key, String value) throws Exception{
		String s = httpPOST(Method.SET, "user",u.name,"name",key,"value",value);
		if(!s.equalsIgnoreCase(getValue(u, key))){
			throw new ServerConnectionException("Unbekannter name/key: " + key + ". Key erstellen mit newValue(...)");
		}
	}
	
	public void newValue(User u, String key, String value) throws Exception{
		httpPOST(Method.NEW, "user",u.name,"name",key,"value",value);
	}
	
	public void newGlobalValue(String key, String value) throws Exception{
		httpPOST(Method.NEW, "user",admin.name,"name",key,"value",value);
	}
	
	
	/*
	 * Getter
	 */
	
	public String getGlobalValue(String key) throws Exception{
		return this.getValue(admin, key);
	}
	
	public String[] getValueStartsWith(User u, String keyStart) throws Exception{
		return httpPOST(Method.ALLKEYS, "user",u.name,"name",keyStart).split("<nextentry>");
	}
	
	/**
	 * Working keys: email (use .trim()!), premium, passwordHash 
	 * @param u
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String getValue(User u, String key) throws Exception{
		return httpPOST(Method.GET, "user",u.name,"name",key);
	}
	
	public boolean getValueAsBoolean(User u, String key) throws Exception{
		return stringToBoolean(getValue(u, key).trim());
	}
	
	public int getValueAsInt(User u, String key){
		try{
			return Integer.parseInt(getValue(u, key).trim());
		} catch(Exception e){
			return 0;
		}
	}
	
	public boolean isConnected(){
		//TODO
		return false;
	}
	
	private boolean stringToBoolean(String s){
		if(s.toLowerCase().equalsIgnoreCase("true") || s.toLowerCase().equals("1") || s.toLowerCase().equals("wahr"))
			return true;
		return false;
	}
	
	public ArrayList<User> getAllUsers() throws Exception {
		String str = httpPOST(Method.ALL).trim();
		ArrayList<User> users = new ArrayList<User>();
		if(debugPostMethod) System.out.println("Getting all users: " + str);
		for(String s : str.split(",")){
			if(s.length()>3){
				users.add(new User(s));
				if(debugPostMethod) System.out.println("Added user to selection: \"" + s + "\"");
			} else {
				if(debugPostMethod) System.out.println("Invalid user: \"" + s + "\"");
			}
		}
		return users;
	}
	
	/*private ArrayList<NameValuePair> generateParameters(NV... nameValue){
		ArrayList<NameValuePair> ret = new ArrayList<NameValuePair>();
		for(NV nv : nameValue){
			ret.add(new BasicNameValuePair(nv.name, nv.value));
		}
		return ret;
	}
	
	private static class NV {
		
		private String name, value;
		
		public static NV make(String name, String value){
			return new NV(name, value);
		}
		
		public NV(String name, String value){
			this.setName(name);
			this.setValue(value);
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}*/
	
	private String httpPOST(Method m, String... nameValuePairs) throws Exception{
		HttpClient httpclient = HttpClients.createDefault();
		//HttpPost httppost = new HttpPost(m.equals(Method.GET) ? getURL : m.equals(Method.SET) ? setURL : m.equals(Method.NEW) ? newURL : m.equals(Method.ALL) ? getAllUsersURL : "");
		HttpPost httppost = new HttpPost(m.resolve() + m.url);
//		System.out.println(m.resolve() + m.url);
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		boolean value = false;
		String lastName = "";
		for(String s : nameValuePairs){
			if(value){
				params.add(new BasicNameValuePair(lastName,s));
				if(debugPostMethod) System.out.println("Made Name/Val-Pair: " + lastName + "/" + s);
			} else {
				lastName = s;
			}
			value = !value;
		}
		
		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		
		if(debugPostMethod) System.out.println("Sending HttpPost-Req... (" + httppost.toString() + ")");
		
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		
		String responseStr = "";
		
		if (entity != null) {
		    InputStream instream = entity.getContent();
		    
		    if(debugPostMethod) System.out.println("Req sent. Got entity-content id: " + entity.toString() + "\nReading:\n");
		    
	    	StringWriter writer = new StringWriter();
	    	IOUtils.copy(instream, writer, "UTF-8");
	    	responseStr = writer.toString();
	    	
	    	if(debugPostMethod) System.out.println(responseStr + "\n\n<-> Finished!");
	    	
	    	if(responseStr.startsWith("speichern denied") && m.equals(Method.NEW))
	    		throw new ServerConnectionException("Wert bereits vorhanden! " + Arrays.toString(nameValuePairs));
		}
		
		return responseStr;
	}
	
	private static String convertToHex(byte[] data)
    {
        StringBuffer buf = new StringBuffer();
 
        for (int i = 0; i < data.length; i++)
        {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do
            {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            }
            while(two_halfs++ < 1);
        }
        return buf.toString();
    }
 
    public static String sha512(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-512");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("UTF-8"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
    
    public static Object[] cipherCrypt(String s) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException{
		SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
		System.out.println(secretKey);
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] utf8Bytes = s.getBytes("UTF8");
		byte[] encryptedBytes = cipher.doFinal(utf8Bytes);
		return new Object[]{new String(Base64.encodeBase64(encryptedBytes)), secretKey};
    }
    
    public static Object[] cipherCrypt(String s, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException{
		SecretKey secretKey = stringToKey(key);
		System.out.println(secretKey);
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] utf8Bytes = s.getBytes("UTF8");
		byte[] encryptedBytes = cipher.doFinal(utf8Bytes);
		return new Object[]{new String(Base64.encodeBase64(encryptedBytes)), secretKey};
    }
    
    public static String cipherResolve(String s, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException{
    	Cipher cipher = Cipher.getInstance("AES");
    	cipher.init(Cipher.DECRYPT_MODE, secretKey);
    	byte[] encryptedBytes = Base64.decodeBase64(s);
    	byte[] utf8Bytes = cipher.doFinal(encryptedBytes);
		return new String(utf8Bytes, "UTF8");
    }
    
    public static String keyToString(SecretKey s){
    	return new Base64().encodeToString(s.getEncoded());
    }
    
    public static SecretKey stringToKey(String s){
    	byte[] decodedKey = new Base64().decode(s);
    	return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
    }
	
}

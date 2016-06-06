package com.ash.util.files;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Loads SWT libraries according to OS
 * @author Aaron Hilbig
 *
 */
public class SWTLoader {
	
    
    public static void loadSWT() throws Exception {
    	
        String swtJarName = "lib/" + getNewLibName();
        System.out.println("Loading SWT libraries required for the GUI: " + swtJarName);
        
        System.out.println("Could find file: " + new File(swtJarName).exists());
        
        addToClasspath(new File(swtJarName).toURI().toURL());
    }
    
    private static String getNewLibName() throws Exception {
    	String osName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
        String os = osName.contains("win") ? "win" : osName.contains("mac") ? "mac" : osName.contains("linux") || osName.contains("nix") ? "linux" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
        if ("".equals(os)) {
            throw new Exception("Couldn´t load the libraries required for the GUI (SWT Standart " + os + "_" + getArchitecture() +")");
        }
        
        return "swt_" + os + ".jar";
    }
	
	@SuppressWarnings("unused")
	private static String getLibName() throws Exception {
        return "swt_" + getOS() + ".jar";
    }

    private static String getOS() throws Exception {
        String osName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
        String os = osName.contains("win") ? "win32" : osName.contains("mac") ? "mac" : osName.contains("linux") || osName.contains("nix") ? "linux" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
        if ("".equals(os)) { //$NON-NLS-1$
            throw new Exception("Couldn´t load the libraries required for the GUI (SWT Standart " + os + "_" + getArchitecture() +")");
        }
        return os + "_" + getArchitecture(); //$NON-NLS-1$
    }
    
    private static String getArchitecture() {
        String jvmArch = System.getProperty("os.arch").toLowerCase(); //$NON-NLS-1$
        String arch = jvmArch.contains("64") ? "x64" : "x32"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return arch;
    }
    
    @SuppressWarnings("rawtypes")
    private static void addToClasspath(URL u) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> sysclass = URLClassLoader.class;
        
        Class[] parameters = new Class[] { URL.class };
        Method method = sysclass.getDeclaredMethod("addURL", parameters); //$NON-NLS-1$
        method.setAccessible(true);
        method.invoke(sysloader, new Object[] {u});
    }
    
}

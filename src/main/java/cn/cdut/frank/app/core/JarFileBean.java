package cn.cdut.frank.app.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarFileBean {

	private Map<String, List<JarFile>> all = new HashMap<>();
	
	private List<JarFile> allJar;
	
	private Map<String, List<JarFile>> conflict = new HashMap<>();
	
	private boolean recursion;
	
	private String path;
	
	public JarFileBean() {
		
	}
	
	public JarFileBean(String path, boolean recursion) {
		this.path = path;
		this.recursion = recursion;
	}
	
	public void checkConflict() {
		try {
			List<JarFile> jarFileList = allJar = JarFileUtil.getAllJarFile(path, recursion);
			for(JarFile jarFile : jarFileList) {
				Enumeration<JarEntry> enumeration = jarFile.entries();
				while(enumeration.hasMoreElements()) {
					JarEntry jarEntry = enumeration.nextElement();
					String className = jarEntry.getName();
					
					if(!className.endsWith(".class"))
						continue;
					
					List<JarFile> jarsList = all.get(className);
					if(jarsList == null) {
						jarsList = new ArrayList<>();
					}
					jarsList.add(jarFile);
					all.put(className, jarsList);
				}
			}
			
			extractConflictJar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		if(allJar != null) {
			for(JarFile jar : allJar) {
				try {
					jar.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void extractConflictJar() {
		Set<Entry<String, List<JarFile>>> entrySet = all.entrySet();
		for(Entry<String, List<JarFile>> entry : entrySet) {
			if(entry.getValue().size() > 1) {
				conflict.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public Map<String, List<JarFile>> getConflict() {
		return conflict;
	}

	public boolean isRecursion() {
		return recursion;
	}

	public void setRecursion(boolean recursion) {
		this.recursion = recursion;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}

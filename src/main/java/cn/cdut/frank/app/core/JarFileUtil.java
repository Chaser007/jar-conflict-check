package cn.cdut.frank.app.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;

/**
 * jar包工具类
 * @author Administrator
 *
 */
public class JarFileUtil {
	
	public static boolean checkIfJarFile(File file) {
		return file != null && file.exists() && file.isFile() && file.getName().endsWith(".jar");
	}
	
	public static List<JarFile> getAllJarFile(String path, boolean recursion) throws Exception {
		List<JarFile> jarFileList = new LinkedList<>();
		File dir = new File(path);
		if(! dir.exists()) {
			throw new FileNotFoundException(path + ": 文件或目录不存在！");
		}
		
		if(checkIfJarFile(dir)) {
			jarFileList.add(new JarFile(dir));
			return jarFileList;
		}
		
		List<File> jarFiles = getAllJarFile(dir, recursion);
		for(File jar : jarFiles) {
			jarFileList.add(new JarFile(jar));
		}
		
		return jarFileList;
	}
	
	public static List<File> getAllJarFile(File file, boolean recursion) {
		List<File> fileList = new LinkedList<>();
		
		if(checkIfJarFile(file)) {
			fileList.add(file);
			return fileList;
		}
		
		getAllJarFile(fileList, file, recursion);
		return fileList;
	}
	
	/**
	 * 递归遍历所有目录获取jar文件
	 * @param fileList
	 * @param file
	 * @param recursion
	 */
	private static void getAllJarFile(List<File> fileList, File file, boolean recursion) {
		if(file.isDirectory()) {
			File[] jars = file.listFiles((File d, String name) -> name.endsWith(".jar"));
			Collections.addAll(fileList, jars);
			
			if(recursion) {
				File[] dirs = file.listFiles(f -> f.isDirectory());
				for(File dir : dirs) {
					getAllJarFile(fileList, dir, true);
				}
			}
		}
	}
}

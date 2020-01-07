package cn.cdut.frank.app;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.JarFile;

import javax.swing.JFrame;
import javax.swing.JLabel;

import cn.cdut.frank.app.core.JarFileBean;

public class JarConflictCheckApp {
	
	public static void createJFrame() {
		// 创建及设置窗口
        JFrame frame = new JFrame("jar包冲突检测");
        frame.setLocation(1000, 500);//设置窗口的位置
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        // 添加 "Hello World" 标签
        JLabel label = new JLabel("Hello World");
        //label.setSize(1000, 1000);
        frame.getContentPane().add(label);

        // 显示窗口
        frame.pack();
        frame.setVisible(true);
	}

	public static void main(String[] args) {
		String path = "E:\\avetti-workspace\\c10qa\\setup-admin\\target\\enterprise-1.0\\WEB-INF\\lib";
		JarFileBean jarFileBean = new JarFileBean(path, true);
		jarFileBean.checkConflict();
		Map<String, List<JarFile>> conflictMap = jarFileBean.getConflict();
		Set<Entry<String, List<JarFile>>> entrySet = conflictMap.entrySet();
		for(Entry<String, List<JarFile>> entry : entrySet) {
			List<JarFile> jarFiles = entry.getValue();
			for(JarFile jar : jarFiles) {
				System.out.println(entry.getKey() + " -> "+ jar.getName());
			}
			
			System.out.println();
		}
		
		System.out.println("冲突项：" + entrySet.size());
		
		jarFileBean.close();
	}
}

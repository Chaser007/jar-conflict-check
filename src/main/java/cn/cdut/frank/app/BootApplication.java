package cn.cdut.frank.app;

import javax.swing.SwingUtilities;

import cn.cdut.frank.app.jframe.AppJFrame;

/**
 * 启动类
 * @author Huang Yong
 * @date 2020年1月21日 下午11:32:55
 */
public class BootApplication {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				AppJFrame app = new AppJFrame();
			}
		});
	}
}

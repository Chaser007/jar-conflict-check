package cn.cdut.frank.app;


import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AppJFrame extends JFrame{
	
	private static final int WIDTH = 550;
	
	private static final int HEIGHT = 600;
	
	private static final int MARGIN[] = {30, 30};
	
	private JTextArea conflictResult = new JTextArea();
	
	private JTextField inputPath = new JTextField(20);
	
	private JPanel topPanel = new JPanel();
	
	private JScrollPane bottomPanel = new JScrollPane();
	
	private JPanel buttonsPanel = new JPanel();
	
	public AppJFrame() {
		super();
		setTitle("Jar包查找器");

		// 框架居中
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setBounds((int) (width / 2 - WIDTH / 2), (int) (height / 2 - HEIGHT / 2), WIDTH, HEIGHT);
		setResizable(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		init();
		setVisible(true);
	}
	
	private void init() {
		Container container = getContentPane();
		
		Box baseBox = Box.createVerticalBox();
		topPanel.setPreferredSize(new Dimension(WIDTH, Math.round(HEIGHT * 0.25F)));
		bottomPanel.setPreferredSize(new Dimension(WIDTH, Math.round(HEIGHT * 0.65F)));
		buttonsPanel.setPreferredSize(new Dimension(WIDTH, Math.round(HEIGHT * 0.1F)));
		baseBox.add(topPanel);
		baseBox.add(bottomPanel);
		baseBox.add(buttonsPanel);
		
		Box inputArea = Box.createHorizontalBox();
		inputArea.add(new JLabel("路径："));
		inputArea.add(Box.createHorizontalStrut(20));
		inputArea.add(inputPath);
		topPanel.add(inputArea);

		
		conflictResult.append("显示冲突记录");
		bottomPanel.setViewportView(conflictResult);
		
		container.add(baseBox);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				AppJFrame app = new AppJFrame();
			}
		});
	}
}

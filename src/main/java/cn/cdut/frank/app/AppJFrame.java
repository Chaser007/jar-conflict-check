package cn.cdut.frank.app;


import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class AppJFrame extends JFrame{
	
	private static final int WIDTH = 550;
	
	private static final int HEIGHT = 600;
	
	private JTextArea conflictResult;
	
	private JTextField fileNameInput;
	
	private JTextField filePathInput;
	
	private JButton browseFileBtn;
	
	private JPanel topPanel;
	
	private JPanel messagePanel;
	
	private JScrollPane bottomPanel;
	
	private JPanel buttonsPanel;
	
	private JCheckBox filterCheckBox;
	
	private JButton startBtn;
	
	public AppJFrame() {
		super();
		setTitle("Jar包查找器");

		// 框架居中
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setBounds((int) (width / 2 - WIDTH / 2), (int) (height / 2 - HEIGHT / 2), WIDTH, HEIGHT);
		setResizable(false);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initGlobalFontSetting(new Font("alias", Font.PLAIN, 12));
		initMembers();
		init();
		setVisible(true);
	}
	
	/**
	 * 初始化成员，在initGlobalFontSetting后面初始化，使全局字体样式能够应用到组件中。
	 */
	private void initMembers() {
		conflictResult = new JTextArea();
		fileNameInput = new JTextField();
		filePathInput = new JTextField();
		browseFileBtn = new JButton();
		topPanel = new JPanel();
		messagePanel = new JPanel();
		bottomPanel = new JScrollPane();
		buttonsPanel = new JPanel();
		filterCheckBox = new JCheckBox();
		startBtn = new JButton();
	}

	private void init() {
		Container container = getContentPane();
		
		Dimension inputAreaSize = new Dimension(345, 23);
		Dimension buttonSize = new Dimension(75, 22);
		
		Box baseBox = Box.createVerticalBox();
		topPanel.setPreferredSize(new Dimension(WIDTH, Math.round(HEIGHT * 0.19F)));
		messagePanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		messagePanel.setPreferredSize(new Dimension(WIDTH, Math.round(HEIGHT * 0.06F)));
		bottomPanel.setPreferredSize(new Dimension(WIDTH, Math.round(HEIGHT * 0.7F)));
		buttonsPanel.setPreferredSize(new Dimension(WIDTH, Math.round(HEIGHT * 0.08F)));
		baseBox.add(topPanel);
		baseBox.add(Box.createVerticalStrut(10));
		baseBox.add(messagePanel);
		baseBox.add(Box.createVerticalStrut(10));
		baseBox.add(bottomPanel);
		baseBox.add(Box.createVerticalStrut(10));
		baseBox.add(buttonsPanel);
		baseBox.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
		
		topPanel.setLayout(new GridLayout(3, 1));
		Box searchFileBox = Box.createHorizontalBox();
		searchFileBox.add(new JLabel("搜索文件："));
		searchFileBox.add(Box.createHorizontalStrut(20));
		searchFileBox.add(fileNameInput);
		fileNameInput.setMaximumSize(inputAreaSize);
		Box searchPathBox = Box.createHorizontalBox();
		searchPathBox.add(new JLabel("搜索路径："));
		searchPathBox.add(Box.createHorizontalStrut(20));
		filePathInput.setMaximumSize(inputAreaSize);
		searchPathBox.add(filePathInput);
		searchPathBox.add(Box.createHorizontalStrut(20));
		browseFileBtn.setMaximumSize(buttonSize);
		browseFileBtn.setText("选择...");
		searchPathBox.add(browseFileBtn);
		Box filterSelectBox = Box.createHorizontalBox();
		filterSelectBox.add(new JLabel("搜索过滤："));
		filterSelectBox.add(Box.createHorizontalStrut(20));
		filterCheckBox.add(new JCheckBoxMenuItem("迭代子目录", true));
		filterSelectBox.add(filterCheckBox);
		topPanel.add(searchFileBox);
		topPanel.add(searchPathBox);
		topPanel.add(filterSelectBox);
		
		conflictResult.append("显示冲突记录");
		bottomPanel.setViewportView(conflictResult);
		
		startBtn.setText("开始检索");
		startBtn.setMaximumSize(buttonSize);
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonsPanel.add(startBtn);
		container.add(baseBox);
	}
	
	//设置全局字体
	public void initGlobalFontSetting(Font font){
	    FontUIResource fontRes = new FontUIResource(font);
	    for(Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();){
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if(value instanceof FontUIResource)
	            UIManager.put(key, fontRes);
	    }
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

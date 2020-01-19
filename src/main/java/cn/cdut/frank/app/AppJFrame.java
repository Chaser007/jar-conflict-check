package cn.cdut.frank.app;


import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.jar.JarFile;

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

import cn.cdut.frank.app.core.JarFileBean;

public class AppJFrame extends JFrame{
	
	private static final int WIDTH = 550;
	
	private static final int HEIGHT = 600;
	
	private JTextArea conflictResult;
	
	private JTextField fileNameInput;
	
	private JTextField filePathInput;
	
	private JButton browseFileBtn;
	
	private JPanel topPanel;
	
	private JLabel messageLabel;
	
	private JPanel messagePanel;
	
	private JScrollPane bottomPanel;
	
	private JPanel buttonsPanel;
	
	private JCheckBox filterCheckBox;
	
	private JButton startBtn;
	
	private JButton autoCheckBtn;
	
	private JCheckBoxMenuItem iteraterAllFloder;
	
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
		init();
		setVisible(true);
	}
	
	private void init() {
		initGlobalFontSetting(new Font("alias", Font.PLAIN, 14));
		initMembers();
		initGui();
		bindEvents();
	}
	
	private void bindEvents() {
		startBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		
		autoCheckBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				String path = filePathInput.getText();
				if(! new File(path).exists()) {
					logMessage("搜索目录不正确！");
					return;
				}
				boolean iterater = iteraterAllFloder.isSelected();
				logMessage("正在检索目录...");
				logResult("检索目录：" + path, 0, true);
				logResult("是否递归检索目录：" + iterater, 1);
				
				JarFileBean jarFileBean = new JarFileBean(path, iterater);
				jarFileBean.checkConflict();
				Map<String, List<JarFile>> conflictMap = jarFileBean.getConflict();
				Set<Entry<String, List<JarFile>>> entrySet = conflictMap.entrySet();
				for(Entry<String, List<JarFile>> entry : entrySet) {
					logResult("冲突的文件 -> " + entry.getKey(), 0);
					List<JarFile> jarFiles = entry.getValue();
					for(JarFile jar : jarFiles) {
						logResult("文件路径： " + jar.getName(), 0);
					}
					
					logResult("", 1);
				}
				
				logMessage("检索完毕！ 冲突项：" + entrySet.size());
				
				jarFileBean.close();
			}
		});
	}

	/**
	 * 初始化成员，在initGlobalFontSetting后面初始化，使全局字体样式能够应用到组件中。
	 */
	private void initMembers() {
		conflictResult = new JTextArea();
		fileNameInput = new JTextField();
		filePathInput = new JTextField();
		browseFileBtn = new JButton("选择");
		topPanel = new JPanel();
		messageLabel = new JLabel("准备就绪...");
		messagePanel = new JPanel();
		bottomPanel = new JScrollPane();
		buttonsPanel = new JPanel();
		filterCheckBox = new JCheckBox();
		startBtn = new JButton("开始检索");
		autoCheckBtn = new JButton("自动检测重复");
		iteraterAllFloder = new JCheckBoxMenuItem("迭代子目录", true);
	}
	
	private void initGui() {
		Container container = getContentPane();
		
		Dimension inputAreaSize = new Dimension(345, 23);
		Dimension buttonSize = new Dimension(75, 22);
		
		Box baseBox = Box.createVerticalBox();
		topPanel.setPreferredSize(new Dimension(WIDTH, Math.round(HEIGHT * 0.19F)));
		messagePanel.add(messageLabel);
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
		searchPathBox.add(browseFileBtn);
		Box filterSelectBox = Box.createHorizontalBox();
		filterSelectBox.add(new JLabel("搜索过滤："));
		filterSelectBox.add(Box.createHorizontalStrut(20));
		filterCheckBox.add(iteraterAllFloder);
		filterSelectBox.add(filterCheckBox);
		topPanel.add(searchFileBox);
		topPanel.add(searchPathBox);
		topPanel.add(filterSelectBox);
		
		conflictResult.append("显示冲突记录");
		conflictResult.setEditable(false);
		bottomPanel.setViewportView(conflictResult);
		
		startBtn.setMaximumSize(buttonSize);
		autoCheckBtn.setMaximumSize(buttonSize);
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonsPanel.add(startBtn);
		buttonsPanel.add(autoCheckBtn);
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
	
	public void logMessage(String message) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				messageLabel.setText(message);
			}
		});
	}
	
	/**
	 * 打印结果信息
	 * @param line
	 * @param blankRows
	 * @param clear 
	 * @return void   返回类型
	 */
	public void logResult(String line, int blankRows ,boolean clear) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(clear) {
					conflictResult.setText(line);
				} else {
					conflictResult.append(line);
				}
				for(int i=0; i<=blankRows; i++) {
					conflictResult.append("\n");
				}
			}
		});
	}
	
	public void logResult(String line, int blankRows) {
		logResult(line, blankRows ,false);
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

package cn.cdut.frank.app.jframe;


import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.FontUIResource;

import cn.cdut.frank.app.core.JarFileBean;

/**
 * JFrame窗口类
 * @author Huang Yong
 * @date 2020年1月21日 下午11:33:10
 */
public class AppJFrame extends JFrame{
	
	private static final int WIDTH = 550;
	
	private static final int HEIGHT = 600;
	
	private AppJFrame self = this;
	
	private JTextArea conflictResult;
	
	private JLabel qualifiedName;
	
	private JLabel searchPath;
	
	private JTextField fileNameInput;
	
	private JTextField filePathInput;
	
	private JButton browseFileBtn;
	
	private JPanel topPanel;
	
	private JLabel messageLabel;
	
	private JPanel messagePanel;
	
	private JScrollPane bottomPanel;
	
	private JPanel buttonsPanel;
	
	private JButton startBtn;
	
	private JButton autoCheckBtn;
	
	private JCheckBox iteraterAllFloder;
	
	private JCheckBox classFile;
	
	public AppJFrame() {
		super();
		setTitle("Jar包冲突查找器-(仅供学习交流)");

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
		initGlobalFontSetting(new Font("alias", Font.PLAIN, 13));
		initMembers();
		initGui();
		bindEvents();
	}
	
	private void bindEvents() {
		
		browseFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String filePath = showFileOpenDialog(self);
				filePathInput.setText(filePath);
			}
		});
		
		startBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				String fileName = fileNameInput.getText();
				String filePath = filePathInput.getText();
				boolean iterater = iteraterAllFloder.isSelected();
				
				if(fileName.trim().equals("")) {
					logMessage("请输入全限定类名");
					return;
				}
				if(! new File(filePath).exists()) {
					logMessage("搜索路径不正确！");
					return;
				}
				
				logMessage("正在检索文件...");
				logResult("检索目录：" + filePath, 0, true);
				logResult("检索的全限定类名：" + fileName, 0, true);
				logResult("是否递归检索目录：" + iterater, 1);
				
				int count = 0;
				String classPath = fileName.replaceAll("\\.", "/");
				JarFileBean jarFileBean = new JarFileBean(filePath, iterater);
				jarFileBean.checkConflict();
				Map<String, List<JarFile>> conflictMap = jarFileBean.getConflict();
				Set<Entry<String, List<JarFile>>> entrySet = conflictMap.entrySet();
				for(Entry<String, List<JarFile>> entry : entrySet) {
					if(!entry.getKey().startsWith(classPath)) {
						continue;
					}
					count ++;
					logResult("类路径 -> " + entry.getKey(), 0);
					List<JarFile> jarFiles = entry.getValue();
					for(JarFile jar : jarFiles) {
						logResult("文件路径： " + jar.getName(), 0);
					}
					
					logResult("", 1);
				}
				
				logMessage("检索完毕！ 冲突项：" + count);
				
				jarFileBean.close();
			}
		});
		
		autoCheckBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				String path = filePathInput.getText();
				if(! new File(path).exists()) {
					logMessage("搜索路径不正确！");
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
		qualifiedName = new JLabel("全限定名：");
		searchPath = new JLabel("搜索路径：");
		fileNameInput = new JTextField();
		filePathInput = new JTextField();
		browseFileBtn = new JButton("选择");
		topPanel = new JPanel();
		messageLabel = new JLabel("准备就绪...");
		messagePanel = new JPanel();
		bottomPanel = new JScrollPane();
		buttonsPanel = new JPanel();
		startBtn = new JButton("开始检索");
		autoCheckBtn = new JButton("自动检测重复");
		iteraterAllFloder = new JCheckBox("迭代子目录", true);
		classFile = new JCheckBox("class文件", true);
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
		searchFileBox.add(qualifiedName);
		searchFileBox.add(Box.createHorizontalStrut(20));
		searchFileBox.add(fileNameInput);
		fileNameInput.setMaximumSize(inputAreaSize);
		Box searchPathBox = Box.createHorizontalBox();
		searchPathBox.add(searchPath);
		searchPathBox.add(Box.createHorizontalStrut(20));
		filePathInput.setMaximumSize(inputAreaSize);
//		filePathInput.setEditable(false);
		searchPathBox.add(filePathInput);
		searchPathBox.add(Box.createHorizontalStrut(20));
		browseFileBtn.setMaximumSize(buttonSize);
		searchPathBox.add(browseFileBtn);
		Box filterSelectBox = Box.createHorizontalBox();
		filterSelectBox.add(new JLabel("搜索过滤："));
		filterSelectBox.add(Box.createHorizontalStrut(20));
		filterSelectBox.add(iteraterAllFloder);
		filterSelectBox.add(classFile);
		classFile.setEnabled(false);
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
	
	/**
     * 打开文件
     */
    private String showFileOpenDialog(Component parent) {
        // 创建一个默认的文件选取器
        JFileChooser fileChooser = new JFileChooser();
        // 设置默认显示的文件夹为当前文件夹
        fileChooser.setCurrentDirectory(new File("."));
        // 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        // 设置是否允许多选
        fileChooser.setMultiSelectionEnabled(true);
        // 添加可用的文件过滤器（FileNameExtensionFilter 的第一个参数是描述, 后面是需要过滤的文件扩展名 可变参数）
        fileChooser.addChoosableFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return null;
			}
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
		});
        
        // 设置默认使用的文件过滤器
        fileChooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return null;
			}
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
		});
        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            File file = fileChooser.getSelectedFile();
            // 如果允许选择多个文件, 则通过下面方法获取选择的所有文件
            // File[] files = fileChooser.getSelectedFiles();
            logMessage("选择目录: " + file.getAbsolutePath());
            return file.getAbsolutePath();
        }
        return "";
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

}
package hrms.ZMGFH.Interface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.ParseException;

import javax.swing.*;

import hrms.ZMGFH.Loading.Loading;
import hrms.ZMGFH.Objection.Employee;
import hrms.ZMGFH.Service.CameraService;
import hrms.ZMGFH.Service.FaceService;
import hrms.ZMGFH.Service.HRService;
import hrms.ZMGFH.Tools.TimeTools;

public class MainInterface extends JPanel {
	private MainFrame panelFrame;
	private JToggleButton Clock; // 打卡按钮
	private JButton AdminLogin; // 管理员登录按钮
	private JButton EmployeeLogin; // 员工登录按钮
	private JTextArea area; // 信息域
	private JPanel center;
	private DetectFaceThread dft;// 人脸识别线程

	public MainInterface(MainFrame panel) {
		this.panelFrame = panel;
		this.panelFrame.setSize(700,432);
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension Size = tool.getScreenSize();//获取屏幕大小
		setLocation((Size.width - 700) / 2, (Size.height - 432) / 2);//主窗体在屏幕中间显示
		init();
		addListener();
	}

	public void init() {
		panelFrame.setTitle("人事管理系统");

		Clock = new JToggleButton("打卡");
		AdminLogin = new JButton("管理员登录");
		EmployeeLogin = new JButton("员工登录");
		area = new JTextArea();
		center = new JPanel();
		JPanel bottom = new JPanel(); // 创建一个底部面板
		JPanel blackPanel = new JPanel();

		center.setLayout(null); // 采用绝对布局

		area.setEditable(false); // 文本域不可编辑
		area.setFont(new Font("宋体", Font.BOLD, 18)); // 设置字体字号
		JScrollPane s = new JScrollPane(area); // 将文本域放入到滚动面板中
		s.setBounds(380, 20, 280, 227); // 设置文本域的起始坐标，以及宽和高
		center.add(s);

		Clock.setFont(new Font("黑体", Font.BOLD, 40)); // 设置字体字号
		Clock.setBounds(460, 277, 120, 60); // 设置按钮起始坐标，以及宽和高
		center.add(Clock);

		blackPanel.setBounds(30, 20, 280, 317); // 设置纯黑面板的起始坐标，以及宽和高
		blackPanel.setBackground(Color.BLACK); // 设置面板为黑色
		center.add(blackPanel);

		setLayout(new BorderLayout());
		add(center, BorderLayout.CENTER);

		bottom.add(AdminLogin);
		bottom.add(EmployeeLogin); // 添加用户登录和员工登录按钮到底部面板

		add(bottom, BorderLayout.SOUTH);
		HRService.GetPiad();
	}

	private void addListener() {
		AdminLogin.addActionListener(new ActionListener() {// 管理员登录按钮
			public void actionPerformed(ActionEvent e) {
				if (Loading.user == null) {
					AdminLoginInterface adminLoginInterface = new AdminLoginInterface(panelFrame);
					adminLoginInterface.setVisible(true);
				}
				if (Loading.user != null) {
					AdminInterface adminInterface = new AdminInterface(panelFrame);
					panelFrame.setPanel(adminInterface);
				}
			}
		});

		EmployeeLogin.addActionListener(new ActionListener() {// 员工登录按钮
			public void actionPerformed(ActionEvent e) {
				if (Loading.user == null) {
					EmployeeLoginInterface employeeLoginInterface = new EmployeeLoginInterface(panelFrame);
					employeeLoginInterface.setVisible(true);// 展示员工登录按钮
				}
				if (Loading.user != null) {
					EmployeeInterface employeeInterface = new EmployeeInterface(panelFrame);
					panelFrame.setPanel(employeeInterface);
				}
			}
		});

		Clock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Clock.isSelected()) {// 如果打卡按钮是选中状态
					// 文本域添加提示信息
					area.append("正在开启摄像头，请稍后.......\n");
					Clock.setEnabled(false);// 打卡按钮不可用
					Clock.setText("关闭摄像头");// 更改打卡按钮的文本
					// 创建启动摄像头的临时线程
					Thread cameraThread = new Thread() {
						public void run() {
							// 如果摄像头可以正常开启
							if (CameraService.StartCamera()) {
								area.append("请面向摄像头打卡。\n");// 添加提示
								Clock.setEnabled(true);// 打卡按钮可用
								// 获取摄像头画面面板
								JPanel cameraPanel = CameraService.getCameraPanel();
								// 设置面板的坐标与宽高
								cameraPanel.setBounds(30, 20, 280, 317);
								center.add(cameraPanel);// 放到中部面板当中
							} else {
								// 弹出提示
								JOptionPane.showMessageDialog(null, "未检测到摄像头！");
								releaseCamera();// 释放摄像头资源
								return;// 停止方法
							}
						}
					};
					cameraThread.start();// 启动临时线程
					dft = new DetectFaceThread();// 创建人脸识别线程
					dft.start();// 启动人脸识别线程
				} else {// 如果打卡按钮不是选中状态
					releaseCamera();// 释放摄像头资源
				}
			}
		});

	}

	/**
	 * 释放摄像头及面板中的一些列资源
	 */
	private void releaseCamera() {
		CameraService.releaseCamera();// 释放摄像头
		area.append("摄像头已关闭。\n");// 添加提示信息
		if (dft != null) {// 如果人脸识别线程被创建
			dft.stopThread();// 停止线程
		}
		Clock.setText("打  卡");// 更改打卡按钮的文本
		Clock.setSelected(false);// 打卡按钮变为未选中状态
		Clock.setEnabled(true);// 打卡按钮可用
	}

	/**
	 * 人脸识别线程
	 *
	 * @author mingrisoft
	 *
	 */
	private class DetectFaceThread extends Thread {
		boolean work = true;// 人脸识别线程是否继续扫描image

		@Override
		public void run() {
			while (work) {
				// 如果摄像头已开启
				if (CameraService.cameraIsOpen()) {
					// 获取摄像头的当前帧
					BufferedImage frame = CameraService.getCameraFrame();
					if (frame != null) {// 如果可以获得到有效帧
						// 获取当前帧中出现的人脸对应的特征码
						String code = FaceService.detectFace(FaceService.getFaceFeature(frame));
						if (code != null) {// 如果特征码不为null，表明画面中存在某员工的人脸
							Employee e = HRService.getConEmp(code);// 根据特征码获取员工对象
                            try {
                                if(HRService.addClockInRecord(e)) {// 为此员工添加打卡记录
                                    // 文本域添加提示信息
                                    area.append("\n" + TimeTools.nowdatetime() + " \n");
                                    area.append(e.getName() + " 打卡成功。\n\n");
                                    releaseCamera();// 释放摄像头
                                }
                                else{
                                    area.append("今日已打过卡，不可重复打卡");
                                    releaseCamera();// 释放摄像头
                                }
                            } catch (ParseException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
					}
				}
			}
		}

		public synchronized void stopThread() {// 停止人脸识别线程
			work = false;
		}
	}
}

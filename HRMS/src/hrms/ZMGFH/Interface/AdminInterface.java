package hrms.ZMGFH.Interface;


import hrms.ZMGFH.Loading.Loading;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class AdminInterface extends JPanel{
	private MainFrame panelFrame;//主窗体
	
	private JButton employeeManagementButton;//员工管理按钮
	private JButton attendenceButton;//考勤报表按钮
	private JButton backButton;//返回按钮

	public AdminInterface(MainFrame panelFrame) {
		this.panelFrame = panelFrame;
		this.panelFrame.setSize(700,432);
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension Size = tool.getScreenSize();//获取屏幕大小
		setLocation((Size.width - 700) / 2, (Size.height - 432) / 2);//主窗体在屏幕中间显示
		init();
		addListener();
	}
	
	private void init() {
		panelFrame.setTitle("管理员工及考勤信息");
		
		employeeManagementButton = new JButton("管理员工");
		attendenceButton = new JButton("考勤报表");
		backButton = new JButton("返回");

		setLayout(new BorderLayout());

		JPanel bottom = new JPanel();//底部面板
		bottom.add(employeeManagementButton);
		bottom.add(attendenceButton);
		bottom.add(backButton);
		add(bottom,BorderLayout.SOUTH);
		
	}
	
	private void addListener() {
		employeeManagementButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EmpManagementInterface empManagementInterface = new EmpManagementInterface(panelFrame);
				panelFrame.setPanel(empManagementInterface);
			}
		});

		attendenceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AttendanceManagementInterface attendanceManagementInterface = new AttendanceManagementInterface(panelFrame);
				panelFrame.setPanel(attendanceManagementInterface);
			}
		});

		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panelFrame.setPanel(new MainInterface(panelFrame));
				Loading.exit();
			}
		});

	}


}

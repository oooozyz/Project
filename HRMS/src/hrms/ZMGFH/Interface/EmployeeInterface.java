package hrms.ZMGFH.Interface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import hrms.ZMGFH.Loading.Loading;
import hrms.ZMGFH.Objection.Employee;
import hrms.ZMGFH.Service.HRService;

public class EmployeeInterface extends JPanel {
	private MainFrame panelFrame;// 主窗体

	private JButton updateButton;// 更新信息按钮
	private JButton backButton;// 返回按钮
	private JButton detailButton;// 奖金扣款按钮

    public EmployeeInterface(MainFrame panelFrame) {
		this.panelFrame = panelFrame;
		this.panelFrame.setSize(600,540);
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension Size = tool.getScreenSize();//获取屏幕大小
		setLocation((Size.width - 600) / 2, (Size.height - 540) / 2);//主窗体在屏幕中间显示
		init();
		addListener();
	}

	private void init() {

		updateButton = new JButton("更新信息");
		backButton = new JButton("返回");
		detailButton = new JButton("奖金扣款");
		Employee employee = HRService.FindEmp(Loading.user.getId(),true);

		employee = HRService.FindEmp(Loading.user.getId(),employee.getSign().equals("辞退"));

		String[] valueString = Objects.requireNonNull(employee).to_string();
		float wagefloat = Integer.parseInt(valueString[14]) + Integer.parseInt(valueString[15]) * Float.parseFloat(valueString[18]);

		JLabel name = new JLabel("姓名：" + employee.getName());
		JLabel age = new JLabel("年龄：" + employee.getAge());
		JLabel gender = new JLabel("性别：" + employee.getGender());
		JLabel identity = new JLabel("身份证号：" + employee.getIdentity());
		JLabel education = new JLabel("教育水平：" + employee.getEducation());
		JLabel phone = new JLabel("电话号：" +employee.getPhone_number());
		JLabel email = new JLabel("电子邮件：" + employee.getE_mail());
		JLabel card = new JLabel("银行卡号：" + employee.getCard_number());
		JLabel marriage = new JLabel("婚姻状况：" + employee.getMarriage_status());
		JLabel department = new JLabel("部门：" + employee.getDepartment());
		JLabel post = new JLabel("职位：" + employee.getPost());
		JLabel title = new JLabel("职称：" + employee.getTitle());
		JLabel entry_time = new JLabel("入公司时间：" + employee.getEntry_date());
		JLabel wage = new JLabel("工资：" + wagefloat);

		name.setBounds(150,30,200,20);
		age.setBounds(150,60,200,20);
		gender.setBounds(150,90,200,20);
		identity.setBounds(150,120,200,20);
		education.setBounds(150,150,200,20);
		phone.setBounds(150,180,200,20);
		email.setBounds(150,210,200,20);
		card.setBounds(150,240,200,20);
		marriage.setBounds(150,270,200,20);
		department.setBounds(150,300,200,20);
		post.setBounds(150,330,200,20);
		title.setBounds(150,360,200,20);
		entry_time.setBounds(150,390,200,20);
		wage.setBounds(150,420,200,20);

		setLayout(new BorderLayout());// 采用边界布局

		JPanel center = new JPanel();

		center.setLayout(null);
		center.add(name);
		center.add(age);
		center.add(gender);
		center.add(identity);
		center.add(education);
		center.add(phone);
		center.add(email);
		center.add(card);
		center.add(marriage);
		center.add(department);
		center.add(post);
		center.add(title);
		center.add(entry_time);
		center.add(wage);
		JScrollPane scrollPane = new JScrollPane(center);
		add(scrollPane, BorderLayout.CENTER);

		JPanel bottom = new JPanel();// 底部面板
		bottom.add(updateButton);
		bottom.add(detailButton);
		bottom.add(backButton);
		add(bottom, BorderLayout.SOUTH);

	}

	private void addListener() {
		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UpdateMessageInterface updateMessageInterface = new UpdateMessageInterface(panelFrame);
				updateMessageInterface.setVisible(true);
			}
		});

		detailButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				WageDetailInterface wageDetailInterface = new WageDetailInterface(panelFrame);
				wageDetailInterface.setVisible(true);
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

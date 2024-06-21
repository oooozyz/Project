package hrms.ZMGFH.Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.*;

import hrms.ZMGFH.Judging.BankJudging;
import hrms.ZMGFH.Judging.EmailJudging;
import hrms.ZMGFH.Judging.IdentityJudging;
import hrms.ZMGFH.Judging.PhoneJudging;
import hrms.ZMGFH.Loading.Loading;
import hrms.ZMGFH.Objection.Employee;
import hrms.ZMGFH.Service.HRService;

public class UpdateMessageInterface extends JDialog {
	private JButton updateButton;// 更新按钮
	private JButton cancelButton;// 取消按钮

	private JTextField nameField;// 姓名文本域
	private JTextField identityField;// 身份证号文本域
	private JTextField phone_numberField;// 电话号文本域
	private JTextField e_mailField;// 电子邮件文本域
	private JTextField card_numberField;// 银行卡号文本域

	private JComboBox<Integer> ageBox;// 年龄的下拉列表
	private JComboBox<String> genderBox, educationBox, marital_statusBox;// 性别、教育水平、婚姻状况的下拉列表
	private DefaultComboBoxModel<Integer> ageBoxModel;// 年龄下拉列表所使用的数据模型
	private DefaultComboBoxModel<String> genderBoxModel, educationBoxModel, marital_statusBoxModel;// 性别、教育水平、婚姻状况所使用的数据模型

	private IdentityJudging identityJudging = new IdentityJudging();
	private EmailJudging emailJudging = new EmailJudging();
	private PhoneJudging phoneJudging = new PhoneJudging();
	private BankJudging bankJudging = new BankJudging();

	public UpdateMessageInterface(Frame f) {
		super(f, "信息更新", true);
		setSize(600, 180);
		setLocation(f.getX() + (f.getWidth() - 600) / 2, f.getY() + (f.getHeight() - 180) / 2);// 在窗体的中间显示
		init();
		Comboinit();
		addListener();
	}

	private void init() {
		updateButton = new JButton("更新");
		cancelButton = new JButton("返回");

		JPanel bottom = new JPanel();// 底部面板
		bottom.add(updateButton);
		bottom.add(cancelButton);
		add(bottom, BorderLayout.SOUTH);
	}

	private void addListener() {
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText().trim();
				String identity = identityField.getText().trim();
				String phone_number = phone_numberField.getText().trim();
				String e_mail = e_mailField.getText().trim();
				String card_number = card_numberField.getText().trim();
				int age = (int) ageBox.getSelectedItem();
				String gender = (String) genderBox.getSelectedItem();
				String education = (String) educationBox.getSelectedItem();
				String marital_status = (String) marital_statusBox.getSelectedItem();
				Map<String, String> map = new HashMap<String, String>();
				Employee employee = HRService.FindEmp(Loading.user.getId(),true);
				employee = HRService.FindEmp(Loading.user.getId(),employee.getSign().equals("辞退"));
				if (!(name == null || "".equals(name))&&!Objects.equals(name, employee.getName())) {
					map.put("name", name);
				}
				if (age != employee.getAge()) {
					map.put("age", Integer.toString(age));
				}
				if (!Objects.equals(gender, employee.getGender())) {
					map.put("gender", gender);
				}
				if (!Objects.equals(education, employee.getEducation())) {
					map.put("education", education);
				}
				if (!Objects.equals(marital_status, employee.getMarriage_status())) {
					map.put("marital_status", marital_status);
				}
				if (!(identity == null || "".equals(identity))) {
					if(!identityJudging.CheckIdentity(identity)) {
						JOptionPane.showMessageDialog(null, "身份证号不合法！");
						return;// 中断方法
					}
					if(!Objects.equals(identity, employee.getIdentity()))
						map.put("identity", identity);
				}

				if (!(phone_number == null || "".equals(phone_number))) {
					if(!phoneJudging.CheckPhone(phone_number)) {
						JOptionPane.showMessageDialog(null, "电话号不合法！");
						return;// 中断方法
					}
					if(!Objects.equals(phone_number, employee.getPhone_number()))
						map.put("phone_number", phone_number);
				}
				if (!(e_mail == null || "".equals(e_mail))) {
					if(!emailJudging.CheckEmail(e_mail)) {
						JOptionPane.showMessageDialog(null, "邮箱不合法！");
						return;// 中断方法
					}
					if(!Objects.equals(e_mail, employee.getE_mail()))
						map.put("e_mail", e_mail);
				}
				if (!(card_number == null || "".equals(card_number))) {
					if(!bankJudging.CheckBank(card_number)) {
						JOptionPane.showMessageDialog(null, "银行卡号不合法！");
						return;// 中断方法
					}
					if(!Objects.equals(card_number, employee.getCard_number()))
						map.put("card_number", card_number);
				}
				if (!map.isEmpty()) {
					if(HRService.UpdateMessage_p(map, Loading.user.getId())){
						JOptionPane.showMessageDialog(null, "修改成功！");
					}
					else{
						JOptionPane.showMessageDialog(null, "修改失败！");
						return;
					}
				}
				UpdateMessageInterface.this.dispose();
			}
		});

		cancelButton.addActionListener(e -> UpdateMessageInterface.this.dispose());
	}

	private void Comboinit() {
		ageBoxModel = new DefaultComboBoxModel<>();
		genderBoxModel = new DefaultComboBoxModel<>();
		educationBoxModel = new DefaultComboBoxModel<>();
		marital_statusBoxModel = new DefaultComboBoxModel<>();

		for (int i = 20; i <= 60; i++) {
			ageBoxModel.addElement(i);
		}

		genderBoxModel.addElement("男");
		genderBoxModel.addElement("女");

		educationBoxModel.addElement("大专");
		educationBoxModel.addElement("本科");
		educationBoxModel.addElement("研究生");
		educationBoxModel.addElement("高中及以下");

		marital_statusBoxModel.addElement("已婚");
		marital_statusBoxModel.addElement("未婚");

		ageBox = new JComboBox<>(ageBoxModel);
		genderBox = new JComboBox<>(genderBoxModel);
		educationBox = new JComboBox<>(educationBoxModel);
		marital_statusBox = new JComboBox<>(marital_statusBoxModel);

		nameField = new JTextField(8);
		identityField = new JTextField(15);
		phone_numberField = new JTextField(11);
		e_mailField = new JTextField(15);
		card_numberField = new JTextField(15);

		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension Size = tool.getScreenSize();// 获取屏幕大小

		JLabel lineLabel1 = new JLabel();
		lineLabel1.setPreferredSize(new Dimension(Size.width, 0)); // 足够宽以触发换行，但高度为0
		JLabel lineLabel2 = new JLabel();
		lineLabel2.setPreferredSize(new Dimension(Size.width, 0)); // 足够宽以触发换行，但高度为0
		JLabel lineLabel3 = new JLabel();
		lineLabel3.setPreferredSize(new Dimension(Size.width, 0)); // 足够宽以触发换行，但高度为0

		JPanel center = new JPanel();
		center.setLayout(new FlowLayout());
		center.add(new JLabel("姓名：", SwingConstants.RIGHT));
		center.add(nameField);
		center.add(new JLabel("年龄：", SwingConstants.RIGHT));
		center.add(ageBox);
		center.add(new JLabel("性别：", SwingConstants.RIGHT));
		center.add(genderBox);
		center.add(new JLabel("学历：", SwingConstants.RIGHT));
		center.add(educationBox);
		center.add(new JLabel("婚姻状况：", SwingConstants.RIGHT));
		center.add(marital_statusBox);
		center.add(lineLabel2);
		center.add(new JLabel("身份证号：", SwingConstants.RIGHT));
		center.add(identityField);
		center.add(new JLabel("电话号：", SwingConstants.RIGHT));
		center.add(phone_numberField);
		center.add(lineLabel3);
		center.add(new JLabel("邮箱：", SwingConstants.RIGHT));
		center.add(e_mailField);
		center.add(new JLabel("银行卡号：", SwingConstants.RIGHT));
		center.add(card_numberField);
		add(center, BorderLayout.CENTER);

	}
}

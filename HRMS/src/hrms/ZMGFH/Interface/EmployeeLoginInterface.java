package hrms.ZMGFH.Interface;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import hrms.ZMGFH.Service.HRService;

public class EmployeeLoginInterface extends JDialog {
	private JTextField usernameField = null;// 用户名输入框
	private JPasswordField passwordField = null;// 密码输入框
	private JButton loginButton = null;// 登录按钮
	private JButton cancelButton = null;// 取消按钮

	public EmployeeLoginInterface(Frame f) {
		super(f, "管理员登录", true);
		setSize(300, 150);// 设置宽高
		setLocation(f.getX() + (f.getWidth() - 300) / 2, f.getY() + (f.getHeight() - 150) / 2);// 在窗体的中间显示
		init();// 初始化
		addListener();// 添加各种按钮回车的监听
	}

	private void init() {
		JLabel usernameLabel = new JLabel("用户名：", JLabel.CENTER);
		JLabel passwordLabel = new JLabel("密码：", JLabel.CENTER);
		usernameField = new JTextField();
		passwordField = new JPasswordField();
		loginButton = new JButton("登录");
		cancelButton = new JButton("取消");

		Container c = getContentPane();
		c.setLayout(new GridLayout(3, 2));
		c.add(usernameLabel);
		c.add(usernameField);
		c.add(passwordLabel);
		c.add(passwordField);
		c.add(loginButton);
		c.add(cancelButton);

	}

	private void addListener() {

		cancelButton.addActionListener(new ActionListener() {// 取消按钮的事件监听

			@Override
			public void actionPerformed(ActionEvent e) {
				EmployeeLoginInterface.this.dispose();// 销毁登录对话框
			}

		});
		loginButton.addActionListener(new ActionListener() {// 确定按钮的事件监听

			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText().trim();// 获取用户名
				String password = new String(passwordField.getPassword());// 获取密码

				boolean judge = HRService.userLogin(username, password);
				if (judge) {
					EmployeeLoginInterface.this.dispose();// 销毁登录对话框
				} else {
					JOptionPane.showMessageDialog(EmployeeLoginInterface.this, "用户名或密码有误！");
				}

			}
		});
		usernameField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				passwordField.grabFocus();// 转到密码输入框

			}
		});

		passwordField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loginButton.doClick();// 确定按钮

			}
		});

	}

}

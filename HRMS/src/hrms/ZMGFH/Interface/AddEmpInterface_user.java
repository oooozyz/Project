package hrms.ZMGFH.Interface;

import hrms.ZMGFH.Loading.Loading;
import hrms.ZMGFH.Service.CameraService;
import hrms.ZMGFH.Service.HRService;
import hrms.ZMGFH.Service.ImageService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEmpInterface_user extends JPanel {
    private MainFrame frame;

    private JTextField username =null;
    private JPasswordField password1 =null;
    private JPasswordField password2 =null;

    private JButton finsh;
    private JButton back;
    public AddEmpInterface_user(MainFrame frame) {
        this.frame = frame;
        this.frame.setSize(400,300);

        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension Size = tool.getScreenSize();//获取屏幕大小
        setLocation((Size.width - 400) / 2, (Size.height - 300) / 2);//主窗体在屏幕中间显示
        init();
        Cominit();
        addListener();
    }
    private void init() {
        username = new JTextField();
        password1 = new JPasswordField();
        password2 = new JPasswordField();
        finsh = new JButton("完成");
        back = new JButton("返回");

        setLayout(new BorderLayout());

        JPanel bottom = new JPanel();
        bottom.add(finsh);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);
    }

    private void Cominit(){
        JLabel users = new JLabel("请输入用户名：");
        JLabel passwords1 = new JLabel("请输入密码：");
        JLabel passwords2 = new JLabel("再次确认密码：");

        users.setBounds(50,50,150,50);
        username.setBounds(200,50,150,30);
        passwords1.setBounds(50,100,150,50);
        password1.setBounds(200,100,150,30);
        passwords2.setBounds(50,150,150,50);
        password2.setBounds(200,150,150,30);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.add(users);
        panel.add(username);
        panel.add(passwords1);
        panel.add(password1);
        panel.add(passwords2);
        panel.add(password2);
        add(panel, BorderLayout.CENTER);

    }
    private void addListener() {
        finsh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = username.getText().trim();
                String pass1 = new String(password1.getPassword());
                String pass2 = new String(password2.getPassword());
                if(!pass1.equals(pass2)){
                    JOptionPane.showMessageDialog(null, "两次密码输入值不同！");
                    return;
                }
                if(HRService.AddEmp(user,pass1)){
                    JOptionPane.showMessageDialog(null, "添加成功！");
                    ImageService.SaveFace(Loading.image,Loading.employee.getCode());
                    Loading.FACE_FEATURE_MAP.put(Loading.employee.getCode(),Loading.FACE_FEATURE_MAP.put(Loading.employee.getCode(),Loading.faceFeature));
                    back.doClick();
                }else{
                    JOptionPane.showMessageDialog(null, "添加失败！");
                    return;
                }

            }
        });

        back.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               CameraService.releaseCamera();
               Loading.Delete_Emp();
               frame.setPanel(new EmpManagementInterface(frame));
           }
        });
    }
}

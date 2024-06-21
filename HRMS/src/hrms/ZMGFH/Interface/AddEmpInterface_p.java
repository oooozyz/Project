package hrms.ZMGFH.Interface;

import com.arcsoft.face.FaceFeature;
import hrms.ZMGFH.Judging.BankJudging;
import hrms.ZMGFH.Judging.EmailJudging;
import hrms.ZMGFH.Judging.IdentityJudging;
import hrms.ZMGFH.Judging.PhoneJudging;
import hrms.ZMGFH.Loading.Loading;
import hrms.ZMGFH.Objection.Employee;
import hrms.ZMGFH.Service.CameraService;
import hrms.ZMGFH.Service.FaceService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.UUID;

public class AddEmpInterface_p extends JPanel{
    private MainFrame mainFrame;

    private JLabel message;
    private JButton Next;
    private JButton Back;

    private JTextField nameField;// 姓名文本域
    private JTextField identityField;// 身份证号文本域
    private JTextField phone_numberField;// 电话号文本域
    private JTextField e_mailField;// 电子邮件文本域
    private JTextField card_numberField;// 银行卡号文本域

    private JComboBox<Integer> ageBox;// 年龄的下拉列表
    private JComboBox<String> genderBox, educationBox, marital_statusBox;// 性别、教育水平、婚姻状况的下拉列表
    private DefaultComboBoxModel<Integer> ageBoxModel;// 年龄下拉列表所使用的数据模型
    private DefaultComboBoxModel<String> genderBoxModel, educationBoxModel, marital_statusBoxModel;// 性别、教育水平、婚姻状况所使用的数据模型

    private JPanel center;

    private IdentityJudging identityJudging = new IdentityJudging();
    private EmailJudging emailJudging = new EmailJudging();
    private PhoneJudging phoneJudging = new PhoneJudging();
    private BankJudging bankJudging = new BankJudging();

    public AddEmpInterface_p(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        this.mainFrame.setSize(700,432);
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension Size = tool.getScreenSize();//获取屏幕大小
        setLocation((Size.width - 700) / 2, (Size.height - 432) / 2);//主窗体在屏幕中间显示
        init();
        addListener();
    }
    private void init(){
        mainFrame.setTitle("添加员工");
        Next = new JButton("下一步");
        Back = new JButton("返回");

        setLayout(new BorderLayout());

        JPanel bottom = new JPanel();
        bottom.add(Next);
        bottom.add(Back);
        add(bottom, BorderLayout.SOUTH);

        center = new JPanel();
        center.setLayout(null);

        JPanel blackpanel = new JPanel();
        blackpanel.setBackground(Color.black);
        blackpanel.setBounds(40, 40, 320, 240);
        center.add(blackpanel);
        Comboinit();

        Thread cameraThread = new Thread() {
            public void run() {
                if (CameraService.StartCamera()) {// 如果摄像头成功开启
                    // 获取摄像头画面面板
                    JPanel cameraPanel = CameraService.getCameraPanel();
                    // 设置面板的坐标和宽高
                    cameraPanel.setBounds(40, 40, 320, 240);
                    center.add(cameraPanel);// 放到中部面板
                } else {
                    // 弹出提示
                    JOptionPane.showMessageDialog(mainFrame, "未检测到摄像头！");
                    Back.doClick();// 出发返回按钮的点击事件
                }
            }
        };
        cameraThread.start();// 开启线程
    }
    private void addListener(){
        Next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String identity = identityField.getText().trim();
                String phone_number = phone_numberField.getText().trim();
                String e_mail = e_mailField.getText().trim();
                String card_number = card_numberField.getText().trim();
                Integer age = (int) ageBox.getSelectedItem();
                String gender = (String) genderBox.getSelectedItem();
                String education = (String) educationBox.getSelectedItem();
                String marital_status = (String) marital_statusBox.getSelectedItem();
                if (name == null || "".equals(name)) {// 如果是空内容
                    JOptionPane.showMessageDialog(null, "名字不能为空！");
                    return;// 中断方法
                }

                if(identity == null || "".equals(identity)) {
                    JOptionPane.showMessageDialog(null, "身份证号不能为空！");
                    return;// 中断方法
                }
                if(!identityJudging.CheckIdentity(identity)) {
                    JOptionPane.showMessageDialog(null, "身份证号不合法！");
                    return;// 中断方法
                }
                if(phone_number == null || "".equals(phone_number)) {
                    JOptionPane.showMessageDialog(null, "电话号不能为空！");
                    return;// 中断方法
                }
                if(!phoneJudging.CheckPhone(phone_number)) {
                    JOptionPane.showMessageDialog(null, "电话号不合法！");
                    return;// 中断方法
                }
                if(e_mail == null || "".equals(e_mail)) {
                    JOptionPane.showMessageDialog(null, "邮箱不能为空！");
                    return;// 中断方法
                }
                if(!emailJudging.CheckEmail(e_mail)) {
                    JOptionPane.showMessageDialog(null, "邮箱不合法！");
                    return;// 中断方法
                }
                if(card_number == null || "".equals(card_number)) {
                    JOptionPane.showMessageDialog(null, "银行卡号不能为空！");
                    return;// 中断方法
                }
                if(!bankJudging.CheckBank(card_number)) {
                    JOptionPane.showMessageDialog(null, "银行卡号不合法！");
                    return;// 中断方法
                }

                if(!CameraService.cameraIsOpen()){
                    JOptionPane.showMessageDialog(null, "摄像头未开启！");
                    return;// 中断方法
                }

                Loading.image = CameraService.getCameraFrame();
                Loading.faceFeature = FaceService.getFaceFeature(Loading.image);
                if(Loading.faceFeature == null){
                    JOptionPane.showMessageDialog(null, "未检测人脸信息！");
                    return;// 中断方法
                }
                Loading.employee = new Employee();
                String code = UUID.randomUUID().toString();
                Loading.employee.setCode(code);
                Loading.employee.setId(null);
                Loading.employee.setName(name);
                Loading.employee.setIdentity(identity);
                Loading.employee.setAge(age);
                Loading.employee.setGender(gender);
                Loading.employee.setEducation(education);
                Loading.employee.setCard_number(card_number);
                Loading.employee.setPhone_number(phone_number);
                Loading.employee.setE_mail(e_mail);
                Loading.employee.setMarriage_status(marital_status);
                AddEmpInterface_a addEmpInterface_a = new AddEmpInterface_a(mainFrame);
                mainFrame.setPanel(addEmpInterface_a);
            }
        });


        Back.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               CameraService.releaseCamera();
               Loading.Delete_Emp();
               mainFrame.setPanel(new EmpManagementInterface(mainFrame));
           }
        });
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

        JLabel name = new JLabel("姓名：");
        JLabel age = new JLabel("年龄：");
        JLabel gender = new JLabel("性别：");
        JLabel education = new JLabel("教育水平：");
        JLabel marital_status = new JLabel("婚姻状况：");
        JLabel identity = new JLabel("身份证号：");
        JLabel phone = new JLabel("电话号：");
        JLabel email = new JLabel("电子邮件：");
        JLabel card_number = new JLabel("银行卡号：");

        name.setBounds(400,30,80,50);
        age.setBounds(400,55,80,50);
        gender.setBounds(400,80,80,50);
        education.setBounds(400,105,80,50);
        marital_status.setBounds(400,130,80,50);
        identity.setBounds(400,155,80,50);
        phone.setBounds(400,180,80,50);
        email.setBounds(400,205,80,50);
        card_number.setBounds(400,230,80,50);

        nameField.setBounds(480,45,130,20);
        ageBox.setBounds(480,70,130,20);
        genderBox.setBounds(480,95,130,20);
        educationBox.setBounds(480,120,130,20);
        marital_statusBox.setBounds(480,145,130,20);
        identityField.setBounds(480,170,130,20);
        phone_numberField.setBounds(480,195,130,20);
        e_mailField.setBounds(480,220,130,20);
        card_numberField.setBounds(480,245,130,20);

        center.add(name);
        center.add(nameField);
        center.add(age);
        center.add(ageBox);
        center.add(gender);
        center.add(genderBox);
        center.add(education);
        center.add(educationBox);
        center.add(marital_status);
        center.add(marital_statusBox);
        center.add(identity);
        center.add(identityField);
        center.add(phone);
        center.add(phone_numberField);
        center.add(email);
        center.add(e_mailField);
        center.add(card_number);
        center.add(card_numberField);
        add(center, BorderLayout.CENTER);

    }
}

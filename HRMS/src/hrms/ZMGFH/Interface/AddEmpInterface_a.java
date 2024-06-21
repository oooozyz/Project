package hrms.ZMGFH.Interface;

import hrms.ZMGFH.Loading.Loading;
import hrms.ZMGFH.Service.CameraService;
import hrms.ZMGFH.Service.HRService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

public class AddEmpInterface_a extends JPanel{
    private MainFrame frame;

    private JButton Next;
    private JButton Back;

    private JComboBox<String> DepartmentBox,PostBox,TitleBox;//部门、岗位、职称的下拉列表

    private DefaultComboBoxModel<String> DepartmentBoxModel,PostBoxModel,TitleBoxModel;//部门、岗位、职称的下拉列表所用的数据模型


    public AddEmpInterface_a(MainFrame frame) {
        this.frame = frame;
        this.frame.setSize(350,132);
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension Size = tool.getScreenSize();//获取屏幕大小
        setLocation((Size.width - 350) / 2, (Size.height - 132) / 2);//主窗体在屏幕中间显示
        init();
        Comboinit();
        addListener();
    }
    private void init() {
        Next = new JButton("下一步");
        Back = new JButton("返回");

        setLayout(new BorderLayout());

        JPanel bottom = new JPanel();
        bottom.add(Next);
        bottom.add(Back);
        add(bottom, BorderLayout.SOUTH);
    }
    private void addListener() {
        Next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String dept = DepartmentBox.getSelectedItem().toString();
                String post = PostBox.getSelectedItem().toString();
                String title = TitleBox.getSelectedItem().toString();
                if (dept.equals("---")) {// 如果是空内容
                    JOptionPane.showMessageDialog(null, "部门不能为空！");
                    return;// 中断方法
                }
                if (post.equals("---")) {// 如果是空内容
                    JOptionPane.showMessageDialog(null, "职位不能为空！");
                    return;// 中断方法
                }
                if (title.equals("---")) {// 如果是空内容
                    JOptionPane.showMessageDialog(null, "职称不能为空！");
                    return;// 中断方法
                }
                Loading.employee.setDepartment(dept);
                Loading.employee.setPost(post);
                Loading.employee.setTitle(title);

                LocalDate localDate = LocalDate.now();
                java.sql.Date date = java.sql.Date.valueOf(localDate);

                Loading.employee.setEntry_date(date);
                Loading.employee.setSign("在职");
                Loading.employee.setWage_change(0);
                AddEmpInterface_user a = new AddEmpInterface_user(frame);
                frame.setPanel(a);
            }
        });

        Back.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               CameraService.releaseCamera();
               Loading.Delete_Emp();
               frame.setPanel(new EmpManagementInterface(frame));
           }
        });

        DepartmentBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                post_init();
                PostBoxModel.setSelectedItem("---");
                Title_init();
                TitleBoxModel.setSelectedItem("---");
            }
        });

    }
    private void Comboinit(){
        DepartmentBoxModel = new DefaultComboBoxModel<>();
        PostBoxModel = new DefaultComboBoxModel<>();
        TitleBoxModel = new DefaultComboBoxModel<>();

        DepartmentBoxModel.addElement("---");
        DepartmentBoxModel.addElement("技术部");
        DepartmentBoxModel.addElement("人事部");
        DepartmentBoxModel.addElement("后勤部");
        DepartmentBoxModel.addElement("法务部");
        PostBoxModel.addElement("---");
        TitleBoxModel.addElement("---");

        DepartmentBoxModel.setSelectedItem("---");
        PostBoxModel.setSelectedItem("---");
        TitleBoxModel.setSelectedItem("---");

        DepartmentBox = new JComboBox<>(DepartmentBoxModel);
        PostBox = new JComboBox<>(PostBoxModel);
        TitleBox = new JComboBox<>(TitleBoxModel);

        JLabel department = new JLabel("部门：");
        JLabel post = new JLabel("职位：");
        JLabel title = new JLabel("职称：");

        department.setBounds(100,100,50,50);
        DepartmentBox.setBounds(150,100,80,50);
        post.setBounds(200,100,50,50);
        PostBox.setBounds(250,100,80,50);
        title.setBounds(300,100,50,50);
        TitleBox.setBounds(350,100,80,50);

        JPanel middle = new JPanel();
        middle.add(department);
        middle.add(DepartmentBox);
        middle.add(post);
        middle.add(PostBox);
        middle.add(title);
        middle.add(TitleBox);
        add(middle,BorderLayout.CENTER);
    }

    private void post_init(){
        String dept = DepartmentBox.getSelectedItem().toString();
        ArrayList<String> arr = HRService.Post(dept);
        PostBoxModel.removeAllElements();
        PostBoxModel.addElement("---");
        for(String arr1 : arr){
            PostBoxModel.addElement(arr1);
        }
    }

    private void Title_init(){
        TitleBoxModel.removeAllElements();
        TitleBoxModel.addElement("---");
        for(int i=1;i<=8;i++){
            TitleBoxModel.addElement("k"+i);
        }
    }
}

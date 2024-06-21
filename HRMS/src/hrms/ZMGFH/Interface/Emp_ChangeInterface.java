package hrms.ZMGFH.Interface;

import hrms.ZMGFH.Objection.Employee;
import hrms.ZMGFH.Service.HRService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Emp_ChangeInterface extends JDialog {
    private Employee emp = null;
    private JButton Update;
    private JButton Back;
    private JComboBox<String> NameBox,SignBox,DepartmentBox,PostBox,TitleBox;//姓名、状态、部门、岗位、职称的下拉列表

    private DefaultComboBoxModel<String> NameBoxModel,SignBoxModel,DepartmentBoxModel,PostBoxModel,TitleBoxModel;//姓名、状态、部门、岗位、职称的下拉列表所用的数据模型

    private Set<Employee> AllEmp;
    public Emp_ChangeInterface(Frame frame) {
        super(frame, "员工变动",true);
        setSize(700,132);
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension Size = tool.getScreenSize();//获取屏幕大小
        setLocation((Size.width - 700) / 2, (Size.height - 132) / 2);//主窗体在屏幕中间显示
        init();
        addListener();
    }

    private void init(){
        Update = new JButton("更新");
        Back = new JButton("返回");

        JPanel bottom = new JPanel();
        bottom.add(Update);
        bottom.add(Back);
        add(bottom,BorderLayout.SOUTH);

        NameBoxModel = new DefaultComboBoxModel<>();
        SignBoxModel = new DefaultComboBoxModel<>();
        DepartmentBoxModel = new DefaultComboBoxModel<>();
        PostBoxModel = new DefaultComboBoxModel<>();
        TitleBoxModel = new DefaultComboBoxModel<>();

        AllEmp = HRService.GetAllEmployee();

        NameBoxModel.addElement("---");
        SignBoxModel.addElement("---");
        DepartmentBoxModel.addElement("---");
        PostBoxModel.addElement("---");
        TitleBoxModel.addElement("---");
        for(Employee empl : AllEmp){
            NameBoxModel.addElement(empl.getName());
        }

        NameBoxModel.setSelectedItem("---");
        SignBoxModel.setSelectedItem("---");
        DepartmentBoxModel.setSelectedItem("---");
        PostBoxModel.setSelectedItem("---");
        TitleBoxModel.setSelectedItem("---");

        NameBox = new JComboBox<>(NameBoxModel);
        SignBox = new JComboBox<>(SignBoxModel);
        DepartmentBox = new JComboBox<>(DepartmentBoxModel);
        PostBox = new JComboBox<>(PostBoxModel);
        TitleBox = new JComboBox<>(TitleBoxModel);

        JPanel top = new JPanel();
        top.setLayout(new FlowLayout());
        top.add(new JLabel("姓名："));
        top.add(NameBox);
        top.add(new JLabel("状态："));
        top.add(SignBox);
        top.add(new JLabel("部门："));
        top.add(DepartmentBox);
        top.add(new JLabel("职位："));
        top.add(PostBox);
        top.add(new JLabel("职称："));
        top.add(TitleBox);
        add(top,BorderLayout.NORTH);
    }

    private void addListener(){
        Update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String,String> map = new HashMap<>();
                map.put("sign",SignBox.getSelectedItem().toString());
                map.put("department",DepartmentBox.getSelectedItem().toString());
                map.put("post",PostBox.getSelectedItem().toString());
                map.put("title",TitleBox.getSelectedItem().toString());
                HRService.UpdateMessage_a(map,emp.getId());
                Emp_ChangeInterface.this.dispose();
            }
        });

        Back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Emp_ChangeInterface.this.dispose();
            }
        });

        ActionListener DepartmentBoxListener = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String Department = DepartmentBox.getSelectedItem().toString();
                UpdateMessage_Post(Department);
            }
        };
        DepartmentBox.addActionListener(DepartmentBoxListener);

        ActionListener SignBoxListener = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                DepartmentBox.removeActionListener(DepartmentBoxListener);
                UpdateMessage_Sign();
                DepartmentBox.addActionListener(DepartmentBoxListener);
            }
        };
        SignBox.addActionListener(SignBoxListener);

        ActionListener NameBoxListener = new ActionListener() {
          public void actionPerformed(ActionEvent e){
              String name = NameBox.getSelectedItem().toString();
              if(name.equals("---")){
                  return;
              }
              for(Employee employee : AllEmp){
                  if(employee.getName().equals(name)){
                      emp = employee;
                      break;
                  }
              }
              DepartmentBox.removeActionListener(DepartmentBoxListener);
              SignBox.removeActionListener(SignBoxListener);
              UpdateMessage_All(emp.getSign());
              SignBox.addActionListener(SignBoxListener);
              DepartmentBox.addActionListener(DepartmentBoxListener);
          }
        };
        NameBox.addActionListener(NameBoxListener);
    }

    private void Sign_init(){
        SignBoxModel.removeAllElements();
        SignBoxModel.addElement("---");
        SignBoxModel.addElement("在职");
        SignBoxModel.addElement("辞退");
        SignBoxModel.addElement("休假");
        SignBoxModel.addElement("临时员工");
        SignBoxModel.addElement("病假");
    }

    private void Department_init(){
        DepartmentBoxModel.removeAllElements();
        DepartmentBoxModel.addElement("---");
        DepartmentBoxModel.addElement("技术部");
        DepartmentBoxModel.addElement("人事部");
        DepartmentBoxModel.addElement("后勤部");
        DepartmentBoxModel.addElement("法务部");
    }

    private void Title_init(){
        TitleBoxModel.removeAllElements();
        TitleBoxModel.addElement("---");
        for(int i=1;i<=8;i++){
            TitleBoxModel.addElement("k"+i);
        }
        TitleBoxModel.setSelectedItem("---");
    }

    private void UpdateMessage_All(String sign){

        Sign_init();
        SignBoxModel.setSelectedItem(sign);
        if(!sign.equals("辞退"))
            Department_init();
        DepartmentBoxModel.setSelectedItem(emp.getDepartment());
        if(!emp.getPost().equals("---")) {
            UpdateMessage_Post(emp.getDepartment());
        }
        PostBoxModel.setSelectedItem(emp.getPost());
        if(!sign.equals("辞退")) {
            Title_init();
        }
        TitleBoxModel.setSelectedItem(emp.getTitle());
    }

    private void UpdateMessage_Sign(){
        String Sign = SignBox.getSelectedItem().toString();
        if(Sign.equals("辞退")){
            DepartmentBoxModel.removeAllElements();
            DepartmentBoxModel.addElement("---");
            DepartmentBoxModel.setSelectedItem("---");
            PostBoxModel.removeAllElements();
            PostBoxModel.addElement("---");
            PostBoxModel.setSelectedItem("---");
            TitleBoxModel.removeAllElements();
            TitleBoxModel.addElement("---");
            TitleBoxModel.setSelectedItem("---");
        }
        else{
            UpdateMessage_All(Sign);

        }
    }

    private void UpdateMessage_Post(String dep){

        ArrayList<String> arrayList = HRService.Post(dep);
        PostBoxModel.removeAllElements();
        PostBoxModel.addElement("---");
        for(String arr : arrayList){
            if(arr!=null)
                PostBoxModel.addElement(arr);
        }
    }

}

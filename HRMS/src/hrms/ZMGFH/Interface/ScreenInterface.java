package hrms.ZMGFH.Interface;

import hrms.ZMGFH.Loading.Loading;
import hrms.ZMGFH.Objection.Employee;
import hrms.ZMGFH.Service.HRService;
import hrms.ZMGFH.Tools.TimeTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class ScreenInterface extends JPanel {
    private MainFrame frame;
    JButton Filtering;
    JButton Back;

    Integer[] now;

    private JComboBox<String> SignBox;
    private JComboBox<String> GenderBox;
    private JComboBox<Integer> AgeFromBox;
    private JComboBox<Integer> AgeToBox;
    private JComboBox<String> EducationBox;
    private JComboBox<String> DepartmentBox;
    private JComboBox<String> PostBox;
    private JComboBox<String> TitleBox;
    private JComboBox<String> MariousBox;
    private JComboBox<Integer> YearFromBox;
    private JComboBox<Integer> YearToBox;
    private JComboBox<Integer> MonthFromBox;
    private JComboBox<Integer> MonthToBox;
    private JComboBox<Integer> DayFromBox;
    private JComboBox<Integer> DayToBox;
    private JComboBox<Integer> WageBox;

    private DefaultComboBoxModel<String> SignBoxModel;
    private DefaultComboBoxModel<String> GenderBoxModel;
    private DefaultComboBoxModel<Integer> AgeFromBoxModel;
    private DefaultComboBoxModel<Integer> AgeToBoxModel;
    private DefaultComboBoxModel<String> EducationBoxModel;
    private DefaultComboBoxModel<String> DepartmentBoxModel;
    private DefaultComboBoxModel<String> PostBoxModel;
    private DefaultComboBoxModel<String> TitleBoxModel;
    private DefaultComboBoxModel<String> MariousBoxModel;
    private DefaultComboBoxModel<Integer> YearFromBoxModel;
    private DefaultComboBoxModel<Integer> YearToBoxModel;
    private DefaultComboBoxModel<Integer> MonthFromBoxModel;
    private DefaultComboBoxModel<Integer> MonthToBoxModel;
    private DefaultComboBoxModel<Integer> DayFromBoxModel;
    private DefaultComboBoxModel<Integer> DayToBoxModel;
    private DefaultComboBoxModel<Integer> WageBoxModel;

    private Set<Employee> employees;

    public ScreenInterface(MainFrame frame) {
        this.frame = frame;
        this.frame.setSize(400,430);
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension Size = tool.getScreenSize();//获取屏幕大小
        setLocation((Size.width - 400) / 2, (Size.height - 430) / 2);//主窗体在屏幕中间显示
        init();
        Comboinit();
        addListener();
    }
    private void init() {
        frame.setTitle("筛选员工");
        Filtering = new JButton("筛选");
        Back = new JButton("返回");

        setLayout(new BorderLayout());

        JPanel bottom = new JPanel();
        bottom.add(Filtering);
        bottom.add(Back);
        add(bottom, BorderLayout.SOUTH);
    }
    private void addListener() {
        Filtering.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               String sign = SignBox.getSelectedItem().toString();
               String gender = GenderBox.getSelectedItem().toString();
               Integer agefrom = (int)AgeFromBox.getSelectedItem();
               Integer ageto = (int)AgeToBox.getSelectedItem();
               String education = EducationBox.getSelectedItem().toString();
               String department = DepartmentBox.getSelectedItem().toString();
               String post = PostBox.getSelectedItem().toString();
               String title = TitleBox.getSelectedItem().toString();
               String marious = MariousBox.getSelectedItem().toString();
               Integer yearfrom = (int)YearFromBox.getSelectedItem();
               Integer yearto = (int)YearToBox.getSelectedItem();
               Integer monthfrom = (int)MonthFromBox.getSelectedItem();
               Integer monthto = (int)MonthToBox.getSelectedItem();
               Integer dayfrom = (int)DayFromBox.getSelectedItem();
               Integer dayto = (int)DayToBox.getSelectedItem();
               Integer wage = (int)WageBox.getSelectedItem();

               Calendar calendar = Calendar.getInstance();
               calendar.set(yearfrom, monthfrom - 1, dayfrom, 0, 0, 0);
               java.util.Date utilDate1 = calendar.getTime();
               java.sql.Date DateFrom = new java.sql.Date(utilDate1.getTime());

               calendar.set(yearto, monthto - 1, dayto, 0, 0, 0);
               java.util.Date utilDate2 = calendar.getTime();
               java.sql.Date DateTo = new java.sql.Date(utilDate2.getTime());

               Map<String,String> map = new HashMap<>();
               map.put("sign",sign);

               if(!gender.equals("---")){
                   map.put("gender",gender);
               }
               if(!education.equals("---")){
                   map.put("education",education);
               }
               if(!department.equals("---")){
                   map.put("department",department);
               }
               if(!post.equals("---")){
                   map.put("post",post);
               }
               if(!title.equals("---")){
                   map.put("title",title);
               }
               if(!marious.equals("---")){
                   map.put("marital_status",marious);
               }
               if(sign.equals("辞退")){
                   Loading.employees = HRService.GetConEmp(map,agefrom,ageto,DateFrom,DateTo,wage,true);
               }else{
                   Loading.employees = HRService.GetConEmp(map,agefrom,ageto,DateFrom,DateTo,wage,false);
               }

               if(Loading.employees.isEmpty()){
                   JOptionPane.showMessageDialog(null, "未筛选到指定要求的员工！");
                   return;
               }else{
                    ScreenEmpTableInterface screenEmpTableInterface = new ScreenEmpTableInterface(frame);
                    screenEmpTableInterface.setVisible(true);
               }
           }
        });
        Back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setPanel(new EmpManagementInterface(frame));
                Loading.Delete_employees();
            }
        });
        ActionListener DepartmentBoxListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(DepartmentBox.getSelectedItem().toString().equals("---")){
                    return;
                }
                Post_init();
                PostBoxModel.setSelectedItem("---");
            }
        };
        DepartmentBox.addActionListener(DepartmentBoxListener);
        ActionListener SignBoxListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sign = SignBox.getSelectedItem().toString();
                if(sign.equals("辞退")){
                    DepartmentBox.removeActionListener(DepartmentBoxListener);
                    DepartmentBoxModel.removeAllElements();
                    PostBoxModel.removeAllElements();
                    TitleBoxModel.removeAllElements();
                    WageBoxModel.removeAllElements();

                    DepartmentBoxModel.addElement("---");
                    PostBoxModel.addElement("---");
                    TitleBoxModel.addElement("---");
                    WageBoxModel.addElement(0);

                    DepartmentBoxModel.setSelectedItem("---");
                    PostBoxModel.setSelectedItem("---");
                    TitleBoxModel.setSelectedItem("---");
                    WageBoxModel.setSelectedItem(0);
                    DepartmentBox.addActionListener(DepartmentBoxListener);
                }else{
                    DepartmentBox.removeActionListener(DepartmentBoxListener);
                    Department_init();
                    Post_init();
                    Title_init();
                    Wage_init();

                    DepartmentBoxModel.setSelectedItem("---");
                    PostBoxModel.setSelectedItem("---");
                    TitleBoxModel.setSelectedItem("---");
                    WageBoxModel.setSelectedItem(0);
                    DepartmentBox.addActionListener(DepartmentBoxListener);
                }
            }
        };
        SignBox.addActionListener(SignBoxListener);
    }
    private void Comboinit(){
        SignBoxModel = new DefaultComboBoxModel<>();
        GenderBoxModel = new DefaultComboBoxModel<>();
        AgeFromBoxModel = new DefaultComboBoxModel<>();
        AgeToBoxModel = new DefaultComboBoxModel<>();
        EducationBoxModel = new DefaultComboBoxModel<>();
        DepartmentBoxModel = new DefaultComboBoxModel<>();
        PostBoxModel = new DefaultComboBoxModel<>();
        TitleBoxModel = new DefaultComboBoxModel<>();
        MariousBoxModel = new DefaultComboBoxModel<>();
        YearFromBoxModel = new DefaultComboBoxModel<>();
        YearToBoxModel = new DefaultComboBoxModel<>();
        MonthFromBoxModel = new DefaultComboBoxModel<>();
        MonthToBoxModel = new DefaultComboBoxModel<>();
        DayFromBoxModel = new DefaultComboBoxModel<>();
        DayToBoxModel = new DefaultComboBoxModel<>();
        WageBoxModel = new DefaultComboBoxModel<>();

        Sign_init();
        Gender_init();
        Age_init();
        Education_init();
        Department_init();
        PostBoxModel.addElement("---");
        Title_init();
        Marious_init();
        Year_init();
        Month_init();
        Wage_init();

        SignBoxModel.setSelectedItem("在职");
        GenderBoxModel.setSelectedItem("---");
        AgeFromBoxModel.setSelectedItem(20);
        AgeToBoxModel.setSelectedItem(60);
        EducationBoxModel.setSelectedItem("---");
        DepartmentBoxModel.setSelectedItem("---");
        PostBoxModel.setSelectedItem("---");
        TitleBoxModel.setSelectedItem("---");
        MariousBoxModel.setSelectedItem("---");
        YearFromBoxModel.setSelectedItem(now[0]-10);
        YearToBoxModel.setSelectedItem(now[0]);
        MonthFromBoxModel.setSelectedItem(1);
        MonthToBoxModel.setSelectedItem(12);
        WageBoxModel.setSelectedItem(0);

        SignBox = new JComboBox<>(SignBoxModel);
        GenderBox = new JComboBox<>(GenderBoxModel);
        AgeFromBox = new JComboBox<>(AgeFromBoxModel);
        AgeToBox = new JComboBox<>(AgeToBoxModel);
        EducationBox = new JComboBox<>(EducationBoxModel);
        DepartmentBox = new JComboBox<>(DepartmentBoxModel);
        PostBox = new JComboBox<>(PostBoxModel);
        TitleBox = new JComboBox<>(TitleBoxModel);
        MariousBox = new JComboBox<>(MariousBoxModel);
        YearFromBox = new JComboBox<>(YearFromBoxModel);
        YearToBox = new JComboBox<>(YearToBoxModel);
        MonthFromBox = new JComboBox<>(MonthFromBoxModel);
        MonthToBox = new JComboBox<>(MonthToBoxModel);

        Day_init();
        DayFromBoxModel.setSelectedItem(31);
        DayToBoxModel.setSelectedItem(31);

        DayFromBox = new JComboBox<>(DayFromBoxModel);
        DayToBox = new JComboBox<>(DayToBoxModel);
        WageBox = new JComboBox<>(WageBoxModel);

        JLabel Sign = new JLabel("员工状态：");
        JLabel Gender = new JLabel("性别：");
        JLabel AgeFrom = new JLabel("年龄：从");
        JLabel AgeTo = new JLabel("到");
        JLabel Education = new JLabel("教育水平：");
        JLabel Department = new JLabel("部门：");
        JLabel Post = new JLabel("职位：");
        JLabel Title = new JLabel("职称：");
        JLabel Marious = new JLabel("婚姻状况：");
        JLabel Entry_From = new JLabel("入公司时间：从");
        JLabel Entry_To = new JLabel("到");
        JLabel YearFrom = new JLabel("年");
        JLabel YearTo = new JLabel("年");
        JLabel MonthFrom = new JLabel("月");
        JLabel MonthTo = new JLabel("月");
        JLabel DayFrom = new JLabel("日");
        JLabel DayTo = new JLabel("日");
        JLabel Wage = new JLabel("工资：");
        JLabel Wage_ = new JLabel("元以上");

        Sign.setBounds(30,30,100,30);
        SignBox.setBounds(130,35,100,20);
        Gender.setBounds(30,55,100,30);
        GenderBox.setBounds(130,60,100,20);
        AgeFrom.setBounds(30,80,100,30);
        AgeFromBox.setBounds(130,85,100,20);
        AgeTo.setBounds(67,105,100,30);
        AgeToBox.setBounds(130,110,100,20);
        Education.setBounds(30,130,100,30);
        EducationBox.setBounds(130,135,100,20);
        Department.setBounds(30,155,100,30);
        DepartmentBox.setBounds(130,160,100,20);
        Post.setBounds(30,180,100,30);
        PostBox.setBounds(130,185,100,20);
        Title.setBounds(30,205,100,30);
        TitleBox.setBounds(130,210,100,20);
        Marious.setBounds(30,230,100,30);
        MariousBox.setBounds(130,235,100,20);
        Entry_From.setBounds(30,255,100,30);
        YearFromBox.setBounds(130,260,60,20);
        YearFrom.setBounds(190,255,20,30);
        MonthFromBox.setBounds(210,260,40,20);
        MonthFrom.setBounds(250,255,20,30);
        DayFromBox.setBounds(270,260,40,20);
        DayFrom.setBounds(310,255,20,30);
        Entry_To.setBounds(102,280,100,30);
        YearToBox.setBounds(130,285,60,20);
        YearTo.setBounds(190,280,20,30);
        MonthToBox.setBounds(210,285,40,20);
        MonthTo.setBounds(250,280,20,30);
        DayToBox.setBounds(270,285,40,20);
        DayTo.setBounds(310,280,20,30);
        Wage.setBounds(30,305,100,30);
        WageBox.setBounds(130,310,75,20);
        Wage_.setBounds(205,305,100,30);

        JPanel top = new JPanel();
        top.setLayout(null);
        top.add(Sign);
        top.add(SignBox);
        top.add(Gender);
        top.add(GenderBox);
        top.add(AgeFrom);
        top.add(AgeFromBox);
        top.add(AgeTo);
        top.add(AgeToBox);
        top.add(Education);
        top.add(EducationBox);
        top.add(Department);
        top.add(DepartmentBox);
        top.add(Post);
        top.add(PostBox);
        top.add(Title);
        top.add(TitleBox);
        top.add(Marious);
        top.add(MariousBox);
        top.add(Entry_From);
        top.add(YearFromBox);
        top.add(YearFrom);
        top.add(MonthFromBox);
        top.add(MonthFrom);
        top.add(DayFromBox);
        top.add(DayFrom);
        top.add(Entry_To);
        top.add(YearToBox);
        top.add(YearTo);
        top.add(MonthToBox);
        top.add(MonthTo);
        top.add(DayToBox);
        top.add(DayTo);
        top.add(Wage);
        top.add(WageBox);
        top.add(Wage_);
        add(top,BorderLayout.CENTER);

    }
    private void Sign_init(){
        SignBoxModel.removeAllElements();
        SignBoxModel.addElement("在职");
        SignBoxModel.addElement("辞退");
        SignBoxModel.addElement("休假");
        SignBoxModel.addElement("临时员工");
        SignBoxModel.addElement("病假");
    }
    private void Gender_init(){
        GenderBoxModel.removeAllElements();
        GenderBoxModel.addElement("---");
        GenderBoxModel.addElement("男");
        GenderBoxModel.addElement("女");
    }
    private void Age_init(){
        AgeFromBoxModel.removeAllElements();
        AgeToBoxModel.removeAllElements();
        for (int i = 20; i <= 60; i++) {
            AgeFromBoxModel.addElement(i);
            AgeToBoxModel.addElement(i);
        }
    }
    private void Education_init(){
        EducationBoxModel.removeAllElements();
        EducationBoxModel.addElement("---");
        EducationBoxModel.addElement("大专");
        EducationBoxModel.addElement("本科");
        EducationBoxModel.addElement("研究生");
        EducationBoxModel.addElement("高中及以下");
    }
    private void Department_init(){
        DepartmentBoxModel.removeAllElements();
        DepartmentBoxModel.addElement("---");
        DepartmentBoxModel.addElement("技术部");
        DepartmentBoxModel.addElement("人事部");
        DepartmentBoxModel.addElement("后勤部");
        DepartmentBoxModel.addElement("法务部");
    }
    private void Post_init(){
        String department = DepartmentBoxModel.getSelectedItem().toString();
        ArrayList<String> arrayList = HRService.Post(department);
        PostBoxModel.removeAllElements();
        PostBoxModel.addElement("---");
        for(String post : arrayList){
            PostBoxModel.addElement(post);
        }
    }
    private void Title_init(){
        TitleBoxModel.removeAllElements();
        TitleBoxModel.addElement("---");
        for(int i=1;i<=8;i++){
            TitleBoxModel.addElement("k"+i);
        }
    }
    private void Marious_init(){
        MariousBoxModel.removeAllElements();
        MariousBoxModel.addElement("---");
        MariousBoxModel.addElement("已婚");
        MariousBoxModel.addElement("未婚");
    }
    private void Year_init(){
        YearFromBoxModel.removeAllElements();
        YearToBoxModel.removeAllElements();
        now = TimeTools.now();
        for(int i = now[0] - 10;i<=now[0];i++){
            YearFromBoxModel.addElement(i);
            YearToBoxModel.addElement(i);
        }
    }
    private void Month_init(){
        MonthFromBoxModel.removeAllElements();
        MonthToBoxModel.removeAllElements();
        now = TimeTools.now();
        for(int i = 1;i<=12;i++){
            MonthFromBoxModel.addElement(i);
            MonthToBoxModel.addElement(i);
        }
    }
    private void Day_init(){
        int Year_From = (int) YearFromBox.getSelectedItem();
        int Month_From = (int) MonthFromBox.getSelectedItem();
        int Day_From = TimeTools.lastday(Year_From,Month_From);
        int Year_To = (int) YearToBox.getSelectedItem();
        int Month_To = (int) MonthToBox.getSelectedItem();
        int Day_To = TimeTools.lastday(Year_To,Month_To);

        DayFromBoxModel.removeAllElements();
        DayToBoxModel.removeAllElements();
        for(int i =1;i<=Day_From;i++){
            DayFromBoxModel.addElement(i);
        }
        for(int i=1;i<=Day_To;i++){
            DayToBoxModel.addElement(i);
        }

    }
    private void Wage_init(){
        WageBoxModel.removeAllElements();
        WageBoxModel.addElement(0);
        for(int i=3;i<10;i++){
            WageBoxModel.addElement(i*1000);
        }
    }
}

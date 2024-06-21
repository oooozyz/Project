package hrms.ZMGFH.Interface;

import hrms.ZMGFH.Objection.Detail;
import hrms.ZMGFH.Objection.Employee;
import hrms.ZMGFH.Service.HRService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

public class Bouns_FineInterface extends JDialog {
    private Employee emp = null;
    private JButton Add;
    private JButton Back;

    private JComboBox<String> NameBox, ChooseBox, ReasonBox, MoneyBox;

    private DefaultComboBoxModel<String> NameBoxModel, ChooseBoxModel, ReasonBoxModel, MoneyBoxModel;
    private Set<Employee> employees;

    public Bouns_FineInterface(Frame frame) {
        super(frame, "奖金/罚款", true);
        setSize(500, 132);
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension Size = tool.getScreenSize();//获取屏幕大小
        setLocation((Size.width - 500) / 2, (Size.height - 132) / 2);//主窗体在屏幕中间显示
        init();
        addListener();
    }

    private void init() {
        Add = new JButton("添加");
        Back = new JButton("返回");

        JPanel bottom = new JPanel();
        bottom.add(Add);
        bottom.add(Back);
        add(bottom, BorderLayout.SOUTH);

        NameBoxModel = new DefaultComboBoxModel<>();
        ChooseBoxModel = new DefaultComboBoxModel<>();
        ReasonBoxModel = new DefaultComboBoxModel<>();
        MoneyBoxModel = new DefaultComboBoxModel<>();

        employees = HRService.GetAllEmployee();

        NameBoxModel.addElement("---");
        ChooseBoxModel.addElement("---");
        ReasonBoxModel.addElement("---");
        MoneyBoxModel.addElement("---");

        for (Employee e : employees) {
            NameBoxModel.addElement(e.getName());
        }
        NameBoxModel.setSelectedItem("---");
        ChooseBoxModel.setSelectedItem("---");
        ReasonBoxModel.setSelectedItem("---");
        MoneyBoxModel.setSelectedItem("---");

        NameBox = new JComboBox<>(NameBoxModel);
        ChooseBox = new JComboBox<>(ChooseBoxModel);
        ReasonBox = new JComboBox<>(ReasonBoxModel);
        MoneyBox = new JComboBox<>(MoneyBoxModel);

        JPanel top = new JPanel();
        top.setLayout(new FlowLayout());
        top.add(new JLabel("姓名："));
        top.add(NameBox);
        top.add(new JLabel("奖金/罚款："));
        top.add(ChooseBox);
        top.add(new JLabel("原因："));
        top.add(ReasonBox);
        top.add(new JLabel("金额："));
        top.add(MoneyBox);
        add(top, BorderLayout.NORTH);
    }

    private void addListener() {
        Add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (NameBox.getSelectedItem().equals("---")) {
                    JOptionPane.showMessageDialog(null, "姓名为空！");
                    return;
                }
                if (ChooseBox.getSelectedItem().equals("---")) {
                    JOptionPane.showMessageDialog(null, "奖金/罚款为空！");
                    return;
                }
                if (ReasonBox.getSelectedItem().equals("---")) {
                    JOptionPane.showMessageDialog(null, "原因为空！");
                    return;
                }
                if (MoneyBox.getSelectedItem().equals("---")) {
                    JOptionPane.showMessageDialog(null, "罚款金额为空！");
                    return;
                }

                String reason = ReasonBox.getSelectedItem().toString();
                int money = Integer.parseInt(MoneyBox.getSelectedItem().toString());
                if (ChooseBox.getSelectedItem().equals("罚款")) {
                    money = -money;
                }
                LocalDate localDate = LocalDate.now();
                java.sql.Date date = java.sql.Date.valueOf(localDate);
                Detail detail = new Detail(emp.getId(), date, money, reason);
                if (HRService.Wage_detail(detail) && HRService.Modify_Change(emp.getId(), money)) {
                    JOptionPane.showMessageDialog(null, "修改成功！");
                    Bouns_FineInterface.this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "修改失败！");
                }
            }
        });
        Back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Bouns_FineInterface.this.dispose();
            }
        });

        ActionListener ChooseListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Reason_init();
                ReasonBoxModel.setSelectedItem("---");
                Money_init();
                MoneyBoxModel.setSelectedItem("---");
            }
        };
        ChooseBox.addActionListener(ChooseListener);

        ActionListener NameBoxListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = NameBox.getSelectedItem().toString();
                if (name.equals("---")) {
                    return;
                }
                for (Employee employee : employees) {
                    if (employee.getName().equals(name)) {
                        emp = employee;
                        break;
                    }
                }
                ChooseBox.removeActionListener(ChooseListener);
                Choose_init();
                ChooseBoxModel.setSelectedItem("---");
                ChooseBox.addActionListener(ChooseListener);
            }
        };

        NameBox.addActionListener(NameBoxListener);
    }

    private void Choose_init() {
        ChooseBoxModel.removeAllElements();
        ChooseBoxModel.addElement("---");
        ChooseBoxModel.addElement("奖金");
        ChooseBoxModel.addElement("罚款");
    }

    private void Reason_init() {
        ReasonBoxModel.removeAllElements();
        ReasonBoxModel.addElement("---");
        String choose = ChooseBox.getSelectedItem().toString();
        ArrayList<String> arrayList = HRService.Reason(choose);
        for (String arr : arrayList) {
            ReasonBoxModel.addElement(arr);
        }
    }

    private void Money_init() {
        MoneyBoxModel.removeAllElements();
        MoneyBoxModel.addElement("---");
        for (int i = 1; i < 500; i++) {
            MoneyBoxModel.addElement(Integer.toString(i * 10));
        }
    }
}

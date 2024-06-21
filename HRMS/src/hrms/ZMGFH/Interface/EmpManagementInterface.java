package hrms.ZMGFH.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmpManagementInterface extends JPanel {
    private final MainFrame frame;

    private JButton Add_Emp;//添加员工
    private JButton Filtering;//筛选员工
    private JButton Bonus_Fine;//奖金及罚款
    private JButton Emp_Change;//员工变动
    private JButton Back;

    public EmpManagementInterface(MainFrame frame) {
        this.frame = frame;
        this.frame.setSize(700,432);
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension Size = tool.getScreenSize();//获取屏幕大小
        setLocation((Size.width - 700) / 2, (Size.height - 432) / 2);//主窗体在屏幕中间显示
        init();
        addListener();
    }

    private void init() {
        frame.setTitle("员工管理");

        Add_Emp = new JButton("添加员工");
        Filtering = new JButton("筛选");
        Bonus_Fine = new JButton("奖金/罚款");
        Emp_Change = new JButton("员工变动");
        Back = new JButton("返回");

        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.add(Add_Emp);
        panel.add(Filtering);
        panel.add(Bonus_Fine);
        panel.add(Emp_Change);
        panel.add(Back);
        add(panel, BorderLayout.SOUTH);

    }

    private void addListener() {
        Add_Emp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddEmpInterface_p addEmpInterface_p = new AddEmpInterface_p(frame);
                frame.setPanel(addEmpInterface_p);
            }
        });
        Filtering.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ScreenInterface screenInterface = new ScreenInterface(frame);
                frame.setPanel(screenInterface);
            }
        });
        Bonus_Fine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Bouns_FineInterface bonusFine = new Bouns_FineInterface(frame);
                bonusFine.setVisible(true);
            }
        });

        Emp_Change.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Emp_ChangeInterface emp = new Emp_ChangeInterface(frame);
                emp.setVisible(true);
            }
        }
        );

        Back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setPanel(new AdminInterface(frame));
            }
        });
    }
}

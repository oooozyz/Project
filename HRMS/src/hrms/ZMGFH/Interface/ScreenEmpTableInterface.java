package hrms.ZMGFH.Interface;

import hrms.ZMGFH.Loading.Loading;
import hrms.ZMGFH.Objection.Employee;
import hrms.ZMGFH.Tools.EmpTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class ScreenEmpTableInterface extends JDialog{
    private JButton Back;
    private JTable table;
    private DefaultTableModel tableModel;
    public ScreenEmpTableInterface(Frame frame){
        super(frame,"筛选员工（共 "+Loading.employees.size()+" 人）",true);
        setSize(1100,300);
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension Size = tool.getScreenSize();//获取屏幕大小
        setLocation((Size.width - 1100) / 2, (Size.height - 300) / 2);//主窗体在屏幕中间显示
        init();
        addListener();
    }

    private void init(){
        Back = new JButton("返回");
        tableModel = new DefaultTableModel();
        table = new EmpTable(tableModel);

        JPanel bottom = new JPanel();
        bottom.add(Back);
        add(bottom,BorderLayout.SOUTH);

        setLayout(new BorderLayout());

        Set<Employee> employees = Loading.employees;
        String[] nameString = { "姓名","员工状态", "年龄", "性别", "身份证号", "教育水平", "电话号", "邮箱号", "银行卡号", "婚姻状态", "部门", "岗位", "职称",
                "入公司时间", "工资" };
        String[][] valueString = new String[employees.size()][15];
        int k = 0;
        for(Employee employee : employees){
            String[] value = employee.to_string();
            valueString[k][0] = value[1];
            valueString[k][1] = value[16];
            for(int i = 2; i < 14; i++){
                valueString[k][i] = value[i];
            }
            float wage = Float.parseFloat(value[18]) * Integer.parseInt(value[15]);
            valueString[k][14] = Float.toString(wage);
            k++;
        }
        tableModel.setDataVector(valueString, nameString);
        Integer[] Column = {75,100,50,50,200,75,125,175,175,75,75,75,50,100,100};
        for(int j = 0; j < Column.length; j++){
            table.getColumnModel().getColumn(j).setPreferredWidth(Column[j]);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addListener(){
        Back.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               ScreenEmpTableInterface.this.dispose();
           }
        });
    }
}

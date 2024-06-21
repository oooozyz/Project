package hrms.ZMGFH.Interface;


import hrms.ZMGFH.Loading.Loading;
import hrms.ZMGFH.Objection.Worktime;
import hrms.ZMGFH.Service.HRService;
import hrms.ZMGFH.Tools.TimeTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AttendanceManagementInterface extends JPanel {
    private MainFrame mainFrame;

    private JToggleButton day;//日报按钮
    private JToggleButton worktime;//作息时间设置按钮
    private JButton back;//返回按钮
    private JButton Dayflush;//日报刷新按钮
    private JPanel center; // 中央面板
    private CardLayout card;// 中央面板使用的卡片布局

    private JPanel dayPanel;// 日报面板
    private JTextArea area;// 日报面板里的文本域

    private JPanel worktimePanel;// 作息时间面板

    private JTextField hourOn, minuteOn, secondOn;    // 上班时间的时、分、秒文本框
    private JTextField hourOff, minuteOff, secondOff;// 下班时间的时、分、秒文本框
    private JButton update;// 替换作息时间按钮

    public AttendanceManagementInterface(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        init();
        addListener();
    }

    private void init(){
        Worktime worktimee = Loading.worktime;// 获取当前作息时间
        // 修改主窗体标题
        mainFrame.setTitle("考勤报表 (上班时间：" + worktimee.getStart() + ",下班时间：" + worktimee.getEnd() + ")");
        day = new JToggleButton("日报");
        day.setSelected(true);// 日报按钮处于选中状态
        worktime = new JToggleButton("作息时间设置");
        // 按钮组，保证三个按钮中只有一个按钮处于选中状态
        ButtonGroup group = new ButtonGroup();
        group.add(day);
        group.add(worktime);

        back = new JButton("返回");
        Dayflush = new JButton("刷新");

        dayInit();// 日报面板初始化
        worktimeInit();// 作息时间面板初始化

        card = new CardLayout();// 卡片布局
        center = new JPanel(card);// 中部面板采用卡片布局
        center.add("day", dayPanel);        // day标签为日报面板
        center.add("worktime", worktimePanel);        // worktime标签为作息时间面板

        JPanel bottom = new JPanel();// 底部面板
        bottom.add(day);// 添加底部的组件
        bottom.add(worktime);
        bottom.add(back);

        setLayout(new BorderLayout());// 采用边界布局
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);//
    }

    private void addListener() {
        // 日报按钮的事件
        day.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 卡片布局切换至日报面板
                card.show(center, "day");
            }
        });
        // 作息时间设置按钮的事件
        worktime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 卡片布局切换至作息时间面板
                card.show(center, "worktime");
            }
        });

        // 返回按钮的事件
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setPanel(new AdminInterface(mainFrame)); // 主窗体切换到主面板
            }
        });
        // 日报面板刷新按钮的事件
        Dayflush.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDay();// 更新日报
            }
        });
        // 替换作息时间按钮的事件
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hs = hourOn.getText().trim();// 上班的小时
                String ms = minuteOn.getText().trim();// 上班的分钟
                String ss = secondOn.getText().trim();// 上班的秒
                String he = hourOff.getText().trim();// 下班的小时
                String me = minuteOff.getText().trim();// 下班的分钟
                String se = secondOff.getText().trim();// 下班的秒

                boolean check = true;// 时间校验成功标志
                String startInput = hs + ":" + ms + ":" + ss;// 拼接上班时间
                String endInput = he + ":" + me + ":" + se;// 拼接下班时间
                // 如果上班时间不是正确的时间格式
                if (!TimeTools.CheckTimeFormat(startInput)) {
                    check = false;// 校验失败
                    // 弹出提示
                    JOptionPane.showMessageDialog(null, "上班时间的格式不正确");
                }
                // 如果下班时间不是正确的时间格式
                if (!TimeTools.CheckTimeFormat(endInput)) {
                    check = false;// 校验失败
                    // 弹出提示
                    JOptionPane.showMessageDialog(null, "下班时间的格式不正确");
                }

                if (check) {// 如果校验通过
                    // 弹出选择对话框，并记录用户选择
                    int confirmation = JOptionPane.showConfirmDialog(mainFrame,
                            "确定做出以下设置？\n上班时间：" + startInput + "\n下班时间：" + endInput, "提示！", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {// 如果用户选择确定
                        Worktime input = new Worktime(startInput, endInput);
                        HRService.UpdateWorkTime(input);// 更新作息时间
                        // 修改标题
                        mainFrame.setTitle("考勤报表 (上班时间：" + startInput + ",下班时间：" + endInput + ")");
                    }
                }
            }
        });

    }
    private void worktimeInit() {
        Worktime worktime = Loading.worktime;// 获取当前的作息时间
        // 将上班时间和下班时间分割成时、分、秒数组
        String[] startTime = worktime.getStart().split(":");
        String[] endTime = worktime.getEnd().split(":");

        Font labelFont = new Font("黑体", Font.BOLD, 20);// 字体

        JPanel top = new JPanel();// 顶部面板

        JLabel startLabel = new JLabel("上班时间：");// 文本标签
        startLabel.setFont(labelFont);// 使用指定字体
        top.add(startLabel);

        hourOn = new JTextField(3);// 上班时间的时输入框，长度为3
        hourOn.setText(startTime[0]);// 默认值为当前上班时间的小时
        top.add(hourOn);

        JLabel colon1 = new JLabel(":");
        colon1.setFont(labelFont);
        top.add(colon1);

        minuteOn = new JTextField(3);// 上班时间的分输入框
        minuteOn.setText(startTime[1]);// 默认值为当前上班时间的分钟
        top.add(minuteOn);

        JLabel colon2 = new JLabel(":");
        colon2.setFont(labelFont);
        top.add(colon2);

        secondOn = new JTextField(3);// 上班时间的秒输入框
        secondOn.setText(startTime[2]);// 默认值为当前上班时间的秒
        top.add(secondOn);

        JPanel bottom = new JPanel();// 底部面板

        JLabel endLabel = new JLabel("下班时间：");
        endLabel.setFont(labelFont);
        bottom.add(endLabel);

        hourOff = new JTextField(3);// 下班时间的时输入框
        hourOff.setText(endTime[0]);// 默认值为当前下班时间的小时
        bottom.add(hourOff);

        JLabel colon3 = new JLabel(":");
        colon3.setFont(labelFont);
        bottom.add(colon3);

        minuteOff = new JTextField(3);// 下班时间的分输入框
        minuteOff.setText(endTime[1]);// 默认值为当前下班时间的分钟
        bottom.add(minuteOff);

        JLabel colon4 = new JLabel(":");
        colon4.setFont(labelFont);
        bottom.add(colon4);

        secondOff = new JTextField(3);// 下班时间的秒输入框
        secondOff.setText(endTime[2]);// 默认值为当前下班时间的秒
        bottom.add(secondOff);

        worktimePanel = new JPanel();
        worktimePanel.setLayout(null);// 作息时间面板采用绝对布局

        JPanel center = new JPanel();// 作息面板中央显示的面板
        center.setLayout(new GridLayout(2, 1));// 采用2行1列的网格布局
        center.add(top);// 第1行放顶部面板
        center.add(bottom);// 第2行放底部面板

        center.setBounds(100, 60, 400, 150);// 设置面板的坐标和宽高
        worktimePanel.add(center);

        update = new JButton("替换作息时间");
        update.setFont(new Font("黑体", Font.BOLD, 15));
        update.setBounds(220, 235, 170, 55);// 按钮的坐标和宽高
        worktimePanel.add(update);
    }

    private void dayInit() {
        area = new JTextArea();
        area.setEditable(false);// 文本域不可编辑
        area.setFont(new Font("宋体", Font.BOLD, 24));
        JScrollPane scroll = new JScrollPane(area);// 文本域放到滚动面板中

        dayPanel = new JPanel();
        dayPanel.setLayout(new BorderLayout());// 日报面板采用边界布局
        dayPanel.add(scroll, BorderLayout.CENTER);// 滚动面板在中部显示

        updateDay();// 更新日报
    }

    private void updateDay() {
        Integer[] now = TimeTools.now();
        String report = HRService.getDayReport(now[0], now[1], now[2]);
        area.setText(report);// 日报报表覆盖到文本域中
    }

}

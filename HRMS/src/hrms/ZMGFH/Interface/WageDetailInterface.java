package hrms.ZMGFH.Interface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import hrms.ZMGFH.Loading.Loading;
import hrms.ZMGFH.Objection.Detail;
import hrms.ZMGFH.Service.HRService;
import hrms.ZMGFH.Tools.EmpTable;
import hrms.ZMGFH.Tools.TimeTools;

public class WageDetailInterface extends JDialog{
	private JButton backButton;
	private JButton Search;

	private JPanel center;
	private EmpTable table;
	private DefaultTableModel model;

	private JComboBox<Integer> yearBox,monthBox;//年月日的下拉列表
	private DefaultComboBoxModel<Integer> yearBoxModel,monthBoxModel;//年月日下拉列表所使用的数据模型
	
	public WageDetailInterface(Frame f) {
		super(f,"奖金扣款明细",true);
		setSize(500,300);
		setLocation(f.getX() + (f.getWidth() - 500) / 2, f.getY() + (f.getHeight() - 300) / 2);// 在窗体的中间显示
		init();
		addListener();
	}
	
	private void init(){
		backButton = new JButton("返回");
		Search = new JButton("查看");

		yearBoxModel = new DefaultComboBoxModel<>();
		monthBoxModel = new DefaultComboBoxModel<>();

		setLayout(new BorderLayout());
		
		Integer now_timeInteger[] = TimeTools.now();
		for(int i=now_timeInteger[0]-10;i<=now_timeInteger[0];i++) {
			yearBoxModel.addElement(i);
		}
		for(int i=1;i<=12;i++) {
			monthBoxModel.addElement(i);
		}
		
		//更改每个月的天数

		yearBox = new JComboBox<>(yearBoxModel);
		monthBox = new JComboBox<>(monthBoxModel);
		
		yearBox.setSelectedItem(now_timeInteger[0]);
		monthBox.setSelectedItem(now_timeInteger[1]);

		JPanel top = new JPanel();
		top.setLayout(new FlowLayout());//采用流布局
		top.add(yearBox);
		top.add(new JLabel("年"));
		top.add(monthBox);
		top.add(new JLabel("月"));
		top.add(Search);
		add(top,BorderLayout.NORTH);

		center = new JPanel();

		model = new DefaultTableModel();
		table = new EmpTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane,BorderLayout.CENTER);
		updatemessage();
		
		JPanel bottom = new JPanel();//底部面板
		bottom.add(backButton);
		add(bottom,BorderLayout.SOUTH);
	}
	
	private void addListener() {
		Search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updatemessage();
			}
		});


		backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				WageDetailInterface.this.dispose();
			}
		});
	}

	private void updatemessage(){
		Integer year = (int)yearBox.getSelectedItem();
		Integer month = (int)monthBox.getSelectedItem();
		ArrayList<Detail> details = HRService.Wage_Detail(Loading.user.getId(),year,month);
		String[] nameString = {"日期","原因","金额变化值"};
		String[][] valueString = new String[details.size()][3];
		int k=0;
		for(Detail detail:details) {
			valueString[k][0]=detail.getDdate().toString();
			valueString[k][1]=detail.getReason();
			valueString[k][2]= String.valueOf(detail.getChange_num());
			k++;
		}
		model.setDataVector(valueString, nameString);
		Integer[] Column = {100,150,100};
		for(int i=0;i<Column.length;i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(Column[i]);
		}
	}
}

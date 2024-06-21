package hrms.ZMGFH.Interface;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import hrms.ZMGFH.Loading.Loading;


public class MainFrame extends JFrame{
	public MainFrame() {
		Loading.init();//初始化
		addListener();//添加监听
		setSize(700, 432);//设置窗口大小
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//点击关闭不发生任何事件
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension Size = tool.getScreenSize();//获取屏幕大小
		setLocation((Size.width - getWidth()) / 2, (Size.height - getHeight()) / 2);//主窗体在屏幕中间显示
	}

	private void addListener() {
		addWindowListener(new WindowAdapter() {//添加窗体监听
			public void windowClosing(WindowEvent window) {
				int closeCode = JOptionPane.showConfirmDialog(MainFrame.this, "是否退出程序？", "提示！",
						JOptionPane.YES_NO_OPTION);
				if (closeCode == JOptionPane.YES_OPTION) {//选择确定
					Loading.release();//释放全局资源
					System.exit(0);//关闭程序
				}
			}
		});
	}
	public void setPanel(JPanel p) {
        Container container = getContentPane();
        container.removeAll();//删除容器中的所有组件
        container.add(p);
        container.validate();//重新验证所有组件
    }
}

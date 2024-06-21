package hrms.ZMGFH.Main;

import hrms.ZMGFH.Interface.MainFrame;
import hrms.ZMGFH.Interface.MainInterface;

public class main {

	public static void main(String[] args) {
		MainFrame f = new MainFrame();// 创建主窗体
        f.setPanel(new MainInterface(f));// 主窗体加载主面板
        f.setVisible(true);// 显示主窗体
	}
}

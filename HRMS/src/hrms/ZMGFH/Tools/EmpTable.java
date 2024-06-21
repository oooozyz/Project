package hrms.ZMGFH.Tools;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class EmpTable extends JTable {

    public EmpTable(TableModel dm) {
        super(dm);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 只能单选
    }

    public boolean isCellEditable(int row, int column) {
        return false;// 表格不可编辑
    }

    public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
        // 获取单元格渲染对象
        DefaultTableCellRenderer cr = (DefaultTableCellRenderer) super.getDefaultRenderer(columnClass);
        // 表格文字居中显示
        cr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        return cr;
    }
}

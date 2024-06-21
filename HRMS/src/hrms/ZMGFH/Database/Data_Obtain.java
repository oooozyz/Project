package hrms.ZMGFH.Database;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

import hrms.ZMGFH.Objection.Detail;
import hrms.ZMGFH.Objection.Employee;
import hrms.ZMGFH.Objection.User;
import hrms.ZMGFH.Objection.Worktime;
import hrms.ZMGFH.Tools.JDBCTools;
import hrms.ZMGFH.Tools.TimeTools;

public class Data_Obtain {
	Connection connection = null;
	Statement stmt = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	// 获取所有员工的全部信息
	public Set<Employee> GetAllEmp() {
		Set<Employee> set = new HashSet<>();
		String sql = "select* from employee_p natural join (employee_a natural join (base_wage natural join title_magnif))";
		connection = JDBCTools.getConnection();
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Employee emp = Ob_emp();
				Integer base_salary = rs.getInt("base_salary");
				Integer magnification = rs.getInt("magnification");
				emp.setBase_salary(base_salary);
				emp.setMagnification(magnification);
				set.add(emp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		return set;
	}
	// 获取全部员工
	public Set<Employee> GetAllEmployee() {
		Set<Employee> set = new HashSet<>();
		String sql = "select* from employee_p natural join employee_a";
		connection = JDBCTools.getConnection();
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				set.add(Ob_emp());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		return set;
	}
	// 获取特定员工及信息
	public Set<Employee> GetConEmp(Map<String, String> M,int agefrom,int ageto, Date datefrom, Date dateto, int money,boolean abdication) {
		Set<Employee> set = new HashSet<>();
		StringBuilder Sql = null;
		if(!abdication) {
			Sql = new StringBuilder("select* from (employee_p natural join (employee_a natural join base_wage)) natural join title_magnif where ");
		}else{
			Sql = new StringBuilder("select* from employee_p natural join employee_a where ");
		}
		boolean isFirst = true;
		for(Map.Entry<String, String> entry : M.entrySet()) {
			if(!isFirst) {
				Sql.append("and ");
			}
			Sql.append(entry.getKey()).append(" = ?");
			isFirst = false;
		}
		if(!abdication) {
			Sql.append(" and (age between ? and ? ) and (entry_date between ? and ?) and base_salary * magnification >= ?");
		}else{
			Sql.append(" and (age between ? and ? ) and (entry_date between ? and ?)");
		}
		String sql = Sql.toString();
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			int index = 1;
			for(String value:M.values()) {
				ps.setString(index++, value);
			}
			ps.setInt(index++, agefrom);
			ps.setInt(index++, ageto);
			ps.setDate(index++, datefrom);
			ps.setDate(index++, dateto);
			if(!abdication) {
				ps.setInt(index, money);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				Employee employee = Ob_emp();
				if(!abdication) {
					employee.setBase_salary(rs.getInt("base_salary"));
					employee.setMagnification(rs.getFloat("magnification"));
				}
				set.add(employee);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}

		if (set.isEmpty()) {
			return new HashSet<>();
		} else {
			return set;
		}
	}
	// 获取指定特征码的信息
	public Employee getConEmp(String code) {
		String sql = "select* from employee_p natural join employee_a where code = ?";
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if (rs.next()) {
				return Ob_emp();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs,stmt,ps);// 关闭数据库接口对象
		}
		return null;// 无此员工则返回null
	}
	// 获取指定id的员工
	public Employee getEmp(int _id,boolean abdication) {
		String sql = null;
		if(abdication) {
			sql = "select *from employee_p natural join employee_a where id = ?";
		}else{
			sql = "select *from employee_p natural join (employee_a natural join base_wage) natural join title_magnif where id = ?";
		}
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, _id);
			rs = ps.executeQuery();
			if (rs.next()) {
				Employee emp = Ob_emp();
				if(!abdication) {
					emp.setBase_salary(rs.getInt("base_salary"));
					emp.setMagnification(rs.getFloat("magnification"));
				}
				return emp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		return null;
	}
	//管理员登录
	public boolean adminLogin(User user) {
		String sql = "select* from admin where username = ? and password = ?";
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		return false;
	}
	//用户登录
	public Integer userLogin(User user) {
		String sql = "select* from user where username = ? and password = ?";
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("id");
			} else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		return 0;
	}
	//添加一个员工——用户信息
	public boolean AddEmp_p(Employee emp) {
		String sql = "insert into employee_p(name,age,identity,gender,education,phone_number,e_mail,card_number,marital_status,code) values(?,?,?,?,?,?,?,?,?,?)";
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, emp.getName());
			ps.setInt(2, emp.getAge());
			ps.setString(3, emp.getIdentity());
			ps.setString(4, emp.getGender());
			ps.setString(5, emp.getEducation());
			ps.setString(6, emp.getPhone_number());
			ps.setString(7, emp.getE_mail());
			ps.setString(8, emp.getCard_number());
			ps.setString(9, emp.getMarriage_status());
			ps.setString(10, emp.getCode());
			int num = ps.executeUpdate();
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		return false;
	}
	//添加一个员工——管理员信息
	public boolean AddEmp_a(Employee emp) {
		String sql = "insert into employee_a(department,post,title,entry_date,sign,wage_change) values(?,?,?,?,?,?)";
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, emp.getDepartment());
			ps.setString(2, emp.getPost());
			ps.setString(3, emp.getTitle());
			ps.setDate(4, emp.getEntry_date());
			ps.setString(5, emp.getSign());
			ps.setInt(6, emp.getWage_change());
			int num = ps.executeUpdate();
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		return false;
	}
	//添加用户名及密码
	public boolean AddEmp_user(String username, String password) {
		String sql = "insert into user(username,password) values(?,?)";
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			int num = ps.executeUpdate();
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		return false;
	}
	//查找员工打卡信息
	public boolean Search_Clock(int empID, java.util.Date now,String mora){
		int year = now.getYear()+1900;
		int month = now.getMonth()+1;
		int day = now.getDate();
		String sql = "select* from clock_message where id = ? and year(time) = ? and month(time) = ? and day(time) = ? and mora = ?";
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, empID);
			ps.setInt(2, year);
			ps.setInt(3, month);
			ps.setInt(4, day);
			ps.setString(5, mora);
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs,stmt,ps);// 关闭数据库接口对象
		}
		return false;
	}
	//添加打卡信息
	public boolean addCLockInRecord(int empID, java.util.Date now,String mora) {
		// 日期格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(now);// 将日期转为字符串
		String sql = "insert into clock_message(id,time,mora) values(?,?,?)";
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, empID);
			ps.setString(2, time);
			ps.setString(3, mora);
			int num = ps.executeUpdate();
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);// 关闭数据库接口对象
		}
		return false;
	}
	//对员工进行奖励/罚款
	public boolean Wage_change(Detail detail) {
		String sql = "insert into wage_detail values(?,?,?,?)";
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, detail.getId());
			ps.setDate(2, detail.getDdate());
			ps.setInt(3, detail.getChange_num());
			ps.setString(4, detail.getReason());
			int num = ps.executeUpdate();
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		return false;
	}
	// 管理员更改任何员工的任何信息
	public boolean UpdateEmpMessage_p(Map<String, String> M, int id) {
		StringBuilder Sql = new StringBuilder("update employee_p set ");
		boolean isFirst = true;
		for(Map.Entry<String, String> entry : M.entrySet()) {
			if(!isFirst) {
				Sql.append(", ");
			}
			Sql.append(entry.getKey()).append(" = ?");
			isFirst = false;
		}
		
		Sql.append(" where id = ?");
		String sql = Sql.toString();
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			int index = 1;
			for(String value:M.values()) {
				ps.setString(index++, value);
			}
			ps.setInt(index, id);
			int num = ps.executeUpdate();
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		return false;
	}
	// 管理员更改任何员工的任何信息
	public boolean UpdateEmpMessage_a(Map<String, String> M, int id) {
		StringBuilder Sql = new StringBuilder("update employee_a set ");
		boolean isFirst = true;
		for(Map.Entry<String, String> entry : M.entrySet()) {
			if(!isFirst) {
				Sql.append(", ");
			}
			Sql.append(entry.getKey()).append(" = ?");
			isFirst = false;
		}
		
		Sql.append(" where id = ?");
		String sql = Sql.toString();
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			int index = 1;
			for(String value:M.values()) {
				ps.setString(index++, value);
			}
			ps.setInt(index, id);
			int num = ps.executeUpdate();
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		return false;
	}
	//获取相关部门的全部职位
	public ArrayList<String> Post(String s){
		String sql = "select* from dep_post where depart = ?";
		ArrayList<String> list = new ArrayList<>();
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, s);
			rs = ps.executeQuery();
			while(rs.next()) {
				list.add(rs.getString("post"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		if(!list.isEmpty()) {
			return list;
		}
		else{
			return new ArrayList<>();
		}
	}
	//获取指定奖金/罚款的全部理由
	public ArrayList<String> Reason(String s){
		String sql = "select* from bf_reason where type = ?";
		ArrayList<String> list = new ArrayList<>();
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, s);
			rs = ps.executeQuery();
			while(rs.next()) {
				list.add(rs.getString("reason"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		if(!list.isEmpty()) {
			return list;
		}
		else{
			return new ArrayList<>();
		}
	}
	//修改员工的工资变化值
	public boolean Modify_Change(int id,int num) {
		String sql = "update employee_a set wage_change = wage_change + ? where id = ?";
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, num);
			ps.setInt(2, id);
			int num1 = ps.executeUpdate();
			return num1 > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		return false;
	}
	//获取工资明细
	public ArrayList<Detail> Wage_Detail(int id,int year,int month){
		ArrayList<Detail> list = new ArrayList<>();
		String sql = "select * from wage_detail where id = ? and year(ddate) = ? and month(ddate) = ?";
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1,id);
			ps.setInt(2,year);
			ps.setInt(3,month);
			rs = ps.executeQuery();
			while(rs.next()) {
				int id_ = rs.getInt("id");
				Date ddate = rs.getDate("ddate");
				int change_num = rs.getInt("change_num");
				String reason = rs.getString("reason");
				Detail detail = new Detail(id_,ddate,change_num,reason);
				list.add(detail);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs, stmt, ps);
		}
		if(!list.isEmpty()) {
			return list;
		}
		else{
			return new ArrayList<>();
		}
	}
	//获取作息时间
	public Worktime GetWorktime(){
		String sql = "select start,end from work_time";
		connection = JDBCTools.getConnection();
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				String start = rs.getString("start");
				String end = rs.getString("end");
				return new Worktime(start, end);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs,stmt,ps);// 关闭数据库接口对象
		}
		return null;// 无记录则返回null
	}
	//更改作息时间
	public boolean UpdateWorktime(Worktime w){
		String sql = "update work_time set start = ? , end = ?";
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, w.getStart());
			ps.setString(2, w.getEnd());
			int num = ps.executeUpdate();
			return num > 0;
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			JDBCTools.close(rs,stmt,ps);// 关闭数据库接口对象
		}
		return false;

	}
	//获取日报信息
	public String[][] getAllClockInRecord() {
		// 保存查询数据的集合。因为不确定行数，所以使用集合而不是二维数组。
		HashSet<String[]> set = new HashSet<>();
		String sql = "select id, time from clock_message ";
		connection = JDBCTools.getConnection();
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String id = rs.getString("id");
				String time = rs.getString("time");
				// 直接将查询的两个结果以字符串数组的形式放到集合中
				set.add(new String[] { id, time });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs,stmt,ps);// 关闭数据库接口对象
		}
		if (set.isEmpty()) {// 如果集合是空的，表示表中没有任何打卡数据
			return null;
		} else {// 如果存在打卡数据
			//创建二维数组作为返回结果，数组行数为集合元素个数，列数为2
			String[][] result = new String[set.size()][2];
			Iterator<String[]> it = set.iterator();//创建集合迭代器
			for (int i = 0; it.hasNext(); i++) {//迭代集合，同时让i递增
				result[i] = it.next();//集合中的每一个元素都作为数组的每一行数据
			}
			return result;
		}
	}
	//获取上次发工资的时间
	public Date getsalarytime(){
		String sql = "select ddate from salary_payment_time";
		connection = JDBCTools.getConnection();
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				Date date = rs.getDate("ddate");
				return date;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs,stmt,ps);// 关闭数据库接口对象
		}
		return null;// 无记录则返回null
	}
	//获取上次是否发工资并且将中间值清零
	public String getwhether(){
		String sql = "select whether from salary_payment_time";
		connection = JDBCTools.getConnection();
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				String whether = rs.getString("whether");
				return whether;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTools.close(rs,stmt,ps);// 关闭数据库接口对象
		}
		return null;// 无记录则返回null
	}
	//修改发工资的时间
	public boolean Updatesalarytime(Date date){
		String sql = "update salary_payment_time set ddate = ? , whether = ?";
		connection = JDBCTools.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setDate(1, date);
			ps.setString(2, "是");
			int num = ps.executeUpdate();
			return num > 0;
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			JDBCTools.close(rs,stmt,ps);// 关闭数据库接口对象
		}
		return false;
	}
	//获取员工信息
	private Employee Ob_emp() throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		int age = rs.getInt("age");
		String identity = rs.getString("identity");
		String gender = rs.getString("gender");
		String education = rs.getString("education");
		String phone_number = rs.getString("phone_number");
		String e_mail = rs.getString("e_mail");
		String card_number = rs.getString("card_number");
		String marriage_status = rs.getString("marital_status");
		String code = rs.getString("code");
		String post = rs.getString("post");
		String department = rs.getString("department");
		String title = rs.getString("title");
		Date entry_date = rs.getDate("entry_date");
		String sign = rs.getString("sign");
		int wage_change = rs.getInt("wage_change");
		return new Employee(id, name, age, identity, gender, education, phone_number, e_mail,
				card_number, marriage_status, code, post, department, title, entry_date, sign, wage_change,
				0, 0);
	}

}

package hrms.ZMGFH.Objection;

public class User {
	private Integer id = null;
	private String username = null;// 用户名
	private String password = null;// 密码
	private Integer role = null;// 当前登录的用户是什么身份,0为管理员，1为员工,null为无员工登录

	public User() {
		super();
	}

	public User(Integer id,String username, String password, int role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}
		
	public void exit() {//退出登录
		id = null;
		username = null;
		password = null;
		role = null;
	}
}

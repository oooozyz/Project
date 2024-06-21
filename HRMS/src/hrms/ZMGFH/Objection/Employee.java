package hrms.ZMGFH.Objection;

import java.sql.Date;

public class Employee {
	private Integer id; // 员工编号
	private String name; // 员工姓名
	private Integer age; // 员工年龄
	private String gender; // 员工性别
	private String identity; // 员工身份证号
	private String education; // 员工学历（可为空）
	private String phone_number; // 员工电话
	private String e_mail; // 员工邮箱（可为空）
	private String card_number; // 员工银行卡号（可为空）
	private String marriage_status; // 员工婚姻状况（可为空）
	private String department; // 员工所属部门
	private String post; // 员工职务
	private String title; // 员工职称
	private Date entry_date; // 员工入公司时间
	private Integer wage_change; // 员工本月工资变化
	private Integer base_salary; // 员工基础工资
	private String sign; // 员工状态
	private String code; // 员工特征码
	private float magnification;//员工职称倍率

	public Employee() {
		super();
	}

	public Employee(Integer id, String name, Integer age, String identity, String gender, String education,
			String phone_number, String e_mail, String card_number, String marriage_status, String code, String post,
			String department, String title, Date entry_date, String sign, Integer wage_change, Integer base_salary, float magnification) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.identity = identity;
		this.gender = gender;
		this.education = education;
		this.phone_number = phone_number;
		this.e_mail = e_mail;
		this.card_number = card_number;
		this.marriage_status = marriage_status;
		this.code = code;
		this.post = post;
		this.department = department;
		this.title = title;
		this.entry_date = entry_date;
		this.sign = sign;
		this.wage_change = wage_change;
		this.base_salary = base_salary;
		this.magnification = magnification;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getAge() {
		return age;
	}

	public String getIdentity() {
		return identity;
	}

	public String getGender() {
		return gender;
	}

	public String getEducation() {
		return education;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public String getE_mail() {
		return e_mail;
	}

	public String getCard_number() {
		return card_number;
	}

	public String getMarriage_status() {
		return marriage_status;
	}

	public String getPost() {
		return post;
	}

	public String getCode() {
		return code;
	}

	public String getDepartment() {
		return department;
	}

	public String getTitle() {
		return title;
	}

	public Date getEntry_date() {
		return entry_date;
	}

	public String getSign() {
		return sign;
	}

	public Integer getWage_change() {
		return wage_change;
	}

	public Integer getBase_salary() {
		return base_salary;
	}

	public Float getMagnification() {
		return magnification;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public void setMarriage_status(String marriage_status) {
		this.marriage_status = marriage_status;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setEntry_date(Date entry_date) {
		this.entry_date = entry_date;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setWage_change(Integer wage_change) {
		this.wage_change = wage_change;
	}

	public void setBase_salary(Integer base_salary) {
		this.base_salary = base_salary;
	}

	public void setMagnification(float magnification) {
		this.magnification = magnification;
	}

	public int hashCode() { // 重写哈希方法
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(Object obj) { // 重写判断方法，判断是否为同一个员工
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String[] to_string() {
        return new String[]{ Integer.toString(id), name, Integer.toString(age), gender, identity, education,
				phone_number, e_mail, card_number, marriage_status, department, post, title, String.valueOf(entry_date),
				Integer.toString(wage_change), Integer.toString(base_salary), sign, code, String.valueOf(magnification)};
	}

}

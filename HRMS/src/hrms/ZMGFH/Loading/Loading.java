package hrms.ZMGFH.Loading;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.arcsoft.face.FaceFeature;

import hrms.ZMGFH.Objection.Employee;
import hrms.ZMGFH.Objection.User;
import hrms.ZMGFH.Objection.Worktime;
import hrms.ZMGFH.Service.FaceService;
import hrms.ZMGFH.Service.HRService;
import hrms.ZMGFH.Service.ImageService;
import hrms.ZMGFH.Tools.JDBCTools;

public class Loading {
	public static User user = null;//当前登录的管理员
	public static Employee employee = null;//当前需要录入的员工
	public static Worktime worktime = null;//当前作息时间
	public static BufferedImage image = null; //当前录入员工的图片信息
	public static FaceFeature faceFeature = null;//当前录入员工的图片特征
	public static Set<Employee> employees = null;//指定要求的员工集合
	public static final HashSet<Employee> EMP_SET = new HashSet<>();//全部员工信息
	public static final HashMap<String, FaceFeature> FACE_FEATURE_MAP = new HashMap<>();//全部人脸特征
	public static final HashMap<String, BufferedImage> IMAGE_MAP = new HashMap<>();//全部人脸图像
	public static final HashMap<Integer, Set<Date>> RECORD_MAP = new HashMap<>();
	
	public static void init() {
		HRService.GetWorktime();
		HRService.LoadAllEmp();//加载所有员工
		HRService.LoadAllClockInRecord();//加载所有打卡记录
		ImageService.LoadAllImage();//加载所有人脸图像文件
		FaceService.LoadAllface();//加载所有人脸特征
	}
	
	public static void release() {
		FaceService.release();
		JDBCTools.closeConnection();
	}
	
	public static void exit() {
		user = null;
		release();
	}

	public static void Delete_Emp(){
		employee = null;
		image = null;
		faceFeature = null;
	}

	public static void Delete_employees(){
		employees = null;
	}
	
}

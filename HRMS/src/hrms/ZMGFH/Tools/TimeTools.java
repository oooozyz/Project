package hrms.ZMGFH.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeTools {
	// 获取当前时间
	public static String nowtime() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
	// 获取当前日期
	public static String nowdate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
	// 获取当前日期时间
	public static String nowdatetime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	// 获取指定年月的最后一天
	public static int lastday(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	//判断具体某一天是否合理
	public static boolean Judgeday(int year, int month, int day) {
		if (month < 1 || month > 12) {
			return false;
		}
		if (day < lastday(year, month)) {
			return true;
		} else {
			return false;
		}
	}
	// 将当前年月日时分秒放入数组中
	public static Integer[] now() {
		Integer nowtime[] = new Integer[6];
		Calendar calendar = Calendar.getInstance();
		nowtime[0] = calendar.get(Calendar.YEAR);
		nowtime[1] = calendar.get(Calendar.MONTH) + 1;
		nowtime[2] = calendar.get(Calendar.DAY_OF_MONTH);
		nowtime[3] = calendar.get(Calendar.HOUR_OF_DAY);
		nowtime[4] = calendar.get(Calendar.MINUTE);
		nowtime[5] = calendar.get(Calendar.SECOND);
		return nowtime;
	}
	// 将yyyy-MM-dd格式转化为Date对象
	public static Date Standardization(String datetime) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime);
	}
	// 创建指定时间的Date对象
	public static Date Standardization(int year, int month, int day, String time) throws ParseException {
		String datetime = String.format("%4d-%02d-%02d %s", year, month, day, time);
		return Standardization(datetime);
	}
	// 检查时间符合格式
	public static boolean CheckTimeFormat(String time) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		try {
			simpleDateFormat.parse(time);
			return true;
		} catch (ParseException e) {// 解析数据异常
			return false;
		}
	}
}

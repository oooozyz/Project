package hrms.ZMGFH.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import hrms.ZMGFH.Database.Data_Obtain;
import hrms.ZMGFH.Loading.Loading;
import hrms.ZMGFH.Objection.Detail;
import hrms.ZMGFH.Objection.Employee;
import hrms.ZMGFH.Objection.User;
import hrms.ZMGFH.Objection.Worktime;
import hrms.ZMGFH.Tools.TimeTools;

public class HRService {
    private static final String CLOCK_IN = "I";// 正常上班打卡标记
    private static final String CLOCK_OUT = "O";// 正常下班打卡标记
    private static final String LATE = "L";// 迟到标记
    private static final String LEFT_EARLY = "E";// 早退标记
    private static final String ABSENT = "A";// 缺席标记

    private static Data_Obtain DO = new Data_Obtain();

    // 加载所有员工
    public static void LoadAllEmp() {
        Loading.EMP_SET.clear();
        Loading.EMP_SET.addAll(DO.GetAllEmp());
    }

    // 查找指定id的员工
    public static Employee FindEmp(int id, boolean flag) {
        return DO.getEmp(id, flag);
    }

    //员工更改个人信息
    public static boolean UpdateMessage_p(Map<String, String> M, int id) {
        return DO.UpdateEmpMessage_p(M, id);
    }

    //管理员更改员工的任何信息
    public static boolean UpdateMessage_a(Map<String, String> M, int id) {
        return DO.UpdateEmpMessage_a(M, id);
    }

    // 管理员登录
    public static boolean adminLogin(String username, String password) {
        User user = new User(-1, username, password, 0);
        if (DO.adminLogin(user)) {
            Loading.user = user;
            return true;
        } else {
            return false;
        }
    }

    // 员工登录
    public static boolean userLogin(String username, String password) {
        User user = new User(-2, username, password, 1);
        int judge = DO.userLogin(user);
        if (judge != 0) {
            user.setId(judge);
            Loading.user = user;
            return true;
        } else {
            return false;
        }
    }

    //录入一个员工
    public static boolean AddEmp(String username, String password) {
        return DO.AddEmp_p(Loading.employee) && DO.AddEmp_a(Loading.employee) && DO.AddEmp_user(username, password);
    }

    //获取指定要求的员工
    public static Set<Employee> GetConEmp(Map<String, String> M, int agefrom, int ageto, Date entryfrom, Date entryto, int money, boolean flag) {
        return DO.GetConEmp(M, agefrom, ageto, entryfrom, entryto, money, flag);
    }

    //获取所有员工所有信息
    public static Set<Employee> GetAllEmp() {
        return DO.GetAllEmp();
    }

    //获取所有员工
    public static Set<Employee> GetAllEmployee() {
        return DO.GetAllEmployee();
    }

    //获取指定部门的所有职位
    public static ArrayList<String> Post(String s) {
        return DO.Post(s);
    }

    //获取指定奖金/罚款的全部理由
    public static ArrayList<String> Reason(String s) {
        return DO.Reason(s);
    }

    //工资明细
    public static boolean Wage_detail(Detail detail) {
        return DO.Wage_change(detail);
    }

    //修改员工的工资变化值
    public static boolean Modify_Change(int id, int change) {
        return DO.Modify_Change(id, change);
    }

    //获取工资明细
    public static ArrayList<Detail> Wage_Detail(int id, int year, int month) {
        return DO.Wage_Detail(id, year, month);
    }

    //获取作息时间
    public static void GetWorktime() {
        Loading.worktime = DO.GetWorktime();
    }

    //更改作息时间
    public static boolean UpdateWorkTime(Worktime worktime) {
        boolean flag = DO.UpdateWorktime(worktime);
        if (flag) {
            Loading.worktime = worktime;
        }
        return flag;
    }

    //获取某一天所有员工的打卡数据
    private static Map<Employee, String> getOneDayRecordData(int year, int month, int day) {
        Map<Employee, String> record = new HashMap<>();// 键为员工对象，值为考勤标记
        // 时间点
        java.util.Date zeroTime = null, noonTime = null, lastTime = null, workTime = null, closingTime = null;
        try {
            // 零点
            zeroTime = TimeTools.Standardization(year, month, day, "00:00:00");
            // 中午12点
            noonTime = TimeTools.Standardization(year, month, day, "12:00:00");
            // 一天中最后一秒
            lastTime = TimeTools.Standardization(year, month, day, "23:59:59");
            Worktime wt = Loading.worktime;
            // 上班时间
            workTime = TimeTools.Standardization(year, month, day, wt.getStart());
            // 下班时间
            closingTime = TimeTools.Standardization(year, month, day, wt.getEnd());
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        for (Employee e : Loading.EMP_SET) {// 遍历所有员工
            if (!e.getSign().equals("在职") && !e.getSign().equals("临时员工")) {
                continue;
            }
            StringBuilder report = new StringBuilder();// 员工打卡记录，初始为空
            // 如果打卡记录中存在该员工的记录
            if (Loading.RECORD_MAP.containsKey(e.getId())) {
                boolean isAbsent = true;// 默认为缺席状态
                // 获取该员工的所有打卡记录
                Set<java.util.Date> lockinSet = Loading.RECORD_MAP.get(e.getId());
                for (java.util.Date r : lockinSet) {// 遍历所有打卡记录
                    // 如果员工在此日期内有打卡记录
                    if (r.after(zeroTime) && r.before(lastTime)) {
                        isAbsent = false;// 不缺席
                        // 上班前打卡
                        if (r.before(workTime) || r.equals(workTime)) {
                            report.append(CLOCK_IN);// 追加上班正常打卡标记
                        }
                        // 下班后打卡
                        if (r.after(closingTime) || r.equals(closingTime)) {
                            report.append(CLOCK_OUT);// 追加下班正常打卡标记
                        }
                        // 上班后，中午前打卡
                        if (r.after(workTime) && r.before(noonTime)) {
                            report.append(LATE);// 追加迟到标记
                        }
                        // 中午后，下班前打卡
                        if (r.after(noonTime) && r.before(closingTime)) {
                            report.append(LEFT_EARLY);// 追加早退标记
                        }
                    }
                }
                if (isAbsent) {// 此人在此日期没有打卡记录
                    report = new StringBuilder(ABSENT);// 指定为缺席标记
                }
            } else {// 如果打卡记录里没有此人记录
                report = new StringBuilder(ABSENT);// 指定为缺席标记
            }
            record.put(e, report.toString());// 保存该员工的打卡记录
        }
        return record;
    }

    //获取日报报表
    public static String getDayReport(int year, int month, int day) {
        Set<String> lateSet = new HashSet<>();// 迟到名单
        Set<String> leftSet = new HashSet<>();// 早退名单
        Set<String> absentSet = new HashSet<>();// 缺席名单
        Set<String> SickSet = new HashSet<>();// 病假名单
        Set<String> VacationSet = new HashSet<>();// 休假名单

        Map<Employee, String> record = HRService.getOneDayRecordData(year, month, day);
        for (Employee e : record.keySet()) {// 遍历每一个员工
            if (e.getSign().equals("休假")) {
                SickSet.add(e.getName());
            } else if (e.getSign().equals("病假")) {
                VacationSet.add(e.getName());
            } else if (!e.getSign().equals("辞退")) {
                String oneRecord = record.get(e);// 获取该员工的考勤标记
                // 如果有迟到标记，并且没有正常上班打卡标记
                if (oneRecord.contains(LATE) && !oneRecord.contains(CLOCK_IN)) {
                    lateSet.add(e.getName());// 添加到迟到名单
                }
                // 如果有早退标记，并且没有正常下班打卡标记
                if (oneRecord.contains(LEFT_EARLY) && !oneRecord.contains(CLOCK_OUT)) {
                    leftSet.add(e.getName());// 添加到早退名单
                }
                // 如果有缺席标记
                if (oneRecord.contains(ABSENT)) {
                    absentSet.add(e.getName());// 添加到缺席名单
                }
            }
        }

        StringBuilder report = new StringBuilder();// 报表字符串
        int count = 0;// 获取员工人数
        int Sum = 0;
        for (Employee e : Loading.EMP_SET) {
            if (!e.getSign().equals("辞退")) {
                Sum++;
            }
            if (e.getSign().equals("在职")) {
                count++;
            }
        }
        // 拼接报表内容
        report.append("-----  " + year + "年" + month + "月" + day + "日  -----\n");
        report.append("总人数：" + Sum + "\n");
        report.append("应到人数：" + count + "\n");

        report.append("缺席人数：" + absentSet.size() + "\n");
        report.append("缺席名单：");
        if (absentSet.isEmpty()) {// 如果缺席名单是空的
            report.append("（空）\n");
        } else {
            // 创建缺席名单的遍历对象
            Iterator<String> it = absentSet.iterator();
            while (it.hasNext()) {// 遍历名单
                // 在报表中添加缺席员工的名字
                report.append(it.next() + " ");
            }
            report.append("\n");
        }

        report.append("迟到人数：" + lateSet.size() + "\n");
        report.append("迟到名单：");
        if (lateSet.isEmpty()) {// 如果迟到名单是空的
            report.append("（空）\n");
        } else {
            // 创建迟到名单的遍历对象
            Iterator<String> it = lateSet.iterator();
            while (it.hasNext()) {// 遍历名单
                // 在报表中添加迟到员工的名字
                report.append(it.next() + " ");
            }
            report.append("\n");
        }

        report.append("早退人数：" + leftSet.size() + "\n");
        report.append("早退名单：");
        if (leftSet.isEmpty()) {// 如果早退名单是空的
            report.append("（空）\n");
        } else {
            // 创建早退名单的遍历对象
            Iterator<String> it = leftSet.iterator();
            while (it.hasNext()) {// 遍历名单
                // 在报表中添加早退员工的名字
                report.append(it.next() + " ");
            }
            report.append("\n");
        }

        report.append("休假人数：" + VacationSet.size() + "\n");
        report.append("休假名单：");
        if (VacationSet.isEmpty()) {// 如果早退名单是空的
            report.append("（空）\n");
        } else {
            // 创建早退名单的遍历对象
            Iterator<String> it = VacationSet.iterator();
            while (it.hasNext()) {// 遍历名单
                // 在报表中添加早退员工的名字
                report.append(it.next() + " ");
            }
            report.append("\n");
        }

        report.append("病假人数：" + SickSet.size() + "\n");
        report.append("病假名单：");
        if (SickSet.isEmpty()) {// 如果早退名单是空的
            report.append("（空）\n");
        } else {
            // 创建早退名单的遍历对象
            Iterator<String> it = SickSet.iterator();
            while (it.hasNext()) {// 遍历名单
                // 在报表中添加早退员工的名字
                report.append(it.next() + " ");
            }
            report.append("\n");
        }

        return report.toString();
    }

    public static void LoadAllClockInRecord() {
        // 获取打卡记录数据
        String[][] record = DO.getAllClockInRecord();
        if (record == null) {// 如果数据库中不存在打卡数据
            System.err.println("表中无打卡数据");
            return;
        }
        // 遍历所有打卡记录
        for (int i = 0, length = record.length; i < length; i++) {
            String[] r = record[i];// 获取第i行记录
            Integer id = Integer.valueOf(r[0]);// 获取员工编号
            // 如果全局会话中没有该员工的打卡记录
            if (!Loading.RECORD_MAP.containsKey(id)) {
                // 为该员工添加空记录
                Loading.RECORD_MAP.put(id, new HashSet<>());
            }
            try {
                // 日期时间字符串转为日期对象
                java.util.Date recodeDate = TimeTools.Standardization(r[1]);
                // 在该员工的打卡记录中添加新的打卡时间
                Loading.RECORD_MAP.get(id).add(recodeDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    //获取指定特征码的员工
    public static Employee getConEmp(String code) {
        return DO.getConEmp(code);
    }

    public static boolean addClockInRecord(Employee e) throws ParseException {
        java.util.Date now = new java.util.Date();// 当前时间
        // 为该员工添加当前时间的打卡记录
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12); // 设置小时为12点
        calendar.set(Calendar.MINUTE, 0);       // 设置分钟为0
        calendar.set(Calendar.SECOND, 0);       // 设置秒为0
        calendar.set(Calendar.MILLISECOND, 0);  // 设置毫秒为0

        java.util.Date noonTime = calendar.getTime();     // 获取设置后的Date对象
        String s="";
        if(now.before(noonTime)) {
            s="签到";
        }else{
            s="签离";
        }
        if(DO.Search_Clock(e.getId(),now,s)){
            return false;
        }
        Worktime worktime = DO.GetWorktime();
        String Start = worktime.getStart();
        String End = worktime.getEnd();
        String nowTime = now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        java.util.Date start = sdf.parse(Start);
        java.util.Date end = sdf.parse(End);
        java.util.Date nowDate = sdf.parse(nowTime);
        LocalDate localDate = LocalDate.now();
        java.sql.Date date = java.sql.Date.valueOf(localDate);
        if(s.equals("签到")&& nowDate.after(start)){
            Detail detail = new Detail(e.getId(),date,-10,"迟到");
            Wage_detail(detail);
            Modify_Change(e.getId(), -10);
        }else if(s.equals("签离")&&nowDate.before(end)){
            Detail detail = new Detail(e.getId(),date,-10,"早退");
            Wage_detail(detail);
            Modify_Change(e.getId(), -10);
        }
        boolean b = DO.addCLockInRecord(e.getId(), now,s);
        // 如果全局会话中没有该员工的打卡记录
        if (!Loading.RECORD_MAP.containsKey(e.getId())) {
            // 为该员工添加空记录
            Loading.RECORD_MAP.put(e.getId(), new HashSet<>());
        }
        // 在该员工的打卡记录中添加新的打卡时间
        Loading.RECORD_MAP.get(e.getId()).add(now);
        return b;
    }

    //判断是否需要发工资
    public static void GetPiad() {
        Integer[] now = TimeTools.now();
        Date date = DO.getsalarytime();
        LocalDate localDate = LocalDate.now();
        Date datenow = Date.valueOf(localDate);
        boolean whether = false;
        String judge = DO.getwhether();
        int op = date.getMonth();
        if (now[0] > date.getYear()+1900) {
            whether = true;
        } else if (now[1] > date.getMonth()+1) {
            whether = true;
        }
        if (whether && judge.equals("是")) {
            for (Employee e : Loading.EMP_SET) {
                if (!e.getSign().equals("辞退")) {
                    e.setWage_change(0);
                    int salary = (int) (e.getBase_salary() * e.getMagnification());
                    Detail detail = new Detail(e.getId(), datenow, salary, "每月工资");
                    DO.Wage_change(detail);
                }
            }
            DO.Updatesalarytime(datenow);
        }
    }
    //
}
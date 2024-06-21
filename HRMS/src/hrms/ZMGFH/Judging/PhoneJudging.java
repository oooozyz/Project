package hrms.ZMGFH.Judging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneJudging {
	private final Pattern pattern;
    private static final String Phone_PATTERN = "1[345789]\\d{9}";
	
	public PhoneJudging() {
		pattern = Pattern.compile(Phone_PATTERN);
	}
	
	public boolean CheckPhone(final String phone) {
        Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}
}

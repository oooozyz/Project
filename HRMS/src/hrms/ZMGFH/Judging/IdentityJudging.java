package hrms.ZMGFH.Judging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hrms.ZMGFH.Tools.TimeTools;

public class IdentityJudging {
	private final Pattern pattern;
    private static final String IDCARD_PATTERN = "^(\\d{6})(19|20)(\\d{2})(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])(\\d{3})([0-9Xx])$";// 身份证号正则表达式

	public IdentityJudging() {
		pattern = Pattern.compile(IDCARD_PATTERN);
	}

	public boolean CheckIdentity(final String Identity) {
		if (Identity.length() != 18) {
			return false;
		}
        Matcher matcher = pattern.matcher(Identity);
		if (!matcher.matches()) {
			return false;
		}
		int birthyear = Integer.parseInt(Identity.substring(6, 10));
		int birthmonth = Integer.parseInt(Identity.substring(10, 12));
		int birthday = Integer.parseInt(Identity.substring(12, 14));
		if(!TimeTools.Judgeday(birthyear,birthmonth,birthday)) {
			return false;
		}

		char[] charArray = Identity.toCharArray();
		int[] weightFactors = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 }; // 权重
		char[] checkCodes = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' }; // 检验码
		int sum = 0;
		for (int i = 0; i < 17; i++) {
			sum += (charArray[i] - '0') * weightFactors[i];
		}
		int index = sum % 11;
		char checkCode = checkCodes[index];
		return true;
		//if (checkCode == charArray[17]) {
		//	return true; // 校验码正确
		//} else {
		//	return false; // 校验码错误
		//}

	}
}

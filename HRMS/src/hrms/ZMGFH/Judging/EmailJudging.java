package hrms.ZMGFH.Judging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailJudging {
	private final Pattern pattern;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";//邮箱正则表达式

	public EmailJudging() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	public boolean CheckEmail(final String email) {
        Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
}

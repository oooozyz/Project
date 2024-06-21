package hrms.ZMGFH.Judging;

public class BankJudging {
	public boolean CheckBank(String bankCardNumber) {

		bankCardNumber = bankCardNumber.replaceAll("\\s", ""); // 去除可能存在的空格
		// 判断银行卡号长度是否在合理范围内
		int length = bankCardNumber.length();
		if (length < 16 || length > 19) {
			return false;
		}

		return luhnCheck(bankCardNumber); // 使用Luhn算法进行校验
	}

	private static boolean luhnCheck(String bankNumber) {
		int sum = 0;
		boolean judge = false;
		for (int i = bankNumber.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(bankNumber.substring(i, i + 1));
			if (judge) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			judge = !judge;
		}
		return (sum % 10) == 0;
	}
	// 通过对数字进行特定的数学运算，然后检查结果是否符合预期的模式。其步骤包括：从右到左，从校验位开始，跳过校验位，对每个其他位进行双倍运算。如果双倍运算的结果大于9，则将结果减去9。然后，将所有数字（包括校验位）加在一起。如果总和是10的倍数，则号码有效
}

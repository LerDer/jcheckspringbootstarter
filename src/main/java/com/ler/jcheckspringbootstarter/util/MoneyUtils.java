package com.ler.jcheckspringbootstarter.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoneyUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoneyUtils.class);


	private static String HanDigiStr[] = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆",
			"柒", "捌", "玖"};

	private static String HanDiviStr[] = new String[]{"", "拾", "佰", "仟", "万", "拾", "佰", "仟",
			"亿", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万",
			"拾", "佰", "仟"};


	/**
     * 把分转换为两位小数点的字符串
     * <p/>
     * 不能直接用double/100.0D，这样可能会出现科学计数法表示的数字
     */
    public static String convertFenToYuanStr(long feeX100) {
        return BigDecimal.valueOf(feeX100).divide(BigDecimal.valueOf(100), 2, RoundingMode.UNNECESSARY).toString();
    }

    /**
     * 元转换为分
     */
    public static long convertYuanToFen(Double yuanFee) {
        yuanFee *= 100;
        return yuanFee.longValue();
    }

    /**
     * 把yuan 的字符串转化成分
     */
    public static Long convertYuanStrToFen(String yuanStr) {
        if (StringUtils.isBlank(yuanStr)) {
            return 0L;
        }
        try {
            boolean nativeNum = false;
            String currency = yuanStr.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额
            if (StringUtils.startsWith(currency, "-")) {
                nativeNum = true;
                currency = currency.replace("-", "");
            }
            int index = currency.indexOf(".");
            int length = currency.length();
            Long amLong;
            if (index == -1) {
                amLong = Long.valueOf(currency + "00");
            } else if (length - index > 3) {
                int third = Integer.valueOf(currency.substring(index + 3, index + 4));
                amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
                if (third >= 5) {
                    amLong += 1;
                }
            } else if (length - index == 3) {
                amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
            } else if (length - index == 2) {
                amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
            } else {
                amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
            }
            if (nativeNum) {
                return 0 - amLong;
            }
            return amLong;
        } catch (Throwable t) {
            // 可能有 deliveryFee=null 的情况
            LOGGER.error("convertYuanStrToFen failed, yuanStr={}", yuanStr, t);
            return null;
        }
    }

    public static Long convertWanYuanString2Fen(String wanyuan) {
        BigDecimal bigDecimal = new BigDecimal(wanyuan);
        String s = bigDecimal.multiply(new BigDecimal("10000")).toString();
        return convertYuanStrToFen(s);
    }

    public static Long convertYuanDoubleToFen(Double yuanDouble) {
        Long result = convertYuanStrToFen(String.valueOf(yuanDouble));
        if (result == null) {
            LOGGER.error("convertYuanDoubleToFen failed, yuanDouble={}", yuanDouble);
        }
        return result;
    }

    public static Long convertBigDecimalYuanToFen(BigDecimal priceInYuan) {
        BigDecimal priceInFen = priceInYuan.multiply(new BigDecimal(100));
        return priceInFen.longValue();
    }

	public static String getDouble2RMB(Double doubleValue) {

		String signStr = "";
		String tailStr = "";
		long fraction, integer;
		int jiao, fen;

		if (doubleValue < 0) {
			doubleValue = -doubleValue;
			signStr = "负";
		}
		if (doubleValue > 99999999999999.999
				|| doubleValue < -99999999999999.999) {
			return "金额数值位数过大!";
		}
		// 四舍五入到分
		long temp = Math.round(doubleValue * 100);
		integer = temp / 100;
		fraction = temp % 100;
		jiao = (int) fraction / 10;
		fen = (int) fraction % 10;
		if (jiao == 0 && fen == 0) {
			tailStr = "整";
		} else {
			tailStr = HanDigiStr[jiao];
			if (jiao != 0) {
				tailStr += "角";
			}
			// 零圆后不写零几分
			if (integer == 0 && jiao == 0) {
				tailStr = "";
			}
			if (fen != 0) {
				tailStr += HanDigiStr[fen] + "分";
			}
		}

		return (doubleValue >= 1) ? (signStr + positiveIntegerToHanString(String.valueOf(integer))
				+ "圆" + tailStr) : tailStr;
	}

	private static String positiveIntegerToHanString(String numberStr) {
		StringBuilder rMBStr = new StringBuilder();
		boolean lastzero = false;
		// 亿、万进位前有数值标记
		boolean hasvalue = false;
		int len, n;
		len = numberStr.length();
		if (len > 15) {
			return "金额过大!";
		}
		for (int i = len - 1; i >= 0; i--) {
			if (numberStr.charAt(len - i - 1) == ' ') {
				continue;
			}
			n = numberStr.charAt(len - i - 1) - '0';
			if (n < 0 || n > 9) {
				return "金额含非数字字符!";
			}

			if (n != 0) {
				if (lastzero) {
					// 若干零后若跟非零值，只显示一个零
					rMBStr.append(HanDigiStr[0]);
				}
				// 除了亿万前的零不带到后面
				// if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) ) //
				// 如十进位前有零也不发壹音用此行
//              if (!(n == 1 && (i % 4) == 1 && i == len - 1)) // 十进位处于第一位不发壹音
				rMBStr.append(HanDigiStr[n]);
				// 非零值后加进位，个位为空
				rMBStr.append(HanDiviStr[i]);
				// 置万进位前有值标记
				hasvalue = true;

			} else {
				// 亿万之间必须有非零值方显示万
				if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue)) {
					// “亿”或“万”
					rMBStr.append(HanDiviStr[i]);
				}
			}
			if (i % 8 == 0) {
				// 万进位前有值标记逢亿复位
				hasvalue = false;
			}
			lastzero = (n == 0) && (i % 4 != 0);
		}

		if (rMBStr.length() == 0) {
			// 输入空字符或"0"，返回"零"
			return HanDigiStr[0];
		}
		return rMBStr.toString();
	}

}

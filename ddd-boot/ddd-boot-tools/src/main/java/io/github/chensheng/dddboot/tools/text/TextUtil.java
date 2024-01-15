package io.github.chensheng.dddboot.tools.text;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil extends StringUtils {
	public static final String EMPTY_STRING = "";

	public static String concatByComma(String[] arr) {
	    if (arr == null || arr.length == 0) {
	        return EMPTY_STRING;
        }

        return concatByComma(Arrays.asList(arr));
    }

    public static String concatByComma(List<String> strList) {
	    if (strList == null) {
	        return EMPTY_STRING;
        }

        StringBuilder result = new StringBuilder();
        for (String str : strList) {
            result.append(str);
            result.append(",");
        }

        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
	
	public static String[] splitByComma(String value) {
		if (value == null) {
			return null;
		}
		return value.split(",");
	}

	public static boolean isNumberWord(String keyWord) {
		boolean flag = false;
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(keyWord)) {
			Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
			Matcher matcher = pattern.matcher(keyWord);
			flag = matcher.matches();
		}
		return flag;
	}

	public static boolean isNumber(String keyWord) {
		int index = keyWord.indexOf(".");
		if (index > -1) {
			String num1 = keyWord.substring(0, index);
			String num2 = keyWord.substring(index + 1);
			return org.apache.commons.lang3.StringUtils.isNumeric(num1) && org.apache.commons.lang3.StringUtils.isNumeric(num2);
		} else {
			return org.apache.commons.lang3.StringUtils.isNumeric(keyWord);
		}

	}

	public static boolean isW(String keyWord) {
		boolean flag = false;
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(keyWord)) {
			Pattern pattern = Pattern.compile("^\\w+$");
			Matcher matcher = pattern.matcher(keyWord);
			flag = matcher.matches();
		}
		return flag;
	}

	public static boolean isCNWord(String keyWord) {

		boolean flag = false;
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(keyWord)) {
			Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5]+$");
			Matcher matcher = pattern.matcher(keyWord);
			flag = matcher.matches();
		}
		return flag;
	}

	public static int strPlaceHold(String keyWord) {

		int keyWordLength = 0;
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(keyWord)) {
			Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");
			Matcher matcher = pattern.matcher(keyWord.trim());
			while (matcher.find()) {
				keyWordLength += 1;
			}
			keyWordLength = keyWord.length() + keyWordLength;
		}
		return keyWordLength;
	}

	public static boolean isMobile(String keyWord) {
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(keyWord)) {
			Pattern pattern = Pattern.compile("^1[3456789][0-9]{9}$");
			Matcher matcher = pattern.matcher(keyWord);
			return matcher.matches();
		}
		return false;
	}

	public static boolean isPhoneNum(String keyWord) {
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(keyWord)) {
			Pattern pattern = Pattern.compile("^[0]{1}[0-9]{2,3}-[0-9]{7,8}$");
			Matcher matcher = pattern.matcher(keyWord);
			return matcher.matches();
		}
		return false;
	}

	public static boolean isEmail(String email) {
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(email)) {
			Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
			Matcher matcher = pattern.matcher(email);
			return matcher.matches();
		}
		return false;
	}
	
	public static String substringAfter(String rawString, String mark) {
		if (rawString == null || mark == null) {
			return null;
		}
		
		int markIndex = rawString.indexOf(mark);
		if (markIndex <= 0) {
			return rawString;
		}
		
		return rawString.substring(markIndex);
	}
	
	public static String substringBefore(String rawString, String mark) {
		if (rawString == null || mark == null) {
			return null;
		}
		
		int markIndex = rawString.indexOf(mark);
		if (markIndex < 0 || markIndex == rawString.length() - 1) {
			return rawString;
		}
		
		return rawString.substring(0, markIndex);
	}
	
	public static String getRandomStr() {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		ThreadLocalRandom random = ThreadLocalRandom.current();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	public static long toLong(String value, long defaultVal) {
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return defaultVal;
		} catch (NullPointerException e) {
			return defaultVal;
		}
	}
	
	public static Object parseToPrimitive(String value, Class<?> type) {
		if (value == null) {
			return null;
		}
		
		try {
			if (type == String.class) {
				return value;
			} if (type == Boolean.class) {
				return Boolean.valueOf(value);
			} else if (type == boolean.class) {
				return Boolean.valueOf(value).booleanValue();
			} else if (type == Byte.class) {
				return Byte.valueOf(value);
			} else if (type == byte.class) {
				return Byte.valueOf(value).byteValue();
			} else if (type == Character.class || type == char.class) {
				return value.length() > 0 ? value.charAt(0) : null;
			} else if (type == Short.class) {
				return Short.valueOf(value);
			} else if (type == short.class) {
				return Short.valueOf(value).shortValue();
			} else if (type == Integer.class) {
				return Integer.valueOf(value);
			} else if (type == int.class) {
				return Integer.valueOf(value).intValue();
			} else if (type == Long.class) {
				return Long.valueOf(value);
			} else if (type == long.class) {
				return Long.valueOf(value).longValue();
			} else if (type == Float.class) {
				return Float.valueOf(value);
			} else if (type == float.class) {
				return Float.valueOf(value).floatValue();
			} else if (type == Double.class) {
				return Double.valueOf(value);
			} else if (type == double.class) {
				return Double.valueOf(value).doubleValue();
			}
		} catch (Exception e) {
		}
		
		return null;
	}
	
	public static String filterEmoji(String source) {
		if (isEmpty(source)) {
			return source;
		}
		
		source = source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
		if (!containsEmoji(source)) {
			return source;
		}
		
		StringBuilder buf = null;
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (isEmojiCharacter(codePoint)) {
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}
				buf.append(codePoint);
			} else {
				buf.append("*");
			}
		}
		
		if (buf == null) {
			return source;
		} 
		
		if (buf.length() == len) {
			buf = null;
			return source;
		}
		
		return buf.toString();
	}
	
	public static boolean containsEmoji(String source) {
		if (org.apache.commons.lang3.StringUtils.isBlank(source)) {
			return false;
		}
		
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i); 
			if (isEmojiCharacter(codePoint)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) 
				|| (codePoint == 0x9) 
				|| (codePoint == 0xA) 
				|| (codePoint == 0xD) 
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) 
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) 
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	} 
	
	public static String maskMobile(String mobile) {
		if (!isMobile(mobile)) {
			return mobile;
		}
		
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < mobile.length(); i++) {
			if (i == 3 || i == 4 || i == 5 || i == 6) {
				result.append("*");
			} else {
				result.append(mobile.charAt(i));
			}
		}
		return result.toString();
	}

	public static String underscoreToCamel(String str) {
		if (str == null) {
			return null;
		}

		if (!str.contains("_")) {
		    return str;
        }

		StringBuilder result = new StringBuilder();
		String[] unitArr = str.split("_");
		for (int i = 0; i < unitArr.length; i++) {
			String unit = unitArr[i];

			if (i == 0) {
				result.append(unit);
				continue;
			}

			char firstChar = unit.charAt(0);
			String formattedUnit = String.valueOf(firstChar).toUpperCase();
			if (unit.length() > 1) {
				formattedUnit += unit.substring(1);
			}
			result.append(formattedUnit);
		}

		return result.toString();
	}

	public static String camelToUnderscore(String str) {
	    if (str == null) {
	        return null;
        }

        String[] words = splitByCharacterTypeCamelCase(str);
	    StringBuilder result = new StringBuilder();
	    for (String word : words) {
	        if (isEmpty(word)) {
	            continue;
            }
            result.append(word);
	        result.append("_");
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
	    return result.toString();
    }
}

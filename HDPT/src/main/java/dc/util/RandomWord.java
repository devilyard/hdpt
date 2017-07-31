package dc.util;

import java.util.Random;

public class RandomWord {
	private static Random randGen = null;
	private static char[] numbersAndLetters = null;
	
	public static final String randomString(int length) {
		/**
		   * 产生随机字符串
		   * */

        if (length < 1) {
            return null;
        }
        if (randGen == null) {
               randGen = new Random();
               numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
                  "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
                 //numbersAndLetters = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
                }
        char [] randBuffer = new char[length];
        for (int i=0; i<randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
         //randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
        }
        return new String(randBuffer);
	}
	
	/**
	 * 生成随机密码带字符
	 *
	 * @param pwd_len
	 *            生成的密码的总长度
	 * @return 密码的字符串
	 */
	public static String genRandomStr(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度

		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		// char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}

		return pwd.toString();
	}
}

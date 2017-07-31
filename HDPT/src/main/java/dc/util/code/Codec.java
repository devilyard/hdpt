package dc.util.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * �ַ�����(����/����)��(����/����)��
 * 
 * ����
 * 
 * ��Base64�У��������[A-Z,a-z,0-9,+,/,=(pad)]��ɵġ� 
 * ������������[a-z,2-7]��ɵģ�
 * -----------------------------------------------
 * a b c d e f g h i j k  l  m  n  o  p  q  r 
 * 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17
 * -----------------------------------------------
 *  s  t  u  v  w  x  y  z  2  3  4  5  6  7
 * 18 19 20 21 22 23 24 25 26 27 28 29 30 31
 * ------------------------------------------------
 * 
 * ��Base64�У��ǽ�����������һ����Ȼ���ٰ�6λ���ָ�ָ������ǰ�油0����������˶�֪��������˵�ˡ�
 * ��������ڷָ����һ����΢�б䶯���ǰ�5λ���ָ����պù��֣��Ǿͺ��ˣ������������զ���أ�
 * 
 * ��Base64�У�����"="������İɡ�
 * �������������ǰ�油0��Ȼ���ں����ٲ��㡣
 * 
 * ���磺�ַ��� "aaa"��(����/����)���� "mfqwc"
 * 
 * �����ƣ�01100001 01100001 01100001
 * ת����(000)01100 (000)00101 (000)10000 (000)10110 (000)0001(0)
 * ʮ���ƣ�    12          5          16         22          2
 * ����Ӧ��   m           f          q          w           c
 * 
 * (����/����)�͸����ˣ�
 * 
 * ����Ӧ��    m       f        q        w       c
 * ʮ���ƣ�     12       5       16       22       2
 * �����ƣ� 00001100 00000101 00010000 00010110 00000010
 * ȥǰ0��01100 00101 10000 10110 00010
 * �ϲ��� 0110000101100001011000010
 * 
 * Ȼ��Ѻϲ���Ĵ��ĳ��ȳ�һ��8�����ֶ��˸�0��
 * 
 * �����ƣ�01100001 01100001 01100001 0
 * 
 * ���˾����ˣ���Ҫ�ˣ���ʵ����{����/����}�ķָ�ʱ���ڷ�ʣ�������ĺ��油��0����
 * Ȼ���ٽ� byte[] ת���ַ�����OK���ּ�"aaa"�ˡ�
 * 
 * ��һ��ֵ��ע��ģ�UTF-8��GBK��GB18030 һ�㶼ûʲô���⣬
 * ���� GB2312 �����ַ��������ḻ����������decode��ʱ����ʺ��ˡ�
 * 
 * 
 * @author gembler
 * @version 2008-12-3 ����03:01:50
 * 
 */
public class Codec {

	/**
	 * ���
	 */
	private final static String CODEC_TABLE = "abcdefghijklmnopqrstuvwxyz234567";

	/**
	 * ��ʾ5bit���ֽ�
	 */
	public final static int FIVE_BIT = 5;

	/**
	 * ��ʾ8bit���ֽ�
	 */
	public final static int EIGHT_BIT = 8;

	/**
	 * ��ʾ������
	 */
	public final static int BINARY = 2;

	/**
	 * (����/����)�ַ���������Ĭ�����Ի����� character set��
	 * 
	 * @author gembler
	 * @version 2008-12-3 ����03:14:36
	 * 
	 * @param keys
	 *            ��Ҫ(����/����)���ַ���
	 * 
	 * @return (����/����)����ַ���
	 */
	public static String encode(String keys) {

		return encode(keys, null);

	}

	/**
	 * (����/����)�ַ���
	 * 
	 * @author gembler
	 * @version 2008-12-3 ����03:14:39
	 * 
	 * @param keys
	 *            ��Ҫ(����/����)���ַ���
	 * @param characterSet
	 *            �ַ���
	 * 
	 * @return (����/����)����ַ���
	 */
	public static String encode(String keys, String characterSet) {

		if (keys == null || "".equals(keys)) {
			return "";
		}

		byte[] keyBytes = null;

		if (characterSet == null || characterSet.length() < 1) {

			// ����Ĭ�����Ի����� character set��
			keyBytes = keys.getBytes();

		} else {

			try {

				// ����ָ���� character set��
				keyBytes = keys.getBytes(characterSet);

			} catch (UnsupportedEncodingException e) {
				// ignore...
			}

		}
		return encode(keyBytes);
	}

	/**
	 * (����/����)�ֽ�����
	 * 
	 * @author gembler
	 * @version 2008-12-3 ����03:14:43
	 * 
	 * @param keyBytes
	 *            ��Ҫ(����/����)���ֽ�����
	 * 
	 * @return (����/����)����ַ���
	 */
	private static String encode(byte[] keyBytes) {

		if (keyBytes == null || keyBytes.length < 1) {

			return "";

		}

		/*
		 * �ϲ��������룬 
		 * �磺 
		 *     00101010 11010011 00101101 10100011 
		 *   to
		 *     00101010110100110010110110100011
		 */

		StringBuilder mergrd = new StringBuilder();

		for (int i = 0; i < keyBytes.length; i++) {

			FormatUtil.formatBinary(keyBytes[i], mergrd);

		}

		/*
		 * ��5��bitΪ��λ�������ֶܷ����飬
		 * �磺
         *     00101010110100110010110110100011
		 *   to
		 *     00101 01011 01001 10010 11011 01000 11
		 *                                          |
		 *                                   �����11Ϊ���µ�λ��
		 */

		int groupCount = mergrd.length() / FIVE_BIT;

		// �������µ�λ��
		int lastCount = mergrd.length() % FIVE_BIT;

		// �������ݷ�ҳ���㷨�����������������Ҫ�� 1��
		if (lastCount > 0) {

			groupCount += 1;

		}

		/*
		 * (����/����)
		 */

		StringBuilder sbEncoded = new StringBuilder();

		// ѭ�����������
		int forMax = groupCount * FIVE_BIT;

		// ÿ�ε���5λ����ȡ
		for (int i = 0; i < forMax; i += FIVE_BIT) {

			// ������
			int end = i + FIVE_BIT;

			/*
			 * �����������Ѻϲ��Ķ������봮�ĳ���Ҫ��
			 * �൱����������
             * ���ұ�ʾ��ǰѭ�����ˣ��Ѻϲ��Ķ������봮�ĳ��� % FIVE_BIT������һ�ء�
			 */

			// ����Ƿ�����������һ��
			boolean flag = false;

			if (end > mergrd.length()) {

				/*
				 * �����������Ѻϲ��Ķ������봮�ĳ���Ҫ��
				 * ��������Ҫ������Ϊ�� 
				 * �Ѻϲ��Ķ������봮�ĳ��ȣ��ȼ��ڣ�i + lastCount). ���������ǡ�
				 */

				end = (i + lastCount);

				flag = true;

			}

			// ��ȡ
			String strFiveBit = mergrd.substring(i, end);

			// ��ȡ��Ӷ�����תΪʮ����
			int intFiveBit = Integer.parseInt(strFiveBit, BINARY);

			if (flag) {

				/*
				 * �����������Ѻϲ��Ķ������봮�ĳ���Ҫ��
				 * �����ǵ�����������һ�أ� 
				 * ��Ҫ���Ʋ������������µĶ�����λΪ��11��
				 * ��ô��Ҫ�Ӻ��油0�����Ʋ�����Ϊ (000)11(000)
				 */

				intFiveBit <<= (FIVE_BIT - lastCount);

			}

			// ���ø�ʮ��������Ϊ����������ȡ��Ӧ���ַ�����׷�ӵ�sbEncoded
			sbEncoded.append(CODEC_TABLE.charAt(intFiveBit));

		}

		return sbEncoded.toString();

	}

	/**
	 * (����/����)�ַ���������Ĭ�����Ի����� character set��
	 * 
	 * @author gembler
	 * @version 2008-12-3 ����03:14:57
	 * 
	 * @param code
	 *            ��Ҫ(����/����)���ַ���
	 * 
	 * @return (����/����)����ַ���
	 */
	public static String decode(String code) {

		return decode(code, null);

	}

	/**
	 * (����/����)�ַ���
	 * 
	 * @author gembler
	 * @version 2008-12-3 ����03:15:00
	 * 
	 * @param code
	 *            ��Ҫ(����/����)���ַ���
	 * @param characterSet
	 *            �ַ���
	 * 
	 * @return (����/����)����ַ���
	 */
	public static String decode(String code, String characterSet) {

		if (code == null || code.length() < 1) {

			return "";

		}

		/*
		 * ���ÿһ���ַ�����������ȡ��Ӧ��������
		 */

		StringBuilder sbBinarys = new StringBuilder();

		for (int i = 0; i < code.length(); i++) {

			// ��������ȡ��Ӧ������
			int index = getCodecTableIndex(code.charAt(i));

			// ��ʮ���Ƶ�����ת��Ϊ�����ƴ�
			String indexBinary = Integer.toBinaryString(index);

			// ȥ��ǰ3��0������׷�ӵ�sbBinarys
			FormatUtil.formatBinary(indexBinary, sbBinarys, FIVE_BIT);

		}

		/*
		 * ��8��bit��֣�ʣ�µ������ӵ���
		 * �ӵ�����������(����/����)�ķָ�ʱ���ڷ�ʣ�������ĺ��油��0
		 */

		byte[] binarys = new byte[sbBinarys.length() / EIGHT_BIT];

		for (int i = 0, j = 0; i < binarys.length; i++) {

			// ÿ8��bit��ȡһ��
			String sub = sbBinarys.substring(j, j += EIGHT_BIT);

			// ����ȡ�����Ķ����ƴ�ת��Ϊʮ����
			Integer intBinary = Integer.valueOf(sub, BINARY);

			binarys[i] = intBinary.byteValue();

		}

		String decoded = null;

		if (characterSet == null || characterSet.length() < 1) {

			// ����Ĭ�����Ի����� character set��
			decoded = new String(binarys);

		} else {

			try {

				// ����ָ���� character set��
				return new String(binarys, characterSet);

			} catch (UnsupportedEncodingException e) {
				// ignore...
			}
		}
		return decoded;
	}

	/**
	 * �������������ַ�������CODEC_TABLE�����ض�Ӧ���±ꡣ
	 * ���û�ҵ����򷵻� -1��
	 * 
	 * @author gembler
	 * @version 2008-12-3 ����03:14:53
	 * 
	 * @param code
	 *            ��CODEC_TABLE��Χ�ڵ��ַ���
	 * 
	 * @return �ַ���CODEC_TABLE���Ӧ���±꣬���û�ҵ����򷵻� -1��
	 */
	private static int getCodecTableIndex(char code) {

		for (int i = 0; i < CODEC_TABLE.length(); i++) {

			if (CODEC_TABLE.charAt(i) == code) {

				return i;

			}

		}

		return -1;

	}

	/**
	 * ����
	 * 
	 * @author gembler
	 * @version 2008-12-3 ����03:05:52
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));

			while (true) {

				System.out.print("�����ַ��Ŵ���");

				String in = br.readLine();

				if ("exit".equalsIgnoreCase(in)) {
					break;
				}

				String enCode = Codec.encode(in);

				String deCode = Codec.decode(enCode);
				System.out.println();
				System.out.println("------------------------------test");
				System.out.println("original: " + in);
				System.out.println("encode: " + enCode);
				System.out.println("decode: " + deCode);
				System.out.println("------------------------------test");
				System.out.println();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
package dc.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64; 

public class SecurityUtil {

	private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish
	
	//keybyte为加密密钥，长度为24字节
    //src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
       try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

            //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    
    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {      
    try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    
  //计算24位长的密码byte值,首先对原始密钥做MD5算hash值，再用前8位数据对应补全后8位  
    
	public static byte[] getKeyBytes(String strKey) throws Exception {

		if (null == strKey || strKey.length() < 1) {
			throw new Exception("key is null or empty!");
		}
		java.security.MessageDigest alg = java.security.MessageDigest.getInstance("MD5");
		alg.update(strKey.getBytes());
		byte[] bkey = alg.digest();
		int start = bkey.length;
		byte[] bkey24 = new byte[24];
		for (int i = 0; i < start; i++) {
			bkey24[i] = bkey[i];
		}

		for (int i = start; i < 24; i++) {// 为了与.net16位key兼容
			bkey24[i] = bkey[i - start];
		}

		return bkey24;

	}
    
    //转换成十六进制字符串
    public static String byte2hex(byte[] b) {
        String hs="";
        String stmp="";

        for (int n=0;n<b.length;n++) {
            stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1) hs=hs+"0"+stmp;
            else hs=hs+stmp;
            if (n<b.length-1)  hs=hs+":";
        }
        return hs.toUpperCase();
    }
    
    /** 
     * 16进制字符串转换成byte数组 
     * @param 16进制字符串 
     * @return 转换后的byte数组 
     */  
    public static byte[] hex2byte(String str){
    	if(str==null){
    		return null;
    	}
    	str=str.trim();
    	int len = str.length();
    	if(len==0 || len %2 == 1){
    		return null;
    	}
    	byte[] b = new byte[len/2];
    	try{
    		for(int i = 0;i<str.length();i++){
    			b[i/2] = (byte)Integer.decode("0x"+str.substring(i,i+2)).intValue();
    		}
    		return b;
    	}catch(Exception e){
    		return null;
    	}
    }
    
    //转换成base64编码  
    public static String byte2Base64(byte[] b) {  
        return Base64.encode(b);  
    } 
    
    public static byte[] Base642byte(String str) {  
        return Base64.decode(str);
    }
    
}
package dc.user;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;

import dc.util.SystemArgsHolder;
import dc.util.code.MD5StringUtil;

@Component(value="userManager")
public class PropUserManager implements UserManager {
	private static String fileName ="WEB-INF/config/user.properties";
	private Properties props = new Properties();
	public PropUserManager(){
		InputStream in;
		try {
			in = new BufferedInputStream (new FileInputStream(SystemArgsHolder.getWEB_ROOT()+fileName));
			props.load(in);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	@Override
	public boolean validatePassword(String uid, String pwd) {
		if(props.containsKey(uid) && MD5StringUtil.MD5Encode(pwd).equals(props.get(uid))){
			return true;
		}
		return false;
	}

}

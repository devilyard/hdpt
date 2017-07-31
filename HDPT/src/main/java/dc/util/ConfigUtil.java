package dc.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class ConfigUtil
{
  private static final String DEFAULT_CONF_FILE = "WEB-INF/config/Params.properties";
  private static Properties config = new Properties();

  static {
    try {
    	 String url = ConfigUtil.class.getResource("").getPath().replaceAll("%20", " ");  
         String absolutePath = url.substring(0, url.indexOf("WEB-INF")) + DEFAULT_CONF_FILE;  
         InputStream in = new BufferedInputStream (new FileInputStream(absolutePath));;  
         config.load(in);
    }
    catch (Exception localException)
    {
    }
  }

  public static String getValue(String key) {
    return config.getProperty (key);
  }

}
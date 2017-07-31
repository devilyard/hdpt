package dc.util;

import org.apache.commons.lang.StringUtils;

import ctd.util.context.Context;
import ctd.util.exp.ExpRunner;

public class ConfigValueParser {
	
	public static String parse(String str,Context ctx){
		if(StringUtils.isEmpty(str)){
			return "";
		}
		if(ctx == null){
			return str;
		}
		str = str.trim();
		switch((int)str.charAt(0)){
			case 37: //%
				return ctx.value(str.substring(1));
			case 91: //[
				try {
					return String.valueOf(ExpRunner.run(str, new Context()));
				} 
				catch (Exception e) {
					return "";
				}
		}
		return str;
	}
	
}

package dc.util;


public class SystemArgsHolder {
	private static String WEB_ROOT_PATH;
	private static boolean WEB_ROOT_PATH_INITED = false;
	public static String getWEB_ROOT() {
		return WEB_ROOT_PATH;
	}
	public static void setWEB_ROOT(String wEBROOT) {
		if(!WEB_ROOT_PATH_INITED){
			WEB_ROOT_PATH = wEBROOT;
			WEB_ROOT_PATH_INITED = true;
		}
	}
}

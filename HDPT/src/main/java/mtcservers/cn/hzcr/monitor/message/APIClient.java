package mtcservers.cn.hzcr.monitor.message;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * 鐢ㄤ簬鍙戦�鐭俊銆佹帴鏀剁煭淇°�鎺ユ敹鍥炴墽鐨勭被銆�
 * 
 * @version 2.2
 * @since JDK 1.4.1
 */
public class APIClient {
	public final static int IMAPI_SUCC = 0; // 鎿嶄綔鎴愬姛
	public final static int IMAPI_CONN_ERR = -1; // 杩炴帴鏁版嵁搴撳嚭閿�
	public final static int IMAPI_CONN_CLOSE_ERR = -2; // 鏁版嵁搴撳叧闂け璐�
	public final static int IMAPI_INS_ERR = -3; // 鏁版嵁搴撴彃鍏ラ敊璇�
	public final static int IMAPI_DEL_ERR = -4; // 鏁版嵁搴撳垹闄ら敊璇�
	public final static int IMAPI_QUERY_ERR = -5; // 鏁版嵁搴撴煡璇㈤敊璇�
	public final static int IMAPI_DATA_ERR = -6; // 鍙傛暟閿欒
	public final static int IMAPI_API_ERR = -7; // API鏍囪瘑闈炴硶
	public final static int IMAPI_DATA_TOOLONG = -8; // 娑堟伅鍐呭澶暱
	public final static int IMAPI_INIT_ERR = -9; // 娌℃湁鍒濆鍖栨垨鍒濆鍖栧け璐�
	public final static int IMAPI_IFSTATUS_INVALID = -10; // API鎺ュ彛澶勪簬鏆傚仠锛堝け鏁堬級鐘舵�
	public final static int IMAPI_GATEWAY_CONN_ERR = -11; // 鐭俊缃戝叧鏈繛鎺�

	public final static int SM_TYPE_NORMAL = 0; // 鐭俊绫诲瀷:鏅�鐭俊
	public final static int SM_TYPE_PDU = 2; // 鐭俊绫诲瀷:PDU鐭俊

	/* 鎺ュ彛鐧诲綍鍚嶃� */
	private String dbUser = null;
	/* 鎺ュ彛鐧诲綍瀵嗙爜銆�*/
	private String dbPwd = null;
	/* 鎺ュ彛缂栫爜銆�*/
	private String apiCode_ = null;
	/* 鏁版嵁搴撳湴鍧�� */
	private String dbUrl = null;
	/* 鏁版嵁搴撹繛鎺ャ� */
	private Connection conn = null;

	/**
	 * 鍒濆鍖朅PIClient瀵硅薄銆�
	 * 
	 * @param dbIP
	 *            淇℃伅鏈虹殑IP鍦板潃
	 * @param dbUser
	 *            鎺ュ彛鐧诲綍鍚�
	 * @param dbPwd
	 *            鎺ュ彛鐧诲綍瀵嗙爜
	 * @param apiCode
	 *            鎺ュ彛缂栫爜
	 * @return
	 */
	public int init(String dbIP, String dbUser, String dbPwd, String apiCode) {
		release(); // 閲婃斁浠ュ墠鐨刣bco,apiOpera瀵硅薄

		this.dbUser = dbUser;
		this.dbPwd = dbPwd;
		this.apiCode_ = apiCode;
		this.dbUrl = "jdbc:mysql://" + dbIP + "/im";

		return testConnect();
	}

	/**
	 * 鍒濆鍖朅PIClient瀵硅薄銆�
	 * 
	 * @param dbIP
	 *            淇℃伅鏈虹殑IP鍦板潃
	 * @param dbUser
	 *            鎺ュ彛鐧诲綍鍚�
	 * @param dbPwd
	 *            鎺ュ彛鐧诲綍瀵嗙爜
	 * @param apiCode
	 *            鎺ュ彛缂栫爜
	 * @param dbName
	 *            鏁版嵁搴撳悕绉�
	 * @return
	 */
	public int init(String dbIP, String dbUser, String dbPwd, String apiCode,
			String dbName) {
		release(); // 閲婃斁浠ュ墠鐨刣bco,apiOpera瀵硅薄

		this.dbUser = dbUser;
		this.dbPwd = dbPwd;
		this.apiCode_ = apiCode;
		this.dbUrl = "jdbc:mysql://" + dbIP + "/" + dbName;

		return testConnect();
	}

	/**
	 * 鍙戦�鐭俊鍐呭鍒颁竴涓墜鏈哄彿鐮併�
	 * 
	 * @param mobile
	 *            鐭俊鍙戦�鐨勭洰鐨勬墜鏈哄彿鐮�
	 * @param content
	 *            鐭俊鍐呭(缂栫爜涓篏B)锛岃秴杩�000涓瓧绗﹂儴鍒嗕細琚埅鏂�
	 * @param smID
	 *            鐭俊ID锛�鍒�9999999涓殑鏌愪竴鏁存暟銆傜‘淇濆敮涓�悗鍙互鐢ㄦ潵鎵惧埌瀵瑰簲鐨勫洖鎵с�鍥炲
	 * @return 鏁存暟銆�=鎴愬姛
	 */
	public int sendSM(String mobile, String content, long smID) {
		return this.sendSM(new String[] { mobile }, content, smID, smID);
	}

	/**
	 * 鍙戦�鐭俊鍐呭鍒颁竴涓墜鏈哄彿鐮併�
	 * 
	 * @param mobiles
	 *            鐭俊鍙戦�鐨勭洰鐨勬墜鏈哄彿鐮佹暟缁�
	 * @param content
	 *            鐭俊鍐呭(缂栫爜涓篏B)锛岃秴杩�000涓瓧绗﹂儴鍒嗕細琚埅鏂�
	 * @param smID
	 *            鐭俊ID锛�鍒�9999999涓殑鏌愪竴鏁存暟銆傜‘淇濆敮涓�悗鍙互鐢ㄦ潵鎵惧埌瀵瑰簲鐨勫洖鎵с�鍥炲
	 * @return 鏁存暟銆�0=鎴愬姛
	 */
	public int sendSM(String[] mobiles, String content, long smID) {
		return sendSM(mobiles, content, smID, smID);
	}

	/**
	 * 鍙戦�鐭俊鍐呭鍒颁竴缇ゆ墜鏈哄彿鐮併�
	 * 
	 * @param mobiles
	 *            鐭俊鍙戦�鐨勬墍鏈夌洰鐨勬墜鏈哄彿鐮佹瀯鎴愮殑鏁扮粍
	 * @param content
	 *            鐭俊鍐呭(缂栫爜涓篏B)锛岃秴杩�000涓瓧绗﹂儴鍒嗕細琚埅鏂�
	 * @param smID
	 *            鐭俊ID锛�鍒�9999999涓殑鏌愪竴鏁存暟銆傜‘淇濆敮涓�悗鍙互鐢ㄦ潵鎵惧埌瀵瑰簲鐨勫洖鎵с�鍥炲
	 * @param srcID
	 *            缁堢婧愬湴鍧�
	 * @return 鏁存暟銆�=鎴愬姛
	 */
	public int sendSM(String[] mobiles, String content, long smID, long srcID) {
		return sendSM(mobiles, content, smID, srcID, "");
	}

	/**
	 * 鍙戦�鐭俊鍐呭鍒颁竴缇ゆ墜鏈哄彿鐮併�
	 * 
	 * @param mobiles
	 *            鐭俊鍙戦�鐨勬墍鏈夌洰鐨勬墜鏈哄彿鐮佹瀯鎴愮殑鏁扮粍
	 * @param content
	 *            鐭俊鍐呭(缂栫爜涓篏B)锛岃秴杩�000涓瓧绗﹂儴鍒嗕細琚埅鏂�
	 * @param sendTime
	 *            鍙戦�鏃堕棿锛堟牸寮�yyyy-MM-dd HH:mm:ss锛�
	 * @param smID
	 *            鐭俊ID锛�鍒�9999999涓殑鏌愪竴鏁存暟銆傜‘淇濆敮涓�悗鍙互鐢ㄦ潵鎵惧埌瀵瑰簲鐨勫洖鎵с�鍥炲
	 * @param srcID
	 *            缁堢婧愬湴鍧�
	 * @return 鏁存暟銆�=鎴愬姛
	 */
	public int sendSM(String[] mobiles, String content, String sendTime,
			long smID, long srcID) {
		return sendSM(mobiles, content, smID, srcID, "", sendTime);
	}

	/**
	 * 鍙戦�Wap Push鐭俊鍒颁竴涓墜鏈哄彿鐮併�
	 * 
	 * @param mobile
	 *            鐭俊鍙戦�鐨勭洰鐨勬墜鏈哄彿鐮�
	 * @param content
	 *            鐭俊鍐呭(缂栫爜涓篏B)锛岃秴杩�000涓瓧绗﹂儴鍒嗕細琚埅鏂�
	 * @param smID
	 *            鐭俊ID锛�鍒�9999999涓殑鏌愪竴鏁存暟銆傜‘淇濆敮涓�悗鍙互鐢ㄦ潵鎵惧埌瀵瑰簲鐨勫洖鎵с�鍥炲
	 * @param url
	 *            Wap鐭俊鐨剈rl鍦板潃,涓嶈兘瓒呰繃100涓瓧绗�
	 * @return 鏁存暟銆�=鎴愬姛
	 */
	public int sendSM(String mobile, String content, long smID, String url) {
		return this.sendSM(new String[] { mobile }, content, smID, url);
	}

	/**
	 * 鍙戦�鐭俊銆�
	 * 
	 * @param mobiles
	 *            鐭俊鍙戦�鐨勬墍鏈夌洰鐨勬墜鏈哄彿鐮佹瀯鎴愮殑鏁扮粍
	 * @param content
	 *            鐭俊鍐呭(缂栫爜涓篏B)锛岃秴杩�000涓瓧绗﹂儴鍒嗕細琚埅鏂�
	 * @param smID
	 *            鐭俊ID锛�鍒�9999999涓殑鏌愪竴鏁存暟銆傜‘淇濆敮涓�悗鍙互鐢ㄦ潵鎵惧埌瀵瑰簲鐨勫洖鎵с�鍥炲
	 * @param url
	 *            Wap鐭俊鐨剈rl鍦板潃,涓嶈兘瓒呰繃100涓瓧绗�
	 * @return 鏁存暟銆�=鎴愬姛
	 */
	public int sendSM(String[] mobiles, String content, long smID, String url) {
		return sendSM(mobiles, content, smID, smID, url);
	}

	/**
	 * 鍙戦�鐭俊銆�
	 * 
	 * @param mobiles
	 *            鐭俊鍙戦�鐨勬墍鏈夌洰鐨勬墜鏈哄彿鐮佹瀯鎴愮殑鏁扮粍
	 * @param content
	 *            鐭俊鍐呭(缂栫爜涓篏B)锛岃秴杩�000涓瓧绗﹂儴鍒嗕細琚埅鏂�
	 * @param smID
	 *            鐭俊ID锛�鍒�9999999涓殑鏌愪竴鏁存暟銆傜‘淇濆敮涓�悗鍙互鐢ㄦ潵鎵惧埌瀵瑰簲鐨勫洖鎵с�鍥炲
	 * @param srcID
	 *            缁堢婧愬湴鍧�
	 * @param url
	 *            Wap鐭俊鐨剈rl鍦板潃,涓嶈兘瓒呰繃100涓瓧绗�
	 * @return 鏁存暟銆�=鎴愬姛
	 */
	public int sendSM(String[] mobiles, String content, long smID, long srcID,
			String url) {
		return sendSM(mobiles, content, smID, srcID, url, null);
	}

	/**
	 * 鍙戦�鐭俊銆�
	 * 
	 * @param mobiles
	 *            鐭俊鍙戦�鐨勬墍鏈夌洰鐨勬墜鏈哄彿鐮佹瀯鎴愮殑鏁扮粍
	 * @param content
	 *            鐭俊鍐呭(缂栫爜涓篏B)锛岃秴杩�000涓瓧绗﹂儴鍒嗕細琚埅鏂�
	 * @param smID
	 *            鐭俊ID锛�鍒�9999999涓殑鏌愪竴鏁存暟銆傜‘淇濆敮涓�悗鍙互鐢ㄦ潵鎵惧埌瀵瑰簲鐨勫洖鎵с�鍥炲
	 * @param srcID
	 *            缁堢婧愬湴鍧�
	 * @param url
	 *            Wap鐭俊鐨剈rl鍦板潃,涓嶈兘瓒呰繃100涓瓧绗�
	 * @param sendTime
	 *            鍙戦�鏃堕棿锛堟牸寮�yyyy-MM-dd HH:mm:ss锛夛紝绔嬪嵆鍙戦�鏃朵负null
	 * @return 鏁存暟銆�=鎴愬姛
	 */
	public int sendSM(String[] mobiles, String content, long smID, long srcID,
			String url, String sendTime) {
		return sendSmAndPdu(mobiles, content, smID, srcID, url, sendTime, 0, 0,
				0, "", "", "", 0, APIClient.SM_TYPE_NORMAL);
	}

	/**
	 * 涓嶆敮鎸乻rcID鐨勬柟娉曪紝瀹氭椂鍙戦�涓�潯PDU鐭俊鍒颁竴涓墜鏈哄彿鐮侀泦銆�
	 * 
	 * @param mobiles
	 *            鐭俊鍙戦�鐨勫涓洰鐨勬墜鏈哄彿鐮佺殑鏁扮粍
	 * @param content
	 *            鐭俊鍐呭锛岃秴杩�000涓瓧绗﹂儴鍒嗕細琚埅鏂�
	 * @param smID
	 *            鐭俊ID锛�鍒�9999999涓殑鏌愪竴鏁存暟銆傜‘淇濆敮涓�悗鍙互鐢ㄦ潵鎵惧埌瀵瑰簲鐨勫洖鎵с�鍥炲
	 * @param sendTime
	 *            瀹氭椂鍙戦�鏃堕棿锛屼负绌烘椂绔嬪嵆鍙戦�
	 * @param tpPID
	 *            榛樿涓�锛屽叿浣撹鍙傝cmpp21鎴�cmpp30鍗忚鏂囨。銆�
	 * @param tpUdhi
	 *            榛樿涓�锛屽叿浣撹鍙傝cmpp21鎴�cmpp30鍗忚鏂囨。銆�
	 * @param feeTerminalID
	 *            琚璐圭敤鎴风殑鍙风爜锛岄粯璁や负绌�
	 * @param feeType
	 *            璧勮垂绫诲埆锛岄粯璁や负绌恒�
	 * @param feeCode
	 *            璧勮垂浠ｇ爜
	 * @param feeUserType
	 *            璁¤垂鐢ㄦ埛绫诲瀷
	 * @return int 0:鎴愬姛;鍏朵粬:澶辫触
	 */
	public int sendPDU(String[] mobiles, byte[] content, long smID, int msgFmt,
			int tpPID, int tpUdhi, String feeTerminalID, String feeType,
			String feeCode, int feeUserType) {
		return sendPDU(mobiles, content, smID, smID, msgFmt, tpPID, tpUdhi,
				feeTerminalID, feeType, feeCode, feeUserType);// 濡傛灉涓嶆敮鎸乻rcID,浠rcID涓簊mID.
	}

	/**
	 * 鏀寔srcID鐨勬柟娉曪紝瀹氭椂鍙戦�涓�潯PDU鐭俊鍒颁竴涓墜鏈哄彿鐮侀泦銆�
	 * 
	 * @param mobiles
	 *            鐭俊鍙戦�鐨勫涓洰鐨勬墜鏈哄彿鐮佺殑鏁扮粍
	 * @param content
	 *            鐭俊鍐呭锛岃秴杩�000涓瓧绗﹂儴鍒嗕細琚埅鏂�
	 * @param smID
	 *            鐭俊ID锛�鍒�9999999涓殑鏌愪竴鏁存暟銆傜‘淇濆敮涓�悗鍙互鐢ㄦ潵鎵惧埌瀵瑰簲鐨勫洖鎵с�鍥炲
	 * @param srcID
	 *            缁堢婧愬湴鍧�紝0鍒�9999999涓殑鏌愪竴鏁存暟銆傜敤鏉ヤ綔婧愬湴鍧�樉绀哄湪缁堢涓�
	 * @param sendTime
	 *            瀹氭椂鍙戦�鏃堕棿锛屼负绌烘椂绔嬪嵆鍙戦�
	 * @param msgFmt
	 *            鐭俊缂栫爜鏍煎紡锛堟渶缁堥�杩嘽mpp21鍗忚鍙戦�鑷崇綉鍏崇殑鐭俊鏍煎紡锛屽叿浣撶被鍨嬭鍙傝cmpp21鎴�cmpp30鍗忚鏂囨。锛涳級榛樿涓�銆�
	 * @param tpPID
	 *            榛樿涓�
	 * @param tpUdhi
	 *            榛樿涓�
	 * @param feeTerminalID
	 *            琚璐圭敤鎴风殑鍙风爜
	 * @param feeType
	 *            璧勮垂绫诲埆
	 * @param feeCode
	 *            璧勮垂浠ｇ爜
	 * @param feeUserType
	 *            璁¤垂鐢ㄦ埛绫诲瀷
	 * @return int 0:鎴愬姛;鍏朵粬:澶辫触
	 */
	public int sendPDU(String[] mobiles, byte[] content, long smID, long srcID,
			int msgFmt, int tpPID, int tpUdhi, String feeTerminalID,
			String feeType, String feeCode, int feeUserType) {
		String contentStr = binary2Hex(content);
		return sendSmAndPdu(mobiles, contentStr, smID, srcID, "", null, msgFmt,
				tpPID, tpUdhi, feeTerminalID, feeType, feeCode, feeUserType,
				APIClient.SM_TYPE_PDU);
	}

	/**
	 * 鏈�悗PDU鍜屾櫘閫氱煭淇￠兘璋冪敤姝ゆ柟娉曪紝鐢辨鏂规硶鏉ュ疄鐜板叿浣撶殑鐭俊鍙戦�銆�
	 * 
	 * @param mobiles
	 *            鐭俊鍙戦�鐨勫涓洰鐨勬墜鏈哄彿鐮佺殑鏁扮粍
	 * @param content
	 *            鐭俊鍐呭锛岃秴杩�000涓瓧绗﹂儴鍒嗕細琚埅鏂�
	 * @param smID
	 *            鐭俊ID锛�鍒�9999999涓殑鏌愪竴鏁存暟銆傜‘淇濆敮涓�悗鍙互鐢ㄦ潵鎵惧埌瀵瑰簲鐨勫洖鎵с�鍥炲
	 * @param srcID
	 *            缁堢婧愬湴鍧�紝0鍒�9999999涓殑鏌愪竴鏁存暟銆傜敤鏉ヤ綔婧愬湴鍧�樉绀哄湪缁堢涓�
	 * @param sendTime
	 *            瀹氭椂鍙戦�鏃堕棿锛屼负绌烘垨涓簄ull鏃剁珛鍗冲彂閫�
	 * @param msgFmt
	 *            鐭俊缂栫爜鏍煎紡锛堟渶缁堥�杩嘽mpp21鍗忚鍙戦�鑷崇綉鍏崇殑鐭俊鏍煎紡锛屽叿浣撶被鍨嬭鍙傝cmpp21鎴�cmpp30鍗忚鏂囨。锛涳級
	 * @param tpPID
	 *            榛樿涓�
	 * @param tpUdhi
	 *            榛樿涓�
	 * @param feeTerminalID
	 *            琚璐圭敤鎴风殑鍙风爜
	 * @param feeType
	 *            璧勮垂绫诲埆
	 * @param feeCode
	 *            璧勮垂浠ｇ爜
	 * @param feeUserType
	 *            璁¤垂鐢ㄦ埛绫诲瀷
	 * @param smType
	 *            鐭俊绫诲瀷
	 * @return int 0:鎴愬姛;鍏朵粬:澶辫触
	 */
	private int sendSmAndPdu(String[] mobiles, String content, long smID,
			long srcID, String url, String sendTime, int msgFmt, int tpPID,
			int tpUdhi, String feeTerminalID, String feeType, String feeCode,
			int feeUserType, int smType) {
		if (dbUrl == null)
			return APIClient.IMAPI_INIT_ERR;
		if (mobiles == null || mobiles.length == 0) {
			return APIClient.IMAPI_DATA_ERR;
		}
		// 鎵嬫満鍙风爜杩炴帴鎴愪竴璧�
		StringBuffer mobileBuf = new StringBuffer();
		for (int i = 0; i < mobiles.length; i++) {
			mobileBuf.append(",").append(mobiles[i]);
		}
		if (mobileBuf.length() > 1)
			mobileBuf.delete(0, 1);
		else
			return APIClient.IMAPI_DATA_ERR;

		// 鏇挎崲鍐呭鐗规畩瀛楃
		String contenttmp = replaceSpecilAlhpa(content);
		if (contenttmp.length() < 1)
			return APIClient.IMAPI_DATA_ERR;

		// 妫�煡鎴彇瀛楃
		if (contenttmp.length() > 2000)
			contenttmp = contenttmp.substring(0, 2000);

		// 妫�煡鐭俊ID鏄惁鍚堟硶
		if (!checkSmID(smID) || !checkSmID(srcID))
			return APIClient.IMAPI_DATA_ERR;

		// 妫�煡URL鏄惁鍚堟硶
		url = nullConvert(url).trim();
		if (url.length() > 0) {
			if (url.length() > 85) {
				return APIClient.IMAPI_DATA_TOOLONG;
			}
			if ((url + contenttmp).length() > 120) {
				return APIClient.IMAPI_DATA_TOOLONG;
			}
		}

		// 鍒ゆ柇缃戝叧鏄惁鍏抽棴锛屾帴鍙ｆ槸鍚﹀彲鐢�
		int ret = checkGatConn();
		if (ret != 1) {
			return ret;
		}

		// 鏃ユ湡鏍煎紡鏄惁姝ｇ‘
		if (sendTime != null && !"".equals(sendTime)) {
			if (isDateTime(sendTime) == null) {
				return APIClient.IMAPI_DATA_ERR;
			}
		}

		// 濡傛灉鍙傛暟鏄痭ull瀹归敊澶勭悊涓�" (String feeTerminalID, String feeType, String
		// feeCode)
		if (APIClient.SM_TYPE_PDU == smType) {
			feeTerminalID = nullConvert(feeTerminalID).trim();
			feeType = nullConvert(feeType).trim();
			feeCode = nullConvert(feeCode).trim();
		}

		return mTInsert(mobileBuf.toString(), contenttmp, smID, srcID, url,
				sendTime, msgFmt, tpPID, tpUdhi, feeTerminalID, feeType,
				feeCode, feeUserType, smType);

	}

	/**
	 * 鎺ユ敹鏉ヨ嚜鎵�湁鎵嬫満鐨勭煭淇°�
	 * 
	 * @return MOItem[]銆傛墍鎺ユ敹鍒扮殑鎵�敤鐭俊鏋勬垚鐨勬暟缁勶紝姣忎釜MOItem鍏冪礌瀵瑰簲涓�潯鎵�帴鏀剁殑鐭俊锛�
	 *         null琛ㄧず鎺ユ敹澶辫触锛屾暟缁勯暱搴︿负0琛ㄧず鐩墠鏃犵煭淇°�
	 */
	public MOItem[] receiveSM() {
		return receiveSM(-1);
	}

	/**
	 * 鎺ユ敹鏉ヨ嚜鎵�湁鎵嬫満鐨勭煭淇°�
	 * 
	 * @param amount
	 *            鍙栧緱鐨凪o璁板綍鏁伴噺(褰揳mount涓猴紞1鏃讹紝涓嶅姞limit闄愬埗鏉′欢)
	 * @return MOItem[]銆傛墍鎺ユ敹鍒扮殑鎵�敤鐭俊鏋勬垚鐨勬暟缁勶紝姣忎釜MOItem鍏冪礌瀵瑰簲涓�潯鎵�帴鏀剁殑鐭俊锛�
	 *         null琛ㄧず鎺ユ敹澶辫触锛屾暟缁勯暱搴︿负0琛ㄧず鐩墠鏃犵煭淇°�
	 */
	public MOItem[] receiveSM(int amount) {
		return receiveSM(-1L, amount);
	}

	/**
	 * 鎺ユ敹鏉ヨ嚜鎵�湁鎵嬫満鐨勭煭淇°�
	 * 
	 * @param srcID
	 *            鎵嬫満涓婃樉绀哄熬鍙�褰搒rcID涓猴紞1鏃讹紝涓嶅姞srcID闄愬埗鏉′欢)
	 * @param amount
	 *            鍙栧緱鐨凪o璁板綍鏁伴噺(褰揳mount涓猴紞1鏃讹紝涓嶅姞limit闄愬埗鏉′欢)
	 * 
	 * @return MOItem[]銆傛墍鎺ユ敹鍒扮殑鎵�敤鐭俊鏋勬垚鐨勬暟缁勶紝姣忎釜MOItem鍏冪礌瀵瑰簲涓�潯鎵�帴鏀剁殑鐭俊锛�
	 *         null琛ㄧず鎺ユ敹澶辫触锛屾暟缁勯暱搴︿负0琛ㄧず鐩墠鏃犵煭淇°�
	 */
	public MOItem[] receiveSM(long srcID, int amount) {
		if (dbUrl == null) // 鏈垵濮嬪寲
			return null;

		if (this.conn == null) {
			int state = initConnect();
			if (state != 0)
				return null;
		}

		Statement st = null;
		ResultSet rs = null;

		String getMoSql = "select * from api_mo_" + this.apiCode_;
		if (srcID != -1) {
			getMoSql += " where SM_ID=" + srcID;
		}
		if (amount != -1) {
			getMoSql += " limit " + amount;
		} else {
			getMoSql += " limit 5000";
		}
		String delMoSql = "delete from api_mo_" + this.apiCode_
				+ " where AUTO_SN in (";

		ArrayList moList = new ArrayList();
		StringBuffer snBuf = new StringBuffer("-1");
		try {
			st = this.conn.createStatement();
			rs = st.executeQuery(getMoSql);
			while (rs.next()) {
				MOItem mItemtmp = new MOItem();
				mItemtmp.setSmID(rs.getLong("SM_ID"));
				mItemtmp.setContent(iso2gbk(rs.getString("CONTENT")));
				mItemtmp.setMobile(rs.getString("MOBILE"));
				mItemtmp.setMoTime(rs.getString("MO_TIME"));
				mItemtmp.setMsgFmt(rs.getInt("MSG_FMT"));

				snBuf.append(",").append(rs.getString("AUTO_SN"));
				moList.add(mItemtmp);
			}
			rs.close();
			st.executeUpdate(delMoSql + snBuf.toString() + ")");
		} catch (Exception e) {
			releaseConn();
			return null;
		} finally {
			closeStatment(st);
		}

		MOItem[] moItem = new MOItem[0];
		return (MOItem[]) moList.toArray(moItem);
	}

	/**
	 * 鎺ユ敹鏉ヨ嚜鎵�湁鎵嬫満鐨勭煭淇″洖鎵с�
	 * 
	 * @return RPTItem[]銆傛墍鎺ユ敹鍒扮殑鎵�敤鐭俊鍥炴墽鏋勬垚鐨勬暟缁勶紝姣忎釜RPTItem鍏冪礌瀵瑰簲涓�潯鎵�帴鏀剁殑
	 *         鐭俊鍥炴墽锛沶ull琛ㄧず鎺ユ敹澶辫触锛屾暟缁勯暱搴︿负0琛ㄧず鐩墠鏃犵煭淇″洖鎵с�
	 */
	public RPTItem[] receiveRPT() {
		return receiveRPT(-1);
	}

	/**
	 * 鎺ユ敹鏉ヨ嚜鎵�湁鎵嬫満鐨勭煭淇″洖鎵с�
	 * 
	 * @param amount
	 *            鍙栧緱鐨勫洖鎵ц褰曟暟閲忥紙褰揳mount涓猴紞1鏃讹紝涓嶅姞limit闄愬埗鏉′欢锛�
	 * 
	 * @return RPTItem[]銆傛墍鎺ユ敹鍒扮殑鎵�敤鐭俊鍥炴墽鏋勬垚鐨勬暟缁勶紝姣忎釜RPTItem鍏冪礌瀵瑰簲涓�潯鎵�帴鏀剁殑
	 *         鐭俊鍥炴墽锛沶ull琛ㄧず鎺ユ敹澶辫触锛屾暟缁勯暱搴︿负0琛ㄧず鐩墠鏃犵煭淇″洖鎵с�
	 */
	public RPTItem[] receiveRPT(int amount) {
		return receiveRPT(-1L, amount);
	}

	/**
	 * 鎺ユ敹鏉ヨ嚜鎵�湁鎵嬫満鐨勭煭淇″洖鎵с�
	 * 
	 * @param smID
	 *            鐭俊smID锛堝綋smID涓猴紞1鏃讹紝涓嶅姞smID闄愬埗鏉′欢锛�
	 * @param amount
	 *            鍙栧緱鐨勫洖鎵ц褰曟暟閲忥紙褰揳mount涓猴紞1鏃讹紝涓嶅姞limit闄愬埗鏉′欢锛�
	 * 
	 * @return RPTItem[]銆傛墍鎺ユ敹鍒扮殑鎵�敤鐭俊鍥炴墽鏋勬垚鐨勬暟缁勶紝姣忎釜RPTItem鍏冪礌瀵瑰簲涓�潯鎵�帴鏀剁殑
	 *         鐭俊鍥炴墽锛沶ull琛ㄧず鎺ユ敹澶辫触锛屾暟缁勯暱搴︿负0琛ㄧず鐩墠鏃犵煭淇″洖鎵с�
	 */
	public RPTItem[] receiveRPT(long smID, int amount) {
		if (dbUrl == null) // 鏈垵濮嬪寲
			return null;

		ResultSet rs = null;
		Statement st = null;
		if (this.conn == null) {
			int state = initConnect();
			if (state != 0)
				return null;
		}

		String getRPTSql = "select * from api_rpt_" + this.apiCode_;
		if (smID != -1) {
			getRPTSql += " where SM_ID=" + smID;
		}
		if (amount != -1) {
			getRPTSql += " limit " + amount;
		} else {
			getRPTSql += " limit 5000";
		}
		String delRPTSql = "delete from api_rpt_" + this.apiCode_
				+ " where AUTO_SN in (";

		RPTItem[] rptItem = null;
		ArrayList rptList = new ArrayList();

		StringBuffer snBuf = new StringBuffer("-1");
		try {
			st = this.conn.createStatement();
			rs = st.executeQuery(getRPTSql);
			while (rs.next()) {
				RPTItem rptItemtmp = new RPTItem();
				rptItemtmp.setSmID(rs.getLong("SM_ID"));
				rptItemtmp.setCode(rs.getInt("RPT_CODE"));
				rptItemtmp.setMobile(rs.getString("MOBILE"));
				rptItemtmp.setDesc(iso2gbk(rs.getString("RPT_DESC")));
				rptItemtmp.setRptTime(rs.getString("RPT_TIME"));

				snBuf.append(",").append(rs.getString("AUTO_SN"));

				rptList.add(rptItemtmp);
			}
			rs.close();

			st.executeUpdate(delRPTSql + snBuf.toString() + ")");

		} catch (SQLException e) {
			releaseConn();
			// if(e.getErrorCode() == 1146 || e.getErrorCode() == 1142)
			// return new RPTItem[0];
			// else
			return null;
		} catch (Exception ex) {
			return null;
		} finally {
			closeStatment(st);
		}

		rptItem = new RPTItem[0];
		return (RPTItem[]) rptList.toArray(rptItem);
	}

	/**
	 * 鍏抽棴APIClient瀵硅薄涓庝俊鎭満鐨勮繛鎺ワ紝閲婃斁APIClient瀵硅薄鎵�崰鐢ㄧ殑鎵�湁璧勬簮銆�
	 */
	public void release() {
		this.dbUser = null;
		this.dbPwd = null;
		this.apiCode_ = null;
		this.dbUrl = null;
		this.releaseConn();
	}

	/*
	 * 娴嬭瘯鏁版嵁搴撹繛鎺�
	 * 
	 * @return
	 */
	private int testConnect() {
		Statement st = null;
		ResultSet rs = null;
		// 鍒涘缓鏁版嵁搴撹繛鎺�
		try {
			if (this.conn != null) {
				releaseConn();
			}

			getConn();

			st = this.conn.createStatement();

		} catch (Exception e) {
			return APIClient.IMAPI_CONN_ERR;// 杩炴帴鏁版嵁搴撳け璐�
		}
		try {
			String tableName = "api_mo_" + this.apiCode_;
			rs = st.executeQuery("select * from " + tableName + " limit 1");
			rs.close();
		} catch (SQLException e) {
			return APIClient.IMAPI_API_ERR;// apiID涓嶅瓨鍦�
		} finally {
			try {
				st.close();
			} catch (Exception ex) {
			}
		}
		return APIClient.IMAPI_SUCC;
	}

	/*
	 * 鍒濆鍖栨暟鎹簱杩炴帴conn
	 * 
	 * @return int 0:鎴愬姛锛�1:杩炴帴澶辫触
	 */
	private int initConnect() {
		try {
			getConn();
		} catch (Exception e) {
			return APIClient.IMAPI_CONN_ERR;
		}

		return APIClient.IMAPI_SUCC;
	}

	/**
	 * 寰楀埌鏁版嵁搴撹繛鎺�
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void getConn() throws ClassNotFoundException, SQLException {
		Class.forName("org.gjt.mm.mysql.Driver");
		this.conn = DriverManager.getConnection(this.dbUrl, this.dbUser,
				this.dbPwd);
	}

	/*
	 * 閲婃斁鏁版嵁搴撹繛鎺onn
	 * 
	 * @return void
	 */
	private void releaseConn() {
		if (this.conn != null) {
			try {
				this.conn.close();
			} catch (SQLException e) {
			}
		}
		this.conn = null;
	}

	/**
	 * PDU鍜屾櫘閫氱煭淇″彂閫佸悜鏁版嵁搴撴彃鍏ヤ竴鏉℃暟鎹�
	 * 
	 * @return 鏁存暟銆�=鎴愬姛锛�1锛氳繛鎺ュけ璐ワ紝-3锛氭暟鎹簱鎻掑叆閿欒
	 */
	private int mTInsert(String mobile, String content, long smID, long srcID,
			String url, String sendTime, int msgFmt, int tpPID, int tpUdhi,
			String feeTerminalID, String feeType, String feeCode,
			int feeUserType, int smType) {
		if (this.conn == null) {
			int state = initConnect();
			if (state != 0) {
				return APIClient.IMAPI_CONN_ERR;
			}
		}
		String sendMTSql = "";

		sendMTSql = "insert into api_mt_"
				+ this.apiCode_
				+ " (SM_ID,SRC_ID,MOBILES,CONTENT,IS_WAP,URL,SEND_TIME,MSG_FMT,TP_PID,TP_UDHI,FEE_TERMINAL_ID,FEE_TYPE,FEE_CODE,FEE_USER_TYPE,SM_TYPE) values ("
				+ smID + "," + srcID + ",'" + mobile + "','" + content + "', ";
		if (url != null && url.trim().length() != 0) {
			sendMTSql += "1,'" + url + "',";
		} else {
			sendMTSql += "0,'',";
		}
		if (sendTime == null || "".equals(sendTime.trim())) {
			sendMTSql += " null ,";
		} else {
			sendMTSql += "'" + sendTime + "',";
		}

		sendMTSql += msgFmt + "," + tpPID + "," + tpUdhi + ",'" + feeTerminalID
				+ "','" + feeType + "','" + feeCode + "'," + feeUserType + ","
				+ smType + ")";

		Statement st = null;
		try {
			st = this.conn.createStatement();
			st.executeUpdate(gb2Iso(sendMTSql));
		} catch (Exception e) {
			releaseConn();
			return APIClient.IMAPI_INS_ERR;
		} finally {
			closeStatment(st);
		}
		return APIClient.IMAPI_SUCC;
	}

	/*
	 * 鍏抽棴鏁版嵁搴撳０鏄巗t
	 * 
	 * @return void
	 */
	private void closeStatment(Statement st) {
		try {
			st.close();
		} catch (Exception e) {
		}
	}

	/*
	 * 鏇挎崲瀛楃涓蹭腑鐨勯潪娉曞瓧绗�
	 * 
	 * @param content 杈撳叆鐨勭煭淇″唴瀹�@return String
	 */
	private String replaceSpecilAlhpa(String content) {
		if (content == null || content.trim().length() == 0)
			return "";

		String spec_char = "\\'";
		String retStr = "";
		for (int i = 0; i < content.length(); i++) {
			if (spec_char.indexOf(content.charAt(i)) >= 0) {
				retStr += "\\";
			}
			retStr += content.charAt(i);
		}
		return retStr;
	}

	/*
	 * 妫�煡鐭俊鐨剆mID,鍦�~99999999
	 * 
	 * @param smID 鐭俊ID @return boolean
	 */
	private boolean checkSmID(long smID) {
		// smID鍦�~99999999涔嬮棿
		if (smID < 0 || smID > 99999999)
			return false;

		return true;
	}

	/*
	 * 灏嗗瓧绗︿覆杞崲鎴恑so8859-1绫诲瀷
	 * 
	 * @param str @return String
	 */
	private String gb2Iso(String str) {
		if (str == null) {
			return "";
		}
		String temp = "";
		try {
			byte[] buf = str.trim().getBytes("GBK");
			temp = new String(buf, "iso8859-1");
		} catch (UnsupportedEncodingException e) {
			temp = str;
		}
		return temp;
	}

	/**
	 * 鑾峰彇缃戝叧杩炴帴鍙傛暟銆佹帴鍙ｅ紑閫氬弬鏁�
	 * 
	 * 妫�獙缃戝叧鏄惁杩炴帴(1锛氳繛閫�0锛氭湭杩炴帴) 鍒ゆ柇鎺ュ彛鏄惁鍙敤(1锛氳繍琛岀姸鎬�0锛氳皟璇曠姸鎬佺幇鍦ㄤ竴涓姸鎬侊細2锛氭殏鍋滐紙澶辨晥锛夌姸鎬�
	 * 
	 * @return ret
	 */
	private int checkGatConn() {
		int ret = 1;
		ResultSet rs = null;
		Statement st = null;
		if (this.conn == null) {
			initConnect();
		}

		String sql = "select if_status,conn_succ_status from tbl_api_info as api where api.if_code='"
				+ this.apiCode_ + "' limit 1";

		try {
			st = this.conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				if ("2".equals(rs.getString("if_status"))) {
					ret = APIClient.IMAPI_IFSTATUS_INVALID;
				}
				if ("0".equals(rs.getString("conn_succ_status"))) {
					ret = APIClient.IMAPI_GATEWAY_CONN_ERR;
				}
			}
			rs.close();
		} catch (SQLException e) {
			return APIClient.IMAPI_API_ERR;// apiID涓嶅瓨鍦�
		} finally {
			try {
				st.close();
			} catch (Exception ex) {
			}
		}
		return ret;
	}

	/**
	 * 
	 * 妫�獙鏄惁涓烘棩鏈�YYYY-MM-dd HH:mm:ss),濡傛灉涓嶆槸锛岃繑鍥瀗ull锛屽惁鍒欒繑鍥炴棩鏈�
	 * 
	 * @param str
	 * @return
	 */
	public static String isDateTime(String str) {
		int temp;
		if (str == null)
			return null;
		if (str.length() != 19)
			return null;

		temp = Integer.parseInt(str.substring(5, 7));
		if (12 < temp || temp < 1)
			return null;

		temp = Integer.parseInt(str.substring(8, 10));
		if (31 < temp || temp < 1)
			return null;

		temp = Integer.parseInt(str.substring(11, 13));
		if (23 < temp || temp < 0)
			return null;
		temp = Integer.parseInt(str.substring(14, 16));
		if (59 < temp || temp < 0)
			return null;
		temp = Integer.parseInt(str.substring(17, 19));
		if (59 < temp || temp < 0)
			return null;
		Date returnDate = null;
		DateFormat df = DateFormat.getDateInstance();

		try {
			returnDate = df.parse(str);
			return returnDate.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 灏嗗瓧鑺傛暟缁勭被鍨嬬殑鏁版嵁杞寲鎴�6杩涘埗鐨勫瓧绗︿覆
	 * 
	 * @param bys
	 * @return String
	 */
	private String binary2Hex(byte[] bys) {
		if (bys == null || bys.length < 1)
			return null;

		StringBuffer sb = new StringBuffer(100);

		for (int i = 0; i < bys.length; i++) {
			if (bys[i] >= 16)
				sb.append(Integer.toHexString(bys[i]));
			else if (bys[i] >= 0)
				sb.append("0" + Integer.toHexString(bys[i]));
			else
				sb.append(Integer.toHexString(bys[i]).substring(6, 8));
		}

		return sb.toString();
	}

	/**
	 * 灏嗗瓧绗︿覆杞崲鎴恎bk绫诲瀷
	 * 
	 * @param str
	 * @return
	 */
	private String iso2gbk(String str) {
		if (str == null) {
			return "";
		}
		String temp = "";
		try {
			byte[] buf = str.trim().getBytes("iso8859-1");
			temp = new String(buf, "GBK");
		} catch (UnsupportedEncodingException e) {
			temp = str;
		}
		return temp;
	}

	/**
	 * 灏嗗瓧绗︿覆杞崲鎴愰潪NULL鐨凷tring
	 * 
	 * @param str
	 * @return 闈瀗ull鐨凷tring
	 */
	private String nullConvert(String str) {
		if (str == null) {
			str = "";
		}
		return str;

	}

}

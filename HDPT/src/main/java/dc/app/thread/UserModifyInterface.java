package dc.app.thread;

import dc.domain.main.YyUserAccount;
import dc.util.code.MD5StringUtil;
import org.apache.xerces.impl.dv.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 修改用户信息接口
 * Created by WYI on 2016/4/15.
 */
public class UserModifyInterface extends AbstractThread{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserModifyInterface.class);

    private YyUserAccount account;

    /**
     * 用户信息中的密码需为未加密的明文
     * @param account
     */
    public UserModifyInterface(YyUserAccount account){
        this.account = account;
    }

    @Override
    protected void recvResponse(Map<String, Object> params) {
        Object errorCcode = params.get("errcode");
        if (!"0".equals(errorCcode)) {
            Object errorMsg = params.get("errmsg");
            try {
                errorMsg = new String(errorMsg.toString().getBytes("UTF-8"), "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            LOGGER.error("Receive error when request modifyInterface!-->errorCode[{}] errorMsg[{}]", errorCcode, errorMsg);
        }else{
            LOGGER.debug("Request modifyInterface success!");
        }
    }

    @Override
    public void run() {
        if(account == null){
            LOGGER.error("YyUserAccount is null!Can not request modifyInterface!");
            return ;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("UserName", account.getUsername());
        params.put("TelePhoneNo", account.getPhoneno());
        params.put("Gender", account.getSex());
        params.put("IdCard", account.getIdcard());
        params.put("CardType", account.getCardtype());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Object birthday = account.getBirthday();
        if (birthday != null) {
            params.put("BirthDay", sdf.format(birthday));
        }

        params.put("Nationality", account.getNationality());
        params.put("act", "modify");

        sdf.applyPattern("yyyyMMddhhmmss");
        String thistime = sdf.format(Calendar.getInstance().getTime());
        params.put("thistime", thistime);

        String signature = MD5StringUtil.MD5Encode(thistime + SECRETKEY);
        params.put("signature", signature);

        sendRequest(REQUEST_URL, params);
    }

    /**
     * 启动接口发送数据
     * 外部使用接口的唯一方法
     */
    public void runInterface(){
        new Thread(this).start();
    }
}

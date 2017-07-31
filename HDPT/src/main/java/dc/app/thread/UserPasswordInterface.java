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
 * 修改密码接口
 * Created by WYI on 2016/4/15.
 */
public class UserPasswordInterface extends AbstractThread{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserPasswordInterface.class);

    private YyUserAccount account;
    private String oldPassword;
    /**
     * 用户信息中的密码以及参数中的旧密码，必须为未加密的明文
     * @param account
     */
    public UserPasswordInterface(YyUserAccount account, String oldPassword){
        this.account = account;
        this.oldPassword = oldPassword;
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
            LOGGER.error("Receive error when request passwordInterface!-->errorCode[{}] errorMsg[{}]", errorCcode, errorMsg);
        }else{
            LOGGER.debug("Request passwordInterface success!");
        }
    }

    @Override
    public void run() {
        if(account == null){
            LOGGER.error("YyUserAccount is null!Can not request passwordInterface!");
            return ;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("TelePhoneNo", account.getPhoneno());
        params.put("IdCard", account.getIdcard());
        params.put("CardType", account.getCardtype());
        params.put("OldPassword", Base64.encode(oldPassword.getBytes()));
        params.put("NewPassword", Base64.encode(account.getPassword().getBytes()));
        params.put("act", "password");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
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

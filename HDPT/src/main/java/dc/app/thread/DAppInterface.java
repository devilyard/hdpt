package dc.app.thread;

import dc.util.ConfigUtil;

/**
 * 接口相关常数定义
 * Created by WYI on 2016/4/18.
 */
public interface DAppInterface {
    /**
     * 校验码秘钥
     */
    String SECRETKEY = "Txbytcwsjapp20160415";

    String REQUEST_URL = ConfigUtil.getValue("app.interface.user");
}

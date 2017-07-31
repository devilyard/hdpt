package dc.app.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dc.util.SpringContextUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * http接口父类
 * Created by WYI on 2016/4/15.
 */
public abstract class AbstractThread implements Runnable, DAppInterface{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractThread.class);




    /**
     * 发送http请求
     * @param url
     * @param params
     */
    protected void sendRequest(String url, Map<String, Object> params){
        HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        if(url == null){
            LOGGER.error("Request url is null!");
            return ;
        }
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for(String key : params.keySet()){
                nvps.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
            }
            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse res = hc.execute(post);
            Map<String, Object> respParams = getResponse(res);
            this.recvResponse(respParams);
        } catch (Exception e) {
            LOGGER.error("SendRequest is error!-->url:{}", url, e);
            saveErrorRecord(url, params);

        }
    }

    /**
     * 接收返回数据
     * @param params
     */
    protected abstract void recvResponse(Map<String, Object> params);

    private Map<String, Object> getResponse(HttpResponse res)
            throws IllegalStateException, IOException {
        InputStream is = res.getEntity().getContent();
        byte[] b = new byte[10240];
        int length = 0;
        String charset = res.getEntity().getContentEncoding() == null ? "UTF-8"
                : res.getEntity().getContentEncoding().getValue();
        StringBuilder sb = new StringBuilder();
        while ((length = is.read(b)) > 0) {
            sb.append(new String(b, 0, length, Charset.forName(charset)));
        }

        return JSON.parseObject(sb.toString());
    }

    /**
     * 保存接口处理失败记录
     * @param url
     * @param params
     */
    private void saveErrorRecord(String url,Map<String, Object> params){
        String jsonObject = JSONObject.toJSONString(params);
        String insert = "insert into YY_USER_ACCOUNT_ERROR(ID,METHODNAME,INFO) values(SEQ_YY_USER_ACCOUNT_ERROR.nextval,'"
                + url+ "','" + jsonObject + "')";
        DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
        try {
            Connection conn = dataSource.getConnection();
            conn.createStatement().execute(insert);
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}

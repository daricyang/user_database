/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.autologin;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import gmc.extractor.ReglarExpression;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.script.ScriptException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

/**
 *
 * @author Pok
 */
public class TencentAutoLogin extends Thread {

    private static String HEXSTRING = "0123456789ABCDEF";

    public String login(String uid, String pwd) throws IOException, FileNotFoundException, ScriptException, NoSuchMethodException, Exception {
        String cookie = "";
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
        client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
        List<String> captcha = getParam(client, uid);
        String sp = GetPassword(captcha.get(2), pwd, captcha.get(1));
        cookie = getCookie(client, uid, sp, captcha.get(1));
        return cookie;
    }

    private List<String> getParam(HttpClient client, String uid) throws IOException {
        String url = "http://check.ptlogin2.qq.com/check?regmaster=&uin=" + uid + "&appid=46000101&js_ver=10028&js_type=1&login_sig=6sRjjkCeKE0J6NkXMXzD*oLgBDzHzCEGBMElHK*6vLunLhvvc9*9hOlIWWy*nTD1&u1=http%3A%2F%2Ft.qq.com&r=0.8935663888696581";
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String entityStr = EntityUtils.toString(entity);
        String regex = "\'.*?\'";
        List<String> param = ReglarExpression.ReglarArray(regex, entityStr);
        for (int i = 0; i < param.size(); i++) {
            param.set(i, param.get(i).replaceAll("\'", ""));
        }
        System.out.println(param);
        return param;
    }

    private String getCookie(DefaultHttpClient client, String uid, String sp, String captcha) throws IOException {
        String cookie = "";
        String url = "http://ptlogin2.qq.com/login?u=" + uid + "&p=" + sp + "&verifycode=" + captcha + "&aid=46000101&u1=http%3A%2F%2Ft.qq.com&h=1&ptredirect=1&ptlang=2052&from_ui=1&dumy=&low_login_enable=1&low_login_hour=720&regmaster=&fp=loginerroralert&action=4-8-1366017177854&mibao_css=&t=0&g=1&js_ver=10028&js_type=1&login_sig=6sRjjkCeKE0J6NkXMXzD*oLgBDzHzCEGBMElHK*6vLunLhvvc9*9hOlIWWy*nTD1";
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String entityStr = EntityUtils.toString(entity);
        System.out.println(entityStr);
        if (entityStr.indexOf("登录成功") > -1) {
            for (Cookie c : client.getCookieStore().getCookies()) {
                String key = c.getName();
                String value = c.getValue();
                cookie += key + "=" + value + ";";
            }
        } else {
            cookie = "-1";
        }
        return cookie;
    }

    private String md5(String originalText) throws Exception {
        byte buf[] = originalText.getBytes("ISO-8859-1");
        StringBuffer hexString = new StringBuffer();
        String result = "";
        String digit = "";

        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(buf);

            byte[] digest = algorithm.digest();

            for (int i = 0; i < digest.length; i++) {
                digit = Integer.toHexString(0xFF & digest[i]);

                if (digit.length() == 1) {
                    digit = "0" + digit;
                }

                hexString.append(digit);
            }

            result = hexString.toString();
        } catch (Exception ex) {
            result = "";
        }

        return result.toUpperCase();
    }

    private String hexchar2bin(String md5str) throws UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(md5str.length() / 2);

        for (int i = 0; i < md5str.length(); i = i + 2) {
            baos.write((HEXSTRING.indexOf(md5str.charAt(i)) << 4
                    | HEXSTRING.indexOf(md5str.charAt(i + 1))));
        }

        return new String(baos.toByteArray(), "ISO-8859-1");
    }

    private String GetPassword(String qq, String password, String verifycode) throws Exception {
        String P = hexchar2bin(md5(password));
        String U = md5(P + hexchar2bin(qq.replace("\\x", "").toUpperCase()));
        String V = md5(U + verifycode.toUpperCase());
        return V;
    }

    public void pushCookie() throws IOException, JSONException, IllegalBlockSizeException, IllegalBlockSizeException, IllegalBlockSizeException, BadPaddingException, BadPaddingException, BadPaddingException, NoSuchAlgorithmException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, NoSuchPaddingException, InterruptedException, FileNotFoundException, ScriptException, ScriptException, NoSuchMethodException, Exception {
        while (true) {
            DB db = Mongo.connect(new DBAddress("192.168.86.216", "people"));
            db.requestStart();
            DBCollection coll = db.getCollection("c_login_qqcookie");
            DBCollection uidColl = db.getCollection("c_login_qqid");
            DBCursor cur = uidColl.find(new BasicDBObject("status", "available"));
            ArrayList<DBObject> loginList = new ArrayList<DBObject>();
            while (cur.hasNext()) {
                Thread.sleep((long) (1000 * 10 * Math.random()));
                DBObject obj = cur.next();
                String cookie = login(obj.get("uid").toString(), obj.get("pwd").toString());
                if (!cookie.isEmpty() && cookie != null && !cookie.equals("-1")) {
                    Date date = new Date();
                    DBObject inObj = new BasicDBObject();
                    inObj.put("cookie", cookie);
                    inObj.put("date", date.toString());
                    inObj.put("status", "available");
                    loginList.add(inObj);
                } else {
                    continue;
                }
            }
            if (loginList.size() > 0) {
                DBObject queryObj = new BasicDBObject("status", "available");
                DBObject updateObj = new BasicDBObject("$set", new BasicDBObject("status", "disable"));
                coll.update(queryObj, updateObj, false, true);
                for (DBObject o : loginList) {
                    coll.insert(o);
                }
                cur.close();
                db.requestDone();
                db.getMongo().close();
                Thread.sleep(1000 * 60 * 60 * 12);
            } else {
                cur.close();
                db.requestDone();
                db.getMongo().close();
                Thread.sleep((long)(1000 * 60 * Math.random()));
            }
        }
    }

    @Override
    public void run() {
        super.run();
        try {
            pushCookie();
        } catch (IOException ex) {
            Logger.getLogger(TencentAutoLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(TencentAutoLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(TencentAutoLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(TencentAutoLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TencentAutoLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(TencentAutoLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(TencentAutoLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(TencentAutoLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(TencentAutoLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ScriptException ex) {
            Logger.getLogger(TencentAutoLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(TencentAutoLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TencentAutoLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

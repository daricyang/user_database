/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.autologin;

import gmc.extractor.ReglarExpression;
import gmc.pagecrawler.Crawler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.List;
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

/**
 *
 * @author Pok
 */
public class TencentAutoLogin {

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
        String url = "http://check.ptlogin2.qq.com/check?"
                + "regmaster="
                + "&uin=" + uid + ""
                + "&appid=46000101"
                + "&js_ver=10025"
                + "&js_type=1"
                + "&login_sig=hqOyyPiFH3ZiVroEg95mcbqyJk7QrTC97k7NQSLcBtxZlEFlWtfRKzTxUgNMhuFv"
                + "&u1=http%3A%2F%2Ft.qq.com%2F"
                + "&r=0.8728721777442843";
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
        String url = "http://ptlogin2.qq.com/login?"
                + "u=" + uid + ""
                + "&p=" + sp + ""
                + "&verifycode=" + captcha + ""
                + "&aid=46000101"
                + "&u1=http%3A%2F%2Ft.qq.com%2F"
                + "&h=1"
                + "&ptredirect=1"
                + "&ptlang=2052"
                + "&from_ui=1"
                + "&dumy="
                + "&low_login_enable=1"
                + "&low_login_hour=720"
                + "&regmaster="
                + "&fp=loginerroralert"
                + "&action=3-6-1365262771089"
                + "&mibao_css="
                + "&t=1"
                + "&g=1"
                + "&js_ver=10025"
                + "&js_type=1"
                + "&login_sig=hqOyyPiFH3ZiVroEg95mcbqyJk7QrTC97k7NQSLcBtxZlEFlWtfRKzTxUgNMhuFv";
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String entityStr = EntityUtils.toString(entity);
        System.out.println(entityStr);
        for (Cookie c : client.getCookieStore().getCookies()) {
            String key = c.getName();
            String value = c.getValue();
            cookie += key + "=" + value + ";";
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
}

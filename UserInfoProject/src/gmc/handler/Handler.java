/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.handler;

import gmc.autologin.SinaWeiboAutoLogin;
import gmc.config.Config;
import gmc.extractor.Extractor;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.json.JSONException;

/**
 *
 * @author Pok
 */
public class Handler {

    public static void main(String[] a) throws IOException, JSONException,
            IllegalBlockSizeException, BadPaddingException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
            InterruptedException {
        Config conf4Sina = new Config.Builder(Extractor.SINA, "192.168.11.56", "pagebase", "weibo", "192.168.11.56", "people", "c_weibo_userinfo")
                .configServer("192.168.86.216")
                .configErr("192.168.11.56", "people", "c_weibo_error")
                .configProcess("192.168.11.56", "people", "c_weibo_process")
                .configCookie("192.168.86.216", "people", "c_login_id", "c_login_cookie")
                .build();

        Config conf4Tencent = new Config.Builder(Extractor.TENCENT, "192.168.11.56", "pagebase", "tencent", "192.168.11.56", "people", "c_tencent_userinfo")
                .configServer("192.168.86.216")
                .configErr("192.168.11.56", "people", "c_tencent_error")
                .configProcess("192.168.11.56", "people", "c_tencent_process")
                .build();

        SinaWeiboAutoLogin swal = new SinaWeiboAutoLogin(conf4Sina);
        swal.start();
        Extractor es = new Extractor(conf4Sina);
        es.start();
        Extractor et = new Extractor(conf4Tencent);
        et.start();
//        TencentAutoLogin tal=new TencentAutoLogin();
//        tal.start();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.handler;

import gmc.autologin.SinaWeiboAutoLogin;
import gmc.autologin.TencentAutoLogin;
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
            InterruptedException{
        SinaWeiboAutoLogin swal=new SinaWeiboAutoLogin();
        swal.start();
        Extractor e=new Extractor();
        e.start();
        TencentAutoLogin tal=new TencentAutoLogin();
        tal.start();
    }
}

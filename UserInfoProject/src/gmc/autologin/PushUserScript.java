/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.autologin;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.net.UnknownHostException;

/**
 *
 * @author Administrator
 */
public class PushUserScript {
    public static void main(String[] a) throws UnknownHostException{
        Mongo m=new Mongo("192.168.86.216");
//        DB db=m.getDB("people");
//        DBCollection coll=db.getCollection("c_login_id");
//        String pwd="gmcgmc";
//        for(int i=1;i<=5;i++){
//            String uid="gmcda"+i+"@sina.cn";
//            DBObject obj=new BasicDBObject().append("uid", uid).append("pwd", pwd).append("status", "available");
//            coll.save(obj);
//        }
        DB db=m.getDB("people");
        DBObject o=new BasicDBObject().append("capped", true).append("size", 200000).append("max", 100);
        db.createCollection("c_login_cookie", o);
    }
}

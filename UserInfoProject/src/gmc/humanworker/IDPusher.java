/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.humanworker;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import gmc.config.Config;
import java.net.UnknownHostException;

/**
 *
 * @author Pok
 */
public class IDPusher {
    public static void main(String[] a) throws UnknownHostException{
        DB db=Mongo.connect(new DBAddress("192.168.86.216", "people"));
        DBCollection coll=db.getCollection("c_login_id");
        DBObject o=new BasicDBObject().append("uid", "2826374053@qq.com").append("pwd", "a12345").append("status", "available");
        coll.save(o);
    }
}

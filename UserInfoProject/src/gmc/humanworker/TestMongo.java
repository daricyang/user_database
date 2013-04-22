/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.humanworker;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoOptions;
import gmc.config.Config;
import java.net.UnknownHostException;

/**
 *
 * @author Pok
 */
public class TestMongo {

    public static void main(String[] a) throws UnknownHostException {


        DB db = Mongo.connect(new DBAddress("192.168.86.216", "pagebase"));
        DBCollection coll = db.getCollection("weibo");
        DBObject queryObj = new BasicDBObject().append("url", new BasicDBObject("$regex", "info"));
        System.out.println(queryObj.toString());
        DBCursor cur = coll.find(queryObj);
        System.out.println(cur.size());
//        cur.sort(new BasicDBObject("time", 1));
//        while(cur.hasNext()){
//            System.out.println(cur.next().get("time")+"\n"+cur.next().get("url"));
//        }
    }
}

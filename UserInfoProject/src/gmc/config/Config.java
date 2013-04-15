package gmc.config;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.Date;

/**
 *
 * @author pok
 */
public class Config {

    public static void init() throws UnknownHostException {
        System.out.println("--\tinitial process!\t--");
        Mongo mongo = new Mongo(processHost);
        DB db = mongo.getDB(Config.processDBName);
        DBCollection coll = db.getCollection(Config.processCollectionName);
        if (coll.isCapped()) {
            coll.drop();
            db.createCollection(Config.processCollectionName, new BasicDBObject().append("capped", true).append("size", 1024 * 1024).append("max", 1));
            DBObject obj = new BasicDBObject().append("date", new Date()).append("time", 0);
            coll.save(obj);
            System.out.println(coll.count());
        }
        mongo.close();
        System.out.println("--\tinitial process done!\t--");
    }
    public static String localhost = "127.0.0.1";
    public static String server = "192.169.86.216";
    public static String sourceHost = server;
    public static String sourceMongoDBName = "pagebase";
    public static String sourceMongoCollectionName = "weibo";
    public static String disHost = localhost;
    public static String disMongoDBName = "people";
    public static String disMongoCollectionName = "c_weibo_userinfo";
    public static String processHost = localhost;
    public static String processDBName = "people";
    public static String processCollectionName = "c_weibo_process";
    public static String redisHost = server;
    public static String redisSetName = "done";
    public static String errHost = server;
    public static String errMongoDBName = "people";
    public static String errMongoCollectionName = "c_weibo_error";
    public static String cookieHost = server;
    public static String cookieDBName = "people";
    public static String cookieCollectionName = "c_login_cookie";
    public static String cookie_idCollectionName = "c_login_id";
}

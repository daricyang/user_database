package gmc.config;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
import java.net.UnknownHostException;

/**
 *
 * @author pok
 */
public class Config {

    public static void init() throws UnknownHostException {
        System.out.println("--\tinitial process!\t--");
        Mongo mongo = new Mongo(processHost);
        if (!mongo.getDB(Config.processDBName).getCollection(Config.processCollectionName).isCapped()) {
            mongo.getDB(Config.processDBName).createCollection(Config.processCollectionName, new BasicDBObject().append("capped", true).append("size", 1024).append("max", 1));
        }
        mongo.close();
        System.out.println("--\tinitial process done!\t--");
    }
    public static String sourceHost = "192.168.86.216";
    public static String sourceMongoDBName = "pagebase";
    public static String sourceMongoCollectionName = "weibo";
    public static String disHost = "192.168.86.216";
    public static String disMongoDBName = "people";
    public static String disMongoCollectionName = "c_weibo_userinfo";
    public static String processHost = "192.168.86.216";
    public static String processDBName = "people";
    public static String processCollectionName = "c_weibo_process";
    public static String redisHost = "192.168.86.216";
    public static String redisSetName = "done";
    public static String errHost = "192.168.86.216";
    public static String errMongoDBName = "people";
    public static String errMongoCollectionName = "c_weibo_error";
    public static String cookieHost="192.168.86.216";
    public static String cookieDBName="people";
    public static String cookieCollectionName="c_login_cookie";
    public static String cookie_idCollectionName="c_login_id";
}

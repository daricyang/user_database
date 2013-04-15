/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.humanworker;


import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
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
    public static void main(String[] a) throws UnknownHostException{
        
        
        DB db=Mongo.connect(new DBAddress("192.168.86.216", "people"));
        DBCollection coll=db.getCollection("c_weibo_process");
        System.out.println(coll.isCapped());
    }
}

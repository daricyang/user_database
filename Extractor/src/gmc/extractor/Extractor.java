/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.extractor;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Pok
 */
public class Extractor {

    public void extractor() throws UnknownHostException, InterruptedException {
        Jedis jedis = new Jedis(Config.redisHost);
        Mongo mongo = new Mongo(Config.sourceHost);
        DB db = mongo.getDB(Config.sourceMongoDBName);
        DBCollection coll = db.getCollection(Config.sourceMongoCollectionName);
        DBCursor cur = coll.find();
        while (cur.hasNext()) {
            DBObject obj = cur.next();
            String url = obj.get("url").toString();
            if (url.indexOf("info") != -1) {
                url = url.substring(url.indexOf("com/") + 4, url.indexOf("/info"));
                if (jedis.sadd("done", url) != 0) {
                    synchronized (MultiExtractor.class) {
                        int tcount = MultiExtractor.getThreadCount();
                        while (tcount > 20) {
                            MultiExtractor.class.wait();
                            tcount = MultiExtractor.getThreadCount();
                        }
                        MultiExtractor t = new MultiExtractor(url,obj.get("html").toString());
                        t.start();
                    }
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        cur.close();
        mongo.close();
        
    }
    
    public static void main(String[] a) throws UnknownHostException, InterruptedException{
        Extractor e=new Extractor();
        e.extractor();
    }
}

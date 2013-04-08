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
import gmc.config.Config;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.Date;
import javax.xml.crypto.Data;
import redis.clients.jedis.Jedis;
import sun.net.www.content.image.gif;

/**
 *
 * @author Pok
 */
public class Extractor {

    public void extractor() throws UnknownHostException, InterruptedException {
        int c = 0;
        Config.init();
        while (true) {
            System.out.println("--\tRound:"+(++c)+"\t--");
            String lastProcessTime = "0";
            Mongo mongo = new Mongo(Config.sourceHost);
            Mongo proMongo = new Mongo(Config.processHost);
            DB db = mongo.getDB(Config.sourceMongoDBName);
            DB proDb = mongo.getDB(Config.processDBName);
            DBCollection coll = db.getCollection(Config.sourceMongoCollectionName);
            DBCollection proColl = proDb.getCollection(Config.processCollectionName);
            DBObject timeObj = proColl.findOne();
            if (timeObj != null && timeObj.containsField("time")) {
                lastProcessTime = new BigDecimal(timeObj.get("time").toString()).toString();
            }
            DBObject queryObj = new BasicDBObject().append("time", new BasicDBObject().append("$gt", Long.parseLong(lastProcessTime)));
            DBCursor cur = coll.find(queryObj);
            int min = new Date().getMinutes() - 1;
            while (cur.hasNext()) {
                if (new Date().getMinutes() != min) {
                    DBObject obj = cur.next();
                    lastProcessTime = new BigDecimal(obj.get("time").toString()).toString();
                    String url = obj.get("url").toString();
                    if (url.indexOf("info") != -1) {
                        url = url.substring(url.indexOf("com/") + 4, url.indexOf("/info"));
                        synchronized (MultiExtractor.class) {
                            int tcount = MultiExtractor.getThreadCount();
                            while (tcount > 20) {
                                MultiExtractor.class.wait();
                                tcount = MultiExtractor.getThreadCount();
                            }
                            MultiExtractor t = new MultiExtractor(url, obj.get("html").toString());
                            t.start();
                        }
                    } else {
                        continue;
                    }
                } else {
                    break;
                }
            }
            proColl.insert(new BasicDBObject().append("date", new Date().getTime()).append("time", lastProcessTime));
            cur.close();
            mongo.close();
            proMongo.close();
            Thread.sleep(1000*60*60*4);
        }

    }

    public static void main(String[] a) throws UnknownHostException, InterruptedException {
        Extractor e = new Extractor();
        e.extractor();
    }
}

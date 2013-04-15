/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.extractor;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import gmc.config.Config;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pok
 */
public class Extractor extends Thread {

    public void extractor() throws UnknownHostException, InterruptedException {
        int c = 0;
        Config.init();
        while (true) {
            double lastProcessTime = 0;
            DB db = Mongo.connect(new DBAddress("192.168.86.216", "pagebase"));
            DB proDb = Mongo.connect(new DBAddress("192.168.86.216", "people"));
            DBCollection coll = db.getCollection("weibo");
            DBCollection proColl = proDb.getCollection("c_weibo_process");
            DBObject timeObj = proColl.findOne();
            //System.out.println(timeObj != null && timeObj.containsField("time"));
            if (timeObj != null && timeObj.containsField("time")) {
                lastProcessTime = Double.parseDouble(timeObj.get("time").toString());
            }
            System.out.println("--\tRound:" + (++c) + "\tLast Process Time:" + lastProcessTime + "\t--");
            DBObject queryObj = new BasicDBObject().append("time", new BasicDBObject().append("$gt", lastProcessTime));
            DBCursor cur = coll.find(queryObj);
            while (cur.hasNext()) {
                DBObject obj = cur.next();
                double curTime = (Double) obj.get("time");
                if (lastProcessTime < curTime) {
                    lastProcessTime = curTime;
                    proColl.save(new BasicDBObject().append("date", new Date().getTime()).append("time", lastProcessTime));
                }
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

            }
            cur.close();
            db.getMongo().close();
            proDb.getMongo().close();
            Thread.sleep(1000 * 60 * 10);
        }

    }

    @Override
    public void run() {
        super.run();
        try {
            extractor();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] a) throws UnknownHostException, InterruptedException {
        Extractor e = new Extractor();
        e.extractor();
    }
}

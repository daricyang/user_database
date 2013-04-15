/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.extractor;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import gmc.config.Config;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Pok
 */
public class MultiExtractor extends Thread {

    static String threadName[];
    static int threadCount = 0;
    private String error = "";
    private String src;
    private String id;
    private HashMap<String, String> info = new HashMap<String, String>();
    static final int MAXTHREADCOUNT = 200;

    static int getThreadCount() {
        return threadCount;
    }

    static {
        threadName = new String[MAXTHREADCOUNT];
        for (int i = 1; i <= MAXTHREADCOUNT; i++) {
            threadName[i - 1] = "Thread1-" + i;
        }
    }

    public MultiExtractor(String id, String src) {
        super();
        this.src = src;
        this.id = id;
        synchronized (MultiExtractor.class) {
            threadCount++;
            for (int j = 0; j < threadName.length; j++) {
                if (threadName[j] != null) {
                    String temp = threadName[j];
                    threadName[j] = null;
                    this.setName(temp);
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            extractInfo();
            save();
        } catch (Exception e) {
            error += e.getMessage();
        } finally {
            synchronized (MultiExtractor.class) {
                threadCount--;
                String[] nameSpilt = this.getName().split("-");
                threadName[Integer.parseInt(nameSpilt[1]) - 1] = this.getName();
                MultiExtractor.class.notifyAll();
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    error += e.getMessage();
                }
            }
        }
    }

    public void extractInfo() {
        String regex = "label S_txt2.*?t<\\\\/div>";
        ArrayList<String> al = ReglarExpression.ReglarArray(regex, src);
        for (String s : al) {
            try {
                s = s.substring(15);
                s = s.replaceAll("\\\\t", "").replaceAll("\\\\n", "").replaceAll("\\\\r", "").replaceAll("<\\\\/div>", "").replaceAll("<div class=\\\\\"con\\\\\">", ",").replaceAll("<span class=\\\\\"S_func1\\\\\" node-type=\\\\\"tag\\\\\">", " ").replaceAll("<.*?>", "").replaceAll("\\\\", "");
                String[] temp = s.split(",");
                info.put(temp[0], temp[1]);
            } catch (Exception ex) {
                error += ex.getMessage();
            }
        }
    }

    private void save() throws UnknownHostException {
        if (!info.isEmpty()) {
            DB db = Mongo.connect(new DBAddress("192.168.86.216", "people"));
            DBCollection coll = db.getCollection("c_weibo_userinfo");
            DBObject obj = new BasicDBObject("uid", id);
            for (String key : info.keySet()) {
                if (key.equals("标签")) {
                    String[] tag = info.get(key).trim().split(" ");
                    obj.put(key, tag);
                    continue;
                }
                obj.put(key, info.get(key).trim());
            }
            coll.insert(obj);
            info.clear();
        }
        if (!error.isEmpty() || error != null || !error.equals("")) {
            DB db = Mongo.connect(new DBAddress("192.168.86.216", "peopel"));
            DBCollection coll = db.getCollection("c_weibo_error");
            DBObject obj = new BasicDBObject("error", error).append("uid", id);
            coll.insert(obj);
            error = "";
        }
    }
}

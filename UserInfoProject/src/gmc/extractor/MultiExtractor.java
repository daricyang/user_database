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
import gmc.config.Config;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    private Config conf;
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

    public MultiExtractor(Config conf, String id, String src) {
        super();
        this.conf = conf;
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
            if (conf.getType() == Extractor.SINA) {
                extractWeiboInfo();
                save();
            } else if (conf.getType() == Extractor.TENCENT) {
                extractTencentInfo();
                save();
            }
        } catch (Exception e) {
            error += e.getLocalizedMessage();
        } finally {
            synchronized (MultiExtractor.class) {
                threadCount--;
                String[] nameSpilt = this.getName().split("-");
                threadName[Integer.parseInt(nameSpilt[1]) - 1] = this.getName();
                MultiExtractor.class.notifyAll();
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    error += e.getLocalizedMessage();
                }
            }
        }
    }

    public void extractWeiboInfo() {
        String regex = "label S_txt2.*?t<\\\\/div>";
        ArrayList<String> al = ReglarExpression.ReglarArray(regex, src);
        for (String s : al) {
            try {
                s = s.substring(15);
                s = s.replaceAll("\\\\t", "").replaceAll("\\\\n", "").replaceAll("\\\\r", "").replaceAll("<\\\\/div>", "").replaceAll("<div class=\\\\\"con\\\\\">", ",").replaceAll("<span class=\\\\\"S_func1\\\\\" node-type=\\\\\"tag\\\\\">", " ").replaceAll("<.*?>", "").replaceAll("\\\\", "");
                String[] temp = s.split(",");
                info.put(temp[0], temp[1]);
            } catch (Exception ex) {
                error += ex.getLocalizedMessage();
            }
        }
    }

    public void extractTencentInfo() {
        // src = src.replaceAll("\\\\n", "").replaceAll("\\\\t", "").replaceAll("\\\\", "");
        //System.out.println(src);

        String regex = "<li>.*?</li>";
        String res = ReglarExpression.Reglar(regex, src);
        regex = "<li>.*?</li>";
        ArrayList<String> al = ReglarExpression.ReglarArray(regex, res);
        int i = 1;
        for (String s : al) {
            s = s.replaceAll("<.*?>", "").replaceAll(" ", "");
            if (s.equals("设置") || s.equals("标准版") || s.equals("退出")) {
                continue;
            }
            String[] sal = s.split("：");
            if (sal.length == 2) {
                info.put(sal[0], sal[1]);
            } else if (sal.length == 1) {
                info.put("背景" + i, sal[0]);
                i++;
            }
        }
    }

    private void save() throws UnknownHostException {
        if (!info.isEmpty()) {
            DB db = Mongo.connect(new DBAddress(conf.getDisHost(), conf.getDisMongoDBName()));
            DBCollection coll = db.getCollection(conf.getDisMongoCollectionName());
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
            db.getMongo().close();
        }
        if (!error.isEmpty() && error != null && !error.equals("") && !error.equals("null")) {
            DB db = Mongo.connect(new DBAddress(conf.getErrHost(), conf.getErrMongoDBName()));
            DBCollection coll = db.getCollection(conf.getErrMongoCollectionName());
            DBObject obj = new BasicDBObject("error", error).append("uid", id);
            coll.insert(obj);
            error = "";
            db.getMongo().close();
        }
    }

    public static void main(String[] a) throws FileNotFoundException, IOException {
//        BufferedReader br = new BufferedReader(new FileReader(new File("./a.html")));
//        StringBuilder sb = new StringBuilder();
//        String tmp = null;
//        while ((tmp = br.readLine()) != null) {
//            sb.append(tmp);
//        }
//        br.close();
//        String src = sb.toString();
//        
//        System.out.println(al);
    }
}

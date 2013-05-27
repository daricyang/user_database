/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.zone;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import redis.clients.jedis.Jedis;

/**
 *
 * @author pok
 */
public class JobCreator {

    private DBCollection coll;
    private static ArrayList<String> jobBatcher = new ArrayList<String>();
    private static Date date;

    public static void main(String[] a) throws Exception {
        JobCreator jc = new JobCreator();
        jc.setup();
        jc.craeteRamdonQQ();
        jc.shutdown();
    }

    private void setup() throws UnknownHostException {
        DB db = Mongo.connect(new DBAddress("127.0.0.1", "Crawler"));
        coll = db.getCollection("Crawler");

    }

    private void craeteRamdonQQ() throws Exception {
        String[] a = new String[999];
        String[] b = new String[999];
        String[] c = new String[999];
        init(a);
        init(b);
        init(c);
        ramdon(a);
        ramdon(b);
        ramdon(c);
        combine(a, b, c);
    }

    private void init(String[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = String.valueOf(i);
        }
    }

    private void ramdon(String[] a) {
        for (int i = 0; i < a.length; i++) {
            int ex = (int) (Math.random() * a.length);
            String tmp = a[i];
            a[i] = a[ex];
            a[ex] = tmp;
        }
    }

    private void combine(String[] a, String[] b, String[] c) throws Exception {
        for (String aEle : a) {
            boolean aJudge = judge(aEle);
            for (String bEle : b) {
                boolean bJudge = judge(bEle);
                String[] jobs = new String[999];
                for (int i = 0; i < c.length; i++) {
                    if (aJudge) {
                        if (bJudge) {
                            continue;
                        } else {
                            bEle = ensure3number(bEle);
                            c[i] = ensure3number(c[i]);
                            jobs[i] = bEle + c[i];
                        }
                    } else {
                        bEle = ensure3number(bEle);
                        c[i] = ensure3number(c[i]);
                        jobs[i] = aEle + bEle + c[i];
                    }
                }
                ramdon(jobs);
                saveJob(jobs);
            }
        }
    }

    private boolean judge(String aEle) {
        if (Integer.valueOf(aEle) == 0) {
            return true;
        } else {
            return false;
        }
    }

    private String ensure3number(String bEle) throws Exception {
        if (bEle == null || bEle.length() > 3) {
            throw new Exception("illegal element form!");
        } else if (bEle.length() == 1) {
            bEle = "00" + bEle;
            return bEle;
        } else if (bEle.length() == 2) {
            bEle = "0" + bEle;
            return bEle;
        } else {
            return bEle;
        }

    }

    private void saveJob(String[] jobs) throws InterruptedException {
        for (int i = 0; i < (int) (Math.ceil(jobs.length / 100.0)); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < (jobs.length - i * 100 > 100 ? 100 : jobs.length - i * 100); j++) {
                if (jobs[i * 100 + j] == null || jobs[1 * 100 + j].equals("")) {
                    continue;
                } else {
                    sb.append("\"").append("http://base.s2.qzone.qq.com/cgi-bin/user/cgi_userinfo_get_all?uin=").append(jobs[i * 100 + j]).append("\"").append(",");
                    //.append("&vuin=823247241&fupdate=1&rd=0.39848598069511354&g_tk=598014943")
                }
            }
            String urls = sb.toString();
            if (urls.length() > 0) {
                urls = urls.substring(0, urls.length() - 1);
                String job = "{\"handler\":\"zone\",\"urls\":[" + urls + "]}";
                batch(job, false);
            }

        }
        batch(null, true);
    }

    private void debugger() throws InterruptedException {
        String[] a = new String[999];
        System.out.println(a.length);
        for (int i = 0; i < 999; i++) {
            a[i] = String.valueOf(i);
        }
        saveJob(a);
    }

    private void shutdown() {
        System.out.println("All job was created!");
    }

    private void batch(String job, boolean flush) throws InterruptedException {
        if (job != null) {
            String s = job;
            jobBatcher.add(s);
        }
        if (jobBatcher.size() > 100) {
            DBObject obj=new BasicDBObject("jobs", jobBatcher.toString());
            coll.save(obj);
            jobBatcher.clear();
        }
    }
}

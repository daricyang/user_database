/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.userinfocollector;

import java.util.ArrayList;

/**
 * 多线程爬取类
 *
 * @author Pok
 */
public class SinaMultiThread extends Thread {

    public static String threadName[];
    public static int threadCount = 0;
    public static final int MAXTHREADCOUNT = 20;
    private String id;
    private String cookie;
    private String host;
    private String charset;
    private ArrayList<String> F = new ArrayList<String>();

    static {
        threadName = new String[MAXTHREADCOUNT];
        for (int i = 1; i <= MAXTHREADCOUNT; i++) {
            threadName[i - 1] = "Thread1-" + i;
        }
    }

    /**
     * 构造函数
     *
     * @param cookie 登录cookie
     * @param host 主机
     * @param charset 编码
     * @param id 用户id
     */
    public SinaMultiThread(String cookie, String host, String charset, String id) {
        super();
        this.id = id;
        this.cookie = cookie;
        this.host = host;
        this.charset = charset;
        synchronized (SinaMultiThread.class) {
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

    public void run() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            synchronized (SinaMultiThread.class) {
                threadCount--;
                String[] nameSpilt = this.getName().split("-");
                threadName[Integer.parseInt(nameSpilt[1]) - 1] = this.getName();
                SinaMultiThread.class.notifyAll();
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public ArrayList<String> getF() {
        return F;
    }

    public void setF(ArrayList<String> F) {
        this.F = F;
    }
}

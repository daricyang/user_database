/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.pagecrawler;

import gmc.autologin.SinaWeiboAutoLogin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Pok
 */
public class Crawler {

    private String htmlSource;
    private String host;
    private String cookie;
    private String chartset;

    /**
     * 构造函数
     *
     * @param host 主机地址，如：book.douban.com
     * @param cookie 登录cookie
     * @param chartset 编码
     */
    public Crawler(String host, String cookie, String chartset) {
        this.host = host;
        this.cookie = cookie;
        this.chartset = chartset;
    }

    /**
     * 爬取函数
     *
     * @param url 待爬取的url
     * @return content 爬取返回的源代码
     */
    public String crawler(String url) throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        get.setHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
        get.setHeader("Accept-Encoding", "deflate");
        get.setHeader("Accept-Language",
                "zh-CN,zh;q=0.8");
        get.setHeader("Cache-Control", "max-age=0");
        get.setHeader("Cookie", cookie);
        get.setHeader("Connection", "keep-alive");
        get.setHeader("Host", host);
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        InputStreamReader isr = new InputStreamReader(entity.getContent(), chartset);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String temp = null;
        while ((temp = br.readLine()) != null) {
            sb.append(temp);
        }
        return sb.toString();
    }

    public static void main(String[] a) throws Exception {
        SinaWeiboAutoLogin s = new SinaWeiboAutoLogin();
        String cookie = s.getLoginCookie("gmcda2@sina.cn", "gmcgmc");
        Crawler c = new Crawler("weibo.com", cookie, "utf-8");
        while (true) {
            c.crawler("http://weibo.com/u/3236369790?wvr=5&wvr=5&lf=reg");
        }
    }
}

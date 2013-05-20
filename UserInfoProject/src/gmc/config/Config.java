/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.config;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.Date;

/**
 *
 * @author pok
 */
public class Config {

    private String localhost;
    private String server;
    private String sourceHost;
    private String sourceMongoDBName;
    private String sourceMongoCollectionName;
    private String disHost;
    private String disMongoDBName;
    private String disMongoCollectionName;
    private String processHost;
    private String processDBName;
    private String processCollectionName;
    private String errHost;
    private String errMongoDBName;
    private String errMongoCollectionName;
    private String cookieHost;
    private String cookieDBName;
    private String cookieCollectionName;
    private String cookie_idCollectionName;
    private int type;
    public static class Builder {

        private String localhost = "127.0.0.1";
        private String server = "192.169.86.216";
        private String sourceHost = "192.169.86.216";
        private String sourceMongoDBName = "pagebase";
        private String sourceMongoCollectionName = "weibo";
        private String disHost = "192.169.86.216";
        private String disMongoDBName = "people";
        private String disMongoCollectionName = "c_weibo_userinfo";
        private String processHost = disHost;
        private String processDBName = disMongoDBName;
        private String processCollectionName = "c_weibo_process";
        private String errHost = disHost;
        private String errMongoDBName = disMongoDBName;
        private String errMongoCollectionName = "c_weibo_error";
        private String cookieHost = disHost;
        private String cookieDBName = disMongoDBName;
        private String cookieCollectionName = "c_login_cookie";
        private String cookie_idCollectionName = "c_login_id";
        private int type;
        public Builder(int type,String sourceHost, String sourceDBName, String sourceCollName, String disHost, String disDBName, String disCollName) {
            this.type=type;
            this.sourceHost = sourceHost;
            this.sourceMongoDBName = sourceDBName;
            this.sourceMongoCollectionName = sourceCollName;
            this.disHost = disHost;
            this.disMongoDBName = disDBName;
            this.disMongoCollectionName = disCollName;
        }
        
        public Builder(String disHost, String disDBName, String disCollName){
            this.disHost = disHost;
            this.disMongoDBName = disDBName;
            this.disMongoCollectionName = disCollName;
        }

        public Builder configCookie(String cookieHost, String cookieDBName, String cookie_idCollName, String cookieCollName) {
            this.cookieHost = cookieHost;
            this.cookieDBName = cookieDBName;
            this.cookie_idCollectionName = cookie_idCollName;
            this.cookieCollectionName = cookieCollName;
            return this;
        }

        public Builder configProcess(String processHost, String processDBName, String processCollName) {
            this.processHost = processHost;
            this.processDBName = processDBName;
            this.processCollectionName = processCollName;
            return this;
        }

        public Builder configErr(String errHost, String errDBName, String errCollName) {
            this.errHost = errHost;
            this.errMongoDBName = errDBName;
            this.errMongoCollectionName = errCollName;
            return this;
        }

        public Builder configServer(String server) {
            this.server = server;
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }

    private Config(Builder b) {
        this.localhost = b.localhost;
        this.server = b.server;
        this.sourceHost = b.sourceHost;
        this.sourceMongoDBName = b.sourceMongoDBName;
        this.sourceMongoCollectionName = b.sourceMongoCollectionName;
        this.disHost = b.disHost;
        this.disMongoDBName = b.disMongoDBName;
        this.disMongoCollectionName = b.disMongoCollectionName;
        this.processHost = b.processHost;
        this.processDBName = b.processDBName;
        this.processCollectionName = b.processCollectionName;
        this.errHost = b.errHost;
        this.errMongoDBName = b.errMongoDBName;
        this.errMongoCollectionName = b.errMongoCollectionName;
        this.cookieHost = b.cookieHost;
        this.cookieDBName = b.cookieDBName;
        this.cookieCollectionName = b.cookieCollectionName;
        this.cookie_idCollectionName = b.cookie_idCollectionName;
        this.type=b.type;
    }

    public void init() throws UnknownHostException {
        System.out.println("--\tinitial process!\t--");
        DB db = Mongo.connect(new DBAddress(processHost, processDBName));
        DBCollection coll = db.getCollection(processCollectionName);
        if (!coll.isCapped()) {
            coll.drop();
            db.createCollection(processCollectionName, new BasicDBObject().append("capped", true).append("size", 1024 * 1024).append("max", 1));
            DBObject obj = new BasicDBObject().append("date", new Date()).append("time", 0);
            coll.save(obj);
            System.out.println(coll.count());
        }
        db.getMongo().close();
        System.out.println("--\tinitial process done!\t--");
    }

    public String getLocalhost() {
        return localhost;
    }

    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSourceHost() {
        return sourceHost;
    }

    public void setSourceHost(String sourceHost) {
        this.sourceHost = sourceHost;
    }

    public String getSourceMongoDBName() {
        return sourceMongoDBName;
    }

    public void setSourceMongoDBName(String sourceMongoDBName) {
        this.sourceMongoDBName = sourceMongoDBName;
    }

    public String getSourceMongoCollectionName() {
        return sourceMongoCollectionName;
    }

    public void setSourceMongoCollectionName(String sourceMongoCollectionName) {
        this.sourceMongoCollectionName = sourceMongoCollectionName;
    }

    public String getDisHost() {
        return disHost;
    }

    public void setDisHost(String disHost) {
        this.disHost = disHost;
    }

    public String getDisMongoDBName() {
        return disMongoDBName;
    }

    public void setDisMongoDBName(String disMongoDBName) {
        this.disMongoDBName = disMongoDBName;
    }

    public String getDisMongoCollectionName() {
        return disMongoCollectionName;
    }

    public void setDisMongoCollectionName(String disMongoCollectionName) {
        this.disMongoCollectionName = disMongoCollectionName;
    }

    public String getProcessHost() {
        return processHost;
    }

    public void setProcessHost(String processHost) {
        this.processHost = processHost;
    }

    public String getProcessDBName() {
        return processDBName;
    }

    public void setProcessDBName(String processDBName) {
        this.processDBName = processDBName;
    }

    public String getProcessCollectionName() {
        return processCollectionName;
    }

    public void setProcessCollectionName(String processCollectionName) {
        this.processCollectionName = processCollectionName;
    }

    public String getErrHost() {
        return errHost;
    }

    public void setErrHost(String errHost) {
        this.errHost = errHost;
    }

    public String getErrMongoDBName() {
        return errMongoDBName;
    }

    public void setErrMongoDBName(String errMongoDBName) {
        this.errMongoDBName = errMongoDBName;
    }

    public String getErrMongoCollectionName() {
        return errMongoCollectionName;
    }

    public void setErrMongoCollectionName(String errMongoCollectionName) {
        this.errMongoCollectionName = errMongoCollectionName;
    }

    public String getCookieHost() {
        return cookieHost;
    }

    public void setCookieHost(String cookieHost) {
        this.cookieHost = cookieHost;
    }

    public String getCookieDBName() {
        return cookieDBName;
    }

    public void setCookieDBName(String cookieDBName) {
        this.cookieDBName = cookieDBName;
    }

    public String getCookieCollectionName() {
        return cookieCollectionName;
    }

    public void setCookieCollectionName(String cookieCollectionName) {
        this.cookieCollectionName = cookieCollectionName;
    }

    public String getCookie_idCollectionName() {
        return cookie_idCollectionName;
    }

    public void setCookie_idCollectionName(String cookie_idCollectionName) {
        this.cookie_idCollectionName = cookie_idCollectionName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.extractor;

/**
 *
 * @author pok
 */
public class Config {

    static String sourceHost = "192.168.86.216";
    static String sourceMongoDBName = "pagebase";
    static String sourceMongoCollectionName = "weibo";
    static String disHost = "127.0.0.1";
    static String disMongoDBName = "people";
    static String disMongoCollectionName = "c_weibo_userinfo";
    static String redisHost = "127.0.0.1";
    static String redisSetName = "done";
    static String errHost = "127.0.0.1";
    static String errMongoDBName = "people";
    static String errMongoCollectionName = "c_weibo_extracterror";
}

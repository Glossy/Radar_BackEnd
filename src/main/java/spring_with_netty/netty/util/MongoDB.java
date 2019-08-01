package spring_with_netty.netty.util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//import org.bson.Document;
//
//import com.mongodb.async.client.MongoClient;
//import com.mongodb.async.client.MongoClients;
//import com.mongodb.async.client.MongoCollection;
//import com.mongodb.async.client.MongoDatabase;

/**
 * @Author: Wu
 * @Date: 2019/3/24 9:54 AM
 * MongoDB数据库
 */
public class MongoDB {
//    public MongoCollection<Document> collection;
//    protected MongoClient mongoClient;
//    protected MongoDatabase mongoDatabase;
//    private String colName = "data";
//    private String dbName = "tcp";
//
//    public void setColName(String colName) {
//        this.colName = colName;
//    }
//
//    public void setDbName(String dbName) {
//        this.dbName = dbName;
//    }
//
//    public String getDbName() {
//        return dbName;
//    }
//
//    public String getColname() {
//        return colName;
//    }
//
//    public MongoDatabase getDatabase(String dbName) throws IllegalArgumentException{
//        this.dbName = dbName;
//        try {
//            return this.mongoClient.getDatabase(this.dbName);
//        }catch(IllegalArgumentException e) {
//            TCPRadarServer.LOG.error(e.getMessage());
//            throw e;
//        }
//    }
//
//    public MongoCollection<Document> getCollection(String colName) throws IllegalArgumentException{
//        this.colName = colName;
//        try {
//            return this.mongoDatabase.getCollection(this.colName);
//        }catch(IllegalArgumentException e) {
//            TCPRadarServer.LOG.error(e.getMessage());
//            throw e;
//        }
//    }

//    @PostConstruct
//    public void init() {
//        try {
//            mongoClient = MongoClients.create();
//            mongoDatabase = mongoClient.getDatabase(dbName);
//            collection = mongoDatabase.getCollection(colName);
//            TCPRadarServer.LOG.info("Connected to db.col(%s.%s) successfully\n", dbName, colName);
//        }catch(Exception e) {
//            TCPRadarServer.LOG.error("MongoDB init unsuccessfully!\n" + e.getMessage());
//        }
//    }

}

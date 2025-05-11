package com.teamdss.dcu_guide_bot.service;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

@Service
public class MongoCheckService {
    private final MongoClient mongoClient;

    public MongoCheckService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void checkConnection() {
        try {
            MongoDatabase database = mongoClient.getDatabase("admin");
            database.runCommand(new Document("ping", 1));
            System.out.println("MongoDB 연결 성공!");
        } catch (MongoException e) {
            System.err.println("MongoDB 연결 실패!");
            e.printStackTrace();
        }
    }
}

package com.teamdss.dcu_guide_bot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamdss.dcu_guide_bot.service.MongoCheckService;

@RestController
public class MongoCheckController {
    private final MongoCheckService mongoCheckService;

    public MongoCheckController(MongoCheckService mongoCheckService) {
        this.mongoCheckService = mongoCheckService;
    }

    @GetMapping("/api/mongo-check")
    public String checkMongo() {
        mongoCheckService.checkConnection();
        return "MongoDB 연결 체크 완료 (콘솔 로그 확인)";
    }
}

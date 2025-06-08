package com.teamdss.dcu_guide_bot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.teamdss.dcu_guide_bot.model.Message;

@Service
public class ChatHistoryService {
    private final Map<String, List<Message>> historyMap = new ConcurrentHashMap<>();

    public void addMessage(String sessionId, Message message) {
        List<Message> history = historyMap.computeIfAbsent(
            sessionId, k -> new ArrayList<>()
        );
        history.add(message);
    }

    public List<Message> getHistory(String sessionId) {
        return historyMap.getOrDefault(sessionId, new ArrayList<>());
    }
}
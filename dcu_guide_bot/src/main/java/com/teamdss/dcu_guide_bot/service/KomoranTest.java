package com.teamdss.dcu_guide_bot.service;

import java.util.List;

import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

public class KomoranTest {

  public static void main(String[] args) throws Exception {

    Komoran komoran = new Komoran("D:/project/TeamDSS/code/server/komoran/KOMORAN/core/models_full");
    komoran.setFWDic("user_data/fwd.user");
    komoran.setUserDic("user_data/dic.user");

    String input = "대구가톨릭대학교의 컴퓨터공학전공에 대해 알려줘";
    KomoranResult analyzeResultList = komoran.analyze(input);
    List<Token> tokenList = analyzeResultList.getTokenList();

    // 2. print nouns
    System.out.println("==========print 'getNouns()'==========");
    System.out.println(analyzeResultList.getNouns());
    System.out.println();
  }
}
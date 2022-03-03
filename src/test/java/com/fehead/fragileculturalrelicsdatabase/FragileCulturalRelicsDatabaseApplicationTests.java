package com.fehead.fragileculturalrelicsdatabase;

import com.fehead.fragileculturalrelicsdatabase.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class FragileCulturalRelicsDatabaseApplicationTests {

    @Autowired
    FileService fileService;
    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void contextLoads() {
     //   mongoTemplate.save(fileService.allFiles());
//        JSONObject id = mongoTemplate.findOne(new Query(Criteria.where("id").ne("we")), JSONObject.class,"jSONObject");
//        assert id != null;
//        System.out.println(id);
    }


}
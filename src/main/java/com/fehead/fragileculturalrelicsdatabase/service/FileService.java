package com.fehead.fragileculturalrelicsdatabase.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fehead.fragileculturalrelicsdatabase.entity.SavedFile;
import com.fehead.fragileculturalrelicsdatabase.entity.SavedOpj;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wrobby
 * @version 1.0
 * @date 2022/2/28 17:23
 */
@Service
@Slf4j
public class FileService {
    final String PATH = "D:/桌面文件夹/ok";
    //    final String PATH = "D:/桌面文件夹/ok/文物的数据";
    @Autowired
    MongoTemplate mongoTemplate;

    public JSONObject getJson() {
        return mongoTemplate.findOne(new Query(Criteria.where("id").ne("we")), JSONObject.class, "jSONObject");
    }

    /**
     * 图片和xsls的csv
     *
     * @param id
     * @return
     */
    public SavedFile getFileById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        log.info("查询:" + id);
        return mongoTemplate.findOne(query, SavedFile.class, "savedFile");
    }

    /**
     *
     */
    public SavedOpj getOpjById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        log.info("查询:" + id);
        return mongoTemplate.findOne(query, SavedOpj.class, "savedOpj");
    }

    /**
     * 将文件保存到mongoDb
     */
    public JSONObject allFiles() {
        File[] files = FileUtil.ls(PATH);
        JSONArray jsonArray = new JSONArray();
        for (File file : files) {
            add(jsonArray, file);
        }
        JSONObject object = new JSONObject();
        object.set("type", "directory");
        object.set("data", jsonArray);
        object.set("name", "图表");
        return object;
    }

    private void add(JSONArray array, File file) {
        JSONObject jsonObject = new JSONObject();
        array.add(jsonObject);
        jsonObject.set("name", file.getName());

        if (file.isDirectory()) {
            //检查是否为opj-kai头的文件夹
            String s = file.getName();
            String substring = "";
            if (s.indexOf("-") > 0) {
                substring = s.substring(0, s.indexOf("-"));
            }

            if ("opj".equals(substring)) {
                //是则停止递归 将opj存入 savedOpj
                SavedOpj opj = new SavedOpj();
                File[] ls = FileUtil.ls(file.getAbsolutePath());
                Integer num = 0;
                Map<Integer, byte[]> picture = new HashMap<>(8);
                for (File l : ls) {
                    String name = l.getName();
                    if ("csv".equals(name.substring(name.lastIndexOf(".") + 1))) {
                        opj.setCsv(FileUtil.readBytes(l));
                    } else {
                        picture.put(num, FileUtil.readBytes(l));
                        num++;
                    }
                }
                opj.setPicture(picture);
                mongoTemplate.save(opj, "savedOpj");
                jsonObject.set("url", "opj:" + opj.getId());
                jsonObject.set("type", "file");
            } else {
                //正常继续寻找
                jsonObject.set("type", "directory");
                JSONArray jsonArray = new JSONArray();
                File[] ls = FileUtil.ls(file.getAbsolutePath());
                for (File l : ls) {
                    add(jsonArray, l);
                }
                jsonObject.set("data", jsonArray);
            }

        } else {
            //如果为文件则直接添加到savedFile
            SavedFile savedFile = new SavedFile();
            savedFile.setFile(FileUtil.readBytes(file));
            mongoTemplate.save(savedFile, "savedFile");
            jsonObject.set("type", "file");
            String name = file.getName();
            String type = name.substring(name.indexOf("."));
            if (".csv".equals(type)) {
                jsonObject.set("url", "csv:" + savedFile.getId());
            } else {
                jsonObject.set("url", "picture:" + savedFile.getId());
            }
        }

    }


}

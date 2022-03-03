package com.fehead.fragileculturalrelicsdatabase.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import com.fehead.fragileculturalrelicsdatabase.entity.SavedFile;
import com.fehead.fragileculturalrelicsdatabase.entity.SavedOpj;
import com.fehead.fragileculturalrelicsdatabase.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author wrobby
 * @version 1.0
 * @date 2022/2/28 17:18
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/fileTree")
    public JSONObject fileTree() {
        return fileService.getJson();
    }

    /**
     * 返回普通图片
     *
     * @param id
     * @param response
     * @throws IOException
     */
    @GetMapping("/picture/{id}")
    public void getPicFile(@PathVariable String id, HttpServletResponse response) throws IOException {
        SavedFile file = fileService.getFileById(id);
        if (file != null) {
            response.setContentType("image/png");
            response.getOutputStream().write(file.getFile());
        } else {
            System.out.println("异常");
        }
    }

    /**
     * 返回普通csv
     *
     * @param id
     * @param response
     */
    @GetMapping("/csv/{id}")
    public void getCsvFile(@PathVariable String id, HttpServletResponse response) throws IOException {
        SavedFile file = fileService.getFileById(id);
        if (file != null) {
            response.setContentType("txt/csv");
            response.getOutputStream().write(file.getFile());
        } else {
            log.error("/csv/" + id + "获取文件时出错");

        }
    }

    /**
     * 返回opj对应图片个数
     *
     * @param id
     * @throws IOException
     */
    @GetMapping("/csv/number/{id}")
    public Integer getCsvPicNum(@PathVariable String id) throws IOException {
        int number = 0;
        SavedOpj opj = fileService.getOpjById(id);
        if (opj != null) {
            number = opj.getPicture().size();
        }
        return number;
    }

    /**
     * 返回opj对应csv文件
     *
     * @param response
     * @param id
     */
    @GetMapping("/csv/text/{id}")
    public void getCsvText(HttpServletResponse response, @PathVariable String id) throws IOException {
        SavedOpj opj = fileService.getOpjById(id);
        if (opj != null) {
            response.setContentType("txt/csv");
            response.setCharacterEncoding("UTF-8");
            response.getOutputStream().write(opj.getCsv());
        } else {
            log.error("/csv/text/" + id + "文件不存在");
        }
    }

    /**
     * 返回opj对应图片
     *
     * @param response
     * @param id
     * @param key
     */
    @GetMapping("/csv/picture/{id}/{key}")
    public void getCsvPic(HttpServletResponse response, @PathVariable String id, @PathVariable Integer key) throws IOException {
        SavedOpj opj = fileService.getOpjById(id);
        Map<Integer, byte[]> picture = opj.getPicture();
        response.setContentType("image/png");
        response.getOutputStream().write(picture.get(key));
    }

    @GetMapping("/test")
    public void getPic(HttpServletResponse response) throws IOException {
        File file = new File("D:\\桌面文件夹\\图表\\盐的性能\\NaCl盐液滴\\晶体球状\\0_看图王.jpg");
        response.setContentType("image/jpg");
        response.getOutputStream().write(FileUtil.readBytes(file));
    }
}

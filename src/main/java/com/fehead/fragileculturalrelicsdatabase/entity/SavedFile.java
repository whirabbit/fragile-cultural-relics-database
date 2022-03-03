package com.fehead.fragileculturalrelicsdatabase.entity;

import lombok.Data;

/**
 * @author wrobby
 * @version 1.0
 * @date 2022/3/1 9:52
 */
@Data
public class SavedFile {
    private String id;
    private byte[] file;
}

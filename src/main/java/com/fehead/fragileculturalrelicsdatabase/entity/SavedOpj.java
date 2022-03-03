package com.fehead.fragileculturalrelicsdatabase.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author wrobby
 * @version 1.0
 * @date 2022/3/2 9:41
 */
@Data
public class SavedOpj {
    private String id;
    private Map<Integer,byte[]> picture;
    private byte[] csv;
}

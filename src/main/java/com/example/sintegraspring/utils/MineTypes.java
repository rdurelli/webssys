package com.example.sintegraspring.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class MineTypes {

    private Map<String, String> mimeTypes = new HashMap<>();

    public MineTypes() {
        mimeTypes.put("text/html", "html");
        mimeTypes.put("application/pdf", "pdf");
        mimeTypes.put("image/jpeg", "jpeg");
        mimeTypes.put("image/png", "png");
        mimeTypes.put("image/gif", "gif");
        mimeTypes.put("application/gzip", "gzip");
        mimeTypes.put("text/css", "css");
        mimeTypes.put("application/javascript", "js");
        mimeTypes.put("text/plain", "txt");
        mimeTypes.put("application/xml", "xml");
        mimeTypes.put("application/zip", "zip");
        mimeTypes.put("application/x-zip-compressed", "zip");
        mimeTypes.put("multipart/x-zip", "zip");
        mimeTypes.put("application/msword", "doc");
        mimeTypes.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
        mimeTypes.put("application/vnd.ms-excel", "xls");
        mimeTypes.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
        mimeTypes.put("application/vnd.ms-powerpoint", "ppt");
        mimeTypes.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx");
        mimeTypes.put("application/vnd.ms-excel.sheet.macroEnabled.12", "xlsm");
        mimeTypes.put("application/vnd.ms-excel.sheet.binary.macroEnabled.12", "xlsb");
    }

}

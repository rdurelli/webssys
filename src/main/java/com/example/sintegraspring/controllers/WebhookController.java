package com.example.sintegraspring.controllers;

import com.example.sintegraspring.models.WebhookRequestModel;
import com.example.sintegraspring.services.WebhookService;
import com.example.sintegraspring.utils.ConfigProperties;
import com.example.sintegraspring.utils.FileAssert;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.io.File;

@RestController
@RequestMapping("/api/v1/webhook")
@Slf4j
@AllArgsConstructor
public class WebhookController {

    private final ConfigProperties configProperties;
    private final WebhookService service;


    @PostMapping
    public void webhook(@RequestBody WebhookRequestModel requestModel) {
        log.info("Request received " + requestModel);
        Integer statusCode = service.hook(requestModel);
        if (statusCode == -1) {
            log.warn("File already exists.");
        }
        log.info("Status Code " + statusCode);
    }

    @GetMapping(value = "/print", produces = MediaType.TEXT_PLAIN_VALUE)
    public String printAllFiles() {
        // File object
        File maindir = new File(this.configProperties.getName());

        return FileAssert.printDirectoryTree(maindir);
    }

}

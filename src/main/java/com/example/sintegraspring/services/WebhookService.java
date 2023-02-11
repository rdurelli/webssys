package com.example.sintegraspring.services;

import com.example.sintegraspring.models.WebhookRequestModel;
import com.example.sintegraspring.utils.ConfigProperties;
import com.example.sintegraspring.utils.MineTypes;
import io.netty.handler.codec.http.HttpHeaders;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseStatus;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.asynchttpclient.Dsl.asyncHttpClient;

@Service
@Slf4j
@AllArgsConstructor
public class WebhookService {


    private final ConfigProperties directoryName;

    private final MineTypes mimeTypes;



    private static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private static File createFile(String formattedDate, File directory, String fileExtension) {
        File file = new File(directory + "/" + "file_" + formattedDate + "." + fileExtension);
        return file;
    }

    private static File getDirectory(String directoryName, String subDirectoryName) {
        String subDirectoryPath = "./" + directoryName + "/" + subDirectoryName;
        File directory = new File(directoryName);
        File subDirectory = new File(subDirectoryPath);
        if (!directory.exists()) {
            log.info("Creating the directory " + directoryName);
            directory.mkdir();
        } else {
            log.info("Directory " + directoryName + " already exists ");
        }
        // Create the subdirectory if it doesn't exist
        if (!subDirectory.exists()) {
            log.info("Creating the subDirectory " + subDirectoryName);
            subDirectory.mkdir();
        } else {
            log.info("SubDirectory " + subDirectoryName + " already exists ");
        }
        return subDirectory;
    }

    private Boolean exists(File file) {
        //check to verify if the file already exist. If so dont save it.
        return file.exists();
    }

    private String getExtensionFile(String url) {
        URLConnection urlConnection = null;
        try {
            urlConnection = new URL(url).openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String mimeType = urlConnection.getContentType();
        String mineToReturn = this.mimeTypes.getMimeTypes().get(mimeType);
        return mineToReturn;
    }

    public Integer hook(WebhookRequestModel requestModel) {
        log.info("Starting process hook");
        String fileExtension = getExtensionFile(requestModel.url());
        AsyncHttpClient asyncHttpClient = asyncHttpClient();
        String formattedDate = formatDate(requestModel.periodicidade());
        File directory = getDirectory(this.directoryName.getName(), requestModel.nome());
        File file = createFile(formattedDate, directory, fileExtension);
        if (this.exists(file)) {
            return -1;
        } else {
            try {
                log.info("URL to fetch the file " + requestModel.url());
                FileOutputStream stream = new FileOutputStream(file);

                Future<Integer> whenStatusCode = asyncHttpClient.prepareGet(requestModel.url())
                        .execute(new AsyncHandler<>() {
                            private Integer status;

                            @Override
                            public State onStatusReceived(HttpResponseStatus responseStatus) throws Exception {
                                status = responseStatus.getStatusCode();
                                log.info("onStatusReceived has been called " + status);
                                return State.CONTINUE;
                            }

                            @Override
                            public State onHeadersReceived(HttpHeaders headers) throws Exception {
                                log.info("onHeadersReceived has been called " + headers);
                                return State.CONTINUE;
                            }

                            @Override
                            public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                                log.info("onBodyPartReceived has been called " + bodyPart.length());
                                stream.getChannel().write(bodyPart.getBodyByteBuffer());
                                return State.CONTINUE;
                            }

                            @Override
                            public Integer onCompleted() throws Exception {
                                log.info("onCompleted has been called. Status: " + status);
                                return status;
                            }

                            @Override
                            public void onThrowable(Throwable t) {
                                t.printStackTrace();
                                log.error("onThrowable has been called." + t);
                            }
                        });

                Integer statusCode = whenStatusCode.get();

                if (HttpStatus.OK.value() == statusCode) {
                    log.info("File has been download successfully");
                    log.info("File has been saved in " + file.getAbsolutePath());
                } else {
                    log.error("Could not download the file. Status code " + statusCode);
                    log.error("Please try again..");
                }
                return statusCode;

            } catch (FileNotFoundException | InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
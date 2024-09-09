package com.identity.platform.utils;

import com.identity.platform.error.FILE_READING_ERROR;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

@Component
public class FileUtils {

    public String loadFileAsString(String file, Charset charset) throws FILE_READING_ERROR {
        String fileContent;

        synchronized (this) {
            InputStream inputStream = FileUtils.class.getResourceAsStream(file);
            if(inputStream == null) {
                throw new FILE_READING_ERROR("Unable to load the file");
            }
            try {
                fileContent = new BufferedReader(new InputStreamReader(inputStream, charset)).lines().
                        collect(Collectors.joining("\n"));
            } finally {
                try {
                    inputStream.close();
                } catch(IOException e) {
                    throw new FILE_READING_ERROR("Unable to load the file");
                }
            }
            return fileContent;
        }
    }
}

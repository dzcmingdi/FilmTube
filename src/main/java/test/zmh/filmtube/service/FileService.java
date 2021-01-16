package test.zmh.filmtube.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {


    public void transferFile(MultipartFile sourceFile, File targetFile) throws IOException {
        if (targetFile.mkdirs()) {

        }
        sourceFile.transferTo(targetFile);

    }

    public String getImageSuffixFromType(String imageType) {
        String suffix;
        switch (imageType) {
            case "image/jpeg":
                suffix = ".jpeg";
                break;
            case "image/png":
                suffix = ".png";
                break;
            default:
                suffix = ".jpeg";
        }
        return suffix;
    }

    public String getVideoSuffixFromType(String videoType) {
        String suffix;
        switch (videoType) {
            case "/video/mp4":
                suffix = ".mp4";
                break;
            case "video/x-matroska":
                suffix = ".mkv";
                break;
            default:
                suffix = ".mp4";
        }

        return suffix;
    }
}

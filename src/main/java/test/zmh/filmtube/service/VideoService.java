package test.zmh.filmtube.service;


import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.zmh.filmtube.conf.AppConfig;
import test.zmh.filmtube.mapper.VideoMapper;
import test.zmh.filmtube.model.video.Video;
import test.zmh.filmtube.utils.FilmTubeIdGenerator;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

@Service
public class VideoService {
    @Autowired
    VideoMapper videoMapper;

    @Autowired
    FilmTubeIdGenerator filmTubeIdGenerator;


    public List<Video> getVideoList(int l, int r, Integer kind, String keywords) { // bound left, bound right
        return videoMapper.getVideoList(l, r, kind, keywords);
    }
    public Integer getVideoListLength(Integer kind){
        return videoMapper.getVideoListLength(kind);
    }

    public Video getVideoInfo(String id) {
        return videoMapper.getVideoById(id);
    }

    public String getVideoPlaySrc(String id, Integer episode) {
        String type = videoMapper.getVideoPlaySrc(id, episode);
        if (type == null) return null;
        switch (type) {
            case "video/mp4":
                type = ".mp4";
                break;
            case "video/x-matroska":
                type = ".mkv";
                break;

        }
        return type;
    }


    @Transactional(rollbackFor = Exception.class)
    public String insertVideo(Video video, String type) throws SqlSessionException {
        String id = filmTubeIdGenerator.getId();
        video.setId(id);
        videoMapper.insertVideo(video);
        if (video.getKind() == 0) {
            videoMapper.insertVideoLink(id, 1, type);
        }

        return id;
    }

    public void videoBindEpisode(String id, Integer episode, String type) {
        videoMapper.testAndInsertVideoLink(id, episode, type);
    }

    public void mergeFiles(String videoTempBaseDirPath, String name, int chunks, long chunk_size) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(videoTempBaseDirPath, name), true);
        byte[] buffer = new byte[AppConfig.MERGE_BUFFER_SIZE];
        for (int i = 0; i < chunks; i++) {
            File tempFile = new File(videoTempBaseDirPath, i + ".temp");
            FileInputStream fileInputStream = new FileInputStream(tempFile);
            int readLength;
            while ((readLength = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, readLength);
            }
            fileInputStream.close();
            if (tempFile.delete()) {
                /* TODO need log */
            }
        }
        fileOutputStream.close();
//        File tempStorageFile = new File(videoTempBaseDirPath);
//        if (!tempStorageFile.renameTo(new File(AppConfig.MEDIA_STORAGE_LOCATION + File.separator + id))) {
//            throw new IOException("fail to rename by videoId");
//        }
    }

}

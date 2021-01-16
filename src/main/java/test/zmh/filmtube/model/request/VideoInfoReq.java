package test.zmh.filmtube.model.request;

import org.springframework.web.multipart.MultipartFile;
import test.zmh.filmtube.model.video.Video;

public class VideoInfoReq {

    public Video video;
    public String type;



    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }




}

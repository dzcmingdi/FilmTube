package test.zmh.filmtube.controller.video;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import test.zmh.filmtube.conf.AppConfig;
import test.zmh.filmtube.model.request.VideoInfoReq;
import test.zmh.filmtube.model.request.VideoSeriesInfoReq;
import test.zmh.filmtube.model.video.Video;
import test.zmh.filmtube.service.FileService;
import test.zmh.filmtube.service.VideoService;
import test.zmh.filmtube.utils.CliReqRes;
import test.zmh.filmtube.utils.CliReqResParams;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


@Controller
public class VideoController {

    @Autowired
    ObjectFactory<CliReqRes> objectFactory;

    @Autowired
    VideoService videoService;

    @RequestMapping(value = "/video/{id}/info")
    @ResponseBody
    public JsonObject getVideoInfo(@PathVariable(value = "id") String id) {
        Video video = videoService.getVideoInfo(id);
        CliReqRes cliReqRes = objectFactory.getObject();
        cliReqRes.setCode(CliReqResParams.CODE_SUCCESS).setMessage("ok").setData(video);
        return cliReqRes.toJsonObject();
    }

    @RequestMapping(value = "/video/list/length")
    @ResponseBody
    public JsonObject getVideoListLength(
            @RequestParam(value = "kind") Integer kind
    ) {
        CliReqRes cliReqRes = objectFactory.getObject();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("length", videoService.getVideoListLength(kind));
        return cliReqRes.setMessage("success").setCode(CliReqResParams.CODE_SUCCESS).setData(jsonObject).toJsonObject();
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public ModelAndView getVideoUploadPage() {
        return new ModelAndView("upload");
    }


    private final byte[] lock = new byte[0];
    private final byte[] condition = new byte[0];
    private AtomicInteger sum = new AtomicInteger(0);


    @Value("${filmtube.media.location}")
    private String MEDIA_STORAGE_LOCATION;

    @RequestMapping(value = "/video/upload/complete", method = RequestMethod.POST)
    @ResponseBody
    public JsonObject videoUploadComplete(MultipartFile file, @RequestParam(value = "chunk") int chunk,
                                          @RequestParam(value = "chunks") int chunks,
                                          @RequestParam(value = "id") String id,
                                          @RequestParam(value = "kind") Integer kind,
                                          @RequestParam(value = "episode", required = false) Integer episode,
                                          @RequestParam(value = "type") String type
    ) {

        CliReqRes cliReqRes = objectFactory.getObject();
        String videoTempBaseDirPath;
        switch (kind) {
            case 0:
                videoTempBaseDirPath = MEDIA_STORAGE_LOCATION + File.separator + id;
                break;
            case 1:
                videoTempBaseDirPath = MEDIA_STORAGE_LOCATION + File.separator + id + File.separator + episode;
                break;
            default:
                return cliReqRes.setCode(CliReqResParams.CODE_ERROR).setMessage("invalid kind").toJsonObject();
        }

        File tempStorageFile = new File(videoTempBaseDirPath);
        if (!tempStorageFile.exists()) {
            try {
                synchronized (lock) {
                    if (!tempStorageFile.exists()) {
                        if (!tempStorageFile.mkdirs()) {
                            throw new IOException("fail to create temp directory");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File saveFile = new File(videoTempBaseDirPath, chunk + ".temp");
        try {
            file.transferTo(saveFile);
            if (sum.incrementAndGet() == chunks) {
                synchronized (condition) {
                    condition.notify();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            cliReqRes.setMessage("error").setCode(CliReqResParams.CODE_ERROR);
        }
        if (chunk + 1 == chunks) {
            try {
                if (sum.get() < chunks) {
                    synchronized (condition) {
                        condition.wait();
                    }

                }
                sum.set(0);
                String suffix = fileService.getVideoSuffixFromType(type);
                videoService.mergeFiles(videoTempBaseDirPath, "src" + suffix, chunks, AppConfig.CHUNK_SIZE);
                cliReqRes.setCode(CliReqResParams.CODE_SUCCESS).setMessage("upload success");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                cliReqRes.setCode(CliReqResParams.CODE_ERROR).setMessage("upload fail");
            }
        }
        return cliReqRes.toJsonObject();
    }

    @Autowired
    FileService fileService;
    /*
     电影和连续剧信息创建
     */


    @RequestMapping(value = "/video/upload/request", method = RequestMethod.POST)
    @ResponseBody
    public JsonObject videoUploadRequest(
            @RequestBody VideoInfoReq videoInfoReq
    ) {
        CliReqRes cliReqRes = objectFactory.getObject();
        if (videoInfoReq == null || videoInfoReq.getVideo() == null || videoInfoReq.getType() == null) {
            cliReqRes.setCode(CliReqResParams.CODE_ERROR).setMessage("empty");
            return cliReqRes.toJsonObject();
        }
        Video video = videoInfoReq.getVideo();
        String type = videoInfoReq.getType();
        String id = videoService.insertVideo(video, type);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        return cliReqRes.setMessage("ok").setCode(CliReqResParams.CODE_SUCCESS).setData(jsonObject).toJsonObject();
    }

    @RequestMapping(value = "/video/upload/request/cover", method = RequestMethod.POST)
    @ResponseBody
    public JsonObject uploadCoverImage(MultipartFile image,
                                       @RequestParam("id") String id,
                                       @RequestParam("type") String coverImageType
    ) {
        CliReqRes cliReqRes = objectFactory.getObject();
        try {
            String coverImageSuffix = fileService.getImageSuffixFromType(coverImageType);
            fileService.transferFile(image, new File(MEDIA_STORAGE_LOCATION + File.separator + id,
                    "cover" + coverImageSuffix));

        } catch (IOException e) {
            return cliReqRes.setCode(CliReqResParams.CODE_ERROR).setMessage("fail to upload cover image").toJsonObject();
        }
        return cliReqRes.setCode(CliReqResParams.CODE_SUCCESS).setMessage("upload success").toJsonObject();
    }

    /*
     连续剧信息更新
     */
    @RequestMapping(value = "/video/bind", method = RequestMethod.POST)
    @ResponseBody
    public JsonObject videoSeriesUploadRequest(
            @RequestBody VideoSeriesInfoReq videoSeriesInfoReq
    ) {

        CliReqRes cliReqRes = objectFactory.getObject();
        videoService.videoBindEpisode(videoSeriesInfoReq.getId(), videoSeriesInfoReq.getEpisode(), videoSeriesInfoReq.getType());
        return cliReqRes.setMessage("success").setCode(CliReqResParams.CODE_SUCCESS).toJsonObject();
    }


}

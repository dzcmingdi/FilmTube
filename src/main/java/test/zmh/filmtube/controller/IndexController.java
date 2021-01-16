package test.zmh.filmtube.controller;


import com.google.gson.JsonObject;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import test.zmh.filmtube.conf.AppConfig;
import test.zmh.filmtube.model.video.Video;
import test.zmh.filmtube.service.VideoService;
import test.zmh.filmtube.utils.CliReqRes;
import test.zmh.filmtube.utils.CliReqResParams;

import java.util.List;

@RestController
public class IndexController {
    @Autowired
    ObjectFactory<CliReqRes> objectFactory;

    @Autowired
    VideoService videoService;

    @RequestMapping(value = "/index")
    public ModelAndView getIndexPage() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/video/list")
    @ResponseBody
    public JsonObject getVideoList(
            @RequestParam("l") int l,
            @RequestParam("r") int r,
            @RequestParam(value = "kind", required = false) Integer kind,
            @RequestParam(value = "keywords", required = false) String keywords
    ) {
        List<Video> videoList = videoService.getVideoList(l, r, kind, keywords);
        CliReqRes cliReqRes = objectFactory.getObject();
        cliReqRes.setCode(CliReqResParams.CODE_SUCCESS).setMessage("ok").setData(videoList);
        return cliReqRes.toJsonObject();
    }


}

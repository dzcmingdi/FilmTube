package test.zmh.filmtube.controller.video;


import com.google.gson.JsonObject;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import test.zmh.filmtube.service.VideoService;
import test.zmh.filmtube.utils.CliReqRes;
import test.zmh.filmtube.utils.CliReqResParams;

@Controller
public class VideoPlayController {

    @Autowired
    ObjectFactory<CliReqRes> objectFactory;

    @Autowired
    VideoService videoService;


    @RequestMapping(value = "/video/play/src", method = RequestMethod.GET)
    @ResponseBody
    public JsonObject getVideoPlaySrc(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "episode") Integer episode
    ) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", videoService.getVideoPlaySrc(id, episode));
        CliReqRes cliReqRes = objectFactory.getObject();
        return cliReqRes.setCode(CliReqResParams.CODE_SUCCESS).setMessage("success").setData(jsonObject).toJsonObject();

    }
}

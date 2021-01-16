package test.zmh.filmtube;


import com.littlenb.snowflake.sequence.IdGenerator;
import com.littlenb.snowflake.support.ElasticIdGeneratorFactory;
import com.littlenb.snowflake.support.MillisIdGeneratorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import test.zmh.filmtube.conf.AppConfig;
import test.zmh.filmtube.mapper.VideoMapper;
import test.zmh.filmtube.model.video.Video;
import test.zmh.filmtube.utils.FilmTubeIdGenerator;

@SpringBootTest(classes = App.class)
public class FilmTubeTest {
    @Autowired
    VideoMapper videoMapper;

    @Autowired
    FilmTubeIdGenerator filmTubeIdGenerator;


    private String test;
    @Test
    void test0() {
        String id = filmTubeIdGenerator.getId();
        videoMapper.insertVideo(new Video(id, "test", "test", "test", 0));
//        videoMapper.insertVideo(new Video());
    }


}

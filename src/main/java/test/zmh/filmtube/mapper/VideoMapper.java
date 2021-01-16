package test.zmh.filmtube.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import test.zmh.filmtube.model.video.Video;

import java.util.List;


@Repository
@Mapper
public interface VideoMapper {
    @Select("select id,name,content,title,date,duration,kind from filmtube_video where id=#{id}")
    Video getVideoById(@Param("id") String id);

    @Select("select count(number) from filmtube_video where kind=#{kind}")
    Integer getVideoListLength(@Param("kind") Integer kind);

    @Select({
            "<script>",
            "select id,name,content,title,date,duration,kind,coverImageType from filmtube_video",
            "where number between #{l} and #{r} <if test='kind != null' > and kind=#{kind} </if>",
            "<if test='keywords != null'>and name like CONCAT('%',#{keywords},'%')</if>",
            "</script>"
    })
    List<Video> getVideoList(@Param("l") int l, @Param("r") int r, @Param("kind") Integer kind, @Param("keywords") String keywords);

    @Insert("insert into filmtube_video (id,name,content,title,kind,date,duration,coverImageType) value (#{id},#{name},#{content},#{title},#{kind},#{date},#{duration},#{coverImageType})")
    void insertVideo(Video video);

    @Insert("insert into filmtube_video_link (videoId,episode,type) value (#{id},#{episode},#{type})")
    void insertVideoLink(@Param(value = "id") String id, @Param(value = "episode") Integer episode, @Param("type") String type);

    @Insert({
            "insert into filmtube_video_link(videoId, episode, type)",
            "select #{id}, #{episode}, #{type}",
            "where not exists (select * from filmtube_video_link where videoId = #{id} and episode = #{episode})"
    })
    void testAndInsertVideoLink(@Param(value = "id") String id, @Param(value = "episode") Integer episode, @Param("type") String type);

    @Select("select type from filmtube_video_link where videoId=#{id} and episode=#{episode}")
    String getVideoPlaySrc(@Param("id") String id, @Param("episode") Integer episode);

}

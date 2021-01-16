package test.zmh.filmtube.model.video;


import org.springframework.stereotype.Repository;
import test.zmh.filmtube.utils.FilmTubeDateParser;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@Repository
public class Video {
    String id;
    String name;
    String content;
    String title;
    Integer duration;
    Date date;
    Integer kind;
    String coverImageType;

    public int getKind() {
        return kind;
    }


    public Video() {
    }

    public Video(String id, String name, String content, String title, int kind) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.title = title;
        this.kind = kind;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }


    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCoverImageType() {
        return coverImageType;
    }

    public void setCoverImageType(String coverImageType) {
        this.coverImageType = coverImageType;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", date=" + date +
                ", kind=" + kind +
                '}';
    }
}

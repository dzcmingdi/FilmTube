package test.zmh.filmtube.utils;

import com.littlenb.snowflake.sequence.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FilmTubeIdGenerator {
    @Autowired
    IdGenerator idGenerator;

    public String getId() {
        return String.valueOf(idGenerator.nextId());
    }
}

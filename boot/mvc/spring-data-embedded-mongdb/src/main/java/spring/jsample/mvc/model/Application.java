package spring.jsample.mvc.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.StringJoiner;

@Document
public class Application extends ParentModel {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotNull
    private Boolean running;

    public Application() {
        super();
    }

    public Application(String id, String name, boolean running) {
        this.id = id;
        this.name = name;
        this.running = running;
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

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Application.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("running=" + running)
                .add("createdAt=" + createdAt)
                .add("lastModifiedDate=" + lastModifiedDate)
                .toString();
    }
}

package spring.jsample.mvc.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
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

    public Application(
            String name,
            boolean running) {
        this.name = name;
        this.running = running;
    }

    public String getId() {
        return id;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Application)) {
            return false;
        }
        Application that = (Application) o;
        return id.equals(that.id) &&
               lastModifiedDateTime.equals(that.lastModifiedDateTime) &&
               createdDateTime.equals(that.createdDateTime) &&
               version.equals(that.version) &&
               Objects.equals(name, that.name) &&
               Objects.equals(running, that.running);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, running, createdDateTime, lastModifiedDateTime, version);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Application.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("running=" + running)
                .add("createdDateTime=" + createdDateTime)
                .add("lastModifiedDateTime=" + lastModifiedDateTime)
                .add("version='" + version + "'")
                .toString();
    }
}

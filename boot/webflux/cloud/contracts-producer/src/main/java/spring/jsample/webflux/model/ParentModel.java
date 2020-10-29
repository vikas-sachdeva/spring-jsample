package spring.jsample.webflux.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

public class ParentModel {

    @CreatedDate
    protected LocalDateTime createdDateTime;

    @LastModifiedDate
    protected LocalDateTime lastModifiedDateTime;

    @Version
    protected Long version;

    public ParentModel() {
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public Long getVersion() {
        return version;
    }
}
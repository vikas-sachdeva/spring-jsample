package spring.jsample.mvc.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public class ParentModel {

    @CreatedDate
    protected LocalDateTime createdDateTime;

    @LastModifiedDate
    protected LocalDateTime lastModifiedDateTime;

    public ParentModel() {
    }

    public ParentModel(LocalDateTime createdDateTime, LocalDateTime lastModifiedDateTime) {
        this.createdDateTime = createdDateTime;
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(LocalDateTime lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

}
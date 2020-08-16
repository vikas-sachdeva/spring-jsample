package spring.jsample.mvc.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

public class ParentModel {

    @CreatedDate
    protected Date createdAt;

    @LastModifiedDate
    protected Date lastModifiedDate;

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}

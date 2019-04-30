package com.autonomoussystemserver.server.domainModel;


// The domain models are the classes that are mapped to the corresponding tables in the database.
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.auditing

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt"},
        allowGetters = true
)
public abstract class AbstractModel implements Serializable {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false, updatable = false)
    @CreatedDate
    private Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
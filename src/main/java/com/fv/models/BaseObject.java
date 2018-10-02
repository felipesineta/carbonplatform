package com.fv.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class BaseObject {

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", updatable = false)
    public Date createdAt = null;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_AT")
    public Date lastModifiedAt = null;

    @Column(name = "CREATED_BY", updatable = false, length = 42)
    public String createdBy = null;

    @Column(name = "MODIFIED_BY", length = 42)
    public String lastModifiedBy = null;
    
    @PreUpdate
    protected void updateAuditInformation() {
        lastModifiedAt = new Date();
        lastModifiedBy = "";
    }
    
    @PrePersist
    protected void generateAuditInformation() {
        final Date now = new Date();

        createdAt = now;
        lastModifiedAt = now;
        
        createdBy = "";        
        lastModifiedBy = "";
    }
}

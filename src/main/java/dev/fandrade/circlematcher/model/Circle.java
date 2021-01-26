package dev.fandrade.circlematcher.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "circle")
public class Circle extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
    public UUID circleId;
    public UUID workspaceId;
    @Column(columnDefinition = "jsonb")
    public String match;
    @Column(columnDefinition = "jsonb")
    public String parameters;
    public Integer parameters_size;
    @Column(columnDefinition = "boolean default true")
    public Boolean enabled = true;
    @Column(columnDefinition = "boolean default false")
    public Boolean isDefault = false;
    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    public Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    public Date updatedAt;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getCircleId() {
        return circleId;
    }

    public void setCircleId(UUID circleId) {
        this.circleId = circleId;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Integer getParameters_size() {
        return parameters_size;
    }

    public void setParameters_size(Integer parameters_size) {
        this.parameters_size = parameters_size;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
}

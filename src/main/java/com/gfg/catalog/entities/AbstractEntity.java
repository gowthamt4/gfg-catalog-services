package com.gfg.catalog.entities;

import static org.springframework.util.Assert.notNull;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;


@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

  private static final long serialVersionUID = 3454913073228021896L;

  @Id
  @Type(type = "uuid-char")
  @Column(name = "prod_id", unique = true, nullable = false)
  private UUID id;

  @Column(name = "created", unique = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  protected Date created;

  @Column(name = "lastModified", unique = false)
  @Temporal(TemporalType.TIMESTAMP)
  protected Date lastModified;

  protected AbstractEntity() {
    id = UUID.randomUUID();
    this.lastModified = new Date();
  }

  /**
   * Executed before persisting the entity
   */
  @PrePersist
  void beforeCreate() {
    this.setCreated(new Date());
    this.setLastModified(this.getCreated());
  }

  /**
   * Executed before making an update to the entity
   */
  @PreUpdate
  void beforeUpdate() {
    this.setLastModified(new Date());
  }

  @Override
  public int hashCode() {
    if (getId() != null) {
      return getId().hashCode();
    }
    return super.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }

    if (other instanceof AbstractEntity) {
      // Check whether id of entities match
      return id.equals(((AbstractEntity) other).getId());
    } else {
      return false;
    }
  }

  /**
   * @return the id
   */
  public UUID getId() {
    return id;
  }

  public void setId(@NotNull UUID id) {
    notNull(id, "ID of entity may not be null");
    this.id = id;
  }

  /**
   * @return the created
   */
  public Date getCreated() {
    return created;
  }

  /**
   * @param created the created to set
   */
  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   * @return the lastModified
   */
  public Date getLastModified() {
    return lastModified;
  }

  /**
   * @param lastModified the lastModified to set
   */
  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  public static UUID generateUUID() {
    return UUID.randomUUID();
  }
}

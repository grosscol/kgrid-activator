package org.uofm.ot.activator.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

/**
 * Created by nggittle on 3/24/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {

  @JsonInclude(Include.NON_EMPTY)
  private String title;

  @JsonInclude(Include.NON_EMPTY)
  private String description;

  @JsonInclude(Include.NON_EMPTY)
  private List<Citation> citations;

  @JsonInclude(Include.NON_EMPTY)
  private ArkId arkId;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Citation> getCitations() {
    return citations;
  }

  public void setCitations(List<Citation> citations) {
    this.citations = citations;
  }

  public ArkId getArkId() {
    return arkId;
  }

  public void setArkId(ArkId arkId) {
    this.arkId = arkId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Metadata metadata = (Metadata) o;

    if (title != null ? !title.equals(metadata.title) : metadata.title != null) {
      return false;
    }
    if (description != null ? !description.equals(metadata.description)
        : metadata.description != null) {
      return false;
    }
    if (citations != null ? !citations.equals(metadata.citations) : metadata.citations != null) {
      return false;
    }
    return arkId != null ? arkId.equals(metadata.arkId) : metadata.arkId == null;
  }

  @Override
  public int hashCode() {
    int result = title != null ? title.hashCode() : 0;
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (citations != null ? citations.hashCode() : 0);
    result = 31 * result + (arkId != null ? arkId.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Metadata{" +
        "title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", citations=" + citations +
        ", arkId=" + arkId +
        '}';
  }
}

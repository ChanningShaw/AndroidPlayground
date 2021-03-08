package com.wedream.demo.investment;

import java.util.Objects;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.jetbrains.annotations.NotNull;

@Entity
public class NewsEntity {
  @Id
  private String id;
  private long time;
  private String title;
  private int bullCount;
  private int bearCount;

  @Generated(hash = 1538429873)
  public NewsEntity(String id, long time, String title, int bullCount,
          int bearCount) {
      this.id = id;
      this.time = time;
      this.title = title;
      this.bullCount = bullCount;
      this.bearCount = bearCount;
  }

  @Generated(hash = 2121778047)
  public NewsEntity() {
  }

  public String getId() {
      return this.id;
  }

  public void setId(String id) {
      this.id = id;
  }

  public long getTime() {
      return this.time;
  }

  public void setTime(long time) {
      this.time = time;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getBullCount() {
    return bullCount;
  }

  public void setBullCount(int bullCount) {
    this.bullCount = bullCount;
  }

  public int getBearCount() {
    return bearCount;
  }

  public void setBearCount(int bearCount) {
    this.bearCount = bearCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NewsEntity that = (NewsEntity) o;
    return title.equals(that.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }

  public int compareTo(@NotNull NewsEntity n) {
    if (n.time > time) {
      return 0;
    } else if (n.time < time) {
      return -1;
    } else {
      return 0;
    }
  }
}

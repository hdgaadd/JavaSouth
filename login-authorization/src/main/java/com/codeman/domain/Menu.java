package com.codeman.domain;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-28
 */
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private Long parentId;

    /**
     * 菜单级数
     */
    private Long level;

    private LocalDateTime createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Menu{" +
        "id=" + id +
        ", title=" + title +
        ", parentId=" + parentId +
        ", level=" + level +
        ", createTime=" + createTime +
        "}";
    }
}

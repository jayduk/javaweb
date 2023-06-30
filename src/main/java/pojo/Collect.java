package pojo;

import annotation.DaoName;
import annotation.DaoObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * @author javaok
 * 2023/6/28 16:55
 */
@DaoObject
public class Collect implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @DaoName("create_time")
    private Date createTime;

    @DaoName("update_time")
    private Date updateTime;

    @DaoName("is_deleted")
    private Integer isDeleted;


    private Good good;

    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }
}

package pojo;

import annotation.DaoName;
import annotation.DaoObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * @author javaok
 * 2023/6/26 22:08
 */
@DaoObject
public class Good implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 创建时间
     */
    @DaoName("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @DaoName("update_time")
    private Date updateTime;
    /**
     * 是否删除(0-未删, 1-已删)
     */
    @DaoName("is_deleted")
    private Integer isDeleted;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品详细描述
     */
    @JSONField(name = "description")
    @DaoName("detail_info")
    private String detailInfo;
    /**
     * 交易代价
     */
    private String price;
    /**
     * 首页图片路径
     */
    @JSONField(name = "title_img")
    @DaoName("title_img")
    private String titleImg;
    @JSONField(name = "owner_id")
    @DaoName("owner_id")
    private Long ownerId;

    private User owner;

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

    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}

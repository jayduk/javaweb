package dao.impl;

import annotation.Component;
import annotation.Level;
import dao.BaseDao;
import dao.IGoodDao;
import exception.BusinessException;
import pojo.Good;
import util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javaok
 * 2023/6/26 22:10
 */
@Component(value = "goodDao", level = Level.DAO)
public class GoodDaoImpl extends BaseDao<Good> implements IGoodDao {
    public static void main(String[] args) throws SQLException {
        String[] arr = new String[]{
                "//gw.alicdn.com/bao/uploaded/i3/661559176/O1CN01mewzdw2Hef9bXGEt4-661559176.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i3/3841645288/O1CN01g2NXkN1ovxA4SFxnz_!!0-item_pic.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i1/12974530/O1CN01ZzqF6h1jKn217bFFh_!!0-saturn_solar.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i4/4212638250/O1CN01ZtLIzf2AoYJSyhX4O_!!0-item_pic.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i2/33090341/O1CN0112bAew1EODqamVPSW_!!2-saturn_solar.png_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i1/3587492410/O1CN01s0MC5r1TfpBi3nE1E_!!0-item_pic.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i2/762883664/O1CN012UXxV41cw9wwQlaDD_!!762883664.png_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i1/1030523978/O1CN01fo4IWr1fFyF9JXYnV_!!0-item_pic.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/imgextra/i4/385132127/O1CN01vXxjHw1RaDD5KQbZX_!!385132127-0-alimamacc.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i3/3325769618/O1CN01beUWrT2Kv6COPOQCG_!!3325769618.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i3/96998513/O1CN015w1rvZ2Cl0WSCyqo8_!!0-saturn_solar.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i2/355339835/O1CN01syYvWw2MWU2BknKGp_!!355339835.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i3/4107266900/O1CN01OPcGdY20qFgiavaWk_!!4107266900.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i1/2201249994107/O1CN01d0Yj1x1gD3SGloTuN_!!0-item_pic.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/imgextra/i4/385132127/O1CN01kMIXIv1RaDFNO6vgA_!!385132127-0-alimamacc.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i2/3037885118/O1CN01Xaw2ph1ng5qt8PkSI_!!0-item_pic.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded///asearch.alicdn.com/bao/uploaded/O1CN01bXgwEH22NW5SbUJKE_!!2215606447108.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded/i2/2208906611104/O1CN01TGUDkM1K1g2Ik75RI_!!0-item_pic.jpg_300x300q90.jpg_.webp",
                "//gw.alicdn.com/bao/uploaded///asearch.alicdn.com/bao/uploaded/O1CN01E0282j1joYW1wnADt_!!94504595-0-lubanu-s.jpg_300x300q90.jpg_.webp"
        };
        GoodDaoImpl goodDao = new GoodDaoImpl();

        for (Long i = 1L; i < 19; i++) {
            int j = goodDao.update("UPDATE good SET title_img = ? WHERE id = ?", arr[i.intValue()], i);
            System.out.println(j);
        }
    }

    @Override
    public List<Good> GetRandomGoodList(int count) {
        Connection conn = JdbcUtil.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT id, title, title_img, price FROM good WHERE is_deleted = 0 LIMIT ?");
            statement.setInt(1, count);
            ResultSet set = statement.executeQuery();

            List<Good> goodList = new ArrayList<Good>();
            while (set.next()) {
                Good good = new Good();
                good.setId(set.getLong("id"));
                good.setTitle(set.getString("title"));
                good.setTitleImg(set.getString("title_img"));
                good.setPrice(set.getString("price"));
                goodList.add(good);
            }
            return goodList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Good queryGoodById(Long id) {
        Connection conn = JdbcUtil.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT id, title, title_img, price, detail_info, owner_id FROM good WHERE is_deleted = 0 AND id = ?");
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();

            List<Good> goodList = new ArrayList<Good>();

            if (set.next()) {
                Good good = new Good();
                good.setId(set.getLong("id"));
                good.setTitle(set.getString("title"));
                good.setTitleImg(set.getString("title_img"));
                good.setPrice(set.getString("price"));
                good.setDetailInfo(set.getString("detail_info"));
                good.setOwnerId(set.getLong("owner_id"));
                goodList.add(good);
                return good;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> queryImagesOfGood(Long id) {
        Connection conn = JdbcUtil.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT img FROM good_imgs WHERE good_id = ? && is_deleted = 0");
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();

            List<String> goodList = new ArrayList<String>();

            while (set.next()) {
                goodList.add(set.getString("img"));
            }
            return goodList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Good> searchGoodsByName(String q) {
        System.out.println(q);

        StringBuilder stringBuilder = new StringBuilder().append('%');
        for (String s : q.split(" ")) {
            stringBuilder.append(s).append('%');
        }

        return queries("SELECT id, title, title_img, price, detail_info, owner_id " +
                        "FROM good " +
                        "WHERE is_deleted = 0 AND title like ?",
                stringBuilder.toString());
    }

    @Override
    public List<Good> queryGoodsOfUser(Long userId) {
        return queries("SELECT id, title, price, detail_info " +
                        "FROM good " +
                        "WHERE is_deleted = 0 AND owner_id = ?",
                userId);
    }

    @Override
    public void removeGood(Long id) {
        Integer update = update("UPDATE good SET is_deleted = 1 WHERE id = ?", id);
        if (update < 1) {
            throw new BusinessException(BusinessException.SQL_ERR, "删除失败");
        }
    }

    @Override
    public Long addGood(Long id, String title, String description, String price, String titleImg) {
        Connection connection = JdbcUtil.getConnection();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO good (owner_id, title, detail_info, price, title_img) VALUES (?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        ) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, price);
            preparedStatement.setString(5, titleImg);
            preparedStatement.execute();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            }
            throw new BusinessException(BusinessException.SQL_ERR, "添加失败");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addGoodImage(Long goodId, String image) {
        Integer update = update("INSERT INTO good_imgs (good_id, img) VALUES (?, ?)", goodId, image);
        if (update < 1) {
            throw new BusinessException(BusinessException.SQL_ERR, "添加失败");
        }
    }

    @Override
    public void updateGood(Long good_id, String title, String description, String price, String titleImg) {
        Integer update = update("UPDATE good SET title = ?, detail_info = ?, price = ?, title_img = ? WHERE id = ?", title, description, price, titleImg, good_id);
        if (update < 1) {
            throw new BusinessException(BusinessException.SQL_ERR, "更新失败");
        }
    }

    @Override
    public void removeGoodImages(Long good_id) {
        Integer update = update("UPDATE good_imgs SET is_deleted = 1 WHERE good_id = ?", good_id);
        if (update < 1) {
            throw new BusinessException(BusinessException.SQL_ERR, "删除失败");
        }
    }
}

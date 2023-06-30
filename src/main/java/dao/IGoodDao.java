package dao;

import pojo.Good;

import java.util.List;

/**
 * @author javaok
 * 2023/6/26 22:10
 */
public interface IGoodDao {
    List<Good> GetRandomGoodList(int count);

    Good queryGoodById(Long id);

    List<String> queryImagesOfGood(Long id);

    List<Good> searchGoodsByName(String q);

    List<Good> queryGoodsOfUser(Long userId);

    void removeGood(Long id);

    Long addGood(Long id, String title, String description, String price, String titleImg);

    void addGoodImage(Long goodId, String image);

    void updateGood(Long good_id, String title, String description, String price, String titleImg);

    void removeGoodImages(Long good_id);
}

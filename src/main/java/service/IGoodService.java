package service;

import pojo.Good;

import java.util.List;

/**
 * @author javaok
 * 2023/6/26 22:09
 */
public interface IGoodService {
    List<Good> recommendGoodsList(int count);

    Good queryGoodById(Long id);

    List<String> queryImagesOfGoods(Long id);

    List<Good> searchGoodsByName(String q);

    List<Good> queryGoodsOfUser(Long userId);

    void removeGood(Long id);

    Long addGood(Long id, String title, String description, String price, String titleImg);

    void addGoodImages(Long goodId, List<String> imageList);

    void updateGood(Long good_id, String title, String description, String price, String titleImg);

    void updateGoodImages(Long good_id, List<String> imageList);
}

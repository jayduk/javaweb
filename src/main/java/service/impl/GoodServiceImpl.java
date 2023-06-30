package service.impl;

import annotation.AutoWired;
import annotation.Component;
import annotation.Level;
import dao.IGoodDao;
import dao.impl.GoodDaoImpl;
import pojo.Good;
import service.IGoodService;

import java.util.List;

/**
 * @author javaok
 * 2023/6/26 22:09
 */
@Component(level = Level.SERVICE, value = "goodService")
public class GoodServiceImpl implements IGoodService {
    @AutoWired
    private IGoodDao goodDao;

    @Override
    public List<Good> recommendGoodsList(int count) {
        return goodDao.GetRandomGoodList(count);
    }

    @Override
    public Good queryGoodById(Long id) {
        return goodDao.queryGoodById(id);
    }

    @Override
    public List<String> queryImagesOfGoods(Long id) {
        return goodDao.queryImagesOfGood(id);
    }

    @Override
    public List<Good> searchGoodsByName(String q) {
        return goodDao.searchGoodsByName(q);
    }

    @Override
    public List<Good> queryGoodsOfUser(Long userId) {
        return goodDao.queryGoodsOfUser(userId);
    }

    @Override
    public void removeGood(Long id) {
        goodDao.removeGood(id);
    }

    @Override
    public Long addGood(Long id, String title, String description, String price, String titleImg) {
        if (goodDao == null) {
            goodDao = new GoodDaoImpl();
        }
        return goodDao.addGood(id, title, description, price, titleImg);
    }

    @Override
    public void addGoodImages(Long goodId, List<String> imageList) {
        if (imageList == null || imageList.size() == 0) {
            return;
        }
        for (String image : imageList) {
            goodDao.addGoodImage(goodId, image);
        }
    }

    @Override
    public void updateGood(Long good_id, String title, String description, String price, String titleImg) {
        goodDao.updateGood(good_id, title, description, price, titleImg);
    }

    @Override
    public void updateGoodImages(Long good_id, List<String> imageList) {
        goodDao.removeGoodImages(good_id);
        addGoodImages(good_id, imageList);
    }


}

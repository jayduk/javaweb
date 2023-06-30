package controller;

import annotation.AutoWired;
import annotation.Component;
import annotation.Level;
import annotation.RequestMapping;
import pojo.Collect;
import service.ICollectService;

import java.util.List;

/**
 * @author javaok
 * 2023/6/27 10:14
 */
@Component(level = Level.CONTROLLER)
@RequestMapping("/jav")
public class CollectController {
    @AutoWired
    ICollectService collectService;

    @RequestMapping(value = "/collect", method = "POST")
    public String collect(Long userId, Long goodId) {
        if (collectService.queryCollectState(userId, goodId)) {
            collectService.disCollect(userId, goodId);
            return "取消收藏成功";
        } else {
            collectService.collect(userId, goodId);
            return "收藏成功";
        }
    }

    @RequestMapping(value = "/query_collect_state", method = "POST")
    public boolean queryCollectState(Long userId, Long goodId) {
        return collectService.queryCollectState(userId, goodId);
    }

    @RequestMapping(value = "/remove_collect", method = "POST")
    public void removeCollect(Long id) {
        collectService.removeCollectById(id);
    }

    @RequestMapping(value = "/query_collect_of_user", method = "POST")
    public List<Collect> queryCollectOfUser(Long userId) {
        return collectService.queryCollectOfUser(userId);
    }
}

package controller;

import annotation.*;
import pojo.Good;
import service.IGoodService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author javaok
 * 2023/6/26 22:06
 */
@Component(level = Level.CONTROLLER)
@RequestMapping("/jav")
public class GoodController {
    @AutoWired
    IGoodService goodService;


    @RequestMapping(value = "/recommend-goods-list", method = "POST")
    public List<Good> recommendGoodsList(int count) {
        return goodService.recommendGoodsList(count);
    }

    @RequestMapping(value = "/query/good", method = "POST")
    public Good queryGood(Long id) {
        return goodService.queryGoodById(id);
    }

    @RequestMapping(value = "/query-images-of-good", method = "POST")
    public List<String> queryImagesOfGoods(Long id) {
        return goodService.queryImagesOfGoods(id);
    }

    @RequestMapping(value = "/search", method = "POST")
    public List<Good> searchGoods(String q) {
        return goodService.searchGoodsByName(q);
    }

    @RequestMapping(value = "/query-goods-of-user", method = "POST")
    public List<Good> queryGoodsOfUser(Long id) {
        return goodService.queryGoodsOfUser(id);
    }

    @RequestMapping(value = "/remove_good", method = "POST")
    public String removeGood(Long id) {
        goodService.removeGood(id);
        return "下架成功";
    }

    @RequestMapping(value = "/add_good", method = "POST")
    @NoneAutoParameter
    public void addGood(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String price = request.getParameter("price");

            Part titleImgPart = request.getPart("title_img");
            String titleImg = saveFile(request, titleImgPart);

            Collection<Part> parts = request.getParts();
            List<String> imageList = new ArrayList<String>();
            for (Part imagePart : parts) {
                if (!imagePart.getName().equals("images[]")) {
                    continue;
                }

                imageList.add(saveFile(request, imagePart));
            }

            if ("new".equals(request.getParameter("type"))) {
                Long goodId = goodService.addGood(id, title, description, price, titleImg);
                goodService.addGoodImages(goodId, imageList);
                
            } else if ("edit".equals(request.getParameter("type"))) {
                Long good_id = Long.parseLong(request.getParameter("good_id"));
                goodService.updateGood(good_id, title, description, price, titleImg);
                goodService.updateGoodImages(good_id, imageList);
            }

            response.sendRedirect("/owner.html");
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String saveFile(HttpServletRequest request, Part part) {
        String filePath = request.getServletContext().getRealPath("") + File.separator + "assets/img";
        File uploadDir = new File(filePath);

        if (!uploadDir.exists()) {
            boolean mkdir = uploadDir.mkdir();
        }

        // 生成一个唯一的文件名
        String fileName = UUID.randomUUID().toString() + "_" + part.getSubmittedFileName();
        // 将文件保存到指定的目录
        try {
            part.write(filePath + File.separator + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "assets/img/" + fileName;
    }

}

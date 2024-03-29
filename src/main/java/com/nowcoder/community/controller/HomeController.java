package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @RequestMapping(path="/index", method= RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        // 方法调用前，SpringMVC会自动实例化Model和Page，并将Page注入Model
        // 所以在thymeleaf中可以直接访问Page对象中的数据，不需要addAttribute("Page", Page)

        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        //第一页，显示前10条数据
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if(list != null){
            for(DiscussPost post: list){
                Map<String, Object> map = new HashMap<>();
                map.put("post", post); // 把帖子装进去
                User user = userService.findUserById(post.getUserid());
                map.put("user", user);  //把user装进去
                discussPosts.add(map);
            }
            model.addAttribute("discussPosts", discussPosts); //第一个参数是数据名，前端根据此数据名来获取数据
        }
        return "/index";
    }
}

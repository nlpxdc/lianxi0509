package io.cjf.lianxi0509.controller;

import io.cjf.lianxi0509.dao.MenuMapper;
import io.cjf.lianxi0509.dto.MenuNode;
import io.cjf.lianxi0509.po.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuMapper menuMapper;

    @GetMapping("/getTree")
    public List<MenuNode> getTree(Integer menuId){
        List<MenuNode> menus = menuMapper.selectChildren(menuId);
        return menus;
    }
}

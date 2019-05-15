package io.cjf.lianxi0509.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuNode {
    private Integer menuId;

    private String name;

    private String url;

    private List<MenuNode> subMenus;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MenuNode> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<MenuNode> subMenus) {
        this.subMenus = subMenus;
    }
}

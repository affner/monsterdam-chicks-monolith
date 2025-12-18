package com.monsterdam.app.service.dto.bff;

import java.util.ArrayList;
import java.util.List;

public class MenuDTO {

    private List<String> roles = new ArrayList<>();
    private List<String> sections = new ArrayList<>();

    public MenuDTO roles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public MenuDTO sections(List<String> sections) {
        this.sections = sections;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getSections() {
        return sections;
    }

    public void setSections(List<String> sections) {
        this.sections = sections;
    }
}

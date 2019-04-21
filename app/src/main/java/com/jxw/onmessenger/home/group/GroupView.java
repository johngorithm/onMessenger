package com.jxw.onmessenger.home.group;

import com.jxw.onmessenger.models.Group;

import java.util.List;

interface GroupView {
    void displayGroups(List<Group> groups);
    void handNetworkError();
}

package com.android.fitz.fastreading.bean;

/**
 * Created by fitz on 2017/4/16.
 */

public class SelectItem {
    private int index;
    private String id;
    private String name;
    private boolean isSelect;

    public SelectItem(int index, String name, boolean isSelect){
        this.index = index;
        this.name = name;
        this.isSelect = isSelect;
    }

    public SelectItem(String id, String name, boolean isSelect){
        this.id = id;
        this.name = name;

        this.isSelect = isSelect;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}

package com.bn.sample.add;

/**
 * Created by Administrator on 2015/5/28.
 */

//新闻信息类，用来存储每一条新闻信息的具体详情
public class NewsStorage {
    private String date;//新闻日期
    private String hyperlink;//首页获取的新闻超链接
    private String text;//新闻的具体内容
    private String title;//新闻标题

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void Set(String date,String hyperlink,String title){
        this.date=date;
        this.hyperlink=hyperlink;
        this.title=title;
    }

}

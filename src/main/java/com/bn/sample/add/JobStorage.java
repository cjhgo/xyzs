package com.bn.sample.add;

/**
 * Created by Administrator on 2015/6/3.
 */

//工作信息类，用来存储每一条工作信息的具体详情
public class JobStorage {

    String hyperlink;//该工作信息详细文件的超链接
    String positionName;//职位名称
    String companyName;//公司名称
    String workPlace;//工作地点
    String time;//发布时间或具体时间


    public String getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void Set(String hyperlink,String positionName,String companyName,String workPlace,String time){
        this.hyperlink=hyperlink;
        this.positionName=positionName;
        this.companyName=companyName;
        this.workPlace=workPlace;
        this.time=time;
    }

}


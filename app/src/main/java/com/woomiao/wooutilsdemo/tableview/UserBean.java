package com.woomiao.wooutilsdemo.tableview;

/**
 * @author Administrator
 * 版本：1.0
 * 创建日期：2023/4/24 0024 17:52
 * 描述：
 * Email:
 */
public class UserBean {
    private String name;
    private String sex;
    private String age;
    private String remark;

    public UserBean(String name, String sex, String age, String remark) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

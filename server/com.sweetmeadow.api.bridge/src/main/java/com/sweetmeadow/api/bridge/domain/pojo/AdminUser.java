package com.sweetmeadow.api.bridge.domain.pojo;

import java.util.Date;

public class AdminUser {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column admin_user.id
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column admin_user.open_id
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    private String openId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column admin_user.level
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    private Integer level;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column admin_user.nick
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    private String nick;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column admin_user.last_login_time
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    private Date lastLoginTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column admin_user.id
     *
     * @return the value of admin_user.id
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column admin_user.id
     *
     * @param id the value for admin_user.id
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column admin_user.open_id
     *
     * @return the value of admin_user.open_id
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column admin_user.open_id
     *
     * @param openId the value for admin_user.open_id
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column admin_user.level
     *
     * @return the value of admin_user.level
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column admin_user.level
     *
     * @param level the value for admin_user.level
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column admin_user.nick
     *
     * @return the value of admin_user.nick
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    public String getNick() {
        return nick;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column admin_user.nick
     *
     * @param nick the value for admin_user.nick
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column admin_user.last_login_time
     *
     * @return the value of admin_user.last_login_time
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column admin_user.last_login_time
     *
     * @param lastLoginTime the value for admin_user.last_login_time
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
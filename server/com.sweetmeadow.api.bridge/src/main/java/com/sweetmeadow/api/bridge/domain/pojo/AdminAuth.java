package com.sweetmeadow.api.bridge.domain.pojo;

import java.util.Date;

public class AdminAuth {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column admin_auth.id
     *
     * @mbg.generated Thu May 18 11:34:25 CST 2017
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column admin_auth.session_id
     *
     * @mbg.generated Thu May 18 11:34:25 CST 2017
     */
    private String sessionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column admin_auth.create_time
     *
     * @mbg.generated Thu May 18 11:34:25 CST 2017
     */
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column admin_auth.id
     *
     * @return the value of admin_auth.id
     *
     * @mbg.generated Thu May 18 11:34:25 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column admin_auth.id
     *
     * @param id the value for admin_auth.id
     *
     * @mbg.generated Thu May 18 11:34:25 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column admin_auth.session_id
     *
     * @return the value of admin_auth.session_id
     *
     * @mbg.generated Thu May 18 11:34:25 CST 2017
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column admin_auth.session_id
     *
     * @param sessionId the value for admin_auth.session_id
     *
     * @mbg.generated Thu May 18 11:34:25 CST 2017
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column admin_auth.create_time
     *
     * @return the value of admin_auth.create_time
     *
     * @mbg.generated Thu May 18 11:34:25 CST 2017
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column admin_auth.create_time
     *
     * @param createTime the value for admin_auth.create_time
     *
     * @mbg.generated Thu May 18 11:34:25 CST 2017
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
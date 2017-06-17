package com.sweetmeadow.api.bridge.dao.gen;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sweetmeadow.api.bridge.domain.pojo.AdminAuth;
import com.sweetmeadow.api.bridge.domain.pojo.AdminAuthExample;

@Repository
public interface AdminAuthMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_auth
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    long countByExample(AdminAuthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_auth
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    int deleteByExample(AdminAuthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_auth
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_auth
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    int insert(AdminAuth record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_auth
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    int insertSelective(AdminAuth record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_auth
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    List<AdminAuth> selectByExample(AdminAuthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_auth
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    AdminAuth selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_auth
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    int updateByExampleSelective(@Param("record") AdminAuth record, @Param("example") AdminAuthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_auth
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    int updateByExample(@Param("record") AdminAuth record, @Param("example") AdminAuthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_auth
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    int updateByPrimaryKeySelective(AdminAuth record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_auth
     *
     * @mbg.generated Thu May 18 10:53:24 CST 2017
     */
    int updateByPrimaryKey(AdminAuth record);
}
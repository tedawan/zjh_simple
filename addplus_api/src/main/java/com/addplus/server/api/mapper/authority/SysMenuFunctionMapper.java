package com.addplus.server.api.mapper.authority;

import com.addplus.server.api.model.authority.SysMenuFunction;
import com.addplus.server.api.model.authority.ext.SysMenuRoleExt;
import com.addplus.server.api.utils.BaseAddMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuFunctionMapper extends BaseAddMapper<SysMenuFunction> {

    @Select("SELECT\n" +
            " id,\n" +
            " NAME,\n" +
            " icon,\n" +
            " m_desc,\n" +
            " dept,\n" +
            " url,\n" +
            " pid,\n" +
            " type,\n" +
            " sort,\n" +
            " is_deleted,\n" +
            " path,\n" +
            " gmt_create,\n" +
            " gmt_modified,\n" +
            " modify_user \n" +
            "FROM\n" +
            " sys_menu_function \n" +
            "WHERE\n" +
            " sys_menu_function.is_deleted = 0 \n" +
            " AND FIND_IN_SET(#{id},sys_menu_function.path) ORDER BY sys_menu_function.dept desc,sys_menu_function.sort desc")
    List<SysMenuFunction> selectMenuFunctionTreeById(@Param("id") Long id);

    @Select("SELECT\n" +
            " id,\n" +
            " NAME,\n" +
            " icon,\n" +
            " m_desc,\n" +
            " dept,\n" +
            " url,\n" +
            " pid,\n" +
            " type,\n" +
            " sort,\n" +
            " is_deleted,\n" +
            " path,\n" +
            " gmt_create,\n" +
            " gmt_modified,\n" +
            " modify_user \n" +
            "FROM\n" +
            " sys_menu_function \n" +
            "WHERE\n" +
            " sys_menu_function.is_deleted = 0 \n" +
            " AND sys_menu_function.pid = - 1 \n" +
            " AND type = #{type} ORDER BY sys_menu_function.dept desc,sys_menu_function.sort desc")
    List<SysMenuFunction> selectMenuFunctionOrderSort(Page page, @Param("type") Integer type);

    @Select("<script>\n" +
            "SELECT\n" +
            " mf.id AS id,\n" +
            " mf.`name` AS NAME,\n" +
            " mf.icon AS icon,\n" +
            " mf.m_desc AS mDesc,\n" +
            " mf.dept AS dept,\n" +
            " mf.url AS url,\n" +
            " mf.pid AS pid,\n" +
            " mf.sort AS sort,\n" +
            " mf.path AS path \n" +
            "FROM\n" +
            " sys_menu_function mf\n" +
            " LEFT JOIN sys_role_menu_function rm ON rm.m_id = mf.id \n" +
            " AND mf.is_deleted = 0 \n" +
            " AND rm.is_deleted = 0\n" +
            " LEFT JOIN sys_role r ON r.id = rm.r_id \n" +
            " AND r.is_deleted = 0 \n" +
            "WHERE\n" +
            " r.id IN <foreach collection=\"roles\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach>\n" +
            "GROUP BY\n" +
            " mf.id \n" +
            "ORDER BY\n" +
            " mf.dept ASC,\n" +
            " mf.sort DESC \n" +
            "</script>")
    List<SysMenuFunction> getMenuFunctionUserAll(@Param("roles") List<Integer> roles);

    @Select("SELECT\n" +
            " id \n" +
            "FROM\n" +
            " sys_menu_function \n" +
            "WHERE\n" +
            " FIND_IN_SET(#{id},sys_menu_function.path) AND is_deleted = 0")
    List<Integer> getMenuFunctionChilders(@Param("id") Integer id);

    @Update("UPDATE sys_menu_function \n" +
            "SET is_deleted = 1 \n" +
            "WHERE\n" +
            " FIND_IN_SET(#{id},sys_menu_function.path) AND is_deleted =0")
    Integer updateLogicallyDeleteChilders(@Param("id") Integer id);

    @Select("SELECT\n" +
            " * \n" +
            "FROM\n" +
            " (\n" +
            " SELECT\n" +
            "  MAX( mf.id ) AS mId,\n" +
            "  mf.url AS url,\n" +
            "  GROUP_CONCAT( DISTINCT ( r.id ) ) AS rId \n" +
            " FROM\n" +
            "  sys_role_menufunction rm\n" +
            "  LEFT JOIN sys_menu_function mf ON mf.id = rm.m_id \n" +
            "  AND mf.is_deleted = 0\n" +
            "  RIGHT JOIN sys_role r ON r.id = rm.r_id \n" +
            "  AND r.is_deleted = 0 \n" +
            " WHERE\n" +
            "  rm.is_deleted = 0 \n" +
            " GROUP BY\n" +
            "  mf.url \n" +
            " ) mf \n" +
            "ORDER BY\n" +
            " mf.mId")
    List<SysMenuRoleExt> getMenuFunctionRoleAll();

    @Select("<script>\n" +
            "SELECT\n" +
            " id,\n" +
            " NAME,\n" +
            " icon,\n" +
            " m_desc,\n" +
            " dept,\n" +
            " url,\n" +
            " pid,\n" +
            " sort,\n" +
            " path \n" +
            "FROM\n" +
            " sys_menu_function \n" +
            "WHERE\n" +
            " is_deleted = 0 \n" +
            " <if test=\"name != null and name != ''\">\n" +
            "            AND  NAME LIKE CONCAT( '%', #{name}, '%' )\n" +
            "        </if>\n" +
            "        <if test=\"pid != null and pid != ''\">\n" +
            "            <if test=\"name != null and name != '' \">\n" +
            "                OR id  = #{pid}\n" +
            "            </if>\n" +
            "            <if test=\"name == null or name == '' \">\n" +
            "                AND id  = #{pid}\n" +
            "            </if>\n" +
            "        </if>\n" +
            "ORDER BY\n" +
            " sort\n" +
            "</script>")
    List<SysMenuFunction> searchMenuFunctionByName(Page page, @Param("pid") String pid, @Param("name") String name);


}
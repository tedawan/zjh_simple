package com.addplus.server.api.mapper.authority;

import com.addplus.server.api.model.authority.SysMenuElement;
import com.addplus.server.api.model.authority.ext.SysMenuRoleExt;
import com.addplus.server.api.utils.BaseAddMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuElementMapper extends BaseAddMapper<SysMenuElement> {

    @Select("<script>\n" +
            "SELECT DISTINCT\n" +
            " m.e_id AS id \n" +
            "FROM\n" +
            " sys_role_menuelement m\n" +
            " LEFT JOIN sys_role r ON r.id = m.r_id \n" +
            " AND r.is_deleted = 0\n" +
            " LEFT JOIN sys_menu_element e ON e.id = m.e_id \n" +
            " AND e.is_deleted = 0 \n" +
            "WHERE\n" +
            " m.is_deleted = 0 AND e.type=#{type}\n" +
            " AND m.r_id IN\n" +
            " <foreach collection='roles' index='index' item='item' open='(' separator=',' close=')'>#{item}</foreach>\n" +
            "</script>")
    List<String> getUserFunctionList(@Param("roles") List<String> roles, @Param("type") Integer type);

    @Select("SELECT\n" +
            " e.id AS id,\n" +
            " CONCAT('/',e.method,f.url,e.name) as name \n" +
            "FROM\n" +
            " sys_menu_element e\n" +
            " LEFT JOIN sys_menu_function f ON f.id = e.m_id \n" +
            "WHERE\n" +
            " f.is_deleted = 0 \n" +
            " AND e.is_deleted = 0")
    List<SysMenuElement> getMenuElementList();

    @Select("SELECT\n" +
            " e.id AS id,\n" +
            " CONCAT('/',e.method,f.url,e.name) as name\n" +
            "FROM\n" +
            " sys_menu_element e\n" +
            " LEFT JOIN sys_menu_function f ON f.id = e.m_id \n" +
            "WHERE\n" +
            " f.is_deleted = 0 \n" +
            " AND e.is_deleted = 0 \n" +
            " AND e.m_id = #{mId}")
    List<SysMenuElement> getMenuElementListByfunctionId(@Param("mId") Integer mId);

    @Update("<script>\n" +
            "UPDATE sys_menu_element SET is_deleted = 1 WHERE\n" +
            "m_id IN\n" +
            "<foreach collection='ids' index='index' item='item' open='(' separator=',' close=')'>#{item}</foreach>\n" +
            "AND is_deleted = 0\n" +
            "</script>")
    Integer updateMenuElementDelete(@Param("ids") List<Integer> mIdList);

    @Select("<script>\n" +
            "SELECT\n" +
            " me.id,\n" +
            " me.m_id,\n" +
            " me.name,\n" +
            " me.method\n" +
            "FROM\n" +
            " sys_menu_element me\n" +
            " LEFT JOIN sys_menu_function mf ON mf.id = me.m_id \n" +
            " AND mf.is_deleted = 0\n" +
            " LEFT JOIN sys_role_menuelement rm ON rm.e_id = me.id \n" +
            " AND rm.is_deleted = 0 \n" +
            "WHERE\n" +
            " me.is_deleted = 0 \n" +
            " AND mf.id = #{mId} AND rm.r_id IN\n" +
            "<foreach collection='roles' index='index' item='item' open='(' separator=',' close=')'>#{item}\n" +
            "</foreach>\n" +
            "</script>")
    List<SysMenuElement> getUserMenuElementList(@Param("roles") List<Integer> roleList, @Param("mId") Integer mId);

    @Select("SELECT\n" +
            " me.method AS url,\n" +
            " GROUP_CONCAT( DISTINCT ( r.id ) ) AS rId \n" +
            "FROM\n" +
            " sys_role_menu_function rm\n" +
            " LEFT JOIN sys_menu_function mf ON mf.id = rm.m_id \n" +
            " AND mf.is_deleted = 0\n" +
            " LEFT JOIN sys_role r ON r.id = rm.r_id \n" +
            " AND r.is_deleted = 0\n" +
            " RIGHT JOIN sys_menu_element me ON me.m_id = mf.id \n" +
            " AND me.is_deleted = 0 \n" +
            "WHERE\n" +
            " rm.is_deleted = 0 \n" +
            "GROUP BY\n" +
            " me.method")
    List<SysMenuRoleExt> getMunuElementGroupMethodRole(Page page);

    @Select("SELECT\n" +
            " count( 1 ) AS count \n" +
            "FROM\n" +
            " (\n" +
            " SELECT\n" +
            "  max( mf.id ) AS mId,\n" +
            "  me.method AS url,\n" +
            "  GROUP_CONCAT( DISTINCT ( r.id ) ) AS rId \n" +
            " FROM\n" +
            "  sys_role_menu_function rm\n" +
            "  LEFT JOIN sys_menu_function mf ON mf.id = rm.m_id \n" +
            "  AND mf.is_deleted = 0\n" +
            "  LEFT JOIN sys_role r ON r.id = rm.r_id \n" +
            "  AND r.is_deleted = 0\n" +
            "  RIGHT JOIN sys_menu_element me ON me.m_id = mf.id \n" +
            "  AND me.is_deleted = 0 \n" +
            " WHERE\n" +
            "  rm.is_deleted = 0 \n" +
            " GROUP BY\n" +
            " me.method \n" +
            " ) AS temp")
    Integer getMunuElementGroupMethodRoleByCount();

}
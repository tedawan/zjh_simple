package com.addplus.server.api.mapper.authority;

import com.addplus.server.api.model.authority.SysRoleMenuFunction;
import com.addplus.server.api.utils.BaseAddMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleMenuFunctionMapper extends BaseAddMapper<SysRoleMenuFunction> {

   @Select("<script>\n" +
           "SELECT\n" +
           " id,\n" +
           " r_id,\n" +
           " m_id,\n" +
           " is_deleted,\n" +
           " gmt_create,\n" +
           " gmt_modified,\n" +
           " modify_user \n" +
           "FROM\n" +
           " sys_role_menu_function rm \n" +
           "WHERE\n" +
           " r_id IN <foreach collection=\"rds\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach>\n" +
           "</script>")
   List<SysRoleMenuFunction> getUserRoleMenu(@Param("rds") List<String> rds);

   @Update("<script> \n" +
           "UPDATE sys_role_menu_function \n" +
           "SET is_deleted = 1 \n" +
           "WHERE\n" +
           " m_id IN <foreach collection=\"rds\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach>\n" +
           " AND is_deleted = 0 \n" +
           "</script>")
   Integer updateRoleFunctionDeleteByeId(@Param("rds") List<Integer> mid);

   @Update("UPDATE sys_role_menu_function \n" +
           "SET is_deleted = 1 \n" +
           "WHERE\n" +
           " r_id = #{rId} AND is_deleted = 0")
   Integer updateLogicallyDeleteByRoleId(@Param("rId") Long rId);

   @Insert("<script>\n" +
           "INSERT INTO sys_role_menu_function ( r_id, m_id, is_deleted, gmt_create, gmt_modified )\n" +
           "VALUES <foreach collection=\"mIds\" item=\"item\" separator=\",\">(#{rId}, #{item}, 0, NOW(), NOW())</foreach>\n" +
           "</script>")
   Integer batchInsert(@Param("rId") Long rId, @Param("mIds") List<Long> list);

   @Delete("<script>\n" +
           "DELETE FROM sys_role_menu_function \n" +
           "WHERE m_id IN <foreach collection=\"mIds\" item=\"item\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach> \n" +
           "AND r_id = #{rId}\n" +
           "</script>")
   Integer batchDelete(@Param("rId") Long rId, @Param("mIds") List<Long> list);
}
package com.addplus.server.api.mapper.authority;

import com.addplus.server.api.model.authority.SysRoleMenuElement;
import com.addplus.server.api.utils.BaseAddMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleMenuElementMapper extends BaseAddMapper<SysRoleMenuElement> {

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
           " r_id IN\n" +
           " <foreach collection=\"rds\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach>\n" +
           "</script>")
   List<SysRoleMenuElement> getRoleMenuElement(@Param("rds") List<String> rds);

   @Update("UPDATE sys_role_menu_element \n" +
           "SET is_deleted = 1 \n" +
           "WHERE\n" +
           " e_id = #{eId} AND is_deleted = 0")
   Integer updateRoleMenuElementDeleteByeId(@Param("eId") Integer eid);

   @Update("<script>\n" +
           "UPDATE sys_role_menu_function \n" +
           "SET is_deleted = 1 \n" +
           "WHERE\n" +
           " m_id IN <foreach collection=\"rds\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach>\n" +
           " AND is_deleted = 0\n" +
           "</script>")
   Integer updateLogicallyDeleteBymenuElementId(@Param("rds") List<Integer> mid);

   @Update("UPDATE sys_role_menu_element \n" +
           "SET is_deleted = 1 \n" +
           "WHERE\n" +
           " r_id = #{rId} AND is_deleted = 0")
   Integer updateLogicallyDeleteByRoleId(@Param("rId") Long rId);

}
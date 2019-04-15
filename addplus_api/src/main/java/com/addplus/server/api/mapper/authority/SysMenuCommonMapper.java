package com.addplus.server.api.mapper.authority;

import com.addplus.server.api.model.authority.SysMenuCommon;
import com.addplus.server.api.utils.BaseAddMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuCommonMapper extends BaseAddMapper<SysMenuCommon> {

    @Select("SELECT\n" +
            " id,\n" +
            " NAME,\n" +
            " m_desc,\n" +
            " url,\n" +
            " sort,\n" +
            " is_deleted,\n" +
            " gmt_create,\n" +
            " gmt_modified,\n" +
            " module,\n" +
            " filter,\n" +
            " modify_user \n" +
            "FROM\n" +
            " sys_menu_common \n" +
            "WHERE\n" +
            " is_deleted = 0 \n" +
            " AND module = #{module} ORDER BY sort DESC")
    List<SysMenuCommon> getMenuCommonByModule(@Param("module") String module);

}
package com.addplus.server.shiro.config.shiro;
import com.addplus.server.api.model.authority.SysUser;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 类名: CustomerAuthrizer
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/10/27 4:37 PM
 * @description 类描述: 自定义Authorizer ，用于判断用哪个Realm授权
 */
public class CustomerAuthrizer extends ModularRealmAuthorizer {


    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        assertRealmsConfigured();
        Object primaryPrincipal = principals.getPrimaryPrincipal();
        System.out.println(primaryPrincipal);
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer)){
                continue;
            }
            if (primaryPrincipal instanceof SysUser) {
                if (realm instanceof AdminLoginShiroRealm) {
                    return ((AdminLoginShiroRealm) realm).isPermitted(principals, permission);
                }
            }
        }
        return false;
    }
}
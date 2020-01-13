package com.jimu.study.config;

import com.jimu.study.realm.UserRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hxt
 */
@Configuration
public class ShiroConfig {

    /**配置自己的Realm*/
    @Bean
    UserRealm userRealm(){
        return new UserRealm();
    }

    /**把自己的Realm加入容器*/
    @Bean
    DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(userRealm());
        return manager;
    }

    @Bean(name = "shiroFilter")
    ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager());
        //就是被拦截下来的请求暂时都换成这个
        bean.setLoginUrl("/users/error");
        //保留参数，不知道有什么用
        bean.setSuccessUrl("/index");
        bean.setUnauthorizedUrl("/unauthorizedurl");
        //配置拦截规则，一定要是有序的，anon是不拦截的，authc是需要拦截的，'/**'要放最后，shiro是按顺序检索下来的
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/users/login", "anon");
        map.put("/users/register", "anon");
        map.put("/users/**", "authc");
        map.put("/folder/**", "authc");
        map.put("/order/**", "authc");
        map.put("/vipType/**", "authc");
        map.put("/anno/createAnno", "authc");
        map.put("/courseContent/**", "authc");
        map.put("/learnTime/**", "authc");
        bean.setFilterChainDefinitionMap(map);
        return bean;
    }
}

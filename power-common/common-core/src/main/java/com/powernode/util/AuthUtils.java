package com.powernode.util;

import com.powernode.model.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

public class AuthUtils {
    public static SecurityUser getLoginUser() {
        // This method should return the currently logged-in user.
        // In a real application, this would typically retrieve the user from the security context or session.
        // Here, we return null for simplicity.
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Long getLoginUserId() {
        // This method retrieves the ID of the currently logged-in user.

            return getLoginUser().getUserId();
    }


    public static Set<String>  getLoginUserPerms() {
        // This method retrieves the permissions of the currently logged-in user.

            return getLoginUser().getPerms();
    }

}

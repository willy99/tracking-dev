package com.tmw.tracking.service;

import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Permission;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.paging.Page;
import org.apache.shiro.authz.permission.RolePermissionResolver;

import java.util.Collection;
import java.util.List;

public interface PermissionService extends RolePermissionResolver {

    void init();

    Collection<Permission> getPermissions(Role role);

    List<Permission> getAllPermissions();

    Role getRoleById(Long id);

    /**
     * Retrieves the collection of {@link Role Role}
     * @return the collection of {@code Role}
     */
    List<Role> getAllRoles(boolean includeSystem);

    Page<Role> getAllRolesForWeb(SearchCriteria searchCriteria);

    Role getRoleByName(String roleName);

    Role updateRole(Role role);

    void deleteRole(Role role);


}

package com.tmw.tracking.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tmw.tracking.dao.PermissionDao;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.dao.UserDao;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.service.PermissionService;
import com.tmw.tracking.web.service.exception.ValidationException;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.*;

@Singleton
public class PermissionServiceImpl implements PermissionService, RolePermissionResolver {

    private static Map<String, Collection<Permission>> permMap = new HashMap<String, Collection<Permission>>();
    private PermissionDao permissionDao;
    private RoleDao roleDao;
    private UserDao userDao;

    @Inject
    public PermissionServiceImpl(final PermissionDao permissionDao,
                                 final RoleDao roleDao,
                                 final UserDao userDao) {
        this.permissionDao = permissionDao;
        this.roleDao = roleDao;
        this.userDao = userDao;
        init();
    }

    @Override
    public void init() {
        permMap.clear();
        for (Role role : roleDao.getAll()) {
            Collection<Permission> permissions = new HashSet<Permission>();
            for (com.tmw.tracking.entity.Permission permission: role.getPermissionList()) {
                permissions.add(new WildcardPermission(permission.getName().name()));
                //put parent
                if (permission.getParentPermission() != null) {
                    permissions.add(new WildcardPermission(permission.getParentPermission().getName().name()));
                    //master parent
                    if (permission.getParentPermission().getParentPermission() != null) {
                        permissions.add(new WildcardPermission(permission.getParentPermission().getParentPermission().getName().name()));
                    }
                }
            }
            permMap.put(role.getRoleName(), permissions);
        }
    }

    @Override
    public Collection<com.tmw.tracking.entity.Permission> getPermissions(Role role) {
        return role.getPermissionList();
    }

    @Override
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        return permMap.get(roleString);
    }

    @Override
    public List<com.tmw.tracking.entity.Permission> getAllPermissions() {
        List<com.tmw.tracking.entity.Permission> permissionList = new ArrayList<com.tmw.tracking.entity.Permission>();
        for(com.tmw.tracking.entity.Permission permission :  permissionDao.getAllPermissions()){
            if(permission.getParentPermission() == null){
                permissionList.add(permission);
            }
        }

        sortPermission(permissionList);
        return permissionList;
    }

    /* sort by description alphabetically with nested collections */
    private void sortPermission(List<com.tmw.tracking.entity.Permission> permissionList) {
        Collections.sort(permissionList, new Comparator<com.tmw.tracking.entity.Permission>() {
            @Override
            public int compare(com.tmw.tracking.entity.Permission o1, com.tmw.tracking.entity.Permission o2) {
                /*if (o1.getChildren().isEmpty() && o2.getChildren().isEmpty()) {
                    return o1.getDescription().compareTo(o2.getDescription());
                }
                //return o1.getChildren().size()-o2.getChildren().size();*/
                return o1.getDescription().compareTo(o2.getDescription());
            }
        });
        for (com.tmw.tracking.entity.Permission permission: permissionList) {
            if (!permission.getChildren().isEmpty()) {
                sortPermission(permission.getChildren());
            }
        }

    }

    @Override
    public Role getRoleById(Long id) {
        return roleDao.getById(id);
    }

    @Override
    public List<Role> getAllRoles(boolean includeSystem) {
        return roleDao.getAll(includeSystem);
    }

    @Override
    public Page<Role> getAllRolesForWeb(SearchCriteria searchCriteria) {
        return roleDao.getForWeb(searchCriteria);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleDao.getByRoleName(roleName);
    }

    @Override
    public Role updateRole(Role role) {
        if (!role.isEditable()) {
            throw new ValidationException("Role is not editable!");
        }
        return roleDao.update(role);
    }

    @Override
    public void deleteRole(Role role) {
        if (!role.isEditable()) {
            throw new ValidationException("Role is not editable!");
        }
        //TODO check if users are assigned to role
        List<User> users = userDao.getUsersByRole(role.getId());
        if (!users.isEmpty()) {
            throw new ValidationException("Can't delete role, remove assigned users first");
        }
        roleDao.delete(role);
    }
}

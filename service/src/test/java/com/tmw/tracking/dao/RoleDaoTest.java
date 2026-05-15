package com.tmw.tracking.dao;

import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.entity.Permission;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.enums.RoleEnum;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by pzhelnov on 2/21/2017.
 */
@Category(TrackingBaseUnitTest.class)
public class RoleDaoTest extends TrackingBaseUnitTest {

    private RoleDao roleDao;
    private PermissionDao permissionDao;

    private final static Logger logger = LoggerFactory.getLogger(RoleDaoTest.class);

        /**
         * {@inheritDoc}
         * @see TrackingBaseUnitTest#setUp()
         */
        @Before
        public void setUp() throws Exception {
            super.setUp();
            roleDao = injector.getInstance(RoleDao.class);
            permissionDao = injector.getInstance(PermissionDao.class);
        }


        @Test
        public void permissionOrderHierarchyTest() {

            Permission orderPermission = permissionDao.getByPermissionType(PermissionType.ORDER_UPDATE);
            assertNotNull(orderPermission.getChildren());
            assertTrue(orderPermission.getChildren().size() > 0);
            for (Permission permission : orderPermission.getChildren()) {
                logger.info(permission.toString());
                assertEquals(permission.getParentPermission().getId(), orderPermission.getId());
                assertNotNull(permission.getName());
                if (permission.getChildren().size() > 0) {
                    for (Permission childPermission : permission.getChildren()) {
                        assertEquals(permission.getId(), childPermission.getParentPermission().getId());
                    }
                }
            }
        }

        @Test
        public void permissionHierarchyTest() {
            List<Permission> permissions=  permissionDao.getAllPermissions();
            assertNotNull(permissions);
            Set<Long> previous = new HashSet<Long>();
            for(Permission permission : permissions){
                //logger.info(permission.getName()+" child permissions :" + permission.getChildren());
                assertNotNull(permission.getChildren());
                if (!permission.getChildren().isEmpty() && !previous.contains(permission.getId())) {
                    assertFalse(detectCyclic("", permission, previous));
                }
            }
        }

        private boolean detectCyclic(String prefix, Permission permission, Set<Long> previous) {

            logger.info("Checking permission " + prefix + " id: " + permission.getId());
            if (previous.contains(permission.getId())) {
                return true;
            }
            previous.add(permission.getId());
            if (!permission.getChildren().isEmpty()) {
                for (Permission child : permission.getChildren()) {
                    boolean check = detectCyclic(prefix + "--", child, previous);
                    if (check) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Test
        public void testGetRoleByName() {
            Role role = roleDao.getByRoleName(RoleEnum.COMPANY_ADMIN.getName());
            assertNotNull(role);
            assertEquals(RoleEnum.COMPANY_ADMIN.getName(), role.getRoleName());
        }


        @Test
        public void testCRUDForRole() {
            Role role = new Role();
            role.setRoleName("Test");
            Set<Permission> permissionList = new HashSet<Permission>();
            Permission permission = permissionDao.getByPermissionType(PermissionType.DICT_GOODS_SHOW);
            assertNotNull(permission);
            permissionList.add(permission);
            role.setPermissionList(permissionList);

            Role updated = roleDao.update(role);
            assertNotNull(updated.getId());

            Role fromDataBase = roleDao.getById(updated.getId());
            assertNotNull(fromDataBase);
            assertNotNull(fromDataBase.getPermissionList());
            assertTrue(fromDataBase.getPermissionList().size() == 1);

            //delete
            roleDao.delete(updated);

            Role deleted = roleDao.getById(updated.getId());
            assertNull(deleted);
        }


    }

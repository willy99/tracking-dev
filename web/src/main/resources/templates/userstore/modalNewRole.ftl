<div class="pop-up" id="add-role-pop-up">
    <div class="window w-840">
        <div class="close-btn"></div>
        <div class="pop-up-title">New Role Creation</div>
        <form name="roleForm" class="css-form">
            <div class="fieldset visible">
                <div class="row">
                    <div class="name">Name:</div>
                    <input class="input" type="text" ng-model="roleInfo.role.roleName" ng-required="true">
                </div>
                <div class="row tree-row">
                    <div class="name">Permission:</div>
                    <ul class = "tree">
                        <li class="tree-item" ng-repeat = "permission in allPermissions">
                            <div class="sub-tree-title" ng-click="collapseExpand($event)" ng-show="!permission.systemPermission">{{permission.description}}</div>
                            <ul class="sub-tree" ng-show="!permission.systemPermission">

                                <li class="tree-item" ng-repeat="subpermission in permission.children">
                                    <label ng-show = "subpermission.children.length == 0">
                                        <input type="checkbox" ng-model="subpermission.checked">
                                        <span class="checkbox"></span>
                                        <span class="param-name">{{subpermission.description}}</span>
                                    </label>

                                    <div class="sub-tree-title" ng-click="collapseExpand($event)" ng-show="subpermission.children.length > 0">{{subpermission.description}}</div>
                                        <ul class="sub-tree">
                                            <li class="tree-item" ng-repeat="subpermission2 in subpermission.children">
                                                <label>
                                                    <input type="checkbox" ng-model="subpermission2.checked">
                                                    <span class="checkbox"></span>
                                                    <span class="param-name">{{subpermission2.description}}</span>
                                                </label>
                                            </li>
                                        </ul>

                                </li>

                            </ul>
                        </li>
                    </ul>

                </div>

                <div class="pop-up-message" id="pop-up-message"></div>

                <div class="buttons-wrap">
                    <div class="button button-gray button-left cancel-btn">Exit without save</div>
                    <button ng-click="saveRole()" class="button button-green button-right">Create a Role</button>
                </div>

            </div>
        </form>
    </div>
</div>

<div class="pop-up" id="edit-role-pop-up">
    <div class="window w-840">
        <div class="close-btn"></div>
        <div class="pop-up-title">Edit Role</div>
        <form name="roleForm" class="css-form">
            <div class="fieldset visible">
                <div class="row">
                    <div class="name">Name:</div>
                    <input class="input" type="text" ng-model="roleInfo.role.roleName" ng-required="true">
                </div>
                <div class="row tree-row">
                    <div class="name">Permission:</div>
                    <ul class = "tree">
                        <li class="tree-item" ng-repeat = "permission in allPermissions">
                            <div class="sub-tree-title" ng-click="collapseExpand($event)" ng-show="!permission.systemPermission">{{permission.description}}</div>
                            <ul class="sub-tree" ng-show="!permission.systemPermission">

                                <li class="tree-item" ng-repeat="subpermission in permission.children">
                                    <label ng-show = "subpermission.children.length == 0">
                                        <input type="checkbox" ng-model="subpermission.checked">
                                        <span class="checkbox"></span>
                                        <span class="param-name">{{subpermission.description}}</span>
                                    </label>

                                    <div class="sub-tree-title" ng-click="collapseExpand($event)" ng-show="subpermission.children.length > 0">{{subpermission.description}}</div>
                                        <ul class="sub-tree">
                                            <li class="tree-item" ng-repeat="subpermission2 in subpermission.children">
                                                <label>
                                                    <input type="checkbox" ng-model="subpermission2.checked">
                                                    <span class="checkbox"></span>
                                                    <span class="param-name">{{subpermission2.description}}</span>
                                                </label>
                                            </li>
                                        </ul>

                                </li>

                            </ul>
                        </li>
                    </ul>

                </div>

                <div class="pop-up-message" id="pop-up-message"></div>

                <div class="buttons-wrap">
                    <div class="button button-red button-left delete-btn" ng-click="deleteRole()">Delete Role</div>
                    <div class="button button-gray button-left cancel-btn">Exit without save</div>
                    <button ng-click="saveRole()" class="button button-green button-right">Update Role</button>
                </div>

            </div>
        </form>
    </div>

</div>


<!--

            <div id="exTab2">
                <ul class="nav nav-tabs">
                    <li class="active">
                        <a  href="#1" data-toggle="tab">Permission Management</a>
                    </li>
                    <li><a href="#2" data-toggle="tab">User Management</a>
                    </li>
                    <li><a href="#3" data-toggle="tab">Status Workflow</a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane active" id="1">

                        Role :<br>

                            <img id="loading" src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading"/>


                        <form action="#" method="post" ng-show="roleInfo">
                            <div>
                                 <input id="roleName" type="text" class="span3 inline" placeholder="Role Name"
                                   ng-model="roleInfo.role.roleName" required>
                            </div>
                            <div>
                                        <label><b>Permissions:</b>&nbsp;</label>

                                        <p ng-repeat="permission in roleInfo.allPermissions">
                                            <input
                                                    type="checkbox"
                                                    name="selectedPermissions[]"
                                                    value="{{permission.name}}"
                                                    ng-checked="selection.indexOf(permission.id) > -1"
                                                    ng-click="toggleSelection(permission.id)"
                                            > {{permission.name}} - {{permission.description}}
                                        </p>

                                <tree-view checkbox click="clickNode(node)" model="permissions"></tree-view>
                                <button ng-show="roleInfo" ng-disabled="!roleInfo.role.editable" type="button" class="btn btn-primary" ng-click="saveRole()">Save</button>

                                    <button ng-show="roleInfo.role.id" ng-disabled="!roleInfo.role.editable" type="button" class="btn btn-danger" ng-click="deleteRole()">Delete</button>

                            </div>

                        </form>
                    </div>

                    <div class="tab-pane" id="2">

                        <fieldset><legend>Role {{roleInfo.role.roleName}}</legend>

                            <table class="table table-bordered table-striped" ng-table="usersTable" ng-show="roleInfo">
                                <tr ng-repeat="u in $data" ng-click="editUser(u)" ng-class="{active: u.email === selectedUserEmail, deleted: u.active === false}">
                                    <td data-title="'First Name'" sortable="'firstName'">
                                        <span ng-bind-html="u.firstName | highlight:searchtext | transliterate | trusted"></span>
                                    </td>
                                    <td data-title="'Last Name'" sortable="'lastName'">
                                        <span ng-bind-html="u.lastName | highlight:searchtext | transliterate | trusted"></span>
                                    </td>
                                    <td data-title="'Email'" sortable="'email'">
                                        <span ng-bind-html="u.email | highlight:searchtext | trusted"></span>
                                    </td>
                                    <td data-title="'Phone'" sortable="'phone'">
                                        {{u.phone}}
                                    </td>
                                    <td data-title="'Active'" sortable="'active'">
                                        {{u.active? 'Yes':'No'}}
                                    </td>
                                </tr>
                            </table>



                            <a ng-show="roleInfo" class="btn btn-default" href ng-click="onShowModal()">Add new user for {{roleInfo.role.roleName}} Role</a>
                            <button ng-show="user" class="btn btn-danger"  type="button" ng-click="clearRole()">Delete User Role</button>
                        </fieldset>


                    </div>
                    <div class="tab-pane" id="3">
                        <h3>TODO</h3>
                    </div>
                </div>
            </div>
-->
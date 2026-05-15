<#assign top_nav_selected = "roleManagement">
<#assign page_title = "Role Management">
<#include "*/header.ftl"/>
<main class="page" ng-controller="roleController">
    <div class="spinner" ng-show="loading"></div>
    <#include "modalNewRole.ftl"/>

    <div class="block main-block">
        <div class="content">
            <#if help_button><div class="aside-help-btn">help?</div></#if>
            <h1>Role Management</h1>

            <div class="top-menu">
                <div class="button button-green pop-up-opener" ng-click="createRole()">Create a New Role</div>
                <form class="search-form">
                    <input name="search" ng-model="searchtext" placeholder="Search" type="search"><input ng-click="search()" type="submit" class="button button-gray" value="Go!">
                </form>
            </div>
            <table class="common-table" ng-table="tableParams">
                <thead>
                <tr>
                    <th><span class="title">Name</span><div class="arrow"></div></th>
                    <th><span class="title">Users</span><div class="arrow"></div></th>
                    <th><span class="title">Active Permissions</span><div class="arrow"></div></th>
                    <th><span class="title">Action</span><div class="arrow"></div></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="r in $data">
                    <td><span ng-bind-html="r.roleName | highlight:searchtext | transliterate | trusted"></span></td>
                    <td><span ng-bind="r.users"></span></td>
                    <td><span ng-bind="r.checkedPermissions"></span></td>
                    <td class="edit"><img src="${contextPath}/images/pencil.svg" alt="" ng-click="editRole(r)" ng-show="r.editable"></td>
                </tr>
                </tbody>
            </table>

        </div>
    </div>
</main>


<script src="${contextPath}/js/angular/controllers/searchController.js"></script>
<script type="text/javascript">

    app.controller("roleController", function($scope, $filter, $http, $q, NgTableParams, ModalService) {
        var data = [];
        var dataUsers = [];
        $scope.allPermissions = [];
        $scope.searchtext="";

        $scope.search = function() {
            $scope.init($scope.searchtext);
        };


        $http.get('${contextPath}/tmw/userstore/getAllPermissions')
                .then(function(res){
                    $scope.allPermissions = res.data;
                    $scope.permissions = $scope.allPermissions;
                });

        $scope.init = function(searchFor) {

            if (angular.isUndefined(searchFor)) {
                searchFor = "";
            }

            $scope.tableParams = new NgTableParams({count: ${pageSize}}, {getData: function($defer, params) {

                var url = '${contextPath}/tmw/userstore/getAllRoles?searchFor=' + searchFor + '&page=' + params.page();
                var sort = getSortParams(params.sorting());

                if (sort) {
                    url = url + "&sort=" + sort;
                }

                $http.get(url).then(function(response){
                    $scope.roles = response.data;
                    console.log($scope.roles);
                    data = response.data;

                    var totalPages = response.headers("X-Total-Pages");
                    var countPerPage = response.headers("X-Page-Size");

                    params.total(totalPages * countPerPage);
                    $scope.data = data;
                    $defer.resolve(data);
                });
            }});

        };

        $scope.createRole = function() {
            console.log( $scope.allPermissions);
            $scope.createRoleEntity();
            document.getElementById('add-role-pop-up').classList.add('active');
            expandFieldsets();
            document.body.classList.add('fixed');
            $scope.form = '#add-role-pop-up';
        };

        $scope.collapseExpand = function ($event) {
            collapseExpandTree($event.currentTarget);
        };


        $scope.editRole = function (role) {
            $scope.loading = true;
            $scope.createRoleEntity();
            document.getElementById('edit-role-pop-up').classList.add('active');
            document.body.classList.add('fixed');
            expandFieldsets();
            $scope.form = '#edit-role-pop-up';

            $scope.idSelectedRole = role.id;
            $scope.roleInfo = null;
            $http.get('${contextPath}/tmw/userstore/getRole?id=' + role.id).then(function (res) {
                $scope.loading = false;
                if (res.data.errorMessage) {
                    $scope.roleInfo = null;
                    showPopUpMessage($scope.form, data.errorMessage, 'error');
                    $scope.loading = false;
                } else {
                    $scope.roleInfo = res.data;

                    if (!$scope.roleInfo.role.id) {
                    //handle error, something is wrong
                        showPopUpMessage($scope.form, 'The session is logged out. Please relogin', 'error');
                    }
                    $scope.clearPermission($scope.allPermissions);

                    $scope.setSelection($scope.allPermissions);
                    //$scope.permissions= $scope.allPermissions;

                    /*$scope.usersTable = new NgTableParams({count: ${pageSize}}, {
                           getData: function ($defer, params) {

                               var url = '${contextPath}/tmw/userstore/getUsersByRole?roleId=' + $scope.idSelectedRole;
                               var sort = getSortParams(params.sorting());

                               if (sort) {
                                   url = url + "&sort=" + sort;
                               }

                               $http.get(url).then(function (response) {
                                   $scope.users = response.data;
                                   dataUsers = response.data;

                                   var totalPages = response.headers("X-Total-Pages");
                                   var countPerPage = response.headers("X-Page-Size");

                                   params.total(totalPages * countPerPage);
                                   $scope.dataUsers = dataUsers;
                                   $defer.resolve(dataUsers);
                               });
                           }
                       });



                    $scope.editUser = function (user) {
                        $scope.selectedUserEmail = user.email;
                        $scope.user = null;
                        $scope.errorMessage = null;
                        $scope.loading = true;
                        $http.get('${contextPath}/tmw/userstore/find?id=' + user.id).then(function (res) {
                            $scope.loading = false;
                            if (res.data.errorMessage) {
                                $scope.user = null;
                                $scope.errorMessage = res.data.errorMessage;
                            } else {
                                $scope.user = res.data.user;
                            }

                        } , function error(res) {
                            $scope.errorMessage = "Request error";
                            $scope.roleInfo = null;
                        });
                    };


                    $scope.clearRole = function () {
                        $scope.errorMessage = null;
                        $scope.state = null;
                        $scope.loading = true;
                        $http.get('${contextPath}/tmw/userstore/clearRole?email=' + $scope.selectedUserEmail).then(
                                function (res, respStatus, headers, config) {
                                    $scope.loading = false;
                                    if (res.data.errorMessage) {
                                        $scope.errorMessage = res.data.errorMessage;
                                    } else {
                                        $scope.init();
                                        $scope.usersTable.reload();
                                    }
                                }, function error(data, respStatus, headers, config) {
                                    $scope.errorMessage = "Request error";
                                    $scope.loading = false;
                                });
                    };*/

                    $scope.loading = false;

                }

            }, function error(data) {
                showPopUpMessage($scope.form, data.errorMessage, 'error');
                $scope.roleInfo = null;
                $scope.loading = false;
            });

        };

        /*$scope.onShowModal = function() {
            ModalService.showModal({
                        templateUrl: "modalSearchUser",
                        inputs: {
                            idRole: $scope.idSelectedRole,
                            page_size:  ${pageSize},
                            contextPath : '${contextPath}'
                        },
                        controller: "SearchUserController",
                        preClose: (modal) => { modal.element.modal('hide'); }
            }).then(function(modal) {
                modal.element.modal();
                modal.close.then(function(userId) {

                    if(!angular.isUndefined(userId)) {
                        $http.post('${contextPath}/tmw/userstore/addRoleForUser?userId=' + userId + '&roleId=' + $scope.idSelectedRole)
                                .then(function (res) {
                                    $scope.loading = false;
                                    if (res.data.errorMessage) {

                                    } else {
                                        $scope.init();
                                        $scope.usersTable.reload();
                                    }

                                }, function error(res) {

                                });
                    }

                });
            });
        };*/

        $scope.createRoleEntity = function () {
            $scope.errorMessage = null;
            $scope.selection = [];
            $scope.roleInfo = {
                role : {
                    id : "", roleName: "",
                    editable: true,
                    assignable: true
                },
                permissions: []
            };
            $scope.clearPermission($scope.allPermissions);
            $scope.form = '#add-role-pop-up';
        };

        $scope.clearPermission = function($permList) {
            angular.forEach($permList, function(value, key) {
                value.checked = false;
                if (value.children && value.children.length > 0) {
                    $scope.clearPermission(value.children);
                }
            });
        };

        $scope.setSelection = function($permList) {
            angular.forEach($permList, function(value, key) {
                angular.forEach($scope.roleInfo.permissions, function(valueP, keyP) {
                    if (valueP.id === value.id) {
                        value.checked = true;
                    }
                });
                if (value.children && value.children.length > 0) {
                    $scope.setSelection(value.children);
                }
            });
        };

        $scope.convertPermissions = function($permList) {
            angular.forEach($permList, function(value, key) {
                if (value.checked) {
                    $scope.roleInfo.permissions.push(value);
                }
                if (value.children) {
                    $scope.convertPermissions(value.children);
                }
            });
        };

        $scope.saveRole = function () {
            if (!$scope.roleForm.$valid) {
                return;
            }
            $scope.loading = true;
            $scope.roleInfo.permissions = [];
            $scope.convertPermissions($scope.allPermissions);
            $http.post('${contextPath}/tmw/userstore/saveRole', $scope.roleInfo).then(
                    function (res, respStatus, headers, config) {
                        $scope.loading = false;
                        if (res.data.errorMessage) {
                            showPopUpMessage($scope.form, res.data.errorMessage, 'error');
                        } else {
                            showPopUpMessage($scope.form, 'Successfully saved the role', 'success');
                            $scope.init();
                        }
                    }, function error(data, respStatus, headers, config) {
                        $scope.loading = false;
                        showPopUpMessage($scope.form, res.data.error.message, 'error');
                    });


        };

        $scope.deleteRole  = function () {
            $scope.loading = true;
            $http.post('${contextPath}/tmw/userstore/deleteRole', $scope.roleInfo).then(
                    function (res, respStatus, headers, config) {
                        $scope.loading = false;
                        if (res.data.errorMessage) {
                            showPopUpMessage($scope.form, res.data.errorMessage, 'error');
                        } else {
                            showPopUpMessage($scope.form, 'Successfully deleted the role', 'success');
                            $scope.init();
                        }
                    }, function error(data, respStatus, headers, config) {
                        $scope.loading = false;
                        showPopUpMessage($scope.form, res.data.error.message, 'error');
                    });

        };


        // Toggle selection for a given fruit by name
        $scope.toggleSelection = function toggleSelection(id) {
            var idx = $scope.selection.indexOf(id);

            // Is currently selected
            if (idx > -1) {
                $scope.selection.splice(idx, 1);
            }
            // Is newly selected
            else {
                $scope.selection.push(id);
            }
        };

        $scope.init();


    });

</script>
<#include "*/footer.ftl"/>
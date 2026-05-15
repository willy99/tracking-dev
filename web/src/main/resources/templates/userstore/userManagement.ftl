<#assign top_nav_selected = "userManagement">
<#assign page_title = "User Management">
<#include "*/header.ftl"/>


<main class="page" ng-controller="userController">
    <div class="spinner" ng-show="loading"></div>
    <#include "modalNewUser.ftl"/>

    <div class="block main-block">
        <div class="content">
            <#if help_button><div class="aside-help-btn">help?</div></#if>
            <h1>Users</h1>

            <div class="top-menu">
                <div class="button button-green pop-up-opener" ng-click="createUser()">Create a New User</div>
                <form class="search-form">
                    <input name="search" ng-model="searchtext" placeholder="Search" type="search"><input ng-click="search()" type="submit" class="button button-gray" value="Go!">
                </form>
            </div>
            <table class="common-table" ng-table="tableParams">
                <thead>
                <tr>
                    <th><span class="title">First Name</span><div class="arrow"></div></th>
                    <th><span class="title">Last Name</span><div class="arrow"></div></th>
                    <th><span class="title">Active</span><div class="arrow"></div></th>
                    <th><span class="title">Email</span><div class="arrow"></div></th>
                    <th><span class="title">Phone</span><div class="arrow"></div></th>
                    <th><span class="title">Role</span><div class="arrow"></div></th>
                    <th><span class="title">Action</span><div class="arrow"></div></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="u in $data" class="{{u.active? 'active':'inactive'}}" ng-show="!u.role.systemRole">
                    <td><span ng-bind-html="u.firstName | highlight:searchtext | transliterate | trusted"></span></td>
                    <td><span ng-bind-html="u.lastName | highlight:searchtext | transliterate | trusted"></span></td>
                    <td>{{u.active? 'Yes':'No'}}</td>
                    <td><span ng-bind-html="u.email | highlight:searchtext | transliterate | trusted"></span></td>
                    <td><span ng-bind-html="u.phone | highlight:searchtext | transliterate | trusted"></span></td>
                    <td><span ng-bind-html="u.role.roleName | highlight:searchtext | transliterate | trusted"></span></td>
                    <td class="edit"><img ng-show="u.role.assignable" src="${contextPath}/images/pencil.svg" alt="" ng-click="editUser(u)"></td>
                </tr>
                </tbody>
            </table>

        </div>
    </div>
</main>

<script type="text/javascript">

    app.controller("userController", function($scope, $filter, $http, $q, NgTableParams) {

        $scope.roleList = [
        <#list roles as role>
            {
                "id":"${role.id}",
                "roleName":"${role.roleName}"
            },


        </#list>
        ];

        var data = [];

        $scope.searchtext="";

        $scope.search = function() {
            $scope.initUserForm($scope.searchtext);
        };

        $scope.getRoleByName = function(roleName) {
            for (key in $scope.roleList) {
                if ($scope.roleList[key].roleName == roleName) {
                    return $scope.roleList[key];
                }
            }
            return null;
        };

        $scope.initUserForm = function(searchFor) {
            $scope.loading = true;
            if (angular.isUndefined(searchFor)) {
                searchFor = "";
            }

            $scope.tableParams = new NgTableParams({count: ${pageSize}}, {getData: function($defer, params) {
                $scope.filter = {roleId: $scope.selectedRole, matcher: searchFor};

                var url = '${contextPath}/tmw/userstore/findUsers?page=' + params.page();

                var sort = getSortParams(params.sorting());

                if (sort) {
                       url = url + "&sort=" + sort;
                }

                $http.post(url, $scope.filter).then(function(response){
                    $scope.users = response.data;
                    data = response.data;

                    var totalPages = response.headers("X-Total-Pages");
                    var countPerPage = response.headers("X-Page-Size");

                    params.total(totalPages * countPerPage);
                    $scope.data = data;
                    $defer.resolve(data);
                    $scope.loading = false;
                });

            }});
        };

        $scope.editUser = function (user) {
            $scope.loading = true;
            $scope.createUserEntity();
            document.getElementById('edit-user-pop-up').classList.add('active');
            document.body.classList.add('fixed');
            expandFieldsets();

            $http.get('${contextPath}/tmw/userstore/get?id=' + user.id).then(function(response){
                $scope.user = response.data;
                $scope.user.role = $scope.getRoleByName($scope.user.role);
                if (!$scope.user.id) {
                    //handle error, something is wrong
                    showPopUpMessage('#edit-user-pop-up', 'The session is logged out. Please relogin', 'error');
                }
                $scope.user.password = '';
                $scope.loading = false;

            }, function error(data) {
                showPopUpMessage('#edit-user-pop-up', data.errorMessage, 'error');
                $scope.createUserEntity();
                $scope.loading = false;
            });
            $scope.form = '#edit-user-pop-up';
        };

        $scope.createUserEntity = function () {
            $scope.user = {
                email: "",
                active: false,
                role: null
            };
            return true;
        };

        $scope.createUser = function() {
            $scope.createUserEntity();
            document.getElementById('add-user-pop-up').classList.add('active');
            expandFieldsets();
            document.body.classList.add('fixed');
            $scope.form = '#add-user-pop-up';
        };

        $scope.saveUser = function () {
            $scope.loading = true;
            if ($scope.userForm.$valid) {
                $scope.userToUpdate = $scope.user;
                if ($scope.user.role) {
                    $scope.userToUpdate.role = $scope.user.role.roleName;
                }
                $http.post('${contextPath}/webresources/userstore/save', $scope.userToUpdate).then(
                        function (res, respStatus, headers, config) {
                            $scope.loading = false;
                            if (res.data.errorMessage) {
                                showPopUpMessage($scope.form, res.data.errorMessage, 'error');
                            } else {
                                showPopUpMessage($scope.form, 'Successfully saved the user', 'success');
                                $scope.initUserForm();
                            }
                        }, function error(res, respStatus, headers, config) {
                            showPopUpMessage($scope.form, res.data.error.message, 'error');
                            $scope.loading = false;
                        });
            }
        };

        $scope.initUserForm();

    });

</script>
<#include "*/footer.ftl"/>

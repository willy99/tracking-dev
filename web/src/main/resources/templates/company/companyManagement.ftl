<#assign top_nav_selected = "companyManagement">
<#assign page_title = "Company Management">
<#include "*/header.ftl"/>


<main class="page" ng-controller="companyController">
    <div class="spinner" ng-show="loading"></div>
    <#include "modalNewCompany.ftl"/>

    <div class="block main-block">
        <div class="content">
            <#if help_button><div class="aside-help-btn">help?</div></#if>
            <h1>Company List Management</h1>

            <div class="top-menu">
                <div class="button button-green pop-up-opener" ng-click="createCompany()">Create a New Company</div>
                <form class="search-form">
                    <input name="search" ng-model="searchtext" placeholder="Search" type="search"><input ng-click="search()" type="submit" class="button button-gray" value="Go!">
                </form>
            </div>
            <table class="common-table" ng-table="tableParams">
                <thead>
                <tr>
                    <th><span class="title">COMPANY NAME</span><div class="arrow"></div></th>
                    <th><span class="title">ACTIVE</span><div class="arrow"></div></th>
                    <th><span class="title">Users</span> <span class="description">(Active / Nonactive)</span><span class="arrow"></span></th>
                    <th><span class="title">Orders</span><div class="arrow"></div></th>
                    <th><span class="title">Created</span><div class="arrow"></div></th>
                    <th><span class="title">Action</span><div class="arrow"></div></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="u in $data" class="{{u.active? 'active':'inactive'}}">
                    <td><span ng-bind-html="u.name | highlight:searchtext | transliterate | trusted"></span></td>
                    <td>{{u.active? 'Yes':'No'}}</td>
                    <td><span ng-bind="u.activeUsers"></span>/<span ng-bind="u.inactiveUsers"></span></td>
                    <td><span ng-bind="u.orders"></span></td>
                    <td><span ng-bind="u.lastUpdated | stamptodate | trusted "></span></td>
                    <td class="edit"><img src="${contextPath}/images/pencil.svg" alt="" ng-click="editCompany(u.id)"></td>
                </tr>
                </tbody>
            </table>

        </div>
    </div>
</main>





<script>
    app.controller("companyController", function($scope, $filter, $http, $q, NgTableParams, $state) {

        $scope.company = null;

        var data = [];
        $scope.searchtext="";

        $scope.search = function() {
            $scope.initForm($scope.searchtext);
        };

        $scope.initForm = function(searchFor) {
            $scope.loading = true;
            if (angular.isUndefined(searchFor)) {
                searchFor = "";
            }

            $scope.tableParams = new NgTableParams({count: ${pageSize}}, {getData: function($defer, params) {

                    $scope.filter = {matcher: searchFor};
                    var url = '${contextPath}/tmw/company/find?page=' + params.page();

                    var sort = getSortParams(params.sorting());

                    if (sort) {
                        url = url + "&sort=" + sort;
                    }

                    $http.post(url, $scope.filter).then(function(response){
                        $scope.companies = response.data;
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

        $scope.initForm();

        $scope.createCompany = function() {
            $scope.createCompanyEntity();
            document.getElementById('add-company-pop-up').classList.add('active');
            expandFieldsets();
            document.body.classList.add('fixed');
            $scope.form = '#add-company-pop-up';
        };

        $scope.editCompany = function(companyId) {
            $scope.loading = true;
            $scope.createCompanyEntity();
            document.getElementById('edit-company-pop-up').classList.add('active');
            document.body.classList.add('fixed');

            $http.get('${contextPath}/tmw/company/get?id=' + companyId).then(function(response){
                $scope.company = response.data;
                if (!$scope.company.id) {
                    //handle error, something is wrong
                    showPopUpMessage('#edit-company-pop-up', 'The session is logged out. Please relogin', 'error');
                }
                $scope.company.admin.password = '';
                $scope.loading = false;
            }, function error(data) {
                showPopUpMessage('#edit-company-pop-up', data.errorMessage, 'error');
                $scope.createCompanyEntity();
                $scope.loading = false;
            });
            $scope.form = '#edit-company-pop-up';
        };

        $scope.createCompanyEntity = function () {
            $scope.company = {
                name: "",
                active: true,
                admin: {
                    role: 'Company Admin',
                    firstName: "",
                    lastName: "",
                    phone: "",
                    password: "",
                    email: "",
                    active: true
                }
            };
            return true;
        };

        /*$scope.deleteCompany = function() {
            $http.post('${contextPath}/webresources/company/delete', $scope.company.id).then(
                    function (res, respStatus, headers, config) {
                        if (res.data.errorMessage) {
                            showPopUpMessage($scope.form, res.data.errorMessage, 'error');
                        } else {
                            showPopUpMessage($scope.form, 'Successfully deleted the company', 'success');
                            $scope.initForm();
                        }
                    }, function error(res, respStatus, headers, config) {
                        showPopUpMessage($scope.form, res.data.error.message, 'error');
                    });
        };*/

        $scope.saveCompany = function () {
            $scope.loading = true;
            if ($scope.companyForm.$valid) {
                $http.post('${contextPath}/webresources/company/save', $scope.company).then(
                        function (res, respStatus, headers, config) {
                            if (res.data.errorMessage) {
                                showPopUpMessage($scope.form, res.data.errorMessage, 'error');
                            } else {
                                showPopUpMessage($scope.form, 'Successfully saved the company', 'success');
                                $scope.initForm();
                            }
                            $scope.loading = false;
                        }, function error(res, respStatus, headers, config) {
                            showPopUpMessage($scope.form, res.data.error.message, 'error');
                            $scope.loading = false;
                        });
            }
        };

    });

</script>

<#include "*/footer.ftl"/>

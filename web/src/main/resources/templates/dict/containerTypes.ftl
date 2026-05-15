<#assign top_nav_selected = "containerTypesManagement">
<#assign page_title = "Container Types Management">
<#include "*/header.ftl"/>


<main class="page" ng-controller="containerTypeController">
    <div class="spinner" ng-show="loading"></div>

    <div class="block main-block">
        <div class="content">
            <#if help_button><div class="aside-help-btn">help?</div></#if>
            <h1>${label.value("dict_container_type_header")}</h1>

            <div class="top-menu">
                <!--form class="search-form">
                    <input name="search" ng-model="searchtext" placeholder="Search" type="search"><input ng-click="search()" type="submit" class="button button-gray" value="Go!">
                </form-->
            </div>
            <table class="common-table" ng-table="tableParams">
                <thead>
                <tr>
                    <th><span class="title">${label.value("group")}</span><div class="arrow"></div></th>
                    <th><span class="title">${label.value("name")}</span><div class="arrow"></div></th>
                    <th><span class="title">${label.value("length")}</span><div class="arrow"></div></th>
                    <th><span class="title">${label.value("width")}</span><div class="arrow"></div></th>
                    <th><span class="title">${label.value("height")}</span><div class="arrow"></div></th>
                    <th><span class="title">${label.value("dict_container_type_column_workload")}</span><div class="arrow"></div></th>
                    <th><span class="title">${label.value("dict_container_type_column_specific_tonnage")}</span><div class="arrow"></div></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="ct in $data">
                    <td><span ng-bind-html="ct.containerGroup | highlight:searchtext | transliterate | trusted"></span></td>
                    <td><span ng-bind-html="ct.name | highlight:searchtext | transliterate | trusted"></span></td>
                    <td><span ng-bind="ct.length"></span></td>
                    <td><span ng-bind="ct.width"></span></td>
                    <td><span ng-bind="ct.height"></span></td>
                    <td><span ng-bind="ct.workload"></span></td>
                    <td><span ng-bind="ct.specificTonnage"></span></td>
                </tr>
                </tbody>
            </table>

        </div>
    </div>
</main>



<script type="text/javascript">

    app.controller("containerTypeController", function($scope, $filter, $http, $q, NgTableParams) {
        var data = [];

        $scope.searchtext="";

        $scope.search = function() {
            $scope.init($scope.searchtext);
        };

        $scope.init = function(searchFor) {
            if (angular.isUndefined(searchFor)) {
                searchFor = "";
            }
            $scope.tableParams = new NgTableParams({count: ${pageSize}}, {getData: function($defer, params) {

                $scope.filter = {matcher: searchFor};
                var url = '${contextPath}/tmw/dict/getContainerTypes?page=' + params.page();
                var sort = getSortParams(params.sorting());

                if (sort) {
                    url = url + "&sort=" + sort;
                }

                $http.post(url, $scope.filter).then(function(response){
                    data = response.data;

                    var totalPages = response.headers("X-Total-Pages");
                    var countPerPage = response.headers("X-Page-Size");

                    params.total(totalPages * countPerPage);
                    $scope.data = data;
                    $defer.resolve(data);
                });
            }});
        };

        /*$scope.editType = function (containerType) {
            $scope.containerType = null;
            $scope.errorMessage = null;
            $scope.loading = true;
            $scope.selection = [];
            $http.get('${contextPath}/tmw/dict/getContainerType?id=' + containerType.id).then(function (res) {
                $scope.loading = false;
                if (res.data.errorMessage) {
                    $scope.containerType = null;
                    $scope.errorMessage = res.data.errorMessage;
                } else {
                    $scope.containerType = res.data;
                }

            }, function error(data) {
                $scope.errorMessage = "Request error";
                $scope.roleInfo = null;
            });
        };

        $scope.createType = function () {
            $scope.errorMessage = null;
            $scope.selection = [];
            $scope.containerType = {
                id : "",
                type : "",
                length: ""
            };
        };

        $scope.saveType = function () {
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.loading = true;

            $http.post('${contextPath}/tmw/dict/saveContainerType', $scope.containerType).then(
                    function (res, respStatus, headers, config) {
                        $scope.loading = false;
                        if (res.data.errorMessage) {
                            $scope.errorMessage = res.data.errorMessage;
                        } else {
                            $scope.containerType = null;
                            $scope.init();
                        }
                    }, function error(data, respStatus, headers, config) {
                        $scope.loading = false;
                        $scope.errorMessage = "Request error";
                    });


        };

        $scope.deleteType  = function () {
            $scope.errorMessage = null;
            $scope.loading = true;
            $http.post('${contextPath}/tmw/dict/deleteContainerType', $scope.containerType).then(
                    function (res, respStatus, headers, config) {
                        $scope.loading = false;
                        if (res.data.errorMessage) {
                            $scope.errorMessage = res.data.errorMessage;
                        } else {
                            $scope.containerType = null;
                            $scope.init();
                        }
                    }, function error(data, respStatus, headers, config) {
                        $scope.loading = false;
                        $scope.errorMessage = "Request error";
                    });

        };*/

        $scope.init();
    });
</script>

<#include "*/footer.ftl"/>

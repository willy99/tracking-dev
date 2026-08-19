<#assign top_nav_selected = "containerTypesManagement">
<#assign page_title = "Container Types">
<#include "*/header.ftl"/>

<main class="page" ng-controller="containerTypeController">
    <div class="spinner" ng-show="loading"></div>

    <div class="block main-block">
        <div class="content">
            <h1>${label.value("dict_container_type_header")}</h1>

            <fieldset class="mgmt-filter-set">
                <legend><i class="fa fa-search"></i> Search</legend>
                <input type="search" ng-model="searchtext"
                       placeholder="Group, name…"
                       ng-keyup="tableParams.reload()">
                <button class="button button-blue" ng-click="tableParams.reload()">
                    <i class="fa fa-search"></i> Search
                </button>
                <button class="button button-gray" ng-click="clearSearch()">
                    <i class="fa fa-times"></i> Clear
                </button>
            </fieldset>

            <div class="data-card">
                <div class="data-card-header">
                    <span><i class="fa fa-th-list"></i> Container Types</span>
                    <span style="font-weight:400;opacity:.75;text-transform:none;font-size:12px;">
                        {{filtered.length}} records
                    </span>
                </div>
                <table class="common-table dict-table" ng-table="tableParams">
                    <thead>
                    <tr>
                        <th><span class="title">${label.value("group")}</span></th>
                        <th><span class="title">${label.value("name")}</span></th>
                        <th><span class="title">${label.value("length")}</span></th>
                        <th><span class="title">${label.value("width")}</span></th>
                        <th><span class="title">${label.value("height")}</span></th>
                        <th><span class="title">${label.value("dict_container_type_column_workload")}</span></th>
                        <th><span class="title">${label.value("dict_container_type_column_specific_tonnage")}</span></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="ct in $data">
                        <td><span ng-bind-html="ct.containerGroup | highlight:searchtext | transliterate | trusted"></span></td>
                        <td><span ng-bind-html="ct.name | highlight:searchtext | transliterate | trusted"></span></td>
                        <td>{{ct.length}}</td>
                        <td>{{ct.width}}</td>
                        <td>{{ct.height}}</td>
                        <td>{{ct.workload}}</td>
                        <td>{{ct.specificTonnage}}</td>
                    </tr>
                    <tr ng-show="!loading && $data.length === 0">
                        <td colspan="7" style="text-align:center;padding:24px;" class="muted">No records found.</td>
                    </tr>
                    </tbody>
                </table>
            </div><!-- /data-card -->

        </div>
    </div>
</main>

<script type="text/javascript">
    app.controller("containerTypeController", function ($scope, $filter, $http, NgTableParams) {
        $scope.allData   = [];
        $scope.filtered  = [];
        $scope.searchtext = "";
        $scope.loading   = true;

        $scope.clearSearch = function () {
            $scope.searchtext = "";
            $scope.tableParams.reload();
        };

        $scope.tableParams = new NgTableParams({ count: ${pageSize} }, {
            getData: function ($defer, params) {
                $scope.filtered = $filter("filter")($scope.allData, $scope.searchtext);
                params.total($scope.filtered.length);
                var start = (params.page() - 1) * params.count();
                $defer.resolve($scope.filtered.slice(start, start + params.count()));
            }
        });

        $http.get("${contextPath}/tmw/dict/getAllContainerTypes").then(function (res) {
            $scope.allData = res.data || [];
            $scope.loading = false;
            $scope.tableParams.reload();
        });
    });
</script>

<#include "*/footer.ftl"/>

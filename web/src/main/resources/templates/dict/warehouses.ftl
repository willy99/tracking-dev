<#assign top_nav_selected = "warehouseManagement">
<#assign page_title = "Warehouses">
<#include "*/header.ftl"/>

<main class="page" ng-controller="warehouseController">
    <div class="spinner" ng-show="loading"></div>

    <div class="block main-block">
        <div class="content">
            <h1>Warehouses</h1>

            <fieldset class="mgmt-filter-set">
                <legend><i class="fa fa-search"></i> Search</legend>
                <input type="search" ng-model="searchtext"
                       placeholder="Name, type…"
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
                    <span><i class="fa fa-archive"></i> Warehouses</span>
                    <span style="font-weight:400;opacity:.75;text-transform:none;font-size:12px;">
                        {{filtered.length}} records
                    </span>
                </div>
                <table class="common-table dict-table" ng-table="tableParams">
                    <thead>
                    <tr>
                        <th><span class="title">Name</span></th>
                        <th><span class="title">Type</span></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="w in $data">
                        <td>{{w.name}}</td>
                        <td>
                            <span ng-class="{
                                'label-base':       w.warehouseType === 'BASE',
                                'label-reserve':    w.warehouseType === 'RESERVE',
                                'label-writtenoff': w.warehouseType === 'WRITTENOFF'
                            }" class="warehouse-type-label">
                                {{w.warehouseType}}
                            </span>
                        </td>
                    </tr>
                    <tr ng-show="!loading && $data.length === 0">
                        <td colspan="2" style="text-align:center;padding:24px;" class="muted">No records found.</td>
                    </tr>
                    </tbody>
                </table>
            </div><!-- /data-card -->

        </div>
    </div>
</main>

<style>
    .warehouse-type-label {
        display: inline-block;
        padding: 2px 10px;
        border-radius: 10px;
        font-size: 11px;
        font-weight: 600;
        letter-spacing: .4px;
        text-transform: uppercase;
    }
    .label-base       { background: #e0f0ff; color: #1a5fa8; }
    .label-reserve    { background: #e6f9f0; color: #1a6640; }
    .label-writtenoff { background: #fff0f0; color: #8b1a1a; }
</style>

<script type="text/javascript">
    app.controller("warehouseController", function ($scope, $filter, $http, NgTableParams) {
        $scope.allData    = [];
        $scope.filtered   = [];
        $scope.searchtext = "";
        $scope.loading    = true;

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

        $http.get("${contextPath}/tmw/dict/getAllWarehouses").then(function (res) {
            $scope.allData = res.data || [];
            $scope.loading = false;
            $scope.tableParams.reload();
        });
    });
</script>

<#include "*/footer.ftl"/>

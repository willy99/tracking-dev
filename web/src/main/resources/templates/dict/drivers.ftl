<#assign top_nav_selected = "driverManagement">
<#assign page_title = "Drivers">
<#include "*/header.ftl"/>

<main class="page" ng-controller="driverController">
    <div class="spinner" ng-show="loading"></div>

    <div class="block main-block">
        <div class="content">
            <h1>Drivers</h1>

            <fieldset class="mgmt-filter-set">
                <legend><i class="fa fa-search"></i> Search</legend>
                <input type="search" ng-model="searchtext"
                       placeholder="First name, last name…"
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
                    <span><i class="fa fa-id-card-o"></i> Drivers</span>
                    <span style="font-weight:400;opacity:.75;text-transform:none;font-size:12px;">
                        {{filtered.length}} records
                    </span>
                </div>
                <table class="common-table dict-table" ng-table="tableParams">
                    <thead>
                    <tr>
                        <th><span class="title">First Name</span></th>
                        <th><span class="title">Last Name</span></th>
                        <th><span class="title">Tractor #</span></th>
                        <th><span class="title">Trailer #</span></th>
                        <th><span class="title">Mobile</span></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="d in $data">
                        <td><span ng-bind-html="d.firstName | highlight:searchtext | transliterate | trusted"></span></td>
                        <td><span ng-bind-html="d.lastName  | highlight:searchtext | transliterate | trusted"></span></td>
                        <td>{{d.tractorNumber || '—'}}</td>
                        <td>{{d.trailerNumber || '—'}}</td>
                        <td>{{d.mobile        || '—'}}</td>
                    </tr>
                    <tr ng-show="!loading && $data.length === 0">
                        <td colspan="5" style="text-align:center;padding:24px;" class="muted">No records found.</td>
                    </tr>
                    </tbody>
                </table>
            </div><!-- /data-card -->

        </div>
    </div>
</main>

<script type="text/javascript">
    app.controller("driverController", function ($scope, $filter, $http, NgTableParams) {
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

        $http.get("${contextPath}/tmw/dict/getAllDrivers").then(function (res) {
            $scope.allData = res.data || [];
            $scope.loading = false;
            $scope.tableParams.reload();
        });
    });
</script>

<#include "*/footer.ftl"/>

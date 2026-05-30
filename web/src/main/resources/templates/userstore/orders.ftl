<#assign top_nav_selected = "trackingOrdersManagement">
<#assign page_title = "Flex Order Management">
<#include "*/header.ftl"/>

<main class="page" ng-app="TrackingOrderManagement">
    <div class="spinner" ng-show="loading"></div>

    <div class="block main-block">
        <div class="content">
            <h1>Flex Order Management</h1>

            <div ng-controller="flexOrderController">

                <!-- ============ FILTER BAR ============ -->
                <div class="top-menu">
                    <label class="filter-label">Order #</label>
                    <input type="text" class="input-medium" placeholder="Order number"
                           ng-model="filter.searchQuery" ng-keyup="$event.keyCode == 13 && search()">

                    <label class="filter-label">From</label>
                    <input type="date" class="input-medium" ng-model="filter.dateFrom">

                    <label class="filter-label">To</label>
                    <input type="date" class="input-medium" ng-model="filter.dateTo">

                    <label class="filter-label">Type</label>
                    <select class="input-medium" ng-model="filter.orderType">
                        <option value="">All types</option>
                        <option value="IMPORT">Import</option>
                        <option value="EXPORT">Export</option>
                        <option value="MOUNT">Mount</option>
                    </select>

                    <button class="button button-blue" type="button" ng-click="search()">Filter</button>
                    <button class="button button-gray" type="button" ng-click="clearFilters()">Clear</button>
                    <img src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading"/>
                </div>

                <span class="text-error" ng-show="errorMessage">{{errorMessage}}</span>

                <!-- ============ SUMMARY ============ -->
                <p class="hint" ng-show="!loading && orders.length > 0">
                    Showing <strong>{{visibleOrders().length}}</strong>
                    of <strong>{{orders.length}}</strong> orders
                    <span ng-show="orders.length >= 500"> (result capped at 500 — narrow the date range)</span>
                </p>

                <!-- ============ TABLE ============ -->
                <table class="common-table">
                    <thead>
                    <tr>
                        <th ng-click="setSort('orderNumber')">
                            <span class="title">Order Number</span><div class="arrow" ng-show="sortField=='orderNumber'" ng-class="{'arrow-down': sortReverse}"></div>
                        </th>
                        <th ng-click="setSort('orderType')">
                            <span class="title">Type</span><div class="arrow" ng-show="sortField=='orderType'" ng-class="{'arrow-down': sortReverse}"></div>
                        </th>
                        <th ng-click="setSort('status')">
                            <span class="title">Status</span><div class="arrow" ng-show="sortField=='status'" ng-class="{'arrow-down': sortReverse}"></div>
                        </th>
                        <th ng-click="setSort('processedFlexQty')" title="Flexes attached (Export/Mount) or imported (Import)">
                            <span class="title">Flexes</span><div class="arrow" ng-show="sortField=='processedFlexQty'" ng-class="{'arrow-down': sortReverse}"></div>
                        </th>
                        <th><span class="title">Progress</span></th>
                        <th ng-click="setSort('createdDate')">
                            <span class="title">Order Date</span><div class="arrow" ng-show="sortField=='createdDate'" ng-class="{'arrow-down': sortReverse}"></div>
                        </th>
                        <th ng-click="setSort('executionDate')">
                            <span class="title">Execution Date</span><div class="arrow" ng-show="sortField=='executionDate'" ng-class="{'arrow-down': sortReverse}"></div>
                        </th>
                        <th ng-click="setSort('updatedDate')">
                            <span class="title">Last Updated</span><div class="arrow" ng-show="sortField=='updatedDate'" ng-class="{'arrow-down': sortReverse}"></div>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="o in visibleOrders()">
                        <td><strong>{{o.orderNumber}}</strong></td>
                        <td>
                            <span class="label"
                                  ng-class="{'label-info': o.orderType=='IMPORT',
                                             'label-success': o.orderType=='EXPORT',
                                             'label-warning': o.orderType=='MOUNT'}">
                                {{o.orderType}}
                            </span>
                        </td>
                        <td>
                            <span class="label"
                                  ng-class="{'label-important': o.status=='CANCELLED',
                                             'label-success': o.status=='COMPLETED',
                                             'label-info': o.status=='IN_PROGRESS'}">
                                {{o.status}}
                            </span>
                        </td>
                        <td>
                            <strong>{{o.processedFlexQty || 0}}</strong>
                            <span class="muted" ng-show="o.flexQty"> / {{o.flexQty}}</span>
                            <br>
                            <small class="muted">
                                {{o.orderType == 'IMPORT' ? 'imported' : 'attached'}}
                            </small>
                        </td>
                        <td style="min-width: 90px;">
                            <div class="progress" style="margin: 0;" ng-show="o.flexQty > 0">
                                <div class="bar"
                                     ng-class="{'bar-success': progress(o) >= 100}"
                                     style="width: {{progress(o)}}%;"></div>
                            </div>
                            <span ng-show="o.flexQty > 0" class="muted">{{progress(o)}}%</span>
                            <span ng-show="!o.flexQty" class="muted">&mdash;</span>
                        </td>
                        <td>{{o.createdDate | date:'yyyy-MM-dd'}}</td>
                        <td>{{o.executionDate | date:'yyyy-MM-dd'}}</td>
                        <td>{{o.updatedDate | date:'yyyy-MM-dd HH:mm'}}</td>
                    </tr>
                    <tr ng-show="!loading && visibleOrders().length === 0">
                        <td colspan="8" class="muted" style="text-align:center; padding: 24px;">
                            No flex orders found for the selected filters.
                        </td>
                    </tr>
                    </tbody>
                </table>

            </div>
        </div>
    </div>
</main>

<style>
    .filter-label { display: inline-block; margin: 0 4px 0 10px; font-weight: bold; }
    .filter-label:first-child { margin-left: 0; }
    .common-table th { cursor: pointer; white-space: nowrap; }
    .hint { margin: 0 0 8px 2px; color: #888; }
</style>

<script type="text/javascript">

    /* NOTE: confirm this base path matches how FlexController is mounted in your
       Jersey config. The existing page used '${contextPath}/tmw/order', so flex
       endpoints are most likely under '${contextPath}/tmw/flex'. If your REST
       base differs (e.g. '/webresources/flex'), change FLEX_API below only. */
    var FLEX_API = '${contextPath}/tmw/flex';

    app.controller("flexOrderController", function ($scope, $filter, $http) {

        $scope.orders = [];
        $scope.loading = false;
        $scope.errorMessage = "";

        $scope.filter = { searchQuery: "", dateFrom: "", dateTo: "", orderType: "" };

        $scope.sortField = "createdDate";
        $scope.sortReverse = true;

        $scope.search = function () {
            $scope.loading = true;
            $scope.errorMessage = "";

            var payload = {
                searchQuery: $scope.filter.searchQuery || null,
                dateFrom:    $scope.filter.dateFrom    || null,
                dateTo:      $scope.filter.dateTo      || null
            };

            $http.post(FLEX_API + "/getAllFlexOrders", payload).then(function (res) {
                $scope.loading = false;
                $scope.orders = res.data || [];
            }, function () {
                $scope.loading = false;
                $scope.orders = [];
                $scope.errorMessage = "Failed to load flex orders. Please try again.";
            });
        };

        $scope.clearFilters = function () {
            $scope.filter = { searchQuery: "", dateFrom: "", dateTo: "", orderType: "" };
            $scope.search();
        };

        // client-side type filter + sorting on top of the server result
        $scope.visibleOrders = function () {
            var list = $scope.orders;
            if ($scope.filter.orderType) {
                list = list.filter(function (o) { return o.orderType === $scope.filter.orderType; });
            }
            return $filter('orderBy')(list, $scope.sortField, $scope.sortReverse);
        };

        $scope.setSort = function (field) {
            if ($scope.sortField === field) {
                $scope.sortReverse = !$scope.sortReverse;
            } else {
                $scope.sortField = field;
                $scope.sortReverse = false;
            }
        };

        $scope.progress = function (o) {
            if (!o.flexQty || o.flexQty <= 0) return 0;
            return Math.min(100, Math.round((o.processedFlexQty / o.flexQty) * 100));
        };

        // default window: last 30 days
        var today = new Date();
        var from = new Date(today.getTime() - 30 * 24 * 60 * 60 * 1000);
        $scope.filter.dateTo   = today.toISOString().slice(0, 10);
        $scope.filter.dateFrom = from.toISOString().slice(0, 10);

        $scope.search();
    });

</script>
<#include "*/footer.ftl"/>

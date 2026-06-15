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
                    <select class="input-medium" ng-model="filter.typeFilter">
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
                <p class="hint" ng-show="!loading && paged.totalItems > 0">
                    Showing <strong>{{paged.items.length}}</strong>
                    of <strong>{{paged.totalItems}}</strong> orders
                    &nbsp;|&nbsp; Page <strong>{{paged.currentPage}}</strong> of <strong>{{paged.totalPages}}</strong>
                </p>

                <!-- ============ TABLE ============ -->
                <table class="common-table orders-table">
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
                    <tr ng-repeat="o in paged.items">
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
                            <a href="${contextPath}/tmw/flex/flexDetail?orderNum={{o.orderNumber}}&orderType={{o.orderType}}">
                                <strong>{{o.processedFlexQty || 0}}</strong>
                            </a>
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
                    <tr ng-show="!loading && (!paged.items || paged.items.length === 0)">
                        <td colspan="8" class="muted" style="text-align:center; padding: 24px;">
                            No flex orders found for the selected filters.
                        </td>
                    </tr>
                    </tbody>
                </table>

                <!-- ============ PAGINATION BAR ============ -->
                <div class="pagination-bar" ng-show="paged.totalPages > 1">
                    <button class="button button-gray pager-btn" ng-click="goToPage(1)" ng-disabled="paged.currentPage === 1">&laquo;</button>
                    <button class="button button-gray pager-btn" ng-click="goToPage(paged.currentPage - 1)" ng-disabled="paged.currentPage === 1">&lsaquo;</button>

                    <span class="pager-pages">
                        <button class="button pager-btn"
                                ng-repeat="p in pageNumbers()"
                                ng-click="goToPage(p)"
                                ng-class="{'button-blue': p === paged.currentPage, 'button-gray': p !== paged.currentPage}">
                            {{p}}
                        </button>
                    </span>

                    <button class="button button-gray pager-btn" ng-click="goToPage(paged.currentPage + 1)" ng-disabled="paged.currentPage === paged.totalPages">&rsaquo;</button>
                    <button class="button button-gray pager-btn" ng-click="goToPage(paged.totalPages)" ng-disabled="paged.currentPage === paged.totalPages">&raquo;</button>
                </div>

            </div>
        </div>
    </div>
</main>

<style>
    .block.main-block { width: 95%; max-width: none; margin: 0 auto; }
    .orders-table { width: 100%; }
    .filter-label { display: inline-block; margin: 0 4px 0 10px; font-weight: bold; }
    .filter-label:first-child { margin-left: 0; }
    .common-table th { cursor: pointer; white-space: nowrap; }
    .hint { margin: 0 0 8px 2px; color: #888; }
    .pagination-bar { margin-top: 12px; display: flex; align-items: center; gap: 4px; }
    .pager-btn { min-width: 32px; padding: 2px 8px; }
    .pager-pages { display: flex; gap: 4px; }
</style>

<script type="text/javascript">

    var FLEX_API = '${contextPath}/tmw/flex';

    app.controller("flexOrderController", function ($scope, $http) {

        $scope.paged   = { items: [], totalItems: 0, totalPages: 1, pageSize: 10, currentPage: 1 };
        $scope.loading = false;
        $scope.errorMessage = "";

        $scope.filter    = { searchQuery: "", dateFrom: "", dateTo: "", typeFilter: "" };
        $scope.sortField = "createdDate";
        $scope.sortReverse = true;

        $scope.fetch = function (page) {
            $scope.loading = true;
            $scope.errorMessage = "";

            var payload = {
                searchQuery: $scope.filter.searchQuery || null,
                dateFrom:    $scope.filter.dateFrom    || null,
                dateTo:      $scope.filter.dateTo      || null,
                typeFilter:  $scope.filter.typeFilter  || null,
                page:        page || 1,
                pageSize:    $scope.paged.pageSize,
                sortField:   $scope.sortField,
                sortDir:     $scope.sortReverse ? "desc" : "asc"
            };

            $http.post(FLEX_API + "/getAllFlexOrders", payload).then(function (res) {
                $scope.loading = false;
                $scope.paged   = res.data || $scope.paged;
            }, function () {
                $scope.loading = false;
                $scope.errorMessage = "Failed to load flex orders. Please try again.";
            });
        };

        // Filter button — always go back to page 1
        $scope.search = function () { $scope.fetch(1); };

        $scope.clearFilters = function () {
            $scope.filter = { searchQuery: "", dateFrom: "", dateTo: "", typeFilter: "" };
            $scope.fetch(1);
        };

        $scope.goToPage = function (p) {
            if (p < 1 || p > $scope.paged.totalPages) return;
            $scope.fetch(p);
        };

        $scope.setSort = function (field) {
            if ($scope.sortField === field) {
                $scope.sortReverse = !$scope.sortReverse;
            } else {
                $scope.sortField = field;
                $scope.sortReverse = false;
            }
            $scope.fetch($scope.paged.currentPage);
        };

        // Show at most 7 page buttons; always include first, last, and current ± 2
        $scope.pageNumbers = function () {
            var total = $scope.paged.totalPages, cur = $scope.paged.currentPage;
            var pages = [], i;
            for (i = 1; i <= total; i++) {
                if (i === 1 || i === total || (i >= cur - 2 && i <= cur + 2)) {
                    pages.push(i);
                }
            }
            return pages;
        };

        $scope.progress = function (o) {
            if (!o.flexQty || o.flexQty <= 0) return 0;
            return Math.min(100, Math.round((o.processedFlexQty / o.flexQty) * 100));
        };

        // default window: last 30 days
        var today = new Date();
        var from  = new Date(today.getTime() - 30 * 24 * 60 * 60 * 1000);
        $scope.filter.dateTo   = today.toISOString().slice(0, 10);
        $scope.filter.dateFrom = from.toISOString().slice(0, 10);

        $scope.fetch(1);
    });

</script>
<#include "*/footer.ftl"/>

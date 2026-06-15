<#assign top_nav_selected = "trackingOrdersManagement">
<#assign page_title = "Flex Detail">
<#include "*/header.ftl"/>

<main class="page" ng-app="FlexDetailApp">
    <div class="spinner" ng-show="loading"></div>

    <div class="block main-block">
        <div class="content">

            <div ng-controller="flexDetailController">

                <!-- ============ BACK + TITLE ============ -->
                <div style="display:flex; align-items:baseline; gap:16px; margin-bottom:12px;">
                    <a href="${contextPath}/tmw/flex/flexDetail/../orders" class="button button-gray">&larr; Back to Orders</a>
                    <h1 style="margin:0;">
                        Flexes for order <strong>{{orderNum}}</strong>
                        <span class="label"
                              ng-class="{'label-info': orderType=='IMPORT',
                                         'label-success': orderType=='EXPORT',
                                         'label-warning': orderType=='MOUNT'}">
                            {{orderType}}
                        </span>
                    </h1>
                </div>

                <!-- ============ FILTER BAR ============ -->
                <div class="top-menu">
                    <label class="filter-label">Serial #</label>
                    <input type="text" class="input-medium" placeholder="Serial number"
                           ng-model="filter.searchQuery"
                           ng-keyup="$event.keyCode == 13 && search()">

                    <button class="button button-blue" type="button" ng-click="search()">Filter</button>
                    <button class="button button-gray" type="button" ng-click="clearFilters()">Clear</button>
                    <img src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading"/>
                </div>

                <span class="text-error" ng-show="errorMessage">{{errorMessage}}</span>

                <!-- ============ SUMMARY ============ -->
                <p class="hint" ng-show="!loading && paged.totalItems > 0">
                    Showing <strong>{{paged.items.length}}</strong>
                    of <strong>{{paged.totalItems}}</strong> flexes
                    &nbsp;|&nbsp; Page <strong>{{paged.currentPage}}</strong> of <strong>{{paged.totalPages}}</strong>
                </p>

                <!-- ============ TABLE ============ -->
                <table class="common-table flex-detail-table">
                    <thead>
                    <tr>
                        <th ng-click="setSort('serialNumber')">
                            <span class="title">Serial Number</span>
                            <div class="arrow" ng-show="sortField=='serialNumber'" ng-class="{'arrow-down': sortReverse}"></div>
                        </th>
                        <th ng-show="orderType=='IMPORT'">
                            <span class="title">Import Container</span>
                        </th>
                        <th ng-show="orderType=='MOUNT'">
                            <span class="title">Mount Container</span>
                        </th>
                        <th><span class="title">Warehouse</span></th>
                        <th ng-click="setSort('importDate')">
                            <span class="title">Import Date</span>
                            <div class="arrow" ng-show="sortField=='importDate'" ng-class="{'arrow-down': sortReverse}"></div>
                        </th>
                        <th ng-show="orderType!='IMPORT'" ng-click="setSort('exportDate')">
                            <span class="title">Export Date</span>
                            <div class="arrow" ng-show="sortField=='exportDate'" ng-class="{'arrow-down': sortReverse}"></div>
                        </th>
                        <th ng-show="orderType=='MOUNT'" ng-click="setSort('mountDate')">
                            <span class="title">Mount Date</span>
                            <div class="arrow" ng-show="sortField=='mountDate'" ng-class="{'arrow-down': sortReverse}"></div>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="f in paged.items">
                        <td><strong>{{f.serialNumber}}</strong></td>
                        <td ng-show="orderType=='IMPORT'">{{f.importContainerNumber || '—'}}</td>
                        <td ng-show="orderType=='MOUNT'">{{f.mountContainerNumber || '—'}}</td>
                        <td>{{f.warehouse ? f.warehouse.name : '—'}}</td>
                        <td>{{f.importDate | date:'yyyy-MM-dd'}}</td>
                        <td ng-show="orderType!='IMPORT'">{{f.exportDate | date:'yyyy-MM-dd'}}</td>
                        <td ng-show="orderType=='MOUNT'">{{f.mountDate | date:'yyyy-MM-dd'}}</td>
                    </tr>
                    <tr ng-show="!loading && (!paged.items || paged.items.length === 0)">
                        <td colspan="6" class="muted" style="text-align:center; padding:24px;">
                            No flexes found for this order.
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
    .flex-detail-table { width: 100%; }
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

    // Read orderNum + orderType from URL query string
    function getUrlParam(name) {
        var match = new RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
        return match ? decodeURIComponent(match[1].replace(/\+/g, ' ')) : null;
    }

    app.controller("flexDetailController", function ($scope, $http) {

        $scope.orderNum  = getUrlParam('orderNum')  || '';
        $scope.orderType = getUrlParam('orderType') || '';

        $scope.paged     = { items: [], totalItems: 0, totalPages: 1, pageSize: 10, currentPage: 1 };
        $scope.loading   = false;
        $scope.errorMessage = '';

        $scope.filter    = { searchQuery: '' };
        $scope.sortField = 'serialNumber';
        $scope.sortReverse = false;

        $scope.fetch = function (page) {
            $scope.loading = true;
            $scope.errorMessage = '';

            var payload = {
                orderNum:  $scope.orderNum,
                orderType: $scope.orderType,
                filter: {
                    searchQuery: $scope.filter.searchQuery || null,
                    page:        page || 1,
                    pageSize:    $scope.paged.pageSize,
                    sortField:   $scope.sortField,
                    sortDir:     $scope.sortReverse ? 'desc' : 'asc'
                }
            };

            $http.post(FLEX_API + '/getFlexesByOrder', payload).then(function (res) {
                $scope.loading = false;
                $scope.paged   = res.data || $scope.paged;
            }, function () {
                $scope.loading = false;
                $scope.errorMessage = 'Failed to load flexes. Please try again.';
            });
        };

        $scope.search = function () { $scope.fetch(1); };

        $scope.clearFilters = function () {
            $scope.filter = { searchQuery: '' };
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
                $scope.sortField  = field;
                $scope.sortReverse = false;
            }
            $scope.fetch($scope.paged.currentPage);
        };

        $scope.pageNumbers = function () {
            var total = $scope.paged.totalPages, cur = $scope.paged.currentPage, pages = [], i;
            for (i = 1; i <= total; i++) {
                if (i === 1 || i === total || (i >= cur - 2 && i <= cur + 2)) pages.push(i);
            }
            return pages;
        };

        // auto-load on page open
        if ($scope.orderNum) { $scope.fetch(1); }
    });

</script>
<#include "*/footer.ftl"/>

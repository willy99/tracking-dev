<#assign top_nav_selected = "trackingOrdersManagement">
<#assign page_title = "Flex Order Management">
<#include "*/header.ftl"/>

<main class="page" ng-app="TrackingOrderManagement">
    <div class="spinner" ng-show="loading"></div>

    <div class="block main-block">
        <div class="content">
            <h1>Flex Order Management</h1>

            <div ng-controller="flexOrderController">

                <!-- ============ TOAST NOTIFICATIONS ============ -->
                <div class="orders-toast-wrap" ng-show="toast.visible" ng-class="'orders-toast--' + toast.type">
                    <span class="orders-toast-icon">
                        <i class="fa" ng-class="{'fa-check-circle': toast.type==='success', 'fa-exclamation-circle': toast.type==='error', 'fa-info-circle': toast.type==='info'}"></i>
                    </span>
                    <span class="orders-toast-msg">{{toast.message}}</span>
                    <button class="orders-toast-close" ng-click="toast.visible=false">&times;</button>
                </div>

                <!-- ============ FILTER FIELDSET ============ -->
                <fieldset class="orders-filter-set">
                    <legend class="orders-filter-legend">
                        <i class="fa fa-filter"></i> Filter Orders
                    </legend>
                    <div class="orders-filter-row">
                        <div class="orders-filter-group">
                            <label class="orders-filter-label">Order #</label>
                            <input type="text" class="orders-filter-input" placeholder="e.g. EXP-0042"
                                   ng-model="filter.searchQuery" ng-keyup="$event.keyCode == 13 && search()">
                        </div>
                        <div class="orders-filter-group">
                            <label class="orders-filter-label">From</label>
                            <input type="date" class="orders-filter-input" ng-model="filter.dateFrom">
                        </div>
                        <div class="orders-filter-group">
                            <label class="orders-filter-label">To</label>
                            <input type="date" class="orders-filter-input" ng-model="filter.dateTo">
                        </div>
                        <div class="orders-filter-group">
                            <label class="orders-filter-label">Type</label>
                            <select class="orders-filter-input" ng-model="filter.typeFilter">
                                <option value="">All types</option>
                                <option value="IMPORT">Import</option>
                                <option value="EXPORT">Export</option>
                                <option value="MOUNT">Mount</option>
                            </select>
                        </div>
                        <div class="orders-filter-actions">
                            <button class="button button-blue orders-btn-search" type="button" ng-click="search()">
                                <i class="fa fa-search"></i> Search
                            </button>
                            <button class="button button-gray" type="button" ng-click="clearFilters()">
                                <i class="fa fa-times"></i> Clear
                            </button>
                            <img src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading" style="height:20px;vertical-align:middle;margin-left:6px;"/>
                        </div>
                    </div>
                </fieldset>

                <!-- ============ TABLE CARD ============ -->
                <div class="orders-card">
                    <div class="orders-card-header">
                        <span><i class="fa fa-list-alt"></i> Orders</span>
                        <span class="orders-card-count" ng-show="!loading && paged.totalItems > 0">
                            {{paged.items.length}} of {{paged.totalItems}} &nbsp;&middot;&nbsp; Page {{paged.currentPage}} / {{paged.totalPages}}
                        </span>
                        <span class="text-error orders-card-error" ng-show="errorMessage">{{errorMessage}}</span>
                    </div>

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
                        <th><span class="title">Actions</span></th>
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
                        <td style="white-space:nowrap;">
                            <button class="button button-gray btn-recalc"
                                    title="Recalculate Status"
                                    ng-disabled="o._recalculating"
                                    ng-click="recalculate(o)">
                                <i ng-show="!o._recalculating" class="fa fa-repeat"></i>
                                <i ng-show="o._recalculating"  class="fa fa-refresh fa-spin"></i>
                            </button>
                        </td>
                    </tr>
                    <tr ng-show="!loading && (!paged.items || paged.items.length === 0)">
                        <td colspan="9" class="muted" style="text-align:center; padding: 24px;">
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

                </div><!-- /orders-card -->

            </div>
        </div>
    </div>
</main>

<style>
    /* ── Layout ── */
    .block.main-block { width: 95%; max-width: none; margin: 0 auto; }

    /* ── Filter fieldset ── */
    .orders-filter-set {
        border: 1px solid #d0d7e2;
        border-radius: 8px;
        padding: 16px 20px 14px;
        margin-bottom: 22px;
        background: #f7f9fb;
    }
    .orders-filter-legend {
        font-size: 12px;
        font-weight: 700;
        letter-spacing: 1px;
        text-transform: uppercase;
        color: #384352;
        padding: 0 8px;
    }
    .orders-filter-row {
        display: flex;
        flex-wrap: wrap;
        align-items: flex-end;
        gap: 14px;
    }
    .orders-filter-group {
        display: flex;
        flex-direction: column;
        gap: 4px;
        min-width: 140px;
    }
    .orders-filter-label {
        font-size: 11px;
        font-weight: 600;
        color: #586069;
        text-transform: uppercase;
        letter-spacing: .6px;
        margin: 0;
    }
    .orders-filter-input {
        padding: 6px 10px;
        font-size: 13px;
        border: 1px solid #c8d0da;
        border-radius: 5px;
        background: #fff;
        outline: none;
        transition: border-color .2s, box-shadow .2s;
        height: 32px;
        box-sizing: border-box;
    }
    .orders-filter-input:focus {
        border-color: #384352;
        box-shadow: 0 0 0 3px rgba(56,67,82,.12);
    }
    .orders-filter-actions {
        display: flex;
        align-items: center;
        gap: 8px;
        padding-top: 2px;
    }

    /* ── Table card ── */
    .orders-card {
        background: #fff;
        border: 1px solid #d0d7e2;
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(56,67,82,.07);
    }
    .orders-card-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        background: #384352;
        color: #fff;
        padding: 11px 18px;
        font-size: 13px;
        font-weight: 700;
        letter-spacing: .5px;
        text-transform: uppercase;
    }
    .orders-card-count {
        font-size: 12px;
        font-weight: 400;
        opacity: .75;
        text-transform: none;
        letter-spacing: 0;
    }
    .orders-card-error {
        font-size: 12px;
        font-weight: 400;
        color: #ff6b6b;
        text-transform: none;
        letter-spacing: 0;
    }

    /* ── Table ── */
    .orders-table { width: 100%; border-collapse: collapse; }
    .orders-table thead tr th {
        background: #4a5568;
        color: #fff;
        font-size: 11px;
        font-weight: 700;
        letter-spacing: .8px;
        text-transform: uppercase;
        padding: 10px 12px;
        white-space: nowrap;
        cursor: pointer;
        border-bottom: none;
        user-select: none;
    }
    .orders-table thead tr th:hover { background: #5a6478; }
    .orders-table thead tr th .arrow {
        display: inline-block;
        width: 0; height: 0;
        margin-left: 5px;
        vertical-align: middle;
        border-left: 4px solid transparent;
        border-right: 4px solid transparent;
        border-bottom: 6px solid rgba(255,255,255,.7);
    }
    .orders-table thead tr th .arrow.arrow-down {
        border-bottom: none;
        border-top: 6px solid rgba(255,255,255,.7);
    }
    .orders-table tbody tr td {
        padding: 9px 12px;
        border-bottom: 1px solid #edf0f4;
        font-size: 13px;
        vertical-align: middle;
    }
    .orders-table tbody tr:last-child td { border-bottom: none; }
    .orders-table tbody tr:hover { background: #f4f7fb; }
    .orders-table tbody tr:nth-child(even) { background: #fafbfc; }
    .orders-table tbody tr:nth-child(even):hover { background: #f4f7fb; }

    /* ── Pagination ── */
    .pagination-bar { padding: 10px 14px; display: flex; align-items: center; gap: 4px; border-top: 1px solid #edf0f4; }
    .pager-btn { min-width: 32px; padding: 2px 8px; }
    .pager-pages { display: flex; gap: 4px; }

    /* ── Recalc button ── */
    .btn-recalc { padding: 3px 8px; font-size: 14px; }

    /* ── Toast notifications ── */
    .orders-toast-wrap {
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 12px 16px;
        border-radius: 7px;
        margin-bottom: 14px;
        font-size: 13px;
        font-weight: 500;
        box-shadow: 0 3px 10px rgba(0,0,0,.12);
        animation: toast-in .2s ease;
    }
    @keyframes toast-in {
        from { opacity: 0; transform: translateY(-6px); }
        to   { opacity: 1; transform: translateY(0); }
    }
    .orders-toast--success { background: #e6f9f0; border: 1px solid #6fcf97; color: #1a6640; }
    .orders-toast--error   { background: #fff0f0; border: 1px solid #e57373; color: #8b1a1a; }
    .orders-toast--info    { background: #edf4ff; border: 1px solid #90b8f8; color: #1a3a6e; }
    .orders-toast-icon     { font-size: 16px; flex-shrink: 0; }
    .orders-toast-msg      { flex: 1; }
    .orders-toast-close {
        background: none; border: none; cursor: pointer;
        font-size: 18px; line-height: 1; opacity: .5; padding: 0 4px;
        color: inherit;
    }
    .orders-toast-close:hover { opacity: 1; }
</style>

<script type="text/javascript">

    var FLEX_API = '${contextPath}/tmw/flex';

    app.controller("flexOrderController", function ($scope, $http) {

        $scope.paged   = { items: [], totalItems: 0, totalPages: 1, pageSize: 10, currentPage: 1 };
        $scope.loading = false;
        $scope.errorMessage = "";
        $scope.toast   = { visible: false, type: 'info', message: '' };

        var toastTimer;
        $scope.showToast = function (msg, type) {
            clearTimeout(toastTimer);
            $scope.toast = { visible: true, type: type || 'info', message: msg };
            toastTimer = setTimeout(function () {
                $scope.$apply(function () { $scope.toast.visible = false; });
            }, 5000);
        };

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

        $scope.recalculate = function (o) {
            o._recalculating = true;
            $http.post(FLEX_API + '/recalculateOrder?orderNum=' + encodeURIComponent(o.orderNumber)).then(function () {
                o._recalculating = false;
                $scope.showToast('Order ' + o.orderNumber + ' status recalculated.', 'success');
                $scope.fetch($scope.paged.currentPage);
            }, function (res) {
                o._recalculating = false;
                var msg = (res && res.data && typeof res.data === 'string') ? res.data
                        : (res && res.status === 403) ? 'Permission denied. Contact your administrator.'
                        : 'Failed to recalculate order ' + o.orderNumber + '. Please try again.';
                $scope.showToast(msg, 'error');
            });
        };

        $scope.progress = function (o) {
            if (!o.flexQty || o.flexQty <= 0) return 0;
            return Math.min(100, Math.round((o.processedFlexQty / o.flexQty) * 100));
        };

        $scope.fetch(1);
    });

</script>
<#include "*/footer.ftl"/>

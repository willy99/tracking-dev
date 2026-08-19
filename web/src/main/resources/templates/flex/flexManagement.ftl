<#assign top_nav_selected = "flexManagement">
<#assign page_title = "Flex Management">
<#include "*/header.ftl"/>

<main class="page" ng-controller="flexMgmtController">

    <div class="block main-block">
        <div class="content">
            <h1>Flex Management</h1>

            <!-- ===== FILTER ===== -->
            <fieldset class="mgmt-filter-set">
                <legend><i class="fa fa-search"></i> Filter</legend>

                <label class="filter-label">Serial #</label>
                <input type="search" ng-model="filter.serialNum"
                       placeholder="Serial number…"
                       ng-keyup="$event.keyCode == 13 && search()">

                <label class="filter-label">Container #</label>
                <input type="search" ng-model="filter.containerNum"
                       placeholder="Import or mount…"
                       ng-keyup="$event.keyCode == 13 && search()">

                <label class="filter-label">Export Order #</label>
                <input type="search" ng-model="filter.exportOrderNum"
                       placeholder="Order number…"
                       ng-keyup="$event.keyCode == 13 && search()">

                <label class="filter-label">Warehouse</label>
                <select ng-model="filter.warehouseName" ng-change="search()">
                    <option value="">— all warehouses —</option>
                    <option ng-repeat="w in warehouses" value="{{w.name}}">{{w.name}}</option>
                </select>

                <span class="filter-checkbox-group">
                    <label class="filter-checkbox-label">
                        <input type="checkbox" ng-model="filter.hasExportOrder" ng-change="search()">
                        Exported
                    </label>
                    <label class="filter-checkbox-label">
                        <input type="checkbox" ng-model="filter.hasMountContainer" ng-change="search()">
                        Mounted
                    </label>
                </span>

                <button class="button button-blue" ng-click="search()">
                    <i class="fa fa-search"></i> Search
                </button>
                <button class="button button-gray" ng-click="clearFilters()">
                    <i class="fa fa-times"></i> Clear
                </button>
                <img src="${contextPath}/img/ajax-loader.gif" alt="" ng-show="loading"
                     style="height:20px;vertical-align:middle;margin-left:8px;">
            </fieldset>

            <!-- ===== TOAST ===== -->
            <div class="orders-toast-wrap" ng-show="toast.visible" ng-class="'orders-toast--' + toast.type">
                <span>{{toast.message}}</span>
            </div>

            <!-- ===== CARD + TABLE ===== -->
            <div class="data-card" ng-show="searched">
                <div class="data-card-header">
                    <span><i class="fa fa-cubes"></i> Flex Items</span>
                    <span style="font-weight:400;opacity:.75;text-transform:none;font-size:12px;">
                        {{allData.length}} records
                    </span>
                </div>

                <table class="common-table flex-mgmt-table" ng-table="tableParams">
                    <thead>
                    <tr>
                        <th><span class="title">Serial #</span></th>
                        <th><span class="title">Warehouse</span></th>
                        <th><span class="title">Import Container</span></th>
                        <th><span class="title">Mount Container</span></th>
                        <th><span class="title">Export Order</span></th>
                        <th><span class="title">Import Date</span></th>
                        <th style="min-width:280px;"><span class="title">Actions</span></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="f in $data" ng-class="rowClass(f)">
                        <td><strong>{{f.serialNumber}}</strong></td>
                        <td>
                            <span class="wh-badge" ng-class="whBadgeClass(f)">
                                {{f.warehouse ? f.warehouse.name : '—'}}
                            </span>
                        </td>
                        <td>{{f.importContainerNumber || '—'}}</td>
                        <td>{{f.mountContainerNumber  || '—'}}</td>
                        <td>{{f.exportOrderNum        || '—'}}</td>
                        <td>{{f.importDate | date:'yyyy-MM-dd'}}</td>
                        <td class="actions-cell">

                            <!-- ── RESERVE: тільки "З резерву" і "Видалити" ─────── -->
                            <span ng-if="isReserve(f)">
                                <button class="button btn-action btn-unreserve"
                                        ng-disabled="f._busy"
                                        ng-click="doAction('unReserveFlex', f)">
                                    <i class="fa" ng-class="f._busy ? 'fa-refresh fa-spin' : 'fa-unlock'"></i>
                                    З резерву
                                </button>
                                <button class="button btn-action btn-delete"
                                        ng-disabled="f._busy"
                                        ng-click="confirmDelete(f)">
                                    <i class="fa fa-times-circle"></i> Видалити
                                </button>
                            </span>

                            <!-- ── MOUNTED → Розвантажити ─────────────────────────── -->
                            <span ng-if="!isReserve(f) && f.mountContainerNumber">
                                <button class="button btn-action btn-unload"
                                        ng-disabled="f._busy"
                                        ng-click="doAction('detachFlexFromContainer', f)">
                                    <i class="fa" ng-class="f._busy ? 'fa-refresh fa-spin' : 'fa-arrow-down'"></i>
                                    Розвантажити
                                </button>
                            </span>

                            <!-- ── FREE (no order, not reserve) ─────────────────── -->
                            <span ng-if="!isReserve(f) && !f.exportOrderNum && !f.mountContainerNumber">

                                <span ng-if="!f._attachOpen">
                                    <button class="button btn-action btn-attach"
                                            ng-disabled="f._busy"
                                            ng-click="f._attachOpen = true; f._orderInput = ''">
                                        <i class="fa fa-link"></i> До ордеру
                                    </button>
                                </span>
                                <span ng-if="f._attachOpen" class="inline-attach">
                                    <input type="text" ng-model="f._orderInput"
                                           placeholder="Номер ордеру"
                                           ng-keyup="$event.keyCode == 13 && doAttachToOrder(f);
                                                      $event.keyCode == 27 && (f._attachOpen = false)">
                                    <button class="button btn-action btn-attach"
                                            ng-disabled="f._busy || !f._orderInput"
                                            ng-click="doAttachToOrder(f)">
                                        <i class="fa fa-check"></i>
                                    </button>
                                    <button class="button button-gray btn-action"
                                            ng-click="f._attachOpen = false">
                                        <i class="fa fa-times"></i>
                                    </button>
                                </span>

                                <button class="button btn-action btn-reserve"
                                        ng-if="!isWrittenOff(f)"
                                        ng-disabled="f._busy"
                                        ng-click="doAction('reserveFlex', f)">
                                    <i class="fa fa-lock"></i> В резерв
                                </button>

                                <button class="button btn-action btn-writeoff"
                                        ng-if="!isWrittenOff(f)"
                                        ng-disabled="f._busy"
                                        ng-click="doAction('writeOffFlex', f)">
                                    <i class="fa fa-trash-o"></i> Списати
                                </button>
                            </span>

                            <!-- ── ON ORDER, not mounted ─────────────────────────── -->
                            <span ng-if="!isReserve(f) && f.exportOrderNum && !f.mountContainerNumber">
                                <button class="button btn-action btn-detach-order"
                                        ng-disabled="f._busy"
                                        ng-click="doAction('detachFlexFromOrder', f)">
                                    <i class="fa" ng-class="f._busy ? 'fa-refresh fa-spin' : 'fa-chain-broken'"></i>
                                    Від'єднати від ордеру
                                </button>
                                <button class="button btn-action btn-writeoff"
                                        ng-disabled="f._busy"
                                        ng-click="doAction('writeOffFlex', f)">
                                    <i class="fa fa-trash-o"></i> Списати
                                </button>
                            </span>

                        </td>
                    </tr>
                    <tr ng-show="searched && !loading && allData.length === 0">
                        <td colspan="7" style="text-align:center;padding:24px;" class="muted">
                            No records found.
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div><!-- /data-card -->

            <p ng-if="!searched" class="muted" style="padding:16px 2px;">
                Заповніть фільтр і натисніть <strong>Search</strong>.
            </p>

        </div>
    </div>
</main>

<style>
    .filter-label { display:inline-block; margin:0 4px 0 12px; font-weight:600; font-size:12px; }
    .filter-checkbox-group { display:inline-flex; align-items:center; gap:12px; margin:0 6px 0 14px; }
    .filter-checkbox-label { display:inline-flex; align-items:center; gap:4px; font-size:12px; font-weight:600; cursor:pointer; user-select:none; }
    .filter-checkbox-label input[type=checkbox] { margin:0; cursor:pointer; }

    tr.row-writtenoff td { opacity:.5; }
    tr.row-reserve td:first-child  { border-left:3px solid #27ae60; }
    tr.row-mounted td:first-child  { border-left:3px solid #2980b9; }

    .wh-badge { display:inline-block; padding:2px 8px; border-radius:10px; font-size:11px; font-weight:600; text-transform:uppercase; }
    .wh-base       { background:#e0f0ff; color:#1a5fa8; }
    .wh-reserve    { background:#e6f9f0; color:#1a6640; }
    .wh-writtenoff { background:#f0f0f0; color:#888; }

    .actions-cell { white-space:nowrap; }
    .btn-action   { margin:0 3px 2px 0; font-size:11px; padding:3px 8px; }
    .btn-unload       { background:#2980b9; color:#fff; }
    .btn-unload:hover { background:#1a6fa8; }
    .btn-attach       { background:#27ae60; color:#fff; }
    .btn-attach:hover { background:#219150; }
    .btn-reserve      { background:#8e44ad; color:#fff; }
    .btn-reserve:hover{ background:#6c3483; }
    .btn-unreserve      { background:#f39c12; color:#fff; }
    .btn-unreserve:hover{ background:#d68910; }
    .btn-detach-order      { background:#e67e22; color:#fff; }
    .btn-detach-order:hover{ background:#ca6f1e; }
    .btn-writeoff      { background:#c0392b; color:#fff; }
    .btn-writeoff:hover{ background:#a93226; }
    .btn-delete        { background:#922b21; color:#fff; }
    .btn-delete:hover  { background:#7b241c; }
    .inline-attach input { height:28px; padding:2px 6px; border:1px solid #ccc; border-radius:3px; vertical-align:middle; width:110px; }

    .orders-toast-wrap { margin:8px 0 12px; padding:10px 16px; border-radius:6px; font-size:13px; }
    .orders-toast--success { background:#e6f9f0; border:1px solid #6fcf97; color:#1a6640; }
    .orders-toast--error   { background:#fff0f0; border:1px solid #e57373; color:#8b1a1a; }
    .orders-toast--info    { background:#edf4ff; border:1px solid #90b8f8; color:#1a3a6e; }
</style>

<script type="text/javascript">
    var FLEX_API = '${contextPath}/tmw/flex';

    app.controller('flexMgmtController', function ($scope, $filter, $http, NgTableParams) {

        $scope.filter     = { serialNum: '', containerNum: '', exportOrderNum: '', warehouseName: '', hasExportOrder: false, hasMountContainer: false };
        $scope.warehouses = [];
        $scope.allData    = [];
        $scope.loading    = false;
        $scope.searched   = false;
        $scope.toast      = { visible: false, type: 'info', message: '' };

        var toastTimer;
        $scope.showToast = function (msg, type) {
            clearTimeout(toastTimer);
            $scope.toast = { visible: true, type: type || 'info', message: msg };
            toastTimer = setTimeout(function () {
                $scope.$apply(function () { $scope.toast.visible = false; });
            }, 5000);
        };

        $http.get('${contextPath}/tmw/dict/getAllWarehouses').then(function (res) {
            $scope.warehouses = res.data || [];
        });

        $scope.tableParams = new NgTableParams({ count: ${pageSize} }, {
            getData: function ($defer, params) {
                params.total($scope.allData.length);
                var start = (params.page() - 1) * params.count();
                $defer.resolve($scope.allData.slice(start, start + params.count()));
            }
        });

        $scope.search = function () {
            $scope.loading  = true;
            $scope.searched = true;
            var body = {
                serialNum:        $scope.filter.serialNum      || null,
                containerNum:     $scope.filter.containerNum   || null,
                exportOrderNum:   $scope.filter.exportOrderNum || null,
                warehouseName:    $scope.filter.warehouseName  || null,
                hasExportOrder:   $scope.filter.hasExportOrder    ? true : null,
                hasMountContainer:$scope.filter.hasMountContainer  ? true : null
            };
            $http.post(FLEX_API + '/searchFlexes', body).then(
                function (res) {
                    $scope.loading = false;
                    $scope.allData = res.data || [];
                    $scope.tableParams.page(1);
                    $scope.tableParams.reload();
                },
                function () {
                    $scope.loading = false;
                    $scope.showToast('Помилка завантаження даних.', 'error');
                }
            );
        };

        $scope.clearFilters = function () {
            $scope.filter   = { serialNum: '', containerNum: '', exportOrderNum: '', warehouseName: '', hasExportOrder: false, hasMountContainer: false };
            $scope.allData  = [];
            $scope.searched = false;
            $scope.tableParams.reload();
        };

        /* helpers for warehouse type */
        $scope.isReserve    = function (f) { return f.warehouse && f.warehouse.warehouseType === 'RESERVE'; };
        $scope.isWrittenOff = function (f) { return f.warehouse && f.warehouse.warehouseType === 'WRITTENOFF'; };

        $scope.rowClass = function (f) {
            if (f.mountContainerNumber) return 'row-mounted';
            if ($scope.isReserve(f))    return 'row-reserve';
            if ($scope.isWrittenOff(f)) return 'row-writtenoff';
            return '';
        };
        $scope.whBadgeClass = function (f) {
            if ($scope.isReserve(f))    return 'wh-reserve';
            if ($scope.isWrittenOff(f)) return 'wh-writtenoff';
            return 'wh-base';
        };

        /* single-flex action (serialNumber in body) */
        $scope.doAction = function (action, f) {
            f._busy = true;
            $http.post(FLEX_API + '/' + action + '/web', { serialNumber: f.serialNumber }).then(
                function () {
                    f._busy = false;
                    $scope.showToast('Виконано: ' + f.serialNumber, 'success');
                    $scope.search();
                },
                function (err) {
                    f._busy = false;
                    var msg = (err.data && err.data.message) ? err.data.message
                            : (err.status === 403 ? 'Недостатньо прав.' : 'Помилка операції.');
                    $scope.showToast(msg, 'error');
                }
            );
        };

        $scope.confirmDelete = function (f) {
            if (!window.confirm('Видалити flex ' + f.serialNumber + '? Цю дію не можна скасувати.')) return;
            $scope.doAction('removeFlex', f);
        };

        $scope.doAttachToOrder = function (f) {
            if (!f._orderInput) return;
            f._busy = true;
            $http.post(FLEX_API + '/attachFlexToOrder/web',
                { serialNumber: f.serialNumber, orderNum: f._orderInput.toUpperCase() }).then(
                function () {
                    f._busy       = false;
                    f._attachOpen = false;
                    $scope.showToast('Прив\'язано до ордеру ' + f._orderInput.toUpperCase(), 'success');
                    $scope.search();
                },
                function (err) {
                    f._busy = false;
                    var msg = (err.data && err.data.message) ? err.data.message
                            : (err.status === 403 ? 'Недостатньо прав.' : 'Помилка прив\'язки до ордеру.');
                    $scope.showToast(msg, 'error');
                }
            );
        };
    });
</script>

<#include "*/footer.ftl"/>

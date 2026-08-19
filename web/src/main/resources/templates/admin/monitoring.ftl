<#assign top_nav_selected = "adminMonitoring">
<#assign page_title = "Monitoring">
<#include "*/header.ftl"/>

<main class="page" ng-controller="adminMonitoringController">

    <div class="block main-block">
        <div class="content">
            <h1>Monitoring</h1>

            <!-- ===== ACTIVE SESSIONS ===== -->
            <div class="data-card">
                <div class="data-card-header">
                    <span><i class="fa fa-users"></i> Active Sessions</span>
                    <span style="font-weight:400;opacity:.75;text-transform:none;font-size:12px;">
                        {{sessions.length}} online
                        <button class="button button-gray" style="margin-left:10px;padding:2px 8px;" ng-click="loadSessions()">
                            <i class="fa fa-refresh" ng-class="{'fa-spin': sessionsLoading}"></i> Refresh
                        </button>
                    </span>
                </div>
                <table class="common-table">
                    <thead>
                    <tr>
                        <th><span class="title">User</span></th>
                        <th><span class="title">Email</span></th>
                        <th><span class="title">Host</span></th>
                        <th><span class="title">Session Started</span></th>
                        <th><span class="title">Last Activity</span></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="s in sessions">
                        <td>{{s.userName}}</td>
                        <td>{{s.userEmail}}</td>
                        <td>{{s.host || '—'}}</td>
                        <td>{{s.startTimestamp | date:'yyyy-MM-dd HH:mm'}}</td>
                        <td>{{s.lastAccessTime | date:'yyyy-MM-dd HH:mm'}}</td>
                    </tr>
                    <tr ng-show="!sessionsLoading && sessions.length === 0">
                        <td colspan="5" style="text-align:center;padding:24px;" class="muted">No active sessions.</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <!-- ===== EVENT LOG FILTER ===== -->
            <fieldset class="mgmt-filter-set" style="margin-top:24px;">
                <legend><i class="fa fa-search"></i> Filter</legend>

                <label class="filter-label">User Email</label>
                <input type="search" ng-model="filter.userEmail"
                       placeholder="user@…"
                       ng-keyup="$event.keyCode == 13 && search()">

                <label class="filter-label">Action</label>
                <input type="search" ng-model="filter.action"
                       placeholder="Controller.method…"
                       ng-keyup="$event.keyCode == 13 && search()">

                <label class="filter-label">From</label>
                <input type="date" ng-model="filter.dateFrom">

                <label class="filter-label">To</label>
                <input type="date" ng-model="filter.dateTo">

                <span class="filter-checkbox-group">
                    <label class="filter-checkbox-label">
                        <input type="checkbox" ng-model="filter.successOnly">
                        Success only
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

            <!-- ===== EVENT LOG TABLE ===== -->
            <div class="data-card" ng-show="searched">
                <div class="data-card-header">
                    <span><i class="fa fa-list-alt"></i> Event Log</span>
                    <span style="font-weight:400;opacity:.75;text-transform:none;font-size:12px;">
                        {{allData.length}} records (last {{maxResults}})
                    </span>
                </div>

                <table class="common-table" ng-table="tableParams">
                    <thead>
                    <tr>
                        <th><span class="title">Date</span></th>
                        <th><span class="title">User</span></th>
                        <th><span class="title">Action</span></th>
                        <th><span class="title">Permission</span></th>
                        <th><span class="title">Method</span></th>
                        <th><span class="title">Status</span></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="e in $data" ng-class="{'row-error': !e.success}">
                        <td>{{e.eventDate | date:'yyyy-MM-dd HH:mm:ss'}}</td>
                        <td>{{e.userEmail}}</td>
                        <td>{{e.action}}</td>
                        <td>{{e.permission}}</td>
                        <td>{{e.httpMethod || '—'}}</td>
                        <td>
                            <span ng-if="e.success" class="ev-badge ev-ok">OK</span>
                            <span ng-if="!e.success" class="ev-badge ev-fail" title="{{e.errorMessage}}">FAILED</span>
                        </td>
                    </tr>
                    <tr ng-show="!loading && allData.length === 0">
                        <td colspan="6" style="text-align:center;padding:24px;" class="muted">No records found.</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <p ng-if="!searched" class="muted" style="padding:16px 2px;">
                Заповніть фільтр і натисніть <strong>Search</strong>, щоб побачити журнал подій.
            </p>

        </div>
    </div>
</main>

<style>
    .filter-label { display:inline-block; margin:0 4px 0 12px; font-weight:600; font-size:12px; }
    .filter-checkbox-group { display:inline-flex; align-items:center; gap:12px; margin:0 6px 0 14px; }
    .filter-checkbox-label { display:inline-flex; align-items:center; gap:4px; font-size:12px; font-weight:600; cursor:pointer; user-select:none; }
    .filter-checkbox-label input[type=checkbox] { margin:0; cursor:pointer; }

    .ev-badge { display:inline-block; padding:2px 8px; border-radius:10px; font-size:11px; font-weight:600; }
    .ev-ok   { background:#e6f9f0; color:#1a6640; }
    .ev-fail { background:#fff0f0; color:#8b1a1a; cursor:help; }
    tr.row-error td { background:#fff8f8; }
</style>

<script type="text/javascript">
    var ADMIN_API = '${contextPath}/tmw/admin/monitoring';

    app.controller('adminMonitoringController', function ($scope, $http, NgTableParams) {

        $scope.sessions        = [];
        $scope.sessionsLoading = false;
        $scope.filter          = { userEmail: '', action: '', successOnly: false, dateFrom: '', dateTo: '' };
        $scope.allData         = [];
        $scope.loading         = false;
        $scope.searched        = false;
        $scope.maxResults      = 1000;

        $scope.loadSessions = function () {
            $scope.sessionsLoading = true;
            $http.get(ADMIN_API + '/activeSessions').then(
                function (res) {
                    $scope.sessionsLoading = false;
                    $scope.sessions = res.data || [];
                },
                function () {
                    $scope.sessionsLoading = false;
                }
            );
        };
        $scope.loadSessions();

        $scope.tableParams = new NgTableParams({ count: 50 }, {
            getData: function ($defer, params) {
                params.total($scope.allData.length);
                var start = (params.page() - 1) * params.count();
                $defer.resolve($scope.allData.slice(start, start + params.count()));
            }
        });

        $scope.search = function () {
            $scope.loading  = true;
            $scope.searched = true;
            var params = {
                userEmail:   $scope.filter.userEmail || null,
                action:      $scope.filter.action || null,
                successOnly: $scope.filter.successOnly ? true : null,
                dateFrom:    $scope.filter.dateFrom ? new Date($scope.filter.dateFrom).getTime() : null,
                dateTo:      $scope.filter.dateTo ? (new Date($scope.filter.dateTo).getTime() + 86399999) : null
            };
            $http.get(ADMIN_API + '/searchEvents', { params: params }).then(
                function (res) {
                    $scope.loading = false;
                    $scope.allData = res.data || [];
                    $scope.tableParams.page(1);
                    $scope.tableParams.reload();
                },
                function () {
                    $scope.loading = false;
                }
            );
        };

        $scope.clearFilters = function () {
            $scope.filter   = { userEmail: '', action: '', successOnly: false, dateFrom: '', dateTo: '' };
            $scope.allData  = [];
            $scope.searched = false;
            $scope.tableParams.reload();
        };
    });
</script>

<#include "*/footer.ftl"/>

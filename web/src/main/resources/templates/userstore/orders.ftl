<#assign top_nav_selected = "trackingOrdersManagement">
<#assign page_title = "Order List Management">
<#include "*/header.ftl"/>

<script type="text/ng-template" id="expandAll.html">
    <button type="button" ng-click="expandAll()">
        <span>{{allExpanded ? '-' : '+'}}</span>
    </button>
</script>

<fieldset ng-app="TrackingOrderManagement">

    <div ng-controller="trackingOrderController">

        <div class="row">
            <div class="span6">
                <div class="controls form-search well">
                    <input id="searchtext" type="text" class="span2 offset2" placeholder="Search for" ng-model="searchtext">
                    <button id="search" class="btn" type="button" ng-click="search()">Search</button>
                    <img id="loading" src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading"/>
                </div>

                <span id="errorMessage" class="text-error">{{errorMessage}}</span>

                <div class="controls form-search well">
                    <table class="table table-bordered table-striped" ng-table="tableParams">
                        <tr ng-repeat-start="t in $data">
                            <td header="'expandAll.html'">
                                <button ng-click="t.expanded = !t.expanded">
                                    <span>{{t.expanded ? '-' : '+'}}</span>
                                </button>
                            </td>

                            <td title="'# B/L'" sortable="'order1c'">
                                <span ng-bind-html="t.order1c | highlight:searchtext | trusted"></span>
                            </td>

                            <td title="'Trend'" sortable="'trend'">
                                <span ng-bind-html="t.trend | trusted"></span>
                            </td>

                            <td title="'Container Line'" sortable="'containerLine'">
                                <span ng-bind-html="t.trackingLine.name | highlight:searchtext | trusted"></span>
                            </td>

                            <td title="'Port'" sortable="'port'">
                                <span ng-bind-html="t.terminal.name | highlight:searchtext | trusted"></span>
                            </td>

                            <td title="'Container Qty'" sortable="'containerQty'">
                                {{t.containerQty}}
                            </td>

                            <td title="'Current Status'" sortable="'status'">
                                <div ng-if="t.workflow.length>0">
                                    {{t.currentStatus}}
                                    <!--div ng-repeat="wf in t.workflow">
                                        <div class="row">
                                            <div class="span2">{{wf.status}}</div>
                                            <div class="span2">{{wf.dealDate | date:'short'}}</div>
                                        </div>
                                    </div-->
                                </div>
                                <div ng-if="t.workflow.length==0">
                                    {{t.currentStatus}}
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat-end ng-if="t.expanded">
                            <td colspan="7">
                                <table class="table table-bordered table-striped">
                                    <tr>
                                        <th width="20%">Number</th>
                                        <th width="10%">Type</th>
                                        <th width="20%">Cargo/Customer #</th>
                                        <th width="10%">Weight</th>
                                        <th width="20%">Driver</th>
                                        <th width="10%">Truck #</th>
                                        <th width="10%">Chassis #</th>
                                    </tr>
                                    <tr ng-repeat="details in t.orderDetails" ng-click="currentLocation(details.id)"
                                        ng-class="{active: details.id === idSelectedDetail}">
                                        <td>
                                            <span ng-bind-html="details.containerNumber | highlight:searchtext | trusted"></span>
                                        </td>
                                        <td>{{details.containerType.type}}</td>
                                        <td>?</td>
                                        <td>{{details.weight}}</td>
                                        <td>
                                            <span ng-bind-html="details.driver.firstName | highlight:searchtext | transliterate | trusted"></span>,
                                            <span ng-bind-html="details.driver.lastName | highlight:searchtext | transliterate | trusted"></span>
                                            <br> {{details.driver.mobile}}
                                        </td>
                                        <td>
                                            <span ng-bind-html="details.driver.trailerNumber | highlight:searchtext | trusted"></span>
                                        </td>
                                        <td>
                                            <span ng-bind-html="details.driver.tractorNumber | highlight:searchtext | trusted"></span>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </div>

            </div>

            <div class="span6">
                <ui-gmap-google-map center='map.center' zoom='map.zoom' options="map.options">
                    <ui-gmap-marker coords="map.marker.coords" idkey="map.marker.id"
                                    options="map.marker.options"></ui-gmap-marker>
                </ui-gmap-google-map>
            </div>


        </div>
    </div>
</fieldset>

<script type="text/javascript">

    initMap(app);

    app.controller("trackingOrderController", function ($scope, $filter, $http, NgTableParams) {

        $scope.searchtext = "";

        $scope.search = function () {
            $scope.init($scope.searchtext);
        };

        $scope.init = function (searchFor) {
            if (angular.isUndefined(searchFor)) {
                searchFor = "";
            }

            $scope.loading = true;

            /*
            $http.get('${contextPath}/tmw/order/getAll?searchFor=' + searchFor)
                    .then(function (res) {
                        $scope.loading = false;
                        $scope.orders = res.data.map(function (order) {
                            order.expanded = isExpanded(order);
                            return order;
                        });
                    });
             */

            $scope.tableParams = new NgTableParams({count: ${pageSize}}, {getData: function($defer, params) {

                var url = '${contextPath}/tmw/order/getAll?page=' + params.page() + '&searchFor=' + searchFor;
                var sort = getSortParams(params.sorting());

                if (sort) {
                    url = url + "&sort=" + sort;
                }

                $http.get(url).then(function(response) {
                    $scope.loading = false;

                    $scope.orders = response.data.map(function (order) {
                        order.expanded = isExpanded(order);
                        return order;
                    });

                    var totalPages = response.headers("X-Total-Pages");
                    var countPerPage = response.headers("X-Page-Size");

                    params.total(totalPages * countPerPage);
                    $scope.data = $scope.orders;
                    $defer.resolve($scope.orders);
                });
            }});

            function isExpanded(order) {
                if (!searchFor || !order.orderDetails || order.orderDetails.length <= 0) {
                    return false;
                }

                return order.orderDetails.some(function (details) {
                    return search(details.containerNumber, searchFor)
                            || search(details.driver.firstName, searchFor)
                            || search(details.driver.lastName, searchFor)
                            || search(details.driver.trailerNumber, searchFor)
                            || search(details.driver.tractorNumber, searchFor)
                });
            }

            function search(text, term) {
                return text.indexOf(term) !== -1;
            }
        };

        $scope.currentLocation = function (detailId) {

            $scope.idSelectedDetail = detailId;

            $http.get('${contextPath}/tmw/order/getCurrentLocation?detailId=' + detailId)
                    .then(function (res) {
                        var curLoc = res.data;
                        if (!angular.isUndefined(curLoc.latitude)) {
                            $scope.map = createMap(curLoc.latitude, curLoc.longitude);
                        }
                    });
        };

        $scope.init();

        $scope.expandAll = function () {
            $scope.allExpanded = !$scope.allExpanded;

            $scope.orders.forEach(function (order) {
                order.expanded = $scope.allExpanded;
            });
        };

        $scope.map = createMap(0, 0);

    });

</script>
<#include "*/footer.ftl"/>

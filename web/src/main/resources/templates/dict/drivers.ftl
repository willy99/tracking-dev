<#assign top_nav_selected = "driverManagement">
<#assign page_title = "Driver List Management">
<#include "*/header.ftl"/>
<fieldset ng-app="DriverManagement">

    <div ng-controller="driverController">

        <span id="errorMessage" class="text-error">{{errorMessage}}</span>


        <table class="table table-bordered table-striped"  ng-table="tableParams">
            <tr ng-repeat="d in $data">
                <td title="'ID'" sortable="'id'">{{d.id}}</td>
                <td title="'First Name'" sortable="'firstName'">{{d.firstName | transliterate}}</td>
                <td title="'Last Name'" sortable="'lastName'">{{d.lastName | transliterate}}</td>
            </tr>
        </table>

        <img id="loading" src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading"/>

    </div>
</fieldset>

<script type="text/javascript">

    app.controller("driverController", function($scope, $filter, $http, $q, NgTableParams) {
        var data = [];
        $scope.init = function() {
            $scope.tableParams = new NgTableParams({count: ${pageSize}}, {getData: function($defer, params) {

                var url = '${contextPath}/tmw/dict/getDrivers?page=' + params.page();
                var sort = getSortParams(params.sorting());

                if (sort) {
                    url = url + "&sort=" + sort;
                }

                $http.get(url).then(function(response){
                    data = response.data;

                    var totalPages = response.headers("X-Total-Pages");
                    var countPerPage = response.headers("X-Page-Size");

                    params.total(totalPages * countPerPage);
                    $scope.data = data;
                    $defer.resolve(data);
                });
            }});
        };

        $scope.init();
    });


</script>
<#include "*/footer.ftl"/>

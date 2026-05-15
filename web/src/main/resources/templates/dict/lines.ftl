<#assign top_nav_selected = "LinesManagement">
<#assign page_title = "Tracking Lines Management">
<#include "*/header.ftl"/>
<fieldset ng-app="LinesManagement">

    <div ng-controller="linesController">

        <span id="errorMessage" class="text-error">{{errorMessage}}</span>

        <table class="table table-bordered table-striped" ng-table="tableParams">
            <tr ng-repeat="d in $data">
                <td title="'Name'" sortable="'name'">{{d.name}}</td>
            </tr>
        </table>

        <img id="loading" src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading"/>
    </div>
</fieldset>

<script type="text/javascript">

    app.controller("linesController", function($scope, $filter, $http, $q, NgTableParams) {
        var data = [];
        $scope.init = function() {
            $scope.tableParams = new NgTableParams({count: ${pageSize}}, {getData: function($defer, params) {

                var url = '${contextPath}/tmw/dict/getLines?page=' + params.page();
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

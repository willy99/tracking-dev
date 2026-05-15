<#assign top_nav_selected = "trackingOrdersManagement">
<#assign page_title = "Batch Mount Flex">
<#include "*/header.ftl"/>

<main class="page" ng-controller="flexController">
    <div class="spinner" ng-show="loading"></div>

    <div class="block main-block">
        <div class="content w-640">
            <img src="${contextPath}/img/flex/mountformat.png" width="100%">
            <br><br>
            <form name="batchForm" class="css-form" enctype="multipart/form-data">
                <div class="fieldset visible">

                    <div>Please attach file (For Mount): </div>
                    <div>
                        <input type="file" id="batchfile" ng-model="batchfile" class="file-upload">

                    </div>
                    <div class="pop-up-message" id="pop-up-message"></div>
                    <div class="buttons-wrap">
                        <button ng-click="upload()" class="button button-green button-right">${label.value("buttons_action_upload")}</button>
                    </div>
                </div>
            </form>
        </div>

    </div>
</main>

<script language="JavaScript">
    app.controller("flexController", function($scope, $filter, $http, $q, NgTableParams) {

        $scope.result = [];
        $scope.upload = function () {
            $scope.loading = true;
            var files = document.getElementById("batchfile").files;
            var fileData = new Blob([files[0]]);
            var fd = new FormData();
            fd.append('batchfile', fileData);
            $http.post('${contextPath}/webresources/flex/executeBatchMountFlex', fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).then(
                function (res, respStatus, headers, config) {
                    console.log('>>' + JSON.stringify(res.data.data));
                    if (res.data) {
                        showMessage(res.data.data, 'error');
                    } else {
                        showMessage("Processed successfully", 'success');
                    }
                    $scope.loading = false;
                }, function error(res, respStatus, headers, config) {
                    console.log('>>>' + JSON.stringify(res.data.data));
                    showMessage(res.data.error.message, 'error');
                    $scope.loading = false;
                });

        };
    });


</script>
<#include "*/footer.ftl"/>

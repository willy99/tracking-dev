<#assign page_title = "Recover Password">
<#include "../header.ftl"/>


<main class="page" ng-controller="recoverController">
    <div class="block main-block">
        <div class="content w-640">

            <div class="fieldset visible" ng-show="showform">
                <h1>Provide email to send the recovery link:</h1>
                <form id="recover" name="recoverForm">
                    <div class="input-prepend input-append" style="position: relative;">
                        <input class="input" type="email" ng-model="user.userId" ng-required="true" id="userId">
                    </div><br/>
                    <button ng-click="recoverPassword()" class="button button-green button-right">Send</button>
                </form>
            </div>
            <div class="pop-up-message" id="message"></div>
        </div>
    </div>
</main>


<script>
    document.getElementById("userId").focus();

    app.controller("recoverController", function($scope, $filter, $http, $q, NgTableParams, $state) {
        $scope.showform = true;

        $scope.recoverPassword = function () {
            if ($scope.recoverForm.$valid) {
                $http.post('${contextPath}/tmw/recover', $scope.user).then(
                        function (res, respStatus, headers, config) {
                            $scope.showform = false;
                            if (res.data.errorMessage) {
                                showMessage(res.data.errorMessage, 'error', 500000);
                            } else {
                                $scope.showform = false;
                                showMessage('Successfully sent the recover link to email', 'success', 500000);
                            }
                        }, function error(res, respStatus, headers, config) {
                            showMessage(res.data, 'error', 500000);

                        });
            }
        }
    });

</script>

<#include "../footer.ftl"/>

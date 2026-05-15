<#assign page_title = "Activate Account and Create New Password">
<#include "../header.ftl"/>

<main class="page" ng-controller="activateController">
    <div class="block main-block">
        <div class="content w-640">

            <div class="fieldset visible" ng-show="user.userId != ''">
                <h1>To activate an account, please provide new password:</h1>
                <form id="recover" name="activateForm">
                    <div class="input-prepend input-append" style="position: relative;">
                        <input class="input" type="password" ng-model="user.password" ng-required="true" id="password">
                    </div><br/>
                    <button ng-click="activatePassword()" class="button button-green button-right">Activate</button>

                </form>
            </div>
            <div class="pop-up-message" id="message"></div>
        </div>
    </div>
</main>


<script>
    document.getElementById("password").focus();

    app.controller("activateController", function($scope, $filter, $http, $q, NgTableParams, $state) {
        $scope.user = {};
        $scope.user.userId = '${userId}';
        $scope.error = '${error}';
        if ($scope.error !== '') {
            showMessage($scope.error, 'error', 500000);
        }

        $scope.activatePassword = function () {
            if ($scope.activateForm.$valid) {
                $http.post('${contextPath}/tmw/activate', $scope.user).then(
                        function (res, respStatus, headers, config) {
                            $scope.user.userId = '';
                            if (res.data.errorMessage) {
                                showMessage(res.data.errorMessage, 'error', 500000);
                            } else {
                                showMessage('Successfully activated the account and changed password', 'success', 500000);
                            }
                        }, function error(res, respStatus, headers, config) {
                            showMessage(res.data.error.message, 'error', 500000);
                            $scope.user.userId = null;
                        });
            }
        }
    });

</script>

<#include "../footer.ftl"/>

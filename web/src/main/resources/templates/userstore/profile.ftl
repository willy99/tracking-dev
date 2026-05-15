<#assign top_nav_selected = "profileManagement">
<#assign page_title = "User Profile">
<#include "*/header.ftl"/>

<main class="page" ng-controller="profileController">
    <div class="spinner" ng-show="loading"></div>


        <div class="pop-up" id="edit-profile-pop-up">

            <div class="window">
                <div class="close-btn"></div>
                <div class="pop-up-title">Edit Profile</div>
                <form name="userForm" class="css-form">
                    <div class="sub-title">Basic info:</div>
                    <div class="fieldset visible">

                        <div class="row">
                            <div class="name">First Name:</div>
                            <input class="input" type="text" ng-model="user.firstName" ng-required="true">
                        </div>
                        <div class="row">
                            <div class="name">Last Name:</div>
                            <input class="input" type="text" ng-model="user.lastName" ng-required="true">
                        </div>
                        <div class="row">
                            <div class="name">Phone Num:</div>
                            <input class="input" type="tel" name="phone" ng-model="user.phone" ng-required="true" placeholder="+__ (___) ___-__-__">
                            <div class="hint" ng-show="userForm.phone.$error.pattern">
                                Not a valid number, Please enter the phone in format +__ (___) ___-__-__</div>
                        </div>
                        <div class="row">
                            <div class="name">Email:</div>
                            <input class="input" type="email" ng-model="user.email" ng-required="true">
                        </div>
                        <div class="row">
                            <div class="name">Locale</div>
                            <div >
                                <select id="mainselection" name="locale" ng-model="userlocale" ng-options="locale as locale.descr for locale in localeList track by locale.name"></select>
                            </div>
                        </div>


                        <div class="row">
                            <div class="name">Password:</div>
                            <input class="input" type="password" ng-model="user.password">
                        </div>

                        <div class="pop-up-message" id="pop-up-message"></div>

                        <div class="buttons-wrap">
                            <button ng-click="saveProfile()" class="button button-green button-right">Save Changes</button>
                        </div>

                    </div>

                </form>
            </div>

    </div>
</main>



<script type="text/javascript">

    app.controller("profileController", function($scope, $filter, $http, $q, localStorageService) {

        $scope.localeList = [
            {descr:'English', name:'en_US'},
            {descr:'Russian', name:'ru_RU'},
            {descr:'Ukrainian', name:'ua_UA'}
        ];


        var data = [];

        $scope.editUser = function () {
            $scope.form = '#edit-profile-pop-up';

            $scope.loading = true;
            document.getElementById('edit-profile-pop-up').classList.add('active');
            document.body.classList.add('fixed');
            expandFieldsets();

            $scope.user = null;

            $http.get('${contextPath}/tmw/userstore/getProfile').then(function (res) {
                $scope.loading = false;
                if (res.data.errorMessage) {
                    $scope.user = null;
                    showPopUpMessage($scope.form, data.errorMessage, 'error');
                } else {
                    $scope.user = res.data.user;
                    $scope.user.password = '';

                    angular.forEach($scope.localeList, function(value, key) {
                        console.log(value.name  + ' '+ $scope.user.locale);
                        if (value.name === $scope.user.locale) {
                            console.log('found)');
                            $scope.userlocale = $scope.localeList[key];
                        }

                    });

                    console.log($scope.userlocale);
                }

            } , function error(res) {
                showPopUpMessage($scope.form, data.errorMessage, 'error');
                $scope.roleInfo = null;
                $scope.loading = false;
            });
        };

        $scope.saveProfile = function () {
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.loading = true;
            $scope.user.locale = $scope.userlocale.name;
            $http.post('${contextPath}/tmw/userstore/saveProfile', $scope.user).then(
                    function (res, respStatus, headers, config) {
                        $scope.loading = false;
                        if (data.errorMessage) {
                            showPopUpMessage($scope.form, res.data.errorMessage, 'error');
                        } else {
                            $scope.user = null;
                            localStorageService.set("locale", res.data.locale);
                            showPopUpMessage($scope.form, 'Profile has been save successfully', 'success');
                        }
                    }, function error(res, respStatus, headers, config) {
                        $scope.loading = false;
                        showPopUpMessage($scope.form, res.data, 'error');
                    });
        };

        $scope.editUser();

    });

</script>
<#include "*/footer.ftl"/>

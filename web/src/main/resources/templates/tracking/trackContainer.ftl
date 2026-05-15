<#assign top_nav_selected = "trackContainer">
<#assign page_title = "Track Container">
<#include "../header.ftl"/>



<main class="page" ng-controller="trackingController">
    <div class="spinner" ng-show="loading"></div>

    <div class="block main-block">
        <div class="content w-640">
            <h2>${label.value("tracking_label_header")}</h2>

            <form name="calcForm" class="css-form">
                <div class="fieldset visible">

                    <div class="row">

                        <div>
                            <input class="input" type="text" name="container" ng-model="container" placeholder="Enter Container Number"
                                   ng-required="true" ng-minlength="11" ng-maxlength="11"
                            >
                            <span class="error" ng-show="calcForm.container.$error.minlength">${label.value('tracking_validation_parameter_length')}</span>
                            <span class="error" ng-show="calcForm.container.$error.maxlength">${label.value('tracking_validation_parameter_length')}</span>

                        </div>

                         <div class="buttons-wrap">
                            <button ng-click="search()" class="button button-green button-right">${label.value("buttons_action_search")}</button>
                         </div>

                        <div class="pop-up-message" id="pop-up-message"></div>
                    </div>

                </div>
            </form>
        </div>


        <div class="content w-840" ng-show="trackingInfo.tracks">

                <h3>${label.value("tracking_label_result")}</h3>
                <h3>${label.value("tracking_label_result_service_company")} : <span ng-bind="trackingInfo.service"></span> </h3>

                <h4>${label.value("tracking_label_result_container_type")} : <span ng-bind="trackingInfo.containerType"></span> </h4>
                <h4>${label.value("tracking_label_result_destination")} : <span ng-bind="trackingInfo.destination"></span> </h4>

                <div>
                    <table class="common-table">
                        <thead>
                        <tr>
                            <th></th>
                            <th><span class="title">${label.value("tracking_label_table_column_date")}</span></th>
                            <th><span class="title">${label.value("tracking_label_table_column_location")}</span></th>
                            <th><span class="title">${label.value("tracking_label_table_column_vessel")}</span></th>
                            <th><span class="title">${label.value("tracking_label_table_column_vessel_type")}</span></th>
                            <th><span class="title">${label.value("tracking_label_table_column_description")}</span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="r in trackingInfo.tracks">
                            <td></td>
                            <td><span ng-bind-html="r.date | stamptodate| trusted"></span></td>
                            <td><span ng-bind-html="r.location | trusted"></span></td>
                            <td><span ng-bind-html="r.vessel | trusted"></span></td>
                            <td><span ng-bind-html="r.vesselType | trusted"></span></td>
                            <td><span ng-bind-html="r.description | trusted"></span></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

        </div>
    </div>
</main>


<script type="text/javascript">



     firebaseConfig = {
            apiKey: "AIzaSyCWLS82pyCLc4FjaylsO5Npvtj5-hp-ld0",
            authDomain: "artinlog-crawler.firebaseapp.com",
            databaseURL: "https://artinlog-crawler.firebaseio.com",
            projectId: "artinlog-crawler",
            storageBucket: "artinlog-crawler.appspot.com",
            messagingSenderId: "389509051640",
            appId: "1:389509051640:web:bb853c6175351d42"
        };
        // Initialize Firebase
     firebase.initializeApp(firebaseConfig);


    app.controller("trackingController", function($scope, $filter, $http, $firebaseObject, $q) {


        $scope.trackingInfo = null;

        $scope.search = function () {
            if ($scope.calcForm.$valid) {
                $scope.loading = true;
                $http.get('${contextPath}/tmw/tracking/find?container=' + $scope.container).then(function (res) {
                    if (res.data.errorMessage) {
                        $scope.loading = false;
                        $scope.message = null;
                        showMessage(res.data.error.message, 'error');
                    } else {

                        var database = firebase.database();
                        var trackingRef = database.ref ('/track_responses/' + res.data);
                        console.log('Searching for ' + res.data);

                        var obj = $firebaseObject(trackingRef);
                        obj.$bindTo($scope, "trackingInfo");
                        obj.$loaded().then(function() {
                            $scope.loading = false;
                        });

                    }

                }, function error(res, respStatus, headers, config) {
                    console.log(res);

                    showMessage(res.data, 'error');
                    $scope.loading = false;
                });
            }
        };

    });


</script>
<#include "*/footer.ftl"/>

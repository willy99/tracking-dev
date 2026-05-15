app.controller('SearchUserController', ['$scope', '$http', '$filter', 'close', 'NgTableParams', 'idRole', 'page_size','contextPath',
    function($scope, $http, $filter, close, NgTableParams, idRole , page_size, contextPath) {


        var dataModal = [];
        $scope.searchtext="";

        $scope.search = function() {
            $scope.initUserForm($scope.searchtext);
        };

        $scope.initUserForm = function(searchFor) {
            if (angular.isUndefined(searchFor)) {
                searchFor = "";
            }

            $scope.usersTableModal = new NgTableParams({count: page_size}, {getData: function($defer, params) {

                $scope.filter = {roleId: idRole, matcher: searchFor, activity : true};

                var url = contextPath + '/tmw/userstore/findUsersNotInRole?&page=' + params.page();

                var sort = getSortParams(params.sorting());

                if (sort) {
                    url = url + "&sort=" + sort;
                }

                $http.post(url, $scope.filter).then(function(response){
                    $scope.users = response.data;
                    dataModal = response.data;

                    var totalPages = response.headers("X-Total-Pages");
                    var countPerPage = response.headers("X-Page-Size");

                    params.total(totalPages * countPerPage);
                    $scope.dataModal = dataModal;
                    $defer.resolve(dataModal);
                });
            }});

        };


        $scope.editUser = function (user) {
            $scope.idSelectedUser = user.id;
            $scope.user = null;
            $scope.errorMessage = null;
            $scope.loading = true;
            $http.get(contextPath + '/tmw/userstore/find?id=' + user.id).then(function (res) {
                $scope.loading = false;
                if (res.data.errorMessage) {
                    $scope.user = null;
                    $scope.errorMessage = res.data.errorMessage;
                } else {
                    $scope.user = res.data.user;
                }

            } , function error(res) {
                $scope.errorMessage = "Request error";
                $scope.roleInfo = null;
            });
        };

        $scope.close = function(result) {
            close(result, 500); // close, but give 500ms for bootstrap to animate
        };

        $scope.initUserForm();


    }]);


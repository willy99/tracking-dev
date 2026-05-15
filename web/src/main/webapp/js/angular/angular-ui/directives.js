
var app = angular.module('app', ["ngTable", "firebase", '$strap.directives', 'uiGmapgoogle-maps', 'LocalStorageModule', 'angularModalService', 'ui.bootstrap', 'ui.router']);

app.config(function (localStorageServiceProvider, $stateProvider, $urlRouterProvider) {

    localStorageServiceProvider
        .setStorageType('localStorage');

    $stateProvider

    // route to show our basic form (/form)
        .state('company', {
            url: '/create/createform',

            onEnter: function($stateParams, $state, $modal) {
                $modal.open({
                    templateUrl: '/tmw/company/create/createform',
                    controller: 'companyController',
                }).result.finally(function() {
                    $state.go('/');
                });
            }
        })
        .state('company.step1', {
            url: '/create/step1',
            templateUrl: 'create/create_step_1'
        })

        // url will be /form/interests
        .state('company.step2', {
            url: '/create/step2',
            templateUrl: 'create/create_step_2'
        })

    // catch all route
    // send users to the form page
    //$urlRouterProvider.otherwise('/company');

});



var rusToLat = {};
rusToLat["Ё"]="Yo";rusToLat["Й"]="I";rusToLat["Ц"]="Ts";rusToLat["У"]="U";rusToLat["К"]="K";rusToLat["Е"]="E";rusToLat["Н"]="N";rusToLat["Г"]="G";rusToLat["Ш"]="Sh";rusToLat["Щ"]="Sch";rusToLat["З"]="Z";rusToLat["Х"]="H";rusToLat["Ъ"]="'";
rusToLat["ё"]="yo";rusToLat["й"]="i";rusToLat["ц"]="ts";rusToLat["у"]="u";rusToLat["к"]="k";rusToLat["е"]="e";rusToLat["н"]="n";rusToLat["г"]="g";rusToLat["ш"]="sh";rusToLat["щ"]="sch";rusToLat["з"]="z";rusToLat["х"]="h";rusToLat["ъ"]="'";
rusToLat["Ф"]="F";rusToLat["Ы"]="Y";rusToLat["В"]="V";rusToLat["А"]="a";rusToLat["П"]="P";rusToLat["Р"]="R";rusToLat["О"]="O";rusToLat["Л"]="L";rusToLat["Д"]="D";rusToLat["Ж"]="Zh";rusToLat["Э"]="E";
rusToLat["ф"]="f";rusToLat["ы"]="y";rusToLat["в"]="v";rusToLat["а"]="a";rusToLat["п"]="p";rusToLat["р"]="r";rusToLat["о"]="o";rusToLat["л"]="l";rusToLat["д"]="d";rusToLat["ж"]="zh";rusToLat["э"]="e";
rusToLat["Я"]="Ya";rusToLat["Ч"]="Ch";rusToLat["С"]="S";rusToLat["М"]="M";rusToLat["И"]="I";rusToLat["Т"]="T";rusToLat["Ь"]="'";rusToLat["Б"]="B";rusToLat["Ю"]="Yu";
rusToLat["я"]="ya";rusToLat["ч"]="ch";rusToLat["с"]="s";rusToLat["м"]="m";rusToLat["и"]="i";rusToLat["т"]="t";rusToLat["ь"]="'";rusToLat["б"]="b";rusToLat["ю"]="yu";

app.controller("mainController", function($scope, $filter, localStorageService) {

    $scope.setLocale = function(locale) {
        console.log("Setting locale "+locale);
        if(localStorageService.isSupported) {
            localStorageService.set("locale", locale);
        }
    };

    if(localStorageService.isSupported) {
        locale = localStorageService.get("locale");
        console.log("Locale = " + locale);
        if (locale === undefined) {
            localStorageService.set("locale", "en");
            locale = 'en';
        }
    }

});

app.filter('transliterate', [function () {

    return function (word) {
        var answer = "";
        if (locale == "ru") {
            return word; //TODO latToRus
        }
        for (i in word){
            if (word.hasOwnProperty(i)) {
                if (rusToLat[word[i]] === undefined) {
                    answer += word[i];
                } else {
                    answer += rusToLat[word[i]];
                }
            }
        }

        return answer;
    };
}]);

app.filter('stamptodate', [function () {

    return function (stamp) {
        if (!stamp) {return;}
        var dt = moment(new Date(stamp * (stamp.length>9?1000:1)));
        var formatted = dt.format("YYYY/MM/DD");
        /*var dateString =
            dt.getUTCFullYear() + "/" +
            ("0" + (dt.getUTCMonth()+1)).slice(-2) + "/" +
            ("0" + dt.getUTCDate()).slice(-2);
        return dateString;*/
        return formatted;
    };
}]);

app.filter('highlight', function() {
    return function(text, phrase) {
        if (phrase) {
            text = text.replace(
                new RegExp('('+phrase+')', 'gi'),
                '<span class="highlighted">$1</span>'
            );
        }

        return text;
    }
});

app.filter('trusted', function($sce) {
    return $sce.trustAsHtml;
});


app.filter('customNumber', function() {
    return function(value) {
        value = value.replace(',','');
        return parseInt(value, 10) //convert to int
    }
});


function getSortParams(sorting) {
    if (!sorting) {
        return false;
    }

    var sortColumn = Object.keys(sorting)[0];
    if (!sortColumn) {
        return false;
    }

    var sortOrder = sorting[sortColumn];

    return sortColumn + "," + sortOrder;
}




//#### TREE VIEW ### //
app.directive('treeView', function($compile) {
    return {
        restrict : 'E',
        scope : {
            localNodes : '=model',
            localClick : '&click'
        },
        link : function (scope, tElement, tAttrs, transclude) {

            var maxLevels = (angular.isUndefined(tAttrs.maxlevels)) ? 10 : tAttrs.maxlevels;
            var hasCheckBox = (angular.isUndefined(tAttrs.checkbox)) ? false : true;
            scope.showItems = [];

            scope.showHide = function(ulId) {
                var hideThis = document.getElementById(ulId);
                var showHide = angular.element(hideThis).attr('class');
                angular.element(hideThis).attr('class', (showHide === 'show' ? 'hide' : 'show'));
            };

            scope.showIcon = function(node) {
                return scope.checkIfChildren(node);
            };

            scope.checkIfChildren = function(node) {
                if (!angular.isUndefined(node.children) && node.children.length>0) return true;
            };

            /////////////////////////////////////////////////
            /// SELECT ALL CHILDRENS
            // as seen at: http://jsfiddle.net/incutonez/D8vhb/5/
            function parentCheckChange(item) {
                for (var i in item.children) {
                    item.children[i].checked = item.checked;
                    if (item.children[i].children) {
                        parentCheckChange(item.children[i]);
                    }
                }
            }

            scope.checkChange = function(node) {
                if (node.children) {
                    parentCheckChange(node);
                }
            }
            /////////////////////////////////////////////////

            function renderTreeView(collection, level, max) {
                var text = '';
                text += '<li ng-repeat="n in ' + collection + '" >';
                text += '<span ng-show=showIcon(n) class="show-hide" ng-click=showHide(n.id)><i class="fa fa-plus-square"></i></span>';
                text += '<span ng-show=!showIcon(n)  style="padding-right: 13px"></span>';

                if (hasCheckBox) {
                    text += '<input class="tree-checkbox " type=checkbox ng-model=n.checked ng-change=checkChange(n)>';
                }


                text+= '<span class="edit " ng-click=localClick({node:n})><i class="fa"></i></span>'


                text += '<span>{{n.name}}</span>';

                if (level < max) {
                    text += '<ul id="{{n.id}}" class="hide" ng-if=checkIfChildren(n)>'+renderTreeView('n.children', level + 1, max)+'</ul></li>';
                } else {
                    text += '</li>';
                }

                return text;
            }// end renderTreeView();

            try {
                var text = '<ul class="tree-view-wrapper">';
                text += renderTreeView('localNodes', 1, maxLevels);
                text += '</ul>';
                tElement.html(text);
                $compile(tElement.contents())(scope);
            }
            catch(err) {
                tElement.html('<b>ERROR!!!</b> - ' + err);
                $compile(tElement.contents())(scope);
            }
        }
    };
});
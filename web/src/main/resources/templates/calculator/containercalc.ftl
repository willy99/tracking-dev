<#assign top_nav_selected = "containercalc">
<#assign page_title = "Container Calculator">
<#include "*/header.ftl"/>

<main class="page" ng-controller="calcController">
    <div class="spinner" ng-show="loading"></div>

    <div class="block main-block">
        <div class="content wide-content">
            <h2 style="margin-bottom: 20px;">${label.value("calculator_label_header")}</h2>

            <form name="calcForm" class="neat-form css-form">

                <div class="top-row">

                    <div class="neat-box">
                        <span class="box-title">${label.value("calculator_label_specification_cargo_type")}</span>

                        <select class="input-compact" name="cargotype"
                                ng-model="cargo.packageType"
                                ng-options="packageType.cargoType for packageType in packageTypes"
                                ng-required="true"
                                ng-change="changePackageType()">
                        </select>
                        <span class="error" style="font-size:11px;" ng-show="calcForm.cargotype.$error.required">${label.value('calculator_validation_empty_package_type')}</span>

                        <div ng-show="cargo.packageType">
                            <div class="dim-grid" ng-show="cargo.packageType.shapeType == 'BOX'">
                                <div style="width: 100%; text-align: center;">
                                    <label style="font-size: 11px; color: #586069; display: block; margin-bottom: 3px;">${label.value("calculator_label_specification_length")}</label>
                                    <input class="input-compact" type="number" name="length1" ng-model="cargo.dimension.length" ng-required="true">
                                </div>
                                <div style="width: 100%; text-align: center;">
                                    <label style="font-size: 11px; color: #586069; display: block; margin-bottom: 3px;">${label.value("calculator_label_specification_width")}</label>
                                    <input class="input-compact" type="number" name="width1" ng-model="cargo.dimension.width" ng-required="true">
                                </div>
                                <div style="width: 100%; text-align: center;">
                                    <label style="font-size: 11px; color: #586069; display: block; margin-bottom: 3px;">${label.value("calculator_label_specification_height")}</label>
                                    <input class="input-compact" type="number" name="height1" ng-model="cargo.dimension.height" ng-required="true">
                                </div>
                            </div>

                            <div class="dim-grid" ng-show="cargo.packageType.shapeType == 'CYLINDER'">
                                <div style="width: 100%; text-align: center;">
                                    <label style="font-size: 11px; color: #586069; display: block; margin-bottom: 3px;">${label.value("calculator_label_specification_diameter")}</label>
                                    <input class="input-compact" type="number" name="diameter2" ng-model="cargo.dimension.width" ng-required="true">
                                </div>
                                <div style="width: 100%; text-align: center;">
                                    <label style="font-size: 11px; color: #586069; display: block; margin-bottom: 3px;">${label.value("calculator_label_specification_height")}</label>
                                    <input class="input-compact" type="number" name="height2" ng-model="cargo.dimension.height" ng-required="true">
                                </div>
                            </div>

                            <div class="dim-grid" ng-show="cargo.packageType.shapeType == 'PIPE'">
                                <div style="width: 100%; text-align: center;">
                                    <label style="font-size: 11px; color: #586069; display: block; margin-bottom: 3px;">${label.value("calculator_label_specification_diameter")}</label>
                                    <input class="input-compact" type="number" name="diameter3" ng-model="cargo.dimension.width" ng-required="true">
                                </div>
                                <div style="width: 100%; text-align: center;">
                                    <label style="font-size: 11px; color: #586069; display: block; margin-bottom: 3px;">${label.value("calculator_label_specification_length")}</label>
                                    <input class="input-compact" type="number" name="length3" ng-model="cargo.dimension.length" ng-required="true">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="neat-box">
                        <span class="box-title">${label.value("calculator_label_specification_weight")}</span>
                        <input class="input-compact" name="weight" type="number" ng-model="cargo.weight" ng-required="true" placeholder="Weight (kg)">
                        <span class="error" style="font-size:11px;" ng-show="calcForm.weight.$error.required">Weight is required</span>
                    </div>

                    <div class="neat-box">
                        <span class="box-title">Options</span>
                        <div class="custom-check-wrapper">
                            <label class="custom-checkbox" ng-show="cargo.packageType.defaultSideLaying == 'true'">
                                <input type="checkbox" ng-model="cargo.sideLaying">
                                <span class="checkmark"></span>
                                ${label.value("calculator_label_specification_side_laying")}
                            </label>

                            <label class="custom-checkbox">
                                <input type="checkbox" ng-model="cargo.layering">
                                <span class="checkmark"></span>
                                ${label.value("calculator_label_specification_layering")}
                            </label>
                        </div>
                    </div>

                    <div class="neat-box">
                        <span class="box-title">Details & Preview</span>
                        <div class="cargo-details-wrapper">
                            <div ng-show="cargo.packageType">
                                <img ng-src="${contextPath}/img/calculator/_{{cargo.packageType.cargoType | lowercase }}.png" alt="Cargo Preview"/>
                                <div class="cargo-details-title">{{cargo.packageType.cargoType}}</div>
                                <div class="badge-pf">PF: {{cargo.packageType.packingFactor}}</div>
                            </div>
                            <div ng-show="!cargo.packageType" style="color: #999; font-size: 13px; margin-top: 15px;">
                                Please select cargo type
                            </div>
                        </div>
                    </div>

                </div> <div style="margin-top: 10px;">
                    <h3 style="font-size: 16px; margin-bottom: 12px;">${label.value("calculator_label_specific_tonnage")}</h3>

                    <div class="ct-strip-container">
                        <div class="neat-box ct-strip" ng-repeat="ct in containerTypes">
                            <div class="ct-name">{{ct.name}}'{{ct.containerGroup}}</div>

                            <div class="ct-inputs">
                                <div class="ct-input-group">
                                    <label>Workload (kg)</label>
                                    <input class="input-compact" name="ctworkload{{$index}}" type="text" ng-model="ct.workload" ng-required="true">
                                </div>
                                <div class="ct-input-group">
                                    <label>Freight Rate ($)</label>
                                    <input class="input-compact" name="ctfreightrate{{$index}}" type="text" ng-model="ct.freightRate" ng-required="true">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="pop-up-message" id="pop-up-message"></div>

                <div class="buttons-wrap" style="text-align: right; margin-top: 15px;">
                    <button ng-click="calculate()" class="button button-green">${label.value("buttons_action_calculate")}</button>
                </div>
            </form>
        </div>

        <div class="content wide-content" ng-show="!result.isEmpty()">
            <h3 style="margin: 20px 0;">${label.value("calculator_label_result")}</h3>
            <div class="table-responsive">
                <table class="common-table" style="width: 100%; min-width: 800px; background: #fff; border-radius: 6px; box-shadow: 0 1px 2px rgba(0,0,0,0.04);">
                    <thead style="background: #f8f9fa;">
                    <tr>
                        <th></th>
                        <th><span class="title">${label.value("calculator_label_table_column_container_type")}</span></th>
                        <th><span class="title">${label.value("calculator_label_table_column_loading_type")}</span></th>
                        <th><span class="title">Measurement</span></th>
                        <th style="text-align:center;"><span class="title">${label.value("calculator_label_table_column_total")}</span></th>
                        <th><span class="title">${label.value("calculator_label_table_column_carry_cost")}, $</span></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="r in result" ng-click="redrawForContainer(r)" ng-class="{'selected-row': selectedResult === r}" style="cursor:pointer; border-bottom: 1px solid #eee;">
                        <td style="text-align: center;"><img src="${contextPath}/img/calculator/_20dv.png" width="40"></td>
                        <td>
                            <strong ng-bind-html="r.containerType.name | trusted"></strong>'<strong ng-bind-html="r.containerType.containerGroup | trusted"></strong><br>
                            <span style="font-size:11px; color:#666;">{{r.containerType.length}} x {{r.containerType.width}} x {{r.containerType.height}} mm</span><br>
                            <span style="font-size:11px; color:#666;">{{r.containerType.workload}} kg</span>
                        </td>
                        <td><span style="background: #e1f5fe; padding: 3px 8px; border-radius: 4px; font-size: 12px;" ng-bind-html="r.loadingType.label | trusted"></span></td>
                        <td>
                            <div ng-repeat="(key, pr) in r.bestCombination.placementTypeResults" style="font-size:12px; margin-bottom: 4px;">
                                <b>{{key}}:</b> {{pr.totalQuantityForPlacementType}} pcs ({{pr.heightQuantity}} lvls)
                                <span class="error" ng-show="key == r.bestCombination.lastPosition">MIXED</span>
                            </div>
                        </td>
                        <td style="text-align:center; font-size:18px; font-weight:bold;"><span ng-bind="r.bestCombination.overallQuantity"></span></td>
                        <td style="font-size: 14px; font-weight: bold; color: #28a745;">
                            &#36;{{r.carryCost}}
                            <span ng-if="r.optimal"> <img src="${contextPath}/img/ok_button.png" width="20" style="vertical-align: middle;"/></span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div id="containerPicture" style="margin-top: 20px; border-radius: 6px; overflow: hidden; box-shadow: 0 2px 5px rgba(0,0,0,0.1);"></div>
        </div>
    </div>
</main>

<script type="text/javascript">


    app.controller("calcController", function($scope, $filter, $http, $q, NgTableParams) {

        $scope.result = [];

        $scope.packageTypes = [
        <#list packageTypes as packageType>
            {
                "id":'${packageType.cargoType.value}',
                "cargoType":"${packageType.cargoType.label}",
                "value":"${packageType.cargoType.value}",
                "shapeType":"${packageType.shapeType}",
                "packingFactor":'${packageType.packingFactor}',
                "defaultSideLaying":'${packageType.defaultSideLaying}',
                "defaultLayering":'${packageType.defaultLayering}'

            },
        </#list>
        ];


        $scope.containerTypes = [
        <#list containerTypes as containerType>
            {
                "name":"${containerType.name}",
                "containerGroup":"${containerType.containerGroup}",
                "length":"${containerType.length}",
                "width":"${containerType.width}",
                "height":"${containerType.height}",
                "specificTonnage":"${containerType.specificTonnage}",
                "workload":"${containerType.workload?string?replace(',','')}",
                "maxWorkload":"${containerType.maxWorkload}",
                "freightRate":"${containerType.freightRate}"

            },
        </#list>
        ];



        $scope.changePackageType = function() {
            $scope.cargo.sideLaying = $scope.cargo.packageType.defaultSideLaying === 'true';
            $scope.cargo.layering = $scope.cargo.packageType.defaultLayering === 'true';

        };

        $scope.redrawForContainer = function(container) {
            $scope.selectedResult = container;
            $scope.renderContainer(container);
        };

        $scope.createCargoEntity = function() {
            $scope.cargo = {
                name : '',
                dimension: {
                    width: 1000.0,
                    height: 1000.0,
                    length: 1000.0
                },
                weight: 100.0,
                sideLaying: false,
                layering: true,
                packageType: null
            };
        };

        $scope.initCalcForm = function() {
            $scope.loading = false;
            $scope.createCargoEntity();
        };

        $scope.calculate = function () {
            if ($scope.calcForm.$valid) {
                $scope.loading = true;
                if ("CYLINDER" == $scope.cargo.packageType.shapeType) {
                    $scope.cargo.dimension.length = $scope.cargo.dimension.width;
                }
                if ("PIPE" == $scope.cargo.packageType.shapeType) {
                    $scope.cargo.dimension.height = $scope.cargo.dimension.width;
                }
                $scope.cargo.packageType.cargoType = $scope.cargo.packageType.id;
                $request = {
                    'cargo': $scope.cargo,
                    'containerTypes': $scope.containerTypes
                };
                //map cargoType
                $http.post('${contextPath}/webresources/calculator/containercalc', $request).then(
                        function (res, respStatus, headers, config) {
                            $scope.loading = false;
                            $scope.result = res.data.data;

                            // АВТОМАТИЧНИЙ ЗАПУСК 3D для першого результату
                            if ($scope.result && $scope.result.length > 0) {
                                // Невелика затримка, щоб DOM встиг оновитися перед рендером
                                setTimeout(function() {
                                    $scope.redrawForContainer($scope.result[0]);
                                    $scope.$apply();
                                }, 100);
                            } else {
                                $scope.clearScene();
                            }
                        }, function error(res, respStatus, headers, config) {
                            showMessage(res.data.error.message, 'error');
                            $scope.loading = false;
                        });
            }
        };

        $scope.initCalcForm();

        var scene = new THREE.Scene();
        var camera = new THREE.PerspectiveCamera( 70, window.innerWidth / window.innerHeight, 1, 3500 );
        var renderer = new THREE.WebGLRenderer({ antialias: true });
        renderer.setSize( 1200, 600);// window.innerWidth, window.innerHeight );
        document.getElementById("containerPicture").appendChild( renderer.domElement );
        var woodTexture = new THREE.TextureLoader().load( '${contextPath}/js/3d/textures/stick.png');


        var modelsMap = [
            {name:'box',model:null},
            {name:'case',model:null},
            {name:'20DV',model:null},
            {name:'40DV',model:null},
            {name:'40HC',model:null},
            {name:'bigbag',model:null},
            {name:'pallete',model:null},
            {name:'pipe',model:null},
            {name:'sack',model:null},
            {name:'barrel',model:null}
        ];

        function findModel(name) {
            for (var i = 0; i < modelsMap.length; i++) {
                if (modelsMap[i].name === name) {
                    return modelsMap[i];
                }
            }
            return null;
        }

        function loadModel(model) {
            var loader = new THREE.ColladaLoader();
            loader.load( '${contextPath}/js/3d/models/' + model.name + '.dae', function ( result ) {
                model.model = result.scene;
            });
        }
        for (var i = 0; i< modelsMap.length; i++) {
            var model = findModel(modelsMap[i].name);
            loadModel(model);
        }
        $scope.clearScene = function() {
            //cleanup
            while(scene.children.length > 0){
                scene.remove(scene.children[0]);
            }
        };

        $scope.renderContainer = function(cargoPlacement) {

            var containerType = cargoPlacement.containerType;

            camera.fov = cargoPlacement.containerType.length / 200;
            camera.updateProjectionMatrix();

            $scope.clearScene();

            function createPipeStick(placementTypeResult, posx, posy, posz, width, useSign) {
                var geometry = new THREE.BoxGeometry( width, placementTypeResult.freeClearance / 10 / 2, 10 );
                var material = new THREE.MeshBasicMaterial( { map: woodTexture } );
                var woodstick = new THREE.Mesh( geometry, material );

                var placeSign = useSign?useSign:(posz < (width / 2) ? -1 : 1);
                woodstick.position.set(posx, posy + 2, ((containerType.width / 10 /2 - placementTypeResult.freeClearance / 10 / 4 -2) * placeSign));
                woodstick.rotation.y = Math.PI / 2;
                woodstick.rotation.z = Math.PI / 2;
                return woodstick;
            }

            function createCargoObject(cargo, dimension, textureName, posx, posy, posz) {
                var box = findModel(textureName).model.clone(true);

                box.scale.x = box.scale.x * dimension.length / 10 ;
                box.scale.y = box.scale.y * dimension.width / 10;
                box.scale.z = box.scale.z * dimension.height / 10;

                //box.castShadow = true;
                //box.receiveShadow = true;

                setInitials(box, posx - dimension.length /10 / 2, posy - dimension.height /2/10, posz + dimension.width /2/10);

                scene.add(box);
            }


            function createPipeObject(placementTypeResult, cargo, dimension, posx, posy, posz) {
                var pipe = findModel('pipe').model.clone(true);

                var length = dimension.length / 10;
                var width = dimension.width / 10;
                var height = dimension.height / 10;

                pipe.rotation.z = Math.PI / 2;

                pipe.scale.x = pipe.scale.x * width ;
                pipe.scale.y = pipe.scale.y * length;
                pipe.scale.z = pipe.scale.z * width;

                //pipe.castShadow = true;
                //pipe.receiveShadow = true;

                setInitials(pipe, posx + length / 2, posy - height /2 + 3, posz + width /2);
                scene.add(pipe);


                debugger;
                if ((posz !== 0 || ( posz === 0 && cargoPlacement.widthQuantity === 1)) && placementTypeResult.placementType.value === 'ROW') {
                    //create sticks to support pipes
                    scene.add( createPipeStick(placementTypeResult, posx + length/2 - 20, posy, posz, width) );
                    scene.add( createPipeStick(placementTypeResult, posx - length/2 + 20, posy, posz, width) );
                    scene.add( createPipeStick(placementTypeResult, posx + length/2 - 20, posy, posz, width , 1) );
                    scene.add( createPipeStick(placementTypeResult, posx - length/2 + 20, posy, posz, width, 1) );
                }

            }


            function createCargo(placementTypeResult, cargo, dimension, posx, posy, posz) {

                var obj = null;
                if (cargo.packageType.cargoType.value === 'BARREL') {
                    obj = createCargoObject(cargo, dimension, 'barrel', posx, posy, posz);
                } else if (cargo.packageType.cargoType.value === 'PIPES') {
                    obj = createPipeObject(placementTypeResult, cargo, dimension, posx, posy, posz);
                } else if (cargo.packageType.cargoType.value === 'SACK') {
                    obj = createCargoObject(cargo, dimension, 'sack', posx, posy, posz);
                } else if (cargo.packageType.cargoType.value === 'CASE') {
                    obj = createCargoObject(cargo, dimension, 'case', posx, posy, posz);
                } else if (cargo.packageType.cargoType.value === 'BIGBAG') {
                    obj = createCargoObject(cargo, dimension, 'bigbag', posx, posy, posz);
                } else if (cargo.packageType.cargoType.value === 'PALLET') {
                    obj = createCargoObject(cargo, dimension, 'pallete', posx, posy, posz);
                } else {
                    obj = createCargoObject(cargo, dimension, 'box', posx, posy, posz);
                }

                return obj;

            }

            var getBoundaryGeometry = function(obj) {
                var modelBoundingBox;

                modelBoundingBox = new THREE.Box3().setFromObject(obj);
                modelBoundingBox.size = {};
                modelBoundingBox.size.x = modelBoundingBox.max.x - modelBoundingBox.min.x;
                modelBoundingBox.size.y = modelBoundingBox.max.y - modelBoundingBox.min.y;
                modelBoundingBox.size.z = modelBoundingBox.max.z - modelBoundingBox.min.z;

                return modelBoundingBox;
            };

            function setInitials(graphObj, posx, posy, posz) {
                graphObj.position.x = posx;
                graphObj.position.y = posy;
                graphObj.position.z = posz;
            }

            function createContainer(length, width, height) {

                length = length/10;
                width = width/10;
                height = height/10;

                var containerName = containerType.name + containerType.containerGroup;
                var container = findModel(containerName).model.clone(true);

                container.rotation.z = Math.PI / 2;

                container.scale.x = container.scale.x * width * 1.02;
                container.scale.y = container.scale.y * length * 1.030;
                container.scale.z = container.scale.z * height;

                setInitials(container, length / 2 + 5, - height /2, width /2);

                scene.add(container);
            }

            function createSceneLight() {
                scene.background = new THREE.Color( 0xffffff );
                var light1 = new THREE.AmbientLight(0xffffff, 0.5);
                scene.add(light1);

                addLight(-containerType.length/10, containerType.height/10 + 50, containerType.width / 10);
                addLight(containerType.length/10, containerType.height/10 + 50, containerType.width / 10);
                addLight(-containerType.length/10, containerType.height/10 + 50, -containerType.width / 10);
                addLight(containerType.length/10, containerType.height/10 + 50, -containerType.width / 10);
            }

            function addLight(posx, posy, posz) {
                var light = new THREE.DirectionalLight(0xffffff, 0.3);
                light.position.set( posx, posy, posz);
                //light.castShadow = true;
                //light.shadowDarkness = 0.5;

                scene.add(light);
            }

            function makeControls() {
                var controls = new THREE.OrbitControls( camera , renderer.domElement);
                controls.target.set( 0, 2, 0 );

                controls.enableDamping = true;
                controls.dampingFactor = 0.01;
                controls.rotateSpeed = 0.01;
                controls.enableZoom = true;
                controls.zoomSpeed = 0.2;
                controls.enablePan = false;
                controls.update();
            }


            function animate() {
                requestAnimationFrame( animate );
                renderer.render( scene, camera );
            }



            //start basic rendering things
            createContainer(containerType.length, containerType.width, containerType.height);

            var cargo = cargoPlacement.cargo;

            //draw objects
            for (var placementTypeResultKey in cargoPlacement.bestCombination.placementTypeResults) {
                var placementTypeResult = cargoPlacement.bestCombination.placementTypeResults[placementTypeResultKey];
                for (var i=0; i<placementTypeResult.cargoPositions.length; i++) {
                    var cp = placementTypeResult.cargoPositions[i];
                    var dimension = placementTypeResult.cargoDimension;
                    createCargo(placementTypeResult, cargo, dimension, cp.x, cp.y, cp.z );
                }

            }

            camera.position.y = 250;
            camera.position.z = 1000;

            createSceneLight();

            animate();

            makeControls();
        }

    });



</script>
<#include "*/footer.ftl"/>

            function createPallete(cargo) {
                var length = (cargo.length / 10);
                var width = (cargo.width / 10);
                var height = (cargo.height / 10);

                var cover = createWoodStick(length, 5, width, 'wood.png');
                var stick1 = createWoodStick(15, 10, width, 'stick.png');
                var stick2 = createWoodStick(15, 10, width, 'stick.png');
                var stick3 = createWoodStick(15, 10, width, 'stick.png');

                var box = createWoodStick(length-10, height-10, width-10, 'crate.gif');

                var group = new THREE.Group();
                group.add( cover );
                group.add( box );
                group.add( stick1 );
                group.add( stick2 );
                group.add( stick3 );

                cover.position.set(0, -height/2+10, 0);
                stick1.position.set(-length/2+10, -height/2+5, 0);
                stick2.position.set(0, -height/2+5, 0);
                stick3.position.set(length/2-10, -height/2+5, 0);
                box.position.set(0, 6, 0);

                var vector = new THREE.Vector3();
                vector.setFromMatrixPosition( box.matrixWorld );

                group.updateMatrixWorld();

                return group;
            }

            function createWoodStick(length, height, width, textureName) {
                var texture = new THREE.TextureLoader().load( '${contextPath}/js/3d/textures/' + textureName);
                var geometry = new THREE.BoxGeometry( length, height, width );
                var material = new THREE.MeshBasicMaterial( { map: texture } );
                var woodstick = new THREE.Mesh( geometry, material );

                return woodstick;
            }


            function createCargoObject(cargo, textureName, posx, posy, posz) {

                loader.load( '${contextPath}/js/3d/models/' + textureName + '.dae', function ( result ) {
                    var box = result.scene;

                    box.scale.x = box.scale.x * cargo.length / 10 ;
                    box.scale.y = box.scale.y * cargo.height / 10;
                    box.scale.z = box.scale.z * cargo.width / 10;

                    setInitials(box, posx - cargo.length /10 / 2, posy - cargo.height /2/10, posz + cargo.width /2/10);
                    scene.add(box);
                });


                /*var length = (cargo.length / 10);
                var width = (cargo.width / 10);
                var height = (cargo.height / 10);

                var texture = new THREE.TextureLoader().load( '${contextPath}/js/3d/textures/' + textureName );

                var geometry = new THREE.BoxGeometry( length, height, width );
                var material = new THREE.MeshBasicMaterial( { map: texture } );

                var cube = new THREE.Mesh( geometry, material );
                return cube;*/
            }



            function createCylinder(cargo, textureName, posx, posy, posz) {

                /*var length = (cargo.length / 10);
                var width = (cargo.width / 10);
                var height = (cargo.height / 10);
                if (cargoPlacement.placementType.value == 'VERTICAL_LATERAL') {
                    width = cargo.height / 10;
                    height = cargo.width / 10;
                }

                var texture = new THREE.TextureLoader().load( '${contextPath}/js/3d/textures/' + textureName);

                var geometry = new THREE.CylinderGeometry( width/2, width/2, height, 32 );
                var material = new THREE.MeshBasicMaterial( { map: texture } );
                var cylinder = new THREE.Mesh( geometry, material );

                if (cargoPlacement.placementType.value == 'VERTICAL_LATERAL') {
                    cylinder.rotation.y = Math.PI / 2;
                    cylinder.rotation.z = Math.PI / 2;
                }
                return cylinder;*/


            }



            function createPipe(cargo, posz, textureName) {
                var length = (cargo.length / 10);
                var width = (cargo.width / 10);
                var height = (cargo.height / 10);

                var texture = new THREE.TextureLoader().load( '${contextPath}/js/3d/textures/' + textureName);

                var geometry = new THREE.CylinderGeometry( width/2, width/2, length, 32 );
                var geometrySmaller = new THREE.CylinderGeometry( width/2 -2, width/2 - 2, length, 32 );

                var material = new THREE.MeshBasicMaterial( { map: texture } );
                //var cylinder = new THREE.Mesh( geometry, material );

                var smallCylinderBSP = new ThreeBSP(geometrySmaller);
                var largeCylinderBSP = new ThreeBSP(geometry);
                var intersectionBSP = largeCylinderBSP.subtract(smallCylinderBSP);

                //var redMaterial = new THREE.MeshLambertMaterial( { color: 0xff0000 } );
                var hollowCylinder = intersectionBSP.toMesh( material );
                //scene.add( hollowCylinder );


                //hollowCylinder.rotation.y = 30;
                hollowCylinder.rotation.z = Math.PI / 2;
                hollowCylinder.rotation.x = Math.PI / 2;

                var group = new THREE.Group();
                group.add( hollowCylinder );

                if ((posz !== 0 || ( posz === 0 && cargoPlacement.widthQuantity === 1)) && cargoPlacement.placementType.value === 'ROW') {

                    //create sticks to support pipes
                    group.add( createPipeStick(posz, length/2-10, width) );
                    group.add( createPipeStick(posz, -length/2+10, width) );
                    if (cargoPlacement.widthQuantity === 1) { //second stick on the right
                        group.add( createPipeStick(posz, length/2-10, width, 1) );
                        group.add( createPipeStick(posz, -length/2+10, width, 1) );
                    }
                }

                var vector = new THREE.Vector3();
                vector.setFromMatrixPosition( hollowCylinder.matrixWorld );

                group.updateMatrixWorld();

                return group;
            }


            function createPipeStick(posz, posx, width, useSign) {
                var stick1 = createWoodStick(width, cargoPlacement.freeClearance / 10 / 2, 10, 'stick.png');
                var placeSign = useSign?useSign:(posz < (width / 2) ? -1 : 1);
                stick1.position.set(posx, 0, (cargoPlacement.freeClearance / 10 / 2 / 2 + width / 2) * placeSign);
                stick1.rotation.y = Math.PI / 2;
                stick1.rotation.z = Math.PI / 2;
                return stick1;
            }


/*

                var geometry = new THREE.BoxBufferGeometry(length, height, width);
                var texture = new THREE.TextureLoader().load(' {contextPath}/js/3d/textures/container.jpg');

                var pictureMaterial = new THREE.MeshBasicMaterial({map: texture, opacity: 0.3, transparent: true});

                var borderMaterial = new THREE.MeshBasicMaterial({
                    color: 0x000000,
                    side: THREE.BackSide
                });

                var materials = [pictureMaterial, // Left side
                    pictureMaterial, // Right side
                    pictureMaterial, // Top side   ---> THIS IS THE FRONT
                    borderMaterial, // Bottom side --> THIS IS THE BACK
                    pictureMaterial, // Front side
                    pictureMaterial  // Back side
                ];

                var materialSimple = new THREE.MeshLambertMaterial({
                    color: 0xffffff,
                    opacity: 0.5,
                    transparent: true
                });


                var container = new THREE.Mesh(geometry, materials);
                container.material.size = THREE.FrontSide;
                container.renderOrder = 1;

                var backSide = new THREE.Mesh(geometry, materialSimple);
                backSide.material.side = THREE.BackSide;
                backSide.renderOrder = 0;
                container.position.x = 0;
                container.position.y = 0;
                container.position.z = 0;

                scene.add(container);
                scene.add(backSide);

                //center

                var centerGeometry = new THREE.SphereGeometry( 5, 32, 32 );
                var centerMaterial = new THREE.MeshBasicMaterial({color: 0xff0000});
                var center = new THREE.Mesh(centerGeometry, centerMaterial);
                center.position.x = 0;
                center.position.y = 0;
                center.position.z = 0;

                scene.add(center);

                var leftBottom = new THREE.Mesh(centerGeometry, centerMaterial);
                leftBottom.position.x = 0 - length / 2;
                leftBottom.position.y = 0 - width / 2;
                leftBottom.position.z = 0 - height / 2;

                scene.add(leftBottom);

                var rightBottom = new THREE.Mesh(centerGeometry, centerMaterial);
                rightBottom.position.x = length / 2;
                rightBottom.position.y = 0 - width / 2;
                rightBottom.position.z = 0 - height / 2;

                scene.add(rightBottom);

*/


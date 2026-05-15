package com.tmw.tracking.web.service.containercalc;

import com.tmw.tracking.domain.containercalc.algorithm.ShapeAlgorithm;
import com.tmw.tracking.domain.containercalc.algorithm.ShapeAlgorithmFactory;
import com.tmw.tracking.domain.containercalc.entities.Cargo;
import com.tmw.tracking.domain.containercalc.entities.ContainerCalcResult;
import com.tmw.tracking.domain.containercalc.entities.PackageType;
import com.tmw.tracking.domain.containercalc.entities.PlacementTypeResult;
import com.tmw.tracking.domain.containercalc.enums.CargoType;
import com.tmw.tracking.domain.containercalc.enums.ContainerPosition;
import com.tmw.tracking.domain.containercalc.enums.LoadingType;
import com.tmw.tracking.domain.containercalc.enums.PlacementType;
import com.tmw.tracking.entity.ContainerType;
import com.tmw.tracking.service.ContainerCalcService;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import org.junit.Before;

import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


public class CalculatorAlgorithmBaseTest extends TrackingBaseUnitTest {

    private ContainerCalcService containerCalcService;
    //private final static Logger logger = LoggerFactory.getLogger(ContainerCalculatorEntitiesTest.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
        containerCalcService = injector.getInstance(ContainerCalcService.class);
    }

    private Collection<PackageType> packageTypes;


    protected PackageType getPackageType(CargoType cargoType) {
        if (packageTypes == null) {
            packageTypes = containerCalcService.getPackagetTypes();
        }

        for (PackageType t: packageTypes) {
            if (t.getCargoType() == cargoType) {
                return t;
            }
        }
        return null;
    }

    protected void checkMainPlacementQuantity(Cargo inputCargo, ContainerType inputContainerType,
                                                LoadingType loadingType, PlacementType placementType, int lengthQ, int widthQ, int heightQ, int totalQ) {

        ShapeAlgorithm shapeAlgorithm = ShapeAlgorithmFactory.createAlgorithm(
                inputCargo, inputContainerType
        );

        ContainerCalcResult result = shapeAlgorithm.calculate();
        assertNotNull(result);

        assertEquals(loadingType, result.getLoadingType());
        assertEquals(1, result.getBestCombination().getPlacementTypeResults().size());
        PlacementTypeResult placementTypeResult = result.getBestCombination().getPlacementTypeResults().get(ContainerPosition.A_MAIN);
        assertEquals(placementType, placementTypeResult.getPlacementType());
        assertEquals(new Integer(lengthQ), placementTypeResult.getLengthQuantity());
        assertEquals(new Integer(widthQ), placementTypeResult.getWidthQuantity());
        assertEquals(new Integer(heightQ), placementTypeResult.getHeightQuantity());
        assertEquals(new Integer(totalQ), result.getBestCombination().getOverallQuantity());
    }

    protected void checkThreePlacementQuantities(Cargo inputCargo, ContainerType inputContainerType,

                                        LoadingType loadingType, int totalQ,
                                               PlacementType placementTypeMain, int lengthQMain, int widthQMain, int heightQMain,
                                               PlacementType placementTypeBig, int lengthQBig, int widthQBig, int heightQBig,
                                               PlacementType placementTypeSmall, int lengthQSmall, int widthQSmall, int heightQSmall) {

        ShapeAlgorithm shapeAlgorithm = ShapeAlgorithmFactory.createAlgorithm(
                inputCargo, inputContainerType
        );

        ContainerCalcResult result = shapeAlgorithm.calculate();
        assertNotNull(result);

        assertEquals(loadingType, result.getLoadingType());
        assertEquals(3, result.getBestCombination().getPlacementTypeResults().size());
        PlacementTypeResult placementTypeResultMain = result.getBestCombination().getPlacementTypeResults().get(ContainerPosition.A_MAIN);
        assertEquals(placementTypeMain, placementTypeResultMain.getPlacementType());
        assertEquals(new Integer(lengthQMain), placementTypeResultMain.getLengthQuantity());
        assertEquals(new Integer(widthQMain), placementTypeResultMain.getWidthQuantity());
        assertEquals(new Integer(heightQMain), placementTypeResultMain.getHeightQuantity());

        PlacementTypeResult placementTypeResultBig = result.getBestCombination().getPlacementTypeResults().get(ContainerPosition.B_BIG);
        assertEquals(placementTypeBig, placementTypeResultBig.getPlacementType());
        assertEquals(new Integer(lengthQBig), placementTypeResultBig.getLengthQuantity());
        assertEquals(new Integer(widthQBig), placementTypeResultBig.getWidthQuantity());
        assertEquals(new Integer(heightQBig), placementTypeResultBig.getHeightQuantity());

        PlacementTypeResult placementTypeResultSmall = result.getBestCombination().getPlacementTypeResults().get(ContainerPosition.C_SMALL);
        assertEquals(placementTypeSmall, placementTypeResultSmall.getPlacementType());
        assertEquals(new Integer(lengthQSmall), placementTypeResultSmall.getLengthQuantity());
        assertEquals(new Integer(widthQSmall), placementTypeResultSmall.getWidthQuantity());
        assertEquals(new Integer(heightQSmall), placementTypeResultSmall.getHeightQuantity());

        assertEquals(new Integer(totalQ), result.getBestCombination().getOverallQuantity());

    }


    protected void checkMainBigPlacementQuantities(Cargo inputCargo, ContainerType inputContainerType,

                                        LoadingType loadingType, int totalQ,
                                               PlacementType placementTypeMain, int lengthQMain, int widthQMain, int heightQMain,
                                               PlacementType placementTypeBig, int lengthQBig, int widthQBig, int heightQBig) {

        ShapeAlgorithm shapeAlgorithm = ShapeAlgorithmFactory.createAlgorithm(
                inputCargo, inputContainerType
        );

        ContainerCalcResult result = shapeAlgorithm.calculate();
        assertNotNull(result);

        assertEquals(loadingType, result.getLoadingType());
        assertEquals(2, result.getBestCombination().getPlacementTypeResults().size());
        PlacementTypeResult placementTypeResultMain = result.getBestCombination().getPlacementTypeResults().get(ContainerPosition.A_MAIN);
        assertEquals(placementTypeMain, placementTypeResultMain.getPlacementType());
        assertEquals(new Integer(lengthQMain), placementTypeResultMain.getLengthQuantity());
        assertEquals(new Integer(widthQMain), placementTypeResultMain.getWidthQuantity());
        assertEquals(new Integer(heightQMain), placementTypeResultMain.getHeightQuantity());

        PlacementTypeResult placementTypeResultBig = result.getBestCombination().getPlacementTypeResults().get(ContainerPosition.B_BIG);
        assertEquals(placementTypeBig, placementTypeResultBig.getPlacementType());
        assertEquals(new Integer(lengthQBig), placementTypeResultBig.getLengthQuantity());
        assertEquals(new Integer(widthQBig), placementTypeResultBig.getWidthQuantity());
        assertEquals(new Integer(heightQBig), placementTypeResultBig.getHeightQuantity());

        assertEquals(new Integer(totalQ), result.getBestCombination().getOverallQuantity());

    }


    protected void checkMainSmallPlacementQuantities(Cargo inputCargo, ContainerType inputContainerType,

                                        LoadingType loadingType, int totalQ,
                                               PlacementType placementTypeMain, int lengthQMain, int widthQMain, int heightQMain,
                                               PlacementType placementTypeSmall, int lengthQSmall, int widthQSmall, int heightQSmall) {

        ShapeAlgorithm shapeAlgorithm = ShapeAlgorithmFactory.createAlgorithm(
                inputCargo, inputContainerType
        );

        ContainerCalcResult result = shapeAlgorithm.calculate();
        assertNotNull(result);

        assertEquals(loadingType, result.getLoadingType());
        assertEquals(2, result.getBestCombination().getPlacementTypeResults().size());
        PlacementTypeResult placementTypeResultMain = result.getBestCombination().getPlacementTypeResults().get(ContainerPosition.A_MAIN);
        assertEquals(placementTypeMain, placementTypeResultMain.getPlacementType());
        assertEquals(new Integer(lengthQMain), placementTypeResultMain.getLengthQuantity());
        assertEquals(new Integer(widthQMain), placementTypeResultMain.getWidthQuantity());
        assertEquals(new Integer(heightQMain), placementTypeResultMain.getHeightQuantity());

        PlacementTypeResult placementTypeResultSmall = result.getBestCombination().getPlacementTypeResults().get(ContainerPosition.C_SMALL);
        assertEquals(placementTypeSmall, placementTypeResultSmall.getPlacementType());
        assertEquals(new Integer(lengthQSmall), placementTypeResultSmall.getLengthQuantity());
        assertEquals(new Integer(widthQSmall), placementTypeResultSmall.getWidthQuantity());
        assertEquals(new Integer(heightQSmall), placementTypeResultSmall.getHeightQuantity());

        assertEquals(new Integer(totalQ), result.getBestCombination().getOverallQuantity());

    }

}

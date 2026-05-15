package com.tmw.tracking.web.service.containercalc;

import com.tmw.tracking.domain.containercalc.entities.Cargo;
import com.tmw.tracking.domain.containercalc.entities.Dimension;
import com.tmw.tracking.domain.containercalc.enums.CargoType;
import com.tmw.tracking.domain.containercalc.enums.LoadingType;
import com.tmw.tracking.domain.containercalc.enums.PlacementType;
import com.tmw.tracking.entity.ContainerType;
import com.tmw.tracking.entity.enums.ContainerGroup;
import com.tmw.tracking.service.ContainerCalcService;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

@Category(TrackingBaseUnitTest.class)
public class CalculatorAlgorithmCylinderTest extends CalculatorAlgorithmBaseTest {

    private ContainerCalcService containerCalcService;
    private final static Logger logger = LoggerFactory.getLogger(ContainerCalculatorEntitiesTest.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
        containerCalcService = injector.getInstance(ContainerCalcService.class);
    }

    @Test
    public void testTC_Barrel_1000() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Barrel").
                setPackageType(getPackageType(CargoType.BARREL)).
                setDimension(new Dimension(1000d, 1000d, 1000d)).
                setWeight(100d).
                setLayering(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.ROW, 5, 2, 2, 20);

    }

    @Test
    public void testTC_Barrel_950() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Barrel").
                setPackageType(getPackageType(CargoType.BARREL)).
                setDimension(new Dimension(950d, 950d, 1000d)).
                setWeight(100d).
                setLayering(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.ROW_WIDTH_ALIGN, 6, 2, 2, 24);

    }

    @Test
    public void testTC_Barrel_500() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Barrel").
                setPackageType(getPackageType(CargoType.BARREL)).
                setDimension(new Dimension(500d, 500d, 1000d)).
                setWeight(100d).
                setLayering(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.ROW_WIDTH_ALIGN, 12, 4, 2, 96);

    }

    @Test
    public void testTC_Barrel_950_Weight() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Barrel").
                setPackageType(getPackageType(CargoType.BARREL)).
                setDimension(new Dimension(950d, 950d, 1000d)).
                setWeight(2000d).
                setLayering(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_WEIGHT, PlacementType.ROW_WIDTH_ALIGN, 6, 2, 2, 13);

    }


    @Test
    public void testTC_Barrel_1000_Weight() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Barrel").
                setPackageType(getPackageType(CargoType.BARREL)).
                setDimension(new Dimension(1000d, 1000d, 1000d)).
                setWeight(2000d).
                setLayering(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_WEIGHT, PlacementType.ROW, 5, 2, 2, 13);

    }

    @Test
    public void testTC_Barrel_1000_NoLayering() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Barrel").
                setPackageType(getPackageType(CargoType.BARREL)).
                setDimension(new Dimension(1000d, 1000d, 1000d)).
                setWeight(100d).
                setLayering(false).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.ROW, 5, 2, 1, 10);

    }


    @Test
    public void testTC_Barrel_950_NoLayering() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Barrel").
                setPackageType(getPackageType(CargoType.BARREL)).
                setDimension(new Dimension(950d, 950d, 1000d)).
                setWeight(100d).
                setLayering(false).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.ROW_WIDTH_ALIGN, 6, 2, 1, 12);

    }

    @Test
    public void testTC_Barrel_950_Weight_NoLayering() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Barrel").
                setPackageType(getPackageType(CargoType.BARREL)).
                setDimension(new Dimension(950d, 950d, 1000d)).
                setWeight(5000d).
                setLayering(false).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_WEIGHT, PlacementType.ROW_WIDTH_ALIGN, 6, 2, 1, 5);

    }

    @Test
    public void testTC_Barrel_1000_Weight_NoLayering() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Barrel").
                setPackageType(getPackageType(CargoType.BARREL)).
                setDimension(new Dimension(1000d, 1000d, 1000d)).
                setWeight(5000d).
                setLayering(false).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_WEIGHT, PlacementType.ROW, 5, 2, 1, 5);

    }


    @Test
    public void testTC_Barrel_HugeAmount() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Barrel").
                setPackageType(getPackageType(CargoType.BARREL)).
                setDimension(new Dimension(200d, 200d, 500d)).
                setWeight(10d).
                setLayering(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        long start = System.currentTimeMillis();
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.ROW_WIDTH_ALIGN, 32, 11, 4, 1408);
        long end = System.currentTimeMillis();
        assertTrue( (end - start) < 1000);

    }


}

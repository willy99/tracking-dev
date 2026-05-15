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
public class CalculatorAlgorithmPipesTest extends CalculatorAlgorithmBaseTest {

    private ContainerCalcService containerCalcService;
    private final static Logger logger = LoggerFactory.getLogger(ContainerCalculatorEntitiesTest.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
        containerCalcService = injector.getInstance(ContainerCalcService.class);
    }

    @Test
    public void testTC_Pipes_1000() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Pipe").
                setPackageType(getPackageType(CargoType.PIPES)).
                setDimension(new Dimension(1000d, 1000d, 1000d)).
                setWeight(100d).
                setLayering(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.ROW, 5, 2, 2, 20);

    }

        @Test
    public void testTC_Pipes_1000_Long() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Pipe").
                setPackageType(getPackageType(CargoType.PIPES)).
                setDimension(new Dimension(4000d, 1000d, 1000d)).
                setWeight(100d).
                setLayering(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.ROW, 1, 2, 2, 4);

    }
    @Test
    public void testTC_Pipes_950_Long() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Pipe").
                setPackageType(getPackageType(CargoType.PIPES)).
                setDimension(new Dimension(4000d, 950d, 950d)).
                setWeight(100d).
                setLayering(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.ROW_LAYER_ALIGN, 1, 2, 3, 5);

    }
    @Test
    public void testTC_Pipes_200_Long() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Pipe").
                setPackageType(getPackageType(CargoType.PIPES)).
                setDimension(new Dimension(4000d, 200d, 200d)).
                setWeight(100d).
                setLayering(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.ROW_LAYER_ALIGN, 1, 11, 13, 137);

    }
    @Test
    public void testTC_Pipes_200_Long_Weight() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Pipe").
                setPackageType(getPackageType(CargoType.PIPES)).
                setDimension(new Dimension(4000d, 200d, 200d)).
                setWeight(2000d).
                setLayering(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_WEIGHT, PlacementType.ROW_LAYER_ALIGN, 1, 11, 2, 13);

    }
    @Test
    public void testTC_Pipes_Weight_NoLayering() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Pipe").
                setPackageType(getPackageType(CargoType.PIPES)).
                setDimension(new Dimension(4000d, 200d, 200d)).
                setWeight(2000d).
                setLayering(false).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_WEIGHT, PlacementType.ROW_LAYER_ALIGN, 1, 11, 1, 11);

    }

    @Test
    public void testTC_Pipe_HugeAmount() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Pipe").
                setPackageType(getPackageType(CargoType.PIPES)).
                setDimension(new Dimension(500d, 100d, 100d)).
                setWeight(1d).
                setLayering(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        long start = System.currentTimeMillis();
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.ROW_LAYER_ALIGN, 11, 23, 27, 6688);
        long end = System.currentTimeMillis();
        assertTrue( (end - start) < 1000);

    }


}

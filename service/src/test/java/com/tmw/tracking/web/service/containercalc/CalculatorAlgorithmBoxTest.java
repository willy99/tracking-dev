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

import static org.junit.Assert.assertTrue;

@Category(TrackingBaseUnitTest.class)
public class CalculatorAlgorithmBoxTest extends CalculatorAlgorithmBaseTest {

    private ContainerCalcService containerCalcService;
    //private final static Logger logger = LoggerFactory.getLogger(ContainerCalculatorEntitiesTest.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
        containerCalcService = injector.getInstance(ContainerCalcService.class);
    }

    @Test
    public void testTC_StandartBox() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Box").
                setPackageType(getPackageType(CargoType.BOX)).
                setDimension(new Dimension(1000d, 1000d, 1000d)).
                setWeight(100d).
                setLayering(true).
                setSideLaying(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.LATERAL, 5, 2, 2, 20);


    }

    @Test
    public void testTC_NarrowBox() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Box").
                setPackageType(getPackageType(CargoType.BOX)).
                setDimension(new Dimension(1000d, 300d, 1000d)).
                setWeight(100d).
                setLayering(true).
                setSideLaying(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkThreePlacementQuantities(cargo, containerType,
                LoadingType.BY_VOLUME, 88,
                PlacementType.EDGE_LATERAL, 5, 2, 7,
                PlacementType.LONGITUDINAL, 5, 1, 2,
                PlacementType.LATERAL, 2, 2, 2);


    }

    @Test
    public void testTC_HugeLongBulks() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Box").
                setPackageType(getPackageType(CargoType.BOX)).
                setDimension(new Dimension(5500d, 900d, 600d)).
                setWeight(100d).
                setLayering(true).
                setSideLaying(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.LONGITUDINAL, 1, 2, 3, 6);

    }

    @Test
    public void testTC_LongTallBulks() {
            Cargo cargo = new Cargo.CargoBuilder().
                    setName("Box").
                    setPackageType(getPackageType(CargoType.BOX)).
                    setDimension(new Dimension(5000d, 100d, 1000d)).
                    setWeight(100d).
                    setLayering(true).
                    setSideLaying(true).
                    build();

            ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
            checkMainBigPlacementQuantities(cargo, containerType,
                    LoadingType.BY_VOLUME, 52,
                    PlacementType.EDGE_LONGITUDINAL, 1, 2, 23,
                    PlacementType.LONGITUDINAL, 1, 3, 2);

    }

    @Test
    public void testTC_FlatTiles_NoLayering() {
            Cargo cargo = new Cargo.CargoBuilder().
                    setName("Box").
                    setPackageType(getPackageType(CargoType.BOX)).
                    setDimension(new Dimension(1000d, 1000d, 300d)).
                    setWeight(100d).
                    setLayering(false).
                    setSideLaying(true).
                    build();

            ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
            checkMainBigPlacementQuantities(cargo, containerType,
                    LoadingType.BY_VOLUME, 41,
                    PlacementType.EDGE_LATERAL, 18, 2, 1,
                    PlacementType.EDGE_LONGITUDINAL, 5, 1, 1);

    }


    @Test
    public void testTC_HeteroLoading() {
         Cargo cargo = new Cargo.CargoBuilder().
                setName("Box").
                setPackageType(getPackageType(CargoType.BOX)).
                setDimension(new Dimension(2000d, 500d, 300d)).
                setWeight(100d).
                setLayering(true).
                setSideLaying(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainBigPlacementQuantities(cargo, containerType,
                LoadingType.BY_VOLUME, 88,
                PlacementType.LATERAL, 11, 1, 7,
                PlacementType.VERTICAL_LONGITUDINAL, 11, 1, 1);

        ContainerType containerType40DV = containerCalcService.getByNameAndContainerGroup("40", ContainerGroup.DV);
        checkMainBigPlacementQuantities(cargo, containerType40DV,
                LoadingType.BY_VOLUME, 184,
                PlacementType.LATERAL, 23, 1, 7,
                PlacementType.VERTICAL_LONGITUDINAL, 23, 1, 1);

        ContainerType containerType40HC = containerCalcService.getByNameAndContainerGroup("40", ContainerGroup.HC);
        checkMainSmallPlacementQuantities(cargo, containerType40HC,
                LoadingType.BY_VOLUME, 200,
                PlacementType.EDGE_LONGITUDINAL, 5, 7, 5,
                PlacementType.EDGE_LATERAL, 5, 1, 5);

    }

    @Test
    public void testTC_FlatTiles_Big_Weigh() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Box").
                setPackageType(getPackageType(CargoType.BOX)).
                setDimension(new Dimension(1500d, 2000d, 300d)).
                setWeight(2000d).
                setLayering(true).
                setSideLaying(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_WEIGHT, PlacementType.LONGITUDINAL, 3, 1, 5, 13);

    }

    @Test
    public void testTC_FlatLongBulks_Big_Weigh() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Box").
                setPackageType(getPackageType(CargoType.BOX)).
                setDimension(new Dimension(5000d, 1000d, 300d)).
                setWeight(2000d).
                setLayering(true).
                setSideLaying(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_WEIGHT, PlacementType.LONGITUDINAL, 1, 2, 7, 13);

    }

    @Test
    public void testTC_FLatTiles_Big_Weigh_NoLayering() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Box").
                setPackageType(getPackageType(CargoType.BOX)).
                setDimension(new Dimension(1000d, 1000d, 300d)).
                setWeight(2000d).
                setLayering(false).
                setSideLaying(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_WEIGHT, PlacementType.EDGE_LATERAL, 18, 2, 1, 13);

    }

    @Test
    public void testTC_FlatLongBulks_Big_Weigh_NoLayering() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Box").
                setPackageType(getPackageType(CargoType.BOX)).
                setDimension(new Dimension(5000d, 1000d, 300d)).
                setWeight(2000d).
                setLayering(false).
                setSideLaying(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_WEIGHT, PlacementType.EDGE_LONGITUDINAL, 1, 7, 1, 7);

    }

    @Test
    public void testTC_Tiles_NoLayering_NoSide() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Box").
                setPackageType(getPackageType(CargoType.BOX)).
                setDimension(new Dimension(1000d, 300d, 1000d)).
                setWeight(100d).
                setLayering(false).
                setSideLaying(false).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.LATERAL, 18, 2, 1, 36);

    }

    @Test
    public void testTC_Boxes_NoLayering_NoSide() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Box").
                setPackageType(getPackageType(CargoType.BOX)).
                setDimension(new Dimension(1000d, 300d, 300d)).
                setWeight(100d).
                setLayering(false).
                setSideLaying(false).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_WEIGHT, PlacementType.LATERAL, 18, 2, 1, 36);

    }


    @Test
    public void testTC_Boxes_Big_Weigh_NoLayering_NoSide() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Box").
                setPackageType(getPackageType(CargoType.BOX)).
                setDimension(new Dimension(1000d, 300d, 300d)).
                setWeight(2000d).
                setLayering(false).
                setSideLaying(false).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_WEIGHT, PlacementType.LATERAL, 18, 2, 1, 13);

    }

    @Test
    public void testTC_FlatTiles() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Box").
                setPackageType(getPackageType(CargoType.BOX)).
                setDimension(new Dimension(1500d, 2000d, 300d)).
                setWeight(100d).
                setLayering(true).
                setSideLaying(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        checkThreePlacementQuantities(cargo, containerType,
                LoadingType.BY_VOLUME, 27,
                PlacementType.LONGITUDINAL, 3, 1, 7,
                PlacementType.EDGE_LONGITUDINAL, 3, 1, 1,
                PlacementType.EDGE_LATERAL, 3, 1, 1);


    }

    @Test
    public void testTC_HugeAmount() {

        Cargo cargo = new Cargo.CargoBuilder().
                setName("Box").
                setPackageType(getPackageType(CargoType.BOX)).
                setDimension(new Dimension(200d, 200d, 200d)).
                setWeight(1d).
                setLayering(true).
                setSideLaying(true).
                build();

        ContainerType containerType = containerCalcService.getByNameAndContainerGroup("20", ContainerGroup.DV);
        long start = System.currentTimeMillis();
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.LATERAL, 28, 11, 11, 3388);
        long end = System.currentTimeMillis();
        assertTrue( (end - start) < 1000);


        containerType = containerCalcService.getByNameAndContainerGroup("40", ContainerGroup.HC);
        start = System.currentTimeMillis();
        checkMainPlacementQuantity(cargo, containerType,
                LoadingType.BY_VOLUME, PlacementType.LATERAL, 58, 11, 12, 7656);
        end = System.currentTimeMillis();
        assertTrue( (end - start) < 1000);


    }

}

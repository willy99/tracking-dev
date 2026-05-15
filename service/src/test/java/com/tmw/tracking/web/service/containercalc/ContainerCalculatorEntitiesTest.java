package com.tmw.tracking.web.service.containercalc;

import com.tmw.tracking.domain.containercalc.entities.Cargo;
import com.tmw.tracking.domain.containercalc.entities.Dimension;
import com.tmw.tracking.domain.containercalc.enums.CargoType;
import com.tmw.tracking.entity.ContainerType;
import com.tmw.tracking.service.ContainerCalcService;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static org.junit.Assert.*;

@Category(TrackingBaseUnitTest.class)
public class ContainerCalculatorEntitiesTest extends CalculatorAlgorithmBaseTest {

    private ContainerCalcService containerCalcService;
    private final static Logger logger = LoggerFactory.getLogger(ContainerCalculatorEntitiesTest.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
        containerCalcService = injector.getInstance(ContainerCalcService.class);
    }

    @Test
    public void testCreationOfCargo() {

        Cargo box = new Cargo.CargoBuilder().
                        setName("Box").
                        setSideLaying(true).
                        setLayering(true).
                        setPackageType(getPackageType(CargoType.BOX)).
                        setDimension(new Dimension(1000d, 900d, 180d)).
                        setWeight(800d).build();

        assertEquals(box.getLength(), new Double(1000));
        assertEquals(box.getWidth(), new Double(900));
        assertEquals(box.getHeight(), new Double(180));
        assertEquals(box.getWeight(), new Double(800));
        assertEquals(box.getPackageType().getCargoType(), CargoType.BOX);
        assertEquals(box.getDiameter(), box.getDimension().getWidth()); //default equals
        assertEquals(box.getVolume(), new Double(box.getLength() * box.getWidth() * box.getHeight()));
   }

   @Test(expected = RuntimeException.class)
   public void testCreationInvalidCargoNoLayeringParameter() {
       Cargo invalid = new Cargo.CargoBuilder().
                        setName("Box").
                        setSideLaying(true).
                        setPackageType(getPackageType(CargoType.BOX)).
                        setDimension(new Dimension(1000d, 1000d, 1000d)).
                        setWeight(800d).build();
   }

   @Test
   public void testCreationInvalidCargoNoSideLaying() {
       Cargo invalid = new Cargo.CargoBuilder().
                        setName("Box").
                        setLayering(true).
                        setPackageType(getPackageType(CargoType.BOX)).
                        setDimension(new Dimension(1000d, 1000d, 1000d)).
                        setWeight(800d).build();
       //it will pass as sidelaying is not mandatory for every types

   }

   @Test(expected = RuntimeException.class)
   public void testCreationInvalidCargoNoPackageType() {
       Cargo invalid = new Cargo.CargoBuilder().
                        setName("Box").
                        setSideLaying(true).
                        setLayering(true).
                        setDimension(new Dimension(1000d, 1000d, 1000d)).
                        setWeight(800d).build();
   }

   @Test
   public void testGettingParametersForCylinderTypes() {
       Cargo cylinder = new Cargo.CargoBuilder().
               setName("Cylinder").
               setSideLaying(true).
               setLayering(true).
               setPackageType(getPackageType(CargoType.PIPES)).
               setDimension(new Dimension(1000d, 650d, 500d)).
               setWeight(800d).build();

       Double diameter = cylinder.getDiameter();
       Double width = cylinder.getWidth();
       assertEquals(diameter, width);
       assertEquals(CargoType.PIPES, cylinder.getPackageType().getCargoType());
   }

    @Test
    public void testContainerTypeVolumes() {
        final Collection<ContainerType> containerTypes = containerCalcService.getContainerTypes();
        for (ContainerType containerType: containerTypes) {
            //check volume
            Double volume = containerType.getVolume()/1000000000;
            logger.debug("volume = " + volume + " name = " + containerType.getName());
            if ("40'HC".equals(containerType.getName())) {
                assertTrue(75.3d>volume && 73d<volume);
            }
            if ("20'DV".equals(containerType.getName())) {
                assertTrue(33.1d>volume && 32d<volume);
            }
            if ("40'DV".equals(containerType.getName())) {
                assertTrue(67.5d>volume && 67d<volume);
            }
        }
        assertNotNull(containerTypes);
    }

}

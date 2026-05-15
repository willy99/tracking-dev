package com.tmw.tracking.domain.containercalc.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tmw.tracking.domain.I18NEnum;
import com.tmw.tracking.utils.I18NService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.EnumSet;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PlacementType implements I18NEnum {
    LATERAL, LONGITUDINAL,
    EDGE_LATERAL, EDGE_LONGITUDINAL,
    VERTICAL_LATERAL, VERTICAL_LONGITUDINAL,

    ROW, ROW_WIDTH_ALIGN, ROW_LENGTH_ALIGN, ROW_LAYER_ALIGN;

    private I18NService i18NService;

    public void setI18NService(I18NService i18NService) {
        this.i18NService = i18NService;
    }

    @Override
    public String getValue() {
        return this.name();
    }

    @Override
    public String getLabel() {
        return i18NService.getValue((DICT_PR + this.getClass().getSimpleName() + '_' + this.name()).toLowerCase() );
    }

    @Singleton
    public static class PlacementTypeInjector {

        @Inject
        public PlacementTypeInjector(I18NService i18NService) {
            for (PlacementType rt : EnumSet.allOf(PlacementType.class))
                rt.setI18NService(i18NService);
        }

    }

}

package com.tmw.tracking.domain.containercalc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tmw.tracking.domain.I18NEnum;
import com.tmw.tracking.utils.I18NService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.EnumSet;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CargoType implements I18NEnum {
    PIPES, BOX, BARREL, SACK, BIGBAG, CASE, PALLET;
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
    public static class CargoTypeInjector {

        @Inject
        public CargoTypeInjector(I18NService i18NService) {
            for (CargoType rt : EnumSet.allOf(CargoType.class))
                rt.setI18NService(i18NService);
        }

    }

}


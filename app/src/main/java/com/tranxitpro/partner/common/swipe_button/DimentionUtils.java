package com.tranxitpro.partner.common.swipe_button;

import android.content.Context;

final class DimentionUtils {

    private DimentionUtils() {
    }

    static float convetPixelsToSp(float px, Context context) {
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }
}

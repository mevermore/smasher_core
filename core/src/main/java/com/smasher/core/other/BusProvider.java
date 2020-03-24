package com.smasher.core.other;

import com.squareup.otto.Bus;


/**
 * @author moyu
 */
public class BusProvider {
    private static final Bus mInstance = new Bus();

    public static Bus getInstance() {
        return mInstance;
    }

    private BusProvider() {
    }
}

package com.bifanas.application.receipts.model;

import org.jeasy.random.EasyRandom;

public class UpdateTotalSample {
    public static UpdateTotal anyUpdateTotal() {
        EasyRandom generator = new EasyRandom();
        return generator.nextObject(UpdateTotal.class);
    }
}

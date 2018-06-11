package com.minelittlepony.model.anim;

import java.util.HashMap;
import java.util.Map;

public class BasicEasingInterpolator implements IInterpolator {

    private final Map<String, Float> properties = new HashMap<String, Float>();

    private float getLast(String key, float to) {
        if (properties.containsKey(key)) {
            return properties.get(key);
        }

        return to;
    }

    @Override
    public float interpolate(String key, float to, float scalingFactor) {
        float from = getLast(key, to);

        from += (to - from) / scalingFactor;

        properties.put(key, from);

        return from;

    }

}

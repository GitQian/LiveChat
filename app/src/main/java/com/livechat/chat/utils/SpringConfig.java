package com.livechat.chat.utils;

public class SpringConfig {

    public double friction;
    public double tension;

    public static SpringConfig defaultConfig = SpringConfig.fromOrigamiTensionAndFriction(40, 7);

    public SpringConfig(double tension, double friction) {
        this.tension = tension;
        this.friction = friction;
    }

    public static SpringConfig fromOrigamiTensionAndFriction(double qcTension, double qcFriction) {
        return new SpringConfig(OrigamiValueConverter.tensionFromOrigamiValue(qcTension),
                OrigamiValueConverter.frictionFromOrigamiValue(qcFriction));
    }

}

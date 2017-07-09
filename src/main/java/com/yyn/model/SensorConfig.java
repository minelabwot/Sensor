package com.yyn.model;

/**
 * Created by koala on 2017/6/19.
 */
public class SensorConfig {
    private String name;
    private String high;
    private String low;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    @Override
    public String toString() {
        return "SensorConfig{" +
                "name='" + name + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                '}';
    }
}

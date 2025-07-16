package org.pofay.jcip_example.chapter3.assignment;

public class NonVolatileConfigHolder {

    private AppConfig currentConfiguration;

    public NonVolatileConfigHolder(AppConfig appConfig) {
        this.currentConfiguration = appConfig;
    }

    public AppConfig get() {
        return currentConfiguration;
    }

    public void updateConfiguration(AppConfig newConfiguration)  {
        this.currentConfiguration = newConfiguration;
    }
}

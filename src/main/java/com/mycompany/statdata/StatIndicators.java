package com.mycompany.statdata;

import static java.lang.Math.sqrt;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.StatUtils;
import java.util.HashMap;
import java.util.Map;

public class StatIndicators {

    public static String[] namesStatIndicators() {
        String[] names = {"mean", "geometric mean", "min", "max", "N", "R", "variance",
            "standard deviation", "K variance", "Confidence interval"};
        return names;
    }

    public static Map<String, Object> calculateStatIndicators(double[] data) {
        double mean = StatUtils.mean(data);
        double geomMean = StatUtils.geometricMean(data);
        int N = data.length; 
        double R = StatUtils.max(data) - StatUtils.min(data);
        double min = StatUtils.min(data);
        double max = StatUtils.max(data);
        double var = StatUtils.variance(data);
        double sd = sqrt(StatUtils.variance(data));
        String k_var = String.valueOf(sqrt(StatUtils.variance(data)) * 100 / StatUtils.mean(data))+"%";
       
        Map<String, Object> results = new HashMap<>();
        results.put("mean", mean);
        results.put("geometric mean", geomMean);
        results.put("min", min);
        results.put("max", max);
        results.put("N", N);
        results.put("R", R);
        results.put("variance", var);
        results.put("standard deviation", sd);
        results.put("K variance", k_var);
        results.put("Confidence interval", confInterval(data, StatUtils.mean(data), StatUtils.variance(data), N));

        return results;
    }

    private static String confInterval(double[] data, double mean, double var, int N) {
        double confidenceLevel = 0.95;
        // Т-критерий
        TDistribution tDist = new TDistribution(N - 1);
        double t = tDist.inverseCumulativeProbability((1 + confidenceLevel) / 2);

        double marginOfError = t * sqrt(var / N);
        double lowBorder = mean - marginOfError;
        double upperBorder = mean + marginOfError;

        return "[" + lowBorder + ", " + upperBorder + "]";
    }

}

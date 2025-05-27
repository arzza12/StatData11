
package com.mycompany.statdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.stat.correlation.Covariance;


public class StatTable {
    public static List<Map<String, Object>> generateStatTable(Map<String, double[]> dataSets) {        
        List<Map<String, Object>> table = new ArrayList<>();
      
       for (Map.Entry<String, double[]> entry : dataSets.entrySet()) {
            String sampleName = entry.getKey();
            double[] sample = entry.getValue();
            
            Map<String, Object> stats = StatIndicators.calculateStatIndicators(sample);
            
            stats.put("sample", sampleName);
          
            table.add(stats);
        }
        return table;
    }
    
    public static List<Map<String, Object>> generateTableK_Cov(Map<String, double[]> dataSets) {
        List<Map<String, Object>> table = new ArrayList<>();
        Covariance covarianceCalculator = new Covariance();

        List<String> keys = new ArrayList<>(dataSets.keySet());

        for (String rowKey : keys) {
            Map<String, Object> row = new HashMap<>();

            for (String colKey : keys) {
                double[] data1 = dataSets.get(rowKey);
                double[] data2 = dataSets.get(colKey);
                if (data1 == null || data2 == null || data1.length != data2.length){
                    row.put(colKey,"-");
                    continue;
                }
                double cov = covarianceCalculator.covariance(data1, data2);
                row.put(colKey, cov);  
            }

            row.put("sample", rowKey);

            table.add(row);
        }
        return table;
    }

}

package com.pkg.occasion.api.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ChartStat {
    public List<String> labels = new ArrayList<>();
    public List<Integer> data = new ArrayList<>();

    public ChartStat(List<MonthCount> counts){
        for (MonthCount monthCount : counts) {
            labels.add(monthCount.getNom());
            data.add(monthCount.getNb());
        }
    }

    public ChartStat(List<AnnonceCount> counts , String a){
        for (AnnonceCount annonceCount : counts) {
            labels.add(annonceCount.getNom());
            data.add(annonceCount.getNb());
        }
    }
}

package com.shardingsphere.test.algorithm;

import com.google.common.collect.Range;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class UserDatabaseAlgorithm implements StandardShardingAlgorithm<Date> {

    private final static String DATA_BASE_NAME = "db";

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Date> preciseShardingValue) {
        if (preciseShardingValue != null) {
            Date value = preciseShardingValue.getValue();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDate localDate = value.toInstant().atZone(zoneId).toLocalDate();
            int year = localDate.getYear();
            for (String each : collection) {
                System.out.println("each:" + each + " year:"+ year);
                if (each.equals(DATA_BASE_NAME + String.valueOf(year % 2))) {
                    return each;
                }
            }
            throw new UnsupportedOperationException("没有匹配到可用的数据库节点");
        } else {
            throw new UnsupportedOperationException("分片列为空");
        }
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Date> rangeShardingValue) {
        Collection<String> collect = new ArrayList<>();
        Range<Date> range = rangeShardingValue.getValueRange();
        Date lowerValue = range.lowerEndpoint();
        Date upperValue = range.upperEndpoint();

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate lowerLocalDate = lowerValue.toInstant().atZone(zoneId).toLocalDate();
        LocalDate upeerLocalDate = upperValue.toInstant().atZone(zoneId).toLocalDate();
        int lowerYear = lowerLocalDate.getYear();
        int upeerYear = upeerLocalDate.getYear();

        for (String each : collection) {
            for (int i = lowerYear; i <= upeerYear; i++) {
                if (each.equals(DATA_BASE_NAME + String.valueOf(i % 2))) {
                    if(!collect.contains(each)) {
                        collect.add(each);
                    }
                }
            }
        }
        return collect;
    }

    @Override
    public void init() {

    }

    @Override
    public String getType() {
        return null;
    }
}

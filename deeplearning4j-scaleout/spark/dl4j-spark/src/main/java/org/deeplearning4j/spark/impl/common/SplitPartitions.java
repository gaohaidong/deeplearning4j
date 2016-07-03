package org.deeplearning4j.spark.impl.common;

import lombok.AllArgsConstructor;
import org.apache.spark.api.java.function.Function2;

import java.util.*;

/**
 * Created by Alex on 02/07/2016.
 */
@AllArgsConstructor
public class SplitPartitions<T> implements Function2<Integer, Iterator<T>, Iterator<T>> {
    private final int splitIndex;
    private final int numSplits;
    private final long baseRngSeed;

    @Override
    public Iterator<T> call(Integer v1, Iterator<T> iter) throws Exception {
        long thisRngSeed = baseRngSeed + v1;

        Random r = new Random(thisRngSeed);
        List<Integer> list = new ArrayList<>();
        for( int i=0; i<numSplits; i++ ){
            list.add(i);
        }

        List<T> outputList = new ArrayList<>();
        int i=0;
        while(iter.hasNext()){
            T next = iter.next();
            if(i%numSplits == 0) Collections.shuffle(list, r);

            if(i%numSplits == splitIndex) outputList.add(next);
            i++;
        }

        return outputList.iterator();
    }
}

package pl.msocha.analyzer;

import lombok.RequiredArgsConstructor;
import pl.msocha.analyzer.model.AnalysisResult;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@RequiredArgsConstructor
public class AnalysisTask implements Runnable {

    private final AnalysisResultRepository analysisResultRepository;
    private final Map<String, Integer> completionById;
    private final String id;
    private final char[] input;
    private final char[] pattern;

    @Override
    public void run() {

        var positionTypoMap = getTyposByPositionMap();

        var lowestTypoValue = Collections.min(positionTypoMap.values());

        var counter = new AtomicInteger();

        positionTypoMap.entrySet()
                .stream()
                .peek(peek -> updateProgressAndDelay(positionTypoMap.size(), counter.incrementAndGet()))
                .filter(entry -> Objects.equals(entry.getValue(), lowestTypoValue))
                .min(Comparator.comparingInt(Map.Entry::getKey))
                .ifPresent(result -> analysisResultRepository.store(
                        new AnalysisResult(id, result.getKey(), result.getValue())));
    }

    private void updateProgressAndDelay(Integer totalSize, Integer counter) {

        int round = (int) Math.round(((double) counter / totalSize) * 100);
        completionById.put(id, round);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<Integer, Integer> getTyposByPositionMap() {

        var positionTypoMap = new HashMap<Integer, Integer>();

        for (int i = 0; i < input.length - pattern.length + 1; i++) {

            var toCheck = getToCheckArr(i);
            var typos = 0;

            for (int j = 0; j < pattern.length; j++) {
                if (toCheck[j] != pattern[j]) {
                   typos++;
                }
            }
            positionTypoMap.put(i, typos);
        }

        return positionTypoMap;
    }

    private char[] getToCheckArr(int startPosition) {
        char[] result = new char[pattern.length];
        System.arraycopy(input, startPosition, result, 0, pattern.length);
        return result;
    }
}

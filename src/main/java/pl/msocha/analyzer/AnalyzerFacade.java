package pl.msocha.analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.msocha.analyzer.exception.InvalidInputException;
import pl.msocha.analyzer.model.AnalysisResult;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Component
public class AnalyzerFacade {

    @Autowired
    AnalysisResultRepository analysisResultRepository;
    @Autowired
    ExecutorService analyzerExecutorService;
    Map<String, Integer> completionById = new ConcurrentHashMap<>();


    public String processText(String input, String pattern) {

        var id = UUID.randomUUID().toString();

        if (pattern.length() > input.length()) {
            completionById.put(id, -1);
            throw new InvalidInputException(id, "Pattern is longer than input. Aborting...");
        }

        analyzerExecutorService.execute(
                new AnalysisTask(analysisResultRepository, completionById, id, input.toCharArray(), pattern.toCharArray())
        );

        return id;
    }

    public Collection<String> getTasksList() {
        return completionById.keySet();
    }

    public String checkStatus(String id) {
        return completionById.get(id) + "%";
    }

    public AnalysisResult getAnalyzeResult(String id) {
        return analysisResultRepository.getAnalyzeResultById(id);
    }
}

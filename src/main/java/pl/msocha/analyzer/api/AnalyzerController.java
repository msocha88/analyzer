package pl.msocha.analyzer.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.msocha.analyzer.AnalyzerFacade;
import pl.msocha.analyzer.model.AnalysisResult;
import pl.msocha.analyzer.model.AnalyzeRequest;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AnalyzerController implements AnalyzerApi {

    @Autowired
    private AnalyzerFacade analyzerFacade;

    @Override
    public ResponseEntity<String> analyzeText(AnalyzeRequest request) {
        log.debug("Received text to analyze [{}]", request);
        return ResponseEntity.ok(analyzerFacade.processText(request.getInput(), request.getPattern()));
    }

    @Override
    public ResponseEntity<Collection<String>> getAllTaskIds() {
        log.debug("Received getAllTasks request");
        return ResponseEntity.ok(analyzerFacade.getTasksList());
    }

    @Override
    public ResponseEntity<String> checkTaskStatus(String id) {
        log.debug("Received checkStatus request fo id {}", id);
        return ResponseEntity.ok(analyzerFacade.checkStatus(id));
    }

    @Override
    public ResponseEntity<AnalysisResult> getTaskResult(String id) {
        log.debug("Received getResult request fo id {}", id);
        return ResponseEntity.ok(analyzerFacade.getAnalyzeResult(id));
    }
}

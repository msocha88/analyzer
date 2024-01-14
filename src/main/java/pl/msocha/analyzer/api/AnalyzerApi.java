package pl.msocha.analyzer.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.msocha.analyzer.model.AnalysisResult;
import pl.msocha.analyzer.model.AnalyzeRequest;

import java.util.Collection;

@RequestMapping("/api/analyze/")
public interface AnalyzerApi {


    @PostMapping(value = "send-text", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> analyzeText(@RequestBody AnalyzeRequest request);

    @GetMapping("list-tasks")
    ResponseEntity<Collection<String>> getAllTaskIds();

    @GetMapping(value = "task/{id}/status")
    ResponseEntity<String> checkTaskStatus(@PathVariable String id);

    @GetMapping(value = "task/{id}/result", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AnalysisResult> getTaskResult(@PathVariable String id);
}

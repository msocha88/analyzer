package pl.msocha.analyzer.model;


import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
public record AnalysisResult(
        @Id
        String id,
        Integer position,
        Integer typos) {
}

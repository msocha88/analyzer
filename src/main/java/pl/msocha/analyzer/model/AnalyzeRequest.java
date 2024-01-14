package pl.msocha.analyzer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyzeRequest {

    private String input;
    private String pattern;

}

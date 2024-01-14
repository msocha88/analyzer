package pl.msocha.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pl.msocha.analyzer.model.AnalysisResult;

@Slf4j
@Repository
public class AnalysisResultRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public void store(AnalysisResult analysisResult) {
        log.debug("Storing result {}", analysisResult);
        mongoTemplate.save(analysisResult);
    }

    public AnalysisResult getAnalyzeResultById(String id) {
        log.debug("Searching db for id {}", id);
        return mongoTemplate.findOne(
                Query.query(Criteria.where("_id").is(id)),
                AnalysisResult.class
        );
    }
}

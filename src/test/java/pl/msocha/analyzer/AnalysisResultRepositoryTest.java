package pl.msocha.analyzer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import pl.msocha.analyzer.model.AnalysisResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@SpringBootTest
class AnalysisResultRepositoryTest extends AbstractIntegrationTest{

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    AnalysisResultRepository tested;

    @AfterEach
    public void afterEach() {
        mongoTemplate.dropCollection(AnalysisResult.class);
    }


    @Test
    public void shouldStore() {
        //given
        var id = UUID.randomUUID().toString();
        var toStore = new AnalysisResult(id, 0, 1);

        //when
        tested.store(toStore);

        //then
        var fromDb = mongoTemplate.findOne(query(where("_id").is(id)), AnalysisResult.class);

        assertThat(fromDb).isNotSameAs(toStore).isEqualTo(toStore);
    }

    @Test
    public void shouldFindInDb() {
        //given
        var id = UUID.randomUUID().toString();
        var stored = new AnalysisResult(id, 0, 1);
        mongoTemplate.save(stored);

        //when
        var fromDb = tested.getAnalyzeResultById(id);

        //then
        assertThat(fromDb).isNotSameAs(stored).isEqualTo(stored);
    }

}
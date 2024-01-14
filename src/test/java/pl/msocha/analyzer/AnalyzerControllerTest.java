package pl.msocha.analyzer;

import io.restassured.response.Response;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import pl.msocha.analyzer.model.AnalysisResult;
import pl.msocha.analyzer.model.AnalyzeRequest;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AnalyzerControllerTest extends AbstractIntegrationTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @SpyBean
    ExecutorService analyzerExecutorService;

    @AfterEach
    public void afterEach() {
        mongoTemplate.dropCollection(AnalysisResult.class);
    }

    
    @Test
    void shouldRequestAnalysisAndReturnRequestId() {

        requestAnalysis("ABCDE", "ABD")
                .then()
                .statusCode(200)
                .body(not(blankOrNullString()));

        verify(analyzerExecutorService, times(1)).execute(Mockito.any(AnalysisTask.class));
    }

    @Test
    void shouldReturnTaskIds() {
        var id = requestAnalysis("ABCDE", "ABD")
                .getBody()
                .asString();
        var id2 = requestAnalysis("ABCDE", "ABD")
                .getBody()
                .asString();

        when()
                .get("/api/analyze/list-tasks")
        .then()
                .statusCode(200)
                .body("$", hasItems(id,id2));
    }

    @Test
    void shouldCheckAnalysisStatus() {

        var id = requestAnalysis("ABCDE", "ABD")
                .getBody()
                .asString();


        with()
                .pathParam("id", id)
                .when()
                .get("/api/analyze/task/{id}/status")
                .then()
                .statusCode(200)
                .body(endsWith("%"));

    }

    @Test
    void shouldGetAnalysisResult() {

        var id = requestAnalysis("AAABBC", "ABG")
                .getBody()
                .asString();


        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .ignoreExceptions()
                .until(() -> mongoTemplate.findOne(query(where("_id").is(id)), AnalysisResult.class) != null);

        with()
                .pathParam("id", id)
                .when()
                .get("/api/analyze/task/{id}/result")
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("position", greaterThanOrEqualTo(0))
                .body("typos", greaterThanOrEqualTo(0));
    }

    private static Response requestAnalysis(String input, String pattern) {
        return with()
                .body(new AnalyzeRequest(input, pattern))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/analyze/send-text");
    }


}
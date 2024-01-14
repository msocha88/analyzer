package pl.msocha.analyzer;

import org.awaitility.Awaitility;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class AnalyzeTaskTest {

    public final static MongoDBContainer mongoContainer;


    static {
        mongoContainer = new MongoDBContainer("mongo:6-jammy").withExposedPorts(27017);
        mongoContainer.start();
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);
    }

    @Autowired
    AnalysisResultRepository analysisResultRepository;

    @ParameterizedTest
    @MethodSource("inputFOrShouldRunTaskLogic")
    void shouldRunTaskAndStoreProperValues(String input, String pattern, Integer position, Integer typos) {

        //given
        var completionMap = new ConcurrentHashMap<String, Integer>();
        var id = UUID.randomUUID().toString();

        var task = new AnalysisTask(
                analysisResultRepository,
                completionMap,
                id,
                input.toCharArray(),
                pattern.toCharArray()
        );

        //when
        task.run();

        //then
        Awaitility.await().atMost(Duration.ofSeconds((input.length() - pattern.length()) + 5))
                .ignoreExceptions()
                .until(() -> analysisResultRepository.getAnalyzeResultById(id) != null);

        var result = analysisResultRepository.getAnalyzeResultById(id);

        assertThat(result.position()).isEqualTo(position);
        assertThat(result.typos()).isEqualTo(typos);

    }


    private static Stream<Arguments> inputFOrShouldRunTaskLogic() {
        return Stream.of(
                Arguments.of("ABCD", "BCD", 1, 0),
                Arguments.of("ABCD", "BWD", 1, 1),
                Arguments.of("ABCDEFG", "CFG", 4, 1),
                Arguments.of("ABCABC", "ABC", 0, 0),
                Arguments.of("ABCDEFG", "TDD", 1, 2));
    }

}

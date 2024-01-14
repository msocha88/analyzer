#analyzer

To start an application one can use `docker-compose up --build` against `./development/docker-compose.yaml`
This should start two docker containers: analyzer application and mongodb.

To test application features use `http://localhost:8080/swagger-ui/index.html`

or following curls:


Post analisys input:
    curl -X 'POST' 'http://localhost:8080/api/analyze/send-text' -H 'Content-Type: application/json' \ 
    -d '{"input": "ABCD","pattern": "BBD"}'

List all tasks
    curl 'http://localhost:8080/api/analyze/list-tasks'

Check status of task (${id} should be replaced with id returned from POST)
    curl 'http://localhost:8080/api/analyze/task/${id}/status' 

Get result of task (${id} should be replaced with id returned from POST)
    curl 'http://localhost:8080/api/analyze/task/${id}/result' 

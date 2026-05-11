package edu.ucsb.cs.scaffold.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class PrairieLearnController {

    private final RestTemplate restTemplate;

    @Value("${pl.api.token:}")
    private String plApiToken;

    @Value("${pl.api.base:https://us.prairielearn.com/pl/api/v1}")
    private String plApiBase;

    @Value("${pl.course.instance.id:213859}")
    private String plCourseInstanceId;

    @GetMapping("/test-pl")
    public Object testPrairieLearn() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Private-Token", plApiToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = UriComponentsBuilder.fromUriString(plApiBase)
                .pathSegment("course_instances", plCourseInstanceId, "assessments")
                .toUriString();
        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object.class
        );
        return response.getBody();
    }
}

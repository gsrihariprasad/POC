package com.srihari;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class BookService {

  private final RestTemplate restTemplate;

  public BookService(RestTemplate rest) {
    this.restTemplate = rest;
  }

  //@HystrixCommand(fallbackMethod = "reliable")
  //@CircuitBreaker(include = { RestClientException.class }, openTimeout = 10_000, resetTimeout = 20_000)
  @HystrixCommand(fallbackMethod = "reliable",
		    commandProperties = {
		       @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "300000"),
		       @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value="60")
		    }
		)
  public String readingList() {
    URI uri = URI.create("http://localhost:9090/users");

    return this.restTemplate.getForObject(uri, String.class);
  }

  public String reliable() {
    return "Cloud Native Java (O'Reilly)";
  }

}
package com.longPolling.longPolling.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class LongPollingController {

	@GetMapping("/long-poll")
	public DeferredResult<ResponseEntity<String>> longPoll() {

	    // Timeout after 30 seconds
	    DeferredResult<ResponseEntity<String>> deferredResult =
	            new DeferredResult<>(30000L);

	    // Simulate async processing
	    CompletableFuture.runAsync(() -> {
	        try {
	            // Simulate waiting for an event
	            Thread.sleep(5000);

	            deferredResult.setResult(
	                    ResponseEntity.ok("Event received from server")
	            );

	        } catch (InterruptedException e) {
	            deferredResult.setErrorResult(
	                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                            .body("Error occurred")
	            );
	        }
	    });

	    // Timeout handling
	    deferredResult.onTimeout(() ->
	            deferredResult.setResult(
	                    ResponseEntity.status(HttpStatus.NO_CONTENT)
	                            .body("No updates")
	            )
	    );

	    return deferredResult;
	}

	
}

package ua.staff.generator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.springframework.http.HttpStatus.NO_CONTENT;

public class ResponseEntityGenerator {

    public static ResponseEntity<Void> getResponseEntity(URI location, HttpStatus httpStatus){
        return ResponseEntity.status(httpStatus)
                .location(location)
                .build();
    }

    public static ResponseEntity<Void> getResponseEntityWithNoContent(URI location){
        return ResponseEntity.status(NO_CONTENT)
                .location(location)
                .build();
    }

    public static <T> ResponseEntity<T> getResponseEntity(URI location,HttpStatus httpStatus,T body){
        return ResponseEntity.status(httpStatus)
                .location(location)
                .body(body);
    }
}

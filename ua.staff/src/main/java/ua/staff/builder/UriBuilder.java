package ua.staff.builder;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class UriBuilder {

    public static URI createUriFromCurrentRequest(){
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
    }

    public static URI createUriFromCurrentServletMapping(String path,Object... params){
        return ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path(path)
                .buildAndExpand(params)
                .toUri();
    }
}

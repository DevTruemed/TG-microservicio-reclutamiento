package com.truemedgroup.reclutamiento.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "servicio-oauth")
public interface OauthFeignClient {

    @GetMapping("encode")
    public String encodePassword(@RequestParam String password);

    @GetMapping("user")
    public Map<String, Object> getInfoToken(@RequestHeader Map<String, String> header);

}

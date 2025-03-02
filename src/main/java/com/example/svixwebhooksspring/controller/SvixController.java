package com.example.svixwebhooksspring.controller;

import com.example.svixwebhooksspring.service.SvixService;
import com.svix.models.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller("/svix")
@AllArgsConstructor
public class SvixController {

    private final SvixService svixService;

    @PostMapping("/apps")
    public ResponseEntity<ApplicationOut> createApp(
            @RequestParam String appId,
            @RequestParam String appName
    ) {
        return ResponseEntity.ok(svixService.createUserApp(appName, appId));
    }

    @GetMapping("/apps")
    public ResponseEntity<List<ApplicationOut>> getApps() {
        return ResponseEntity.ok(svixService.getAllUsersApp());
    }

    @GetMapping("/apps/{appId}")
    public ResponseEntity<ApplicationOut> getApp(@PathVariable String appId) {
        return ResponseEntity.ok(svixService.getUserApp(appId));
    }

    @GetMapping("/apps/{appId}/endpoints/{endpointId}/secret")
    public ResponseEntity<EndpointSecretOut> getEndpointSecret(@PathVariable String appId, @PathVariable String endpointId) {
        return ResponseEntity.ok(svixService.getEndpointSecret(appId, endpointId));
    }

    @PostMapping("/events")
    public ResponseEntity<EventTypeOut> createEvent(
            @RequestParam String event,
            @RequestParam String description,
            @RequestParam String featureFlag
    ) {
        return ResponseEntity.ok(svixService.createEvent(event, description, featureFlag));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventTypeOut>> getEvents() {
        return ResponseEntity.ok(svixService.getEvents());
    }

    @GetMapping("/apps/{appId}/endpoints")
    public ResponseEntity<ListResponseEndpointOut> getEndpoints(@PathVariable String appId) {
        return ResponseEntity.ok(svixService.getEndpoints(appId));
    }

    @GetMapping("/apps/{appId}/endpoints/{endpointId}")
    public ResponseEntity<EndpointOut> getEndpoint(
            @PathVariable String appId,
            @PathVariable String endpointId
    ) {
        return ResponseEntity.ok(svixService.getEndpoint(appId, endpointId));
    }

    @PostMapping("/apps/{appId}/endpoints")
    public ResponseEntity<EndpointOut> createEndpoint(
            @PathVariable String appId,
            @RequestParam String event
            ) {
        return ResponseEntity.ok(svixService.createEndpoint(appId, event));
    }

    @DeleteMapping("/apps/{appId}/endpoints/{endpointId}")
    public ResponseEntity<Void> deleteEndpoint(
            @PathVariable String appId,
            @PathVariable String endpointId
    ) {
        svixService.deleteEndpoint(appId, endpointId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-webhooks/{appId}")
    public ResponseEntity<MessageOut> sendWebhookMessage(
            @PathVariable String appId,
            @RequestParam String event,
            @RequestBody Map<String, Object> payload
    ) {
        return ResponseEntity.ok(svixService.sendWebhookMessage(appId, event, payload));
    }

    @PostMapping("/send-webhooks")
    public ResponseEntity sendWebhooksMessages(
            @RequestParam String event,
            @RequestBody Map<String, Object> payload
    ) {
        svixService.sendWebhooksMessages(event, payload);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/apps/{appId}/messages")
    public ResponseEntity<List<MessageOut>> getAppMessages(@PathVariable String appId) {
        return ResponseEntity.ok(svixService.getAppMessages(appId));
    }

    @PostMapping("/apps/{appId}/portal-access")
    public ResponseEntity<AppPortalAccessOut> appPortalAccess(@PathVariable String appId, @RequestBody Set<String> featureFlags) {
        return ResponseEntity.ok(svixService.appPortalAccess(appId, featureFlags));
    }
}
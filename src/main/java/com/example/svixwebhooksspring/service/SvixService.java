package com.example.svixwebhooksspring.service;

import com.svix.*;
import com.svix.exceptions.ApiException;
import com.svix.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;

@Service
@Slf4j
public class SvixService {

    private final Svix svix;

    public SvixService(@Value("${svix.token}") String svixToken, @Value("${svix.url}") String svixUrl) {
        SvixOptions options = new SvixOptions();
        options.setDebugUrl(svixUrl);
        this.svix = new Svix(svixToken, options);
    }

    public ApplicationOut createUserApp(String appName, String appId) {
        ApplicationOut appOut;
        try {
            appOut = svix.getApplication().create(
                    new ApplicationIn()
                            .name(appName)
                            .uid(appId)
            );
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        return appOut;
    }

    public ApplicationOut getUserApp(String appId) {
        ApplicationOut appOut;
        try {
            appOut = svix.getApplication().get(appId);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return appOut;
    }

    public List<ApplicationOut> getAllUsersApp() {
        List<ApplicationOut> applicationOutList;
        try {
            applicationOutList = svix.getApplication().list(new ApplicationListOptions()).getData();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return applicationOutList;
    }

    public EventTypeOut createEvent(String event, String description) {
        EventTypeOut eventTypeOut;
        try {
            eventTypeOut = svix.getEventType().create(new EventTypeIn().name(event).description(description));
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return eventTypeOut;
    }

    public List<EventTypeOut> getEvents() {
        List<EventTypeOut> eventTypeOutList;
        try {
            eventTypeOutList = svix.getEventType().list(new EventTypeListOptions()).getData();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return eventTypeOutList;
    }

    public EndpointOut createEndpoint(String appId, String event) {
        EndpointOut endpointOut;
        Set<String> filterTypes = new HashSet<>();
        filterTypes.add(event);
        try {
            endpointOut = svix.getEndpoint().create(appId,
                    new EndpointIn()
                            .uid(appId)
                            .url(URI.create("https://play.svix.com/in/e_A83zvHoXsf6YRZiejkFlj2BjpWR/"))
                            .disabled(false)
                            .filterTypes(filterTypes)
            );
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        return endpointOut;
    }

    public ListResponseEndpointOut getEndpoints(String appId) {
        ListResponseEndpointOut endpointOutList;
        try {
            endpointOutList = svix.getEndpoint().list(appId, new EndpointListOptions());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return endpointOutList;
    }

    public EndpointOut getEndpoint(String appId, String endpointId) {
        EndpointOut endpointOut;
        try {
            endpointOut = svix.getEndpoint().get(appId, endpointId);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return endpointOut;
    }

    public EndpointSecretOut getEndpointSecret(String appId, String endpointId) {
        EndpointSecretOut endpointSecretOut;
        try {
            endpointSecretOut = svix.getEndpoint().getSecret(appId, endpointId);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return endpointSecretOut;
    }

    public void deleteEndpoint(String appId, String endpointId) {
        try {
            svix.getEndpoint().delete(appId, endpointId);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public MessageOut sendWebhookMessage(String appId, String event, Map<String, Object> payload) {
        MessageOut messageOut;
        try {
            messageOut = svix.getMessage().create(appId,
                    new MessageIn()
                            .eventType(event)
                            .payload(payload));
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        return messageOut;
    }

    public void sendWebhooksMessages(String event, Map<String, Object> payload) {
        List<ApplicationOut> applications = getAllUsersApp();
        for (ApplicationOut app: applications) {
            try {
                payload.put("appId", app.getUid());
                svix.getMessage().create(app.getUid(),
                        new MessageIn()
                                .eventType(event)
                                .payload(payload));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<MessageOut> getAppMessages(String appId) {
        List<MessageOut> messageOutList;
        try {
            messageOutList = svix.getMessage().list(appId, new MessageListOptions()).getData();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        return messageOutList;
    }

    public DashboardAccessOut getDashboardAuth(String appId) {
        DashboardAccessOut dashboardAccessOut;
        try {
            dashboardAccessOut = svix.getAuthentication().dashboardAccess(appId);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return dashboardAccessOut;
    }

}
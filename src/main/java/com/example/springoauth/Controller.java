package com.example.springoauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
public class Controller {

    private final OAuth2AuthorizedClientService oauthService;

    private OAuth2AccessToken accessToken;

    @Autowired
    public Controller(OAuth2AuthorizedClientService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/api")
    public Map<String, Object> getUserData(@AuthenticationPrincipal OAuth2User principal, Authentication auth){
        var oauthToken = (OAuth2AuthenticationToken) auth;
        var client = oauthService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
        accessToken = client.getAccessToken();
        return principal.getAttributes();
    }

    @GetMapping("/api/{userName}")
    public String getData(@PathVariable String userName) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();

         HttpRequest request = HttpRequest.newBuilder(new URI("https://api.github.com/users/" + userName + "/repos"))
                 .headers("Accept", "application/vnd.github+json", "Authorization", "Bearer " + accessToken.getTokenValue())
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}

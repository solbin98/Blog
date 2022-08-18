package com.project.controller;


import com.project.service.CategoryService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class AdminLoginController {
    @Autowired
    GoogleConnectionFactory googleConnectionFactory;
    @Autowired
    OAuth2Parameters oAuth2Parameters;
    @Autowired
    CategoryService categoryService;

    @RequestMapping("login")
    public String login(Model model){
        OAuth2Operations oAuth2Operations = googleConnectionFactory.getOAuthOperations();
        String url = oAuth2Operations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, oAuth2Parameters);

        model.addAttribute("url",url);
        return "login";
    }

    @RequestMapping(value="loginPhase2")
    public String googleRollin(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                               Model model, @RequestParam String code) throws ServletException, IOException, ParseException {
        OAuth2Operations oauthOperations = googleConnectionFactory.getOAuthOperations();
        AccessGrant accessGrant = oauthOperations.exchangeForAccess(code , oAuth2Parameters.getRedirectUri(), null);

        String accessToken = accessGrant.getAccessToken();
        String refreshToken = accessGrant.getRefreshToken();
        Long expireTime = accessGrant.getExpireTime();

        if (expireTime != null && expireTime < System.currentTimeMillis()) {
            accessToken = accessGrant.getRefreshToken();
            System.out.printf("accessToken is expired. refresh token = {}", accessToken);
        }

        String getUrl = "https://www.googleapis.com/oauth2/v2/userinfo/?access_token=" + accessToken;
        RestTemplate rt = new RestTemplate();
        String ret = rt.getForObject(getUrl , String.class);

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(ret);

        if(jsonObject.get("email").equals("exode4@gmail.com")) session.setAttribute("admin", true);

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("admin", session.getAttribute("admin"));
        return "category";
    }
}

package capstone.mukjaView.Oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.StringUtils;

public class CustomAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private static final String AUTHORIZATION_REQUEST_ATTR_NAME = "oauth2_auth_request";
    private static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return (OAuth2AuthorizationRequest) request.getSession().getAttribute(AUTHORIZATION_REQUEST_ATTR_NAME);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            request.getSession().removeAttribute(AUTHORIZATION_REQUEST_ATTR_NAME);
            return;
        }
        request.getSession().setAttribute(AUTHORIZATION_REQUEST_ATTR_NAME, authorizationRequest);

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.hasText(redirectUriAfterLogin)) {
            request.getSession().setAttribute(REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin);
        }
    }
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest authRequest = this.loadAuthorizationRequest(request);
        request.getSession().removeAttribute(AUTHORIZATION_REQUEST_ATTR_NAME);
        return authRequest;
    }
}

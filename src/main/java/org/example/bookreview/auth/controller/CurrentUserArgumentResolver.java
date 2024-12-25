package org.example.bookreview.auth.controller;

import org.example.bookreview.oauth.CustomOAuth2User;
import org.example.bookreview.oauth.UnauthorizedException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class) &&
            parameter.getParameterType().equals(CustomOAuth2User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isAuthenticationInvalid(authentication)) {
            CurrentUser currentUser = parameter.getParameterAnnotation(CurrentUser.class);
            if (isCurrentUserRequired(currentUser)) {
                throw new UnauthorizedException("인증이 필요한 서비스입니다.");
            }
        }

        return authentication.getPrincipal();
    }

    private boolean isAuthenticationInvalid(Authentication authentication) {
        return authentication == null || "GUEST".equals(authentication.getPrincipal());
    }

    private boolean isCurrentUserRequired(CurrentUser currentUser) {
        return currentUser != null && currentUser.required();
    }
}
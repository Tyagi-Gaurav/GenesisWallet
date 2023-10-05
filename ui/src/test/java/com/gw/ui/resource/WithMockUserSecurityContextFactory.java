package com.gw.ui.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

final class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

	private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
			.getContextHolderStrategy();

	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser withUser) {
		String username = StringUtils.hasLength(withUser.username()) ? withUser.username() : withUser.value();
		Assert.notNull(username, () -> withUser + " cannot have null userName on both userName and value properties");
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (String authority : withUser.authorities()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority));
		}
		if (grantedAuthorities.isEmpty()) {
			for (String role : withUser.roles()) {
				Assert.isTrue(!role.startsWith("ROLE_"), () -> "roles cannot start with ROLE_ Got " + role);
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
		}
		else if (!(withUser.roles().length == 1 && "USER".equals(withUser.roles()[0]))) {
			throw new IllegalStateException("You cannot define roles attribute " + Arrays.asList(withUser.roles())
					+ " with authorities attribute " + Arrays.asList(withUser.authorities()));
		}

		List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("ROLE_USER", "SCOPE_https://www.googleapis.com/auth/userinfo.email",
				"SCOPE_https://www.googleapis.com/auth/userinfo.profile",
				"SCOPE_openid");

		OAuth2AuthenticationToken auth2Authentication = new OAuth2AuthenticationToken(
				getOAuth2UserPrincipal(authorityList), authorityList, "google"
		);

		SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
		context.setAuthentication(auth2Authentication);
		return context;
	}

	@Autowired(required = false)
	void setSecurityContextHolderStrategy(SecurityContextHolderStrategy securityContextHolderStrategy) {
		this.securityContextHolderStrategy = securityContextHolderStrategy;
	}

	private OAuth2User getOAuth2UserPrincipal(Collection<GrantedAuthority> authorities) {
		return new DefaultOAuth2User(authorities, Map.of("name", "testUser"), "name");
	}

}
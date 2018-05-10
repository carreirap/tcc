package br.com.autenticador.fichasordens;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import br.com.fichasordens.entities.UsuarioEntity;
import br.com.fichasordens.repository.UsuarioRepository;

@Component
public class OAuthUserService implements ClientDetailsService,
        UserDetailsService {

    private @Autowired UsuarioRepository userDetailsDao;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException 
    {
        UsuarioEntity user=userDetailsDao.findByUsuario(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
        }
        return new UserRepositoryUserDetails(user);
    }

    private final static class UserRepositoryUserDetails extends UsuarioEntity implements UserDetails
    {
    	UsuarioEntity user;

        /**
         * 
         */
        private static final long serialVersionUID = -4024636858150373536L;

        private UserRepositoryUserDetails() {

        }

        private UserRepositoryUserDetails(UsuarioEntity user) {
            super();
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            //return AuthorityUtils.commaSeparatedStringToAuthorityList(getRoles());
        	return null;
        }

        @Override
        public String getUsername() {
            return user.getUsuario();
        }

        @Override
        public boolean isAccountNonExpired() {

            return isEnabled();
        }

        @Override
        public boolean isAccountNonLocked() {

            return isEnabled();
        }

        @Override
        public boolean isCredentialsNonExpired() {

            return isEnabled();
        }

        @Override
        public boolean isEnabled() {

            return true;
        }

		@Override
		public String getPassword() {
			
			return user.getSenha();
		}

    }

    @Override
    public ClientDetails loadClientByClientId(String clientId)
            throws ClientRegistrationException 
    {
       UsuarioEntity oauthClientDetails= userDetailsDao.findByUsuario(clientId);
       if (oauthClientDetails.getSituacao() == 0) {
    	   throw new UsernameNotFoundException(String.format("Usuario id %s desativado!", clientId));
       }
       if (oauthClientDetails == null) {
           throw new UsernameNotFoundException(String.format("Usuario id %s não cadastrado!", clientId));
       }
       BaseClientDetails clientDetails=new BaseClientDetails();

       clientDetails.setClientId(oauthClientDetails.getUsuario());
//       clientDetails.setScope(StringUtils.commaDelimitedListToSet(oauthClientDetails.getScope()));
//       clientDetails.setAuthorizedGrantTypes(StringUtils.commaDelimitedListToSet(oauthClientDetails.getAuthorizedGrantTypes()));
//       clientDetails.setAuthorities(AuthorityUtils.createAuthorityList(StringUtils.commaDelimitedListToStringArray(oauthClientDetails.getAuthorities())));
//       clientDetails.setAccessTokenValiditySeconds(oauthClientDetails.getAccessTokenValidity());
//       clientDetails.setClientSecret(oauthClientDetails.getClientSecret());
//       clientDetails.setAdditionalInformation(oauthClientDetails.getAdditionalInformation());
//       if(oauthClientDetails.isAutoApprove())
//           clientDetails.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(oauthClientDetails.getAutoApproveScopes()));
//       else
//           clientDetails.setAutoApproveScopes(new HashSet<String>());
//
//       clientDetails.setAccessTokenValiditySeconds(oauthClientDetails.getAccessTokenValidity());
//       clientDetails.setRegisteredRedirectUri(StringUtils.commaDelimitedListToSet(oauthClientDetails.getWebserverRedirectURL()));
//       clientDetails.setResourceIds(StringUtils.commaDelimitedListToSet(oauthClientDetails.getResourceIds()));
//       clientDetails.setAdditionalInformation(oauthClientDetails.getAdditionalInformation());
       clientDetails.setClientSecret(oauthClientDetails.getSenha());
       //clientDetails.setAuthorizedGrantTypes(Arrays.asList("authorization_code") );
       clientDetails.setScope(Arrays.asList("resource-server-read"));
       clientDetails.setRegisteredRedirectUri(Collections.singleton("http://anywhere.com"));
       clientDetails.setResourceIds(Arrays.asList("oauth2-resource"));
       Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
       authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
       if (oauthClientDetails.getPapel().equals("Admin")) {
    	   authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
       }
       clientDetails.setAccessTokenValiditySeconds(3600);
       clientDetails.setAuthorities(authorities);

        return clientDetails;
    }
    }
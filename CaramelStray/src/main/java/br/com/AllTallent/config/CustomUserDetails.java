package br.com.AllTallent.config;

import br.com.AllTallent.model.Funcionario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final String username; 
    private final String password;
    private final Integer codigo;  
    private final Integer areaId;  
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Funcionario funcionario) {
        this.username = funcionario.getEmail();
        this.password = funcionario.getSenhaHash();
        this.codigo = funcionario.getCodigo();
        this.areaId = (funcionario.getArea() != null) ? funcionario.getArea().getCodigo() : null;
        
        if (funcionario.getPerfil() == null) {
            this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            int perfilId = funcionario.getPerfil().getCodigo();
            if (perfilId == 1) { // Diretoria
                this.authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_GESTOR"),
                    new SimpleGrantedAuthority("ROLE_USER")
                );
            } else if (perfilId == 2) { // Supervisão
                this.authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_GESTOR"),
                    new SimpleGrantedAuthority("ROLE_USER")
                );
            } else { // Perfil 3 (Colaborador)
                this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            }
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username; // (email)
    }


    public Integer getCodigo() {
        return this.codigo;
    }

    public Integer getAreaId() {
        return this.areaId;
    }


    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
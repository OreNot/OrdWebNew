package ucproject.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
    USER,
    OPERATOR,
    GROUPBOSS,
    MANAGER,
    SUPERBOSS,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}

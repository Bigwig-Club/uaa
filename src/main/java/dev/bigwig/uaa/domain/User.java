package dev.bigwig.uaa.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Entity
@Table(name = "user")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(length = 50, unique = true, nullable = false)
  private String username;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "password_hash", length = 80, nullable = false)
  private String password;
  @Column(unique = true, nullable = false)
  private String email;
  @Column(length = 50)
  private String name;
  @Column(unique = true, nullable = false)
  private String mobile;
  @Column(nullable = false)
  private boolean enabled;
  @Column(name = "account_non_expired", nullable = false)
  private boolean accountNonExpired;
  @Column(name = "account_non_locked", nullable = false)
  private boolean accountNonLocked;
  @Column(name = "credentials_non_expired", nullable = false)
  private boolean credentialsNonExpired;
  @ManyToMany
  @Fetch(FetchMode.JOIN)
  @JoinTable(name = "user_role",
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
  )
  private Set<Role> authorities;
}

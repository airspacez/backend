package com.example.demo.domain.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class UserAdditional implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="UserID")
    private Integer Id;

    @Column(name = "AltNick")
    private String AltNickname;

    @Column(name="Nick")
    private String Nickname;

    @Column(name="Name")
    private String Name;

    @Column(name="UserEmail")
    private String Email;

    @Column(name="Gender")
    private Boolean IsMale;

    @Column(name = "PhotoFile")
    private String PhotoImagePath;

    @Column(name = "birthday_date")
    private Date birthdayDate;

    @Column(name="username")
    private String username;

    @JsonIgnore
    @Column(name="password")
    private String password;

    @JsonIgnore
    @Column(name="token")
    private String token;

    @Column(name="mobile")
    private String PhoneNumber;

    @Column(name="Archieved")
    private Boolean IsArchived;
    @JsonIgnoreProperties({"usersOfCLub","City", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ClubID", nullable=false)
    private Club Club;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ClubStatuseID", nullable=false)
    private ClubStatus Status;

    @JsonIgnore
    @OneToMany(mappedBy = "User", fetch=FetchType.LAZY)
    private List<GameMember> Members;


    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY)
    private List<UserInEvent> userInEvents;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="roleID", nullable=false)
    private ClubRole Role;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.getDescription()));
        return authorities;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}

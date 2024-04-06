package com.HaveBinProject.HaveBin.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String nickname;

    @JsonIgnore
    @Column(nullable = false)
    String password;

    @Column(nullable = true)
    Long kakaoid;

    @Column(nullable = true)
    String profile_imgpath;
}

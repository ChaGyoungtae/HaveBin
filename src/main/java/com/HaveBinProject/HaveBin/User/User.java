package com.HaveBinProject.HaveBin.User;




import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class User {

    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String nickname;

    @JsonIgnore
    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    Long kakaoid;

    @Column(nullable = false)
    String profile_imgpath;
}

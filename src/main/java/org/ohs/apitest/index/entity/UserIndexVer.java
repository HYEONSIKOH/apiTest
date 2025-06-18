package org.ohs.apitest.index.entity;

import jakarta.persistence.*;

@Entity
@Table(indexes = { @Index(name = "idx_email", columnList = "email") })
public class UserIndexVer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
}

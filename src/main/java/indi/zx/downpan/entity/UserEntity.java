package indi.zx.downpan.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 13:34
 */

@Entity
@Table(name="user",uniqueConstraints ={@UniqueConstraint(columnNames = {"username"})})
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserEntity extends BaseEntity{

    @Column(unique = true)
    private String username;

    private String imgUrl;

    private String password;

    private String email;

    private Integer status;

    private Long used =0L;

    private Long diskCapacity;

    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "MEDIUMBLOB")
    private  byte[] icon;

    @Column(length = 99999)
    private String friends;
}

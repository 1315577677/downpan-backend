package indi.zx.downpan.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 13:34
 */

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
public  abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 7778625551662L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    @NonNull
    private String id;

    @NonNull
    private LocalDateTime createTime = LocalDateTime.now();

    @NonNull
    private String createUser;

    @NonNull
    private LocalDateTime updateTime = LocalDateTime.now();

    @NonNull
    private String updateUser;
}

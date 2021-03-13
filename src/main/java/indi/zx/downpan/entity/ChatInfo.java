package indi.zx.downpan.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ChatInfo extends BaseEntity {
    private String imgUrl;
    private String time;
    private String message;
    private String type;
    private String fromId;
    private String uid;
    private String fid;
}

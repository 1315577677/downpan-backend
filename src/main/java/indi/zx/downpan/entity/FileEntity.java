package indi.zx.downpan.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-20 16:25
 */

@Entity
@Table(name = "file")
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FileEntity extends BaseEntity{
    private String name;
    private String MD5;
    private Long size;
    private String url;
    private String type;
    private Boolean isDelete;
    private Boolean isDir;
    private String parent;
}

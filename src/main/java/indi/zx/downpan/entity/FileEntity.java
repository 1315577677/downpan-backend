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
@Table(name="file")
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FileEntity extends BaseEntity{
    private String fileName;
    private String MD5;
    private Long size;
    private String virtualPath;
    private String realPath;
    private String realType;
    private Boolean isDelete;
    private Boolean isDir;
    private String parent;
}

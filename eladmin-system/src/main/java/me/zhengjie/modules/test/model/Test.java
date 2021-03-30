package me.zhengjie.modules.test.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xinglei
 * @since 2021-03-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Test extends Model {

    private static final long serialVersionUID = 1L;

    /**
     * 1111
     */
    private Long id;

    /**
     * 2222
     */
    private String userName;


}

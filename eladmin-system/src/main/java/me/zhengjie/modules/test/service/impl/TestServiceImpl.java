package me.zhengjie.modules.test.service.impl;

import me.zhengjie.modules.test.model.Test;
import me.zhengjie.modules.test.mapper.TestMapper;
import me.zhengjie.modules.test.service.ITestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xinglei
 * @since 2021-03-29
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements ITestService {

}

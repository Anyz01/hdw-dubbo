package com.hdw.common.validator.group;

import javax.validation.GroupSequence;

/**
 * @Description 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 * @Author TuMinglong
 * @Date 2018/12/13 10:37
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}

package com.powernode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.powernode.domain.LoginSysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface LoginSysUserMapper extends BaseMapper<LoginSysUser> {
    Set<String> selectPermsByUserId(Long userId);
}
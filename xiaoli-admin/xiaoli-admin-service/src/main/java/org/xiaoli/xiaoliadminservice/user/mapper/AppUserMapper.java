package org.xiaoli.xiaoliadminservice.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.AppUserListReqDTO;
import org.xiaoli.xiaoliadminservice.user.domain.entity.AppUser;

import java.util.List;


/**
 * 操作APPuser表的mapper
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {


    /**
     * 查询总数
     * @param appUserListReqDTO 查询C端用户DTO
     * @return 用户总数
     */
    Long selectCount(AppUserListReqDTO appUserListReqDTO);


    /**
     * 分页查询C端用户
     * @param appUserListReqDTO 查询C端用户DTO
     * @return 用户列表
     */
    List<AppUser> selectPage(AppUserListReqDTO appUserListReqDTO);







}

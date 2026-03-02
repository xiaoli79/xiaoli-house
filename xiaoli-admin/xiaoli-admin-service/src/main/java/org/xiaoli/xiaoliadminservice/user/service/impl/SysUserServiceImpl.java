package org.xiaoli.xiaoliadminservice.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoli.xiaoliadminservice.config.service.ISysDictionaryService;
import org.xiaoli.xiaoliadminservice.user.domain.dto.PasswordLoginDTO;
import org.xiaoli.xiaoliadminservice.user.domain.dto.SysUserDTO;
import org.xiaoli.xiaoliadminservice.user.domain.dto.SysUserListReqDTO;
import org.xiaoli.xiaoliadminservice.user.domain.dto.SysUserLoginDTO;
import org.xiaoli.xiaoliadminservice.user.domain.entity.SysUser;
import org.xiaoli.xiaoliadminservice.user.mapper.SysUserMapper;
import org.xiaoli.xiaoliadminservice.user.service.ISysUserService;
import org.xiaoli.xiaolicommoncore.utils.AESUtil;
import org.xiaoli.xiaolicommoncore.utils.VerifyUtil;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;
import org.xiaoli.xiaolicommonsecurity.domain.dto.LoginUserDTO;
import org.xiaoli.xiaolicommonsecurity.domain.dto.TokenDTO;
import org.xiaoli.xiaolicommonsecurity.service.TokenService;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SysUserServiceImpl implements ISysUserService {


    @Autowired
    private SysUserMapper sysUserMapper;


    @Autowired
    private TokenService tokenService;


    @Autowired
    private ISysDictionaryService sysDictionaryService;


    /**
     * B端用户登录
     * @param passwordLoginDTO
     * @return
     */
    @Override
    public TokenDTO login(PasswordLoginDTO passwordLoginDTO) {

        LoginUserDTO loginUserDTO = new LoginUserDTO();


//      校验手机号是否合理
        if(!VerifyUtil.checkPhone(passwordLoginDTO.getPhone())){
            throw new ServiceException("手机号格式不正确", ResultCode.INVALID_PARA.getCode());
        }
//      判断手机号是否存在
        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhoneNumber, AESUtil.encryptHex(passwordLoginDTO.getPhone())));
        if(sysUser == null){
            throw new ServiceException("手机号不存在");
        }

//      校验密码
//      先对密码进行解密
        String password = AESUtil.decryptHex(passwordLoginDTO.getPassword());
        if(StringUtils.isEmpty(password)){
            throw new ServiceException("密码解析为空",ResultCode.INVALID_PARA.getCode());
        }
        String passwordEncode = DigestUtils.sha256Hex(password);
        if(!sysUser.getPassword().equals(passwordEncode)){
            throw new ServiceException("密码不正确",ResultCode.INVALID_PARA.getCode());
        }
        if(sysUser.getStatus() == "disable") {
            throw new ServiceException("账户已停用", ResultCode.USER_DISABLE.getCode());
        }
//       设置登录信息
        loginUserDTO.setUserId(sysUser.getId());
        loginUserDTO.setUserName(sysUser.getNickName());
        loginUserDTO.setUserFrom("sys");
        return tokenService.createToken(loginUserDTO);
    }

    /**
     * 新增与编辑接口的方法
     * @param sysUserDTO
     * @return
     */
    @Override
    public Long addOrEdit(SysUserDTO sysUserDTO) {

//      创建一个空的sysUser对象
        SysUser sysUser = new SysUser();

//      处理新增的逻辑
        if(sysUserDTO.getUserId() == null){
//          校验手机号
            if(!VerifyUtil.checkPhone(sysUserDTO.getPhoneNumber())){
                throw new ServiceException("手机号格式错误", ResultCode.INVALID_PARA.getCode());
            }
//          校验密码
            if(StringUtils.isEmpty(sysUserDTO.getPassword()) || !sysUserDTO.checkPassword(sysUserDTO.getPassword())){
                throw new ServiceException("校验密码不通过",ResultCode.INVALID_PARA.getCode());
            }

//          检查手机号是否是唯一的~~
            if(sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhoneNumber, AESUtil.encryptHex(sysUserDTO.getPhoneNumber())))!=null){
                throw new ServiceException("手机号已经被占用",ResultCode.INVALID_PARA.getCode());
            }

//          判断用户信息
            if(StringUtils.isEmpty(sysUserDTO.getIdentity()) || sysDictionaryService.getDicDataByKey(sysUserDTO.getIdentity()) == null){
                throw new ServiceException("用户身份错误",ResultCode.INVALID_PARA.getCode());
            }
//          判断完成后执行用户新增逻辑
            sysUser.setPhoneNumber(AESUtil.encryptHex(sysUserDTO.getPhoneNumber()));
            sysUser.setPassword(DigestUtils.sha256Hex(sysUserDTO.getPassword()));
            sysUser.setIdentity(sysUserDTO.getIdentity());
        }
        sysUser.setId(sysUserDTO.getUserId());
        sysUser.setNickName(sysUserDTO.getNickName());

//      判断用户状态
        if(sysDictionaryService.getDicDataByKey(sysUserDTO.getStatus()) == null){
            throw new ServiceException("用户状态错误",ResultCode.INVALID_PARA.getCode());
        }
        sysUser.setStatus(sysUserDTO.getStatus());
        sysUser.setRemark(sysUserDTO.getRemark());
        sysUserMapper.insertOrUpdate(sysUser);

//      踢人
        if(sysUserDTO.getUserId() != null && "disable".equals(sysUserDTO.getStatus())) {
            tokenService.delLoginUser(sysUserDTO.getUserId(),"sys");
        }


        return sysUser.getId();
    }


    /**
     * 查看B端用户的接口
     * @param sysUserListReqDTO
     * @return
     */
    @Override
    public List<SysUserDTO> getUserList(SysUserListReqDTO sysUserListReqDTO) {
        SysUser searchSysUser = new SysUser();

//      设置其值
        if(sysUserListReqDTO.getUserId() != null){
            searchSysUser.setId(sysUserListReqDTO.getUserId());
        }
        if(StringUtils.isNotEmpty(sysUserListReqDTO.getStatus())){
            searchSysUser.setStatus(sysUserListReqDTO.getStatus());
        }
        if(StringUtils.isNotEmpty(sysUserListReqDTO.getPhoneNumber())){
            searchSysUser.setPhoneNumber(AESUtil.encryptHex(sysUserListReqDTO.getPhoneNumber()));
        }

//      执行查询sql
        List<SysUser> sysUserList = sysUserMapper.selectUserList(searchSysUser);

//      对查询结果封装转换
        return sysUserList.stream().map(sysUser->{
            SysUserDTO sysUserDTO = new SysUserDTO();
            sysUserDTO.setUserId(sysUser.getId());
            sysUserDTO.setPhoneNumber(AESUtil.decryptHex(sysUser.getPhoneNumber()
            ));
            sysUserDTO.setNickName(sysUser.getNickName());
            sysUserDTO.setStatus(sysUser.getStatus());
            sysUserDTO.setRemark(sysUser.getRemark());
            sysUserDTO.setIdentity(sysUser.getIdentity());
            return sysUserDTO;

        }).collect(Collectors.toList());
    }


    /**
     * 获取B端用户登录信息
     * @return B端用户信息DTO
     */
    @Override
    public SysUserLoginDTO getLoginUser() {


//      1.获取当前用户
        LoginUserDTO loginUserDTO = tokenService.getLoginUser();

//      2.进行对象判断
        if(loginUserDTO == null || loginUserDTO.getUserId() == null){
            throw new ServiceException("用户令牌有误", ResultCode.INVALID_PARA.getCode());
        }
//      3.查询mysql
        SysUser sysUser = sysUserMapper.selectById(loginUserDTO.getUserId());
        if(sysUser == null){
            throw new ServiceException("用户不存在", ResultCode.INVALID_PARA.getCode());
        }
        SysUserLoginDTO sysUserLoginDTO = new SysUserLoginDTO();
        BeanUtils.copyProperties(sysUser, sysUserLoginDTO);
        BeanUtils.copyProperties(loginUserDTO, sysUserLoginDTO);
        return sysUserLoginDTO;

    }
}

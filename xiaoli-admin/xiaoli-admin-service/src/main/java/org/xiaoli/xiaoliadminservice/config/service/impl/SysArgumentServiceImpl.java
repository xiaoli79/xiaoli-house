package org.xiaoli.xiaoliadminservice.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoli.xiaoliadminapi.config.domain.dto.ArgumentAddReqDTO;
import org.xiaoli.xiaoliadminapi.config.domain.dto.ArgumentDTO;
import org.xiaoli.xiaoliadminapi.config.domain.dto.ArgumentEditReqDTO;
import org.xiaoli.xiaoliadminapi.config.domain.dto.ArgumentListReqDTO;
import org.xiaoli.xiaoliadminapi.config.domain.vo.ArgumentVO;
import org.xiaoli.xiaoliadminservice.config.domain.entity.SysArgument;
import org.xiaoli.xiaoliadminservice.config.mapper.SysArgumentMapper;
import org.xiaoli.xiaoliadminservice.config.service.ISysArgumentService;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.vo.BasePageVO;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;
import java.util.ArrayList;
import java.util.List;


@Service
public class SysArgumentServiceImpl implements ISysArgumentService {


    @Autowired
    private SysArgumentMapper sysArgumentMapper;

    @Override
    public R<Long> add(ArgumentAddReqDTO argumentAddReqDTO) {
        /**
         * 新增参数
         * @param argumentAddReqDTO 新增参数请求DTO
         * @return Long
         */
        SysArgument sysArgument = sysArgumentMapper.selectOne(new LambdaQueryWrapper<SysArgument>().eq(SysArgument::getName, argumentAddReqDTO.getName()).or().eq(SysArgument::getConfigKey, argumentAddReqDTO.getConfigKey()));
        if(sysArgument != null){
            throw new ServiceException("参数已存在!!");
        }
        sysArgument = new SysArgument();

        sysArgument.setValue(argumentAddReqDTO.getValue());
        sysArgument.setName(argumentAddReqDTO.getName());
        sysArgument.setConfigKey(argumentAddReqDTO.getConfigKey());
        if(StringUtils.isNotBlank(argumentAddReqDTO.getRemark())){
            sysArgument.setRemark(argumentAddReqDTO.getRemark());
        }
        int insert = sysArgumentMapper.insert(sysArgument);
        if(insert != 1){
            throw new ServiceException("参数插入失败");
        }
        return R.ok(sysArgument.getId());
    }

    /**
     * 查看参数列表
     * @param argumentListReqDTO
     * @return
     */
    @Override
    public BasePageVO<ArgumentVO> list(ArgumentListReqDTO argumentListReqDTO) {


        BasePageVO<ArgumentVO> result = new BasePageVO<>();
        LambdaQueryWrapper<SysArgument> queryWrapper= new LambdaQueryWrapper<>();

        if(StringUtils.isNotBlank(argumentListReqDTO.getConfigKey())){
            queryWrapper.eq(SysArgument::getConfigKey, argumentListReqDTO.getConfigKey());
        }

        if(StringUtils.isNotBlank(argumentListReqDTO.getName())){
            queryWrapper.likeRight(SysArgument::getName, argumentListReqDTO.getName());
        }

        Page<SysArgument> page = sysArgumentMapper.selectPage(new Page<>(argumentListReqDTO.getPageNo().longValue(), argumentListReqDTO.getPageSize().longValue()), queryWrapper);
        result.setTotals((int) page.getTotal());
        result.setTotalPages((int) page.getPages());
        List<ArgumentVO> list = new ArrayList<>();
        for(SysArgument sysArgument : page.getRecords()){
            ArgumentVO argumentVO = new ArgumentVO();
            BeanUtils.copyProperties(sysArgument,argumentVO);
            list.add(argumentVO);
        }
        result.setList(list);
        return result;
    }

    /**
     * 编辑参数
     * @param argumentEditReqDTO
     * @return
     */
    @Override
    public Long editArgument(ArgumentEditReqDTO argumentEditReqDTO) {

//      先查询是否存在
        SysArgument sysArgument = sysArgumentMapper.selectOne(new LambdaQueryWrapper<SysArgument>().eq(SysArgument::getConfigKey,argumentEditReqDTO.getConfigKey()));
        if(sysArgument == null){
            throw new ServiceException("要编辑的参数不存在");
        }

//      查询是否出现冲突~~
        if(sysArgumentMapper.selectOne(new LambdaQueryWrapper<SysArgument>()
                .eq(SysArgument::getName,argumentEditReqDTO.getName())
                .ne(SysArgument::getConfigKey,argumentEditReqDTO.getConfigKey())
        ) != null){
            throw new ServiceException("参数名称存在冲突");
        }
//      4.根据3的结果判空处理~~
        sysArgument.setValue(argumentEditReqDTO.getValue());
        sysArgument.setName(argumentEditReqDTO.getName());
        if(StringUtils.isNotBlank(argumentEditReqDTO.getRemark())){
            sysArgument.setRemark(argumentEditReqDTO.getRemark());
        }
//      5.执行相应的操作
        sysArgumentMapper.updateById(sysArgument);
        return sysArgument.getId();
    }



//  根据参数的键来返回其对象
    @Override
    public ArgumentDTO getByConfigKey(String configKey) {
        SysArgument sysArgument = sysArgumentMapper.selectOne(new LambdaQueryWrapper<SysArgument>().eq(SysArgument::getConfigKey,configKey));
        if(sysArgument == null){
            throw new ServiceException("这个参数键不存在");
        }
        ArgumentDTO argumentDTO = new ArgumentDTO();
        BeanUtils.copyProperties(sysArgument,argumentDTO);
        return argumentDTO;

    }

    @Override
    public List<ArgumentDTO> getByConfigKeys(List<String> configKeys) {
//      1.根据多个参数业务主键来查询多个对象
        List<SysArgument> sysArguments = sysArgumentMapper.selectList(new LambdaQueryWrapper<SysArgument>().in(SysArgument::getConfigKey,configKeys));
        if(sysArguments == null){
            throw new ServiceException("参数键不存在");
        }
        List<ArgumentDTO> list = new ArrayList<>();
        for(SysArgument sysArgument : sysArguments){
            ArgumentDTO argumentDTO = new ArgumentDTO();
            BeanUtils.copyProperties(sysArgument,argumentDTO);
            list.add(argumentDTO);
        }
        return list;
    }
}

package org.xiaoli.xiaoliadminservice.house.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xiaoli.xiaoliadminapi.config.domain.dto.DictionaryDataDTO;
import org.xiaoli.xiaoliadminservice.config.service.impl.SysDictionaryServiceImpl;
import org.xiaoli.xiaoliadminservice.house.domain.dto.*;
import org.xiaoli.xiaoliadminservice.house.domain.entity.*;
import org.xiaoli.xiaoliadminservice.house.domain.enums.HouseStatusEnum;
import org.xiaoli.xiaoliadminservice.house.mapper.*;
import org.xiaoli.xiaoliadminservice.house.service.IHouseService;
import org.xiaoli.xiaoliadminservice.map.domain.entity.SysRegion;
import org.xiaoli.xiaoliadminservice.map.mapper.RegionMapper;
import org.xiaoli.xiaoliadminservice.user.domain.entity.AppUser;
import org.xiaoli.xiaoliadminservice.user.mapper.AppUserMapper;
import org.xiaoli.xiaolicommoncore.domain.dto.BasePageDTO;
import org.xiaoli.xiaolicommoncore.utils.BeanCopyUtil;
import org.xiaoli.xiaolicommoncore.utils.JsonUtil;
import org.xiaoli.xiaolicommoncore.utils.TimestampUtil;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;
import org.xiaoli.xiaolicommonredis.service.RedisService;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Component
@Slf4j
public class HouseServiceImpl implements IHouseService {

    //城市房源映射key前缀
    private static final String CITY_HOUSE_PREFIX = "house:list:";
    // 城市完整信息 key 前缀
    private static final String HOUSE_PREFIX = "house:";

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagHouseMapper tagHouseMapper;
    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    HouseStatusMapper houseStatusMapper;

    @Autowired
    CityHouseMapper cityHouseMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    SysDictionaryServiceImpl sysDictionaryService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addOrEdit(HouseAddOrEditReqDTO houseAddOrEditReqDTO) {


        //1.校验参数
        checkAddOrEditReq(houseAddOrEditReqDTO);

        //2.设置房源基本信息
        House house = new House();
        house.setUserId(houseAddOrEditReqDTO.getUserId());
        house.setTitle(houseAddOrEditReqDTO.getTitle());
        house.setRentType(houseAddOrEditReqDTO.getRentType());
        house.setFloor(houseAddOrEditReqDTO.getFloor());
        house.setAllFloor(houseAddOrEditReqDTO.getAllFloor());
        house.setHouseType(houseAddOrEditReqDTO.getHouseType());
        house.setRooms(houseAddOrEditReqDTO.getRooms());
        house.setPosition(houseAddOrEditReqDTO.getPosition());
        house.setArea(BigDecimal.valueOf(houseAddOrEditReqDTO.getArea()));
        house.setPrice(BigDecimal.valueOf(houseAddOrEditReqDTO.getPrice()));
        house.setIntro(houseAddOrEditReqDTO.getIntro());
//      使用stream流的形式来设置Device列表
        house.setDevices(
                houseAddOrEditReqDTO.getDevices().stream()
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining(","))
        );
        house.setHeadImage(houseAddOrEditReqDTO.getHeadImage());
//      把头像转换为Json字符串
        house.setImages(JsonUtil.obj2String(houseAddOrEditReqDTO.getImages()));
        house.setCityId(houseAddOrEditReqDTO.getCityId());
        house.setCityName(houseAddOrEditReqDTO.getCityName());
        house.setRegionId(houseAddOrEditReqDTO.getRegionId());
        house.setRegionName(houseAddOrEditReqDTO.getRegionName());
        house.setCommunityName(houseAddOrEditReqDTO.getCommunityName());
        house.setDetailAddress(houseAddOrEditReqDTO.getDetailAddress());
        house.setLongitude(BigDecimal.valueOf(houseAddOrEditReqDTO.getLongitude()));
        house.setLatitude(BigDecimal.valueOf(houseAddOrEditReqDTO.getLatitude()));


        // 4.编辑 需要判断是否更新 城市房源映射、标签房源映射
        // 4.1 编辑 MySQL(House TagHouse CityHouse)
        // 4.2 编辑 Redis(城市房源映射)
        if (null != houseAddOrEditReqDTO.getHouseId()) {
            house.setId(houseAddOrEditReqDTO.getHouseId());

            // 判断是否需要修改城市房源的映射
            House existHouse = houseMapper.selectById(houseAddOrEditReqDTO.getHouseId());
            if (cityHouseNeedChange(existHouse, houseAddOrEditReqDTO.getCityId())) {
                // 改变才更新(更新MySQL，更新Redis)
                editCityHouses(houseAddOrEditReqDTO.getHouseId(), existHouse.getCityId(),
                        houseAddOrEditReqDTO.getCityId(), houseAddOrEditReqDTO.getCityName());
            }

            // 判断是否需要修改标签房源的映射
            List<TagHouse> tagHouses = tagHouseMapper.selectList(
                    new LambdaQueryWrapper<TagHouse>()
                            .eq(TagHouse::getHouseId, houseAddOrEditReqDTO.getHouseId()));
            if (tagHouseNeedChange(tagHouses, houseAddOrEditReqDTO.getTagCodes())) {
                // 改变才更新(更新Mysql)
                editTagHouses(houseAddOrEditReqDTO.getHouseId(),
                        tagHouses, houseAddOrEditReqDTO.getTagCodes());
            }

        }

        // 如果是新增，调用完之后，house里会填充 id 字段
        houseMapper.insertOrUpdate(house);


        // 3.新增
        // 3.1 新增 MySQL(House HouseStatus TagHouse CityHouse)
        // 3.2 新增 Redis(城市房源映射)
        if(null == houseAddOrEditReqDTO.getHouseId()) {
            // 新增 HouseStatus TagHouse CityHouse：都需要一个 houseId 去做关联

            HouseStatus houseStatus = new HouseStatus();
            houseStatus.setHouseId(house.getId());
            houseStatus.setStatus(HouseStatusEnum.UP.name());
            houseStatusMapper.insert(houseStatus);

            // MySQL, Redis
            addCityHouse(house.getId(), house.getCityId(), house.getCityName());

            // MySQL
            addTagHouses(house.getId(), houseAddOrEditReqDTO.getTagCodes());

        }

        // 5.缓存房源完整信息 Redis(房源完整信息)
        cacheHouse(house.getId());
        return house.getId();
    }



    /**
     * 查询房源相关的信息
     * @param houseId
     * @return
     */
    @Override
    public HouseDTO detail(Long houseId) {


        //解决缓存穿透的问题~~
        if(null == houseId || houseId <= 0) {
            log.warn("要查询的房源ID为空或者无效");
            return null;
        }

        //1.查询房源详情缓存
        HouseDTO houseDTO = getCacheHouse(houseId);


        //2.判断缓存是否村子啊
        if(null != houseDTO) {
            return houseDTO;
        }


        //3.缓存不存在，查询MySQL
        houseDTO = getHouseDTOById(houseId);

        //4.MySQL不存在，缓存空对象
        if(null == houseDTO) {
            cacheHouse(houseId,60L);
            log.error("查询房源信息错误,houseId:{}",houseId);
            return null;
        }

        //5.mysql存在，缓存房源详情
        cacheHouse(houseDTO);

        //6.返回
        return houseDTO;
    }


    /**
     * 查询房源摘要列表
     * @param houseListReqDTO
     * @return
     */
    @Override
    public BasePageDTO<HouseDescDTO> list(HouseListReqDTO houseListReqDTO) {

        //查询总数，联表查询
        BasePageDTO<HouseDescDTO> result = new  BasePageDTO<>();

        Long totals = houseMapper.selectCountWithStatus(houseListReqDTO);

        if(null ==totals){
            result.setList(new  ArrayList<>());
            result.setTotals(0);
            result.setTotalPages(0);
            log.info("查询房源列表为空，HouseListReqDTO:{}",houseListReqDTO);
            return result;
        }

        //查询列表
        List<HouseDescDTO> houses = houseMapper.selectPageWithStatus(houseListReqDTO);

        result.setTotals(Integer.parseInt(totals.toString()));

        result.setTotalPages(BasePageDTO.calculateTotalPages(totals,houseListReqDTO.getPageSize()));

        if(null == houses){

            log.info("超出查询房源列表范围！HouseListReqDTO:{}",houseListReqDTO);
            result.setList(new  ArrayList<>());
            
            return result;
        }

        result.setList(houses);
        return result;
    }

    /**
     * 房源状态修改
     * @param houseStatusEditReqDTO
     * @return
     */
    @Override
    public void editStatus(HouseStatusEditReqDTO houseStatusEditReqDTO) {


        //校验房源是否存在
        House house = houseMapper.selectById(houseStatusEditReqDTO.getHouseId());
        if(null == house) {
            throw new ServiceException("房源不存在，无法修改其状态");
        }

        //修改状态，必须有状态
        HouseStatus houseStatus = houseStatusMapper.selectOne(new LambdaQueryWrapper<HouseStatus>()
                .eq(HouseStatus::getHouseId, house.getId()));

        if(null ==houseStatus || StringUtils.isEmpty(houseStatus.getStatus())) {
            throw new ServiceException("房源状态不存在，无法修改其状态");
        }

        //校验状态传参
        HouseStatusEnum statusEnum= HouseStatusEnum.getEnumByName(houseStatusEditReqDTO.getStatus());
        if(null == statusEnum) {
            throw new ServiceException("要修改的房源信息有误，无法修改状态");
        }

        //更新数据库
        houseStatus.setStatus(houseStatusEditReqDTO.getStatus());
        //设置其码~~

        if(StringUtils.isEmpty(houseStatusEditReqDTO.getRentTimeCode())) {
            throw new ServiceException("出租时长不能为空，无法修改其状态");
        }

        houseStatus.setRentTimeCode(houseStatusEditReqDTO.getRentTimeCode());


//      房源租期开始时间
        houseStatus.setRentStartTime(TimestampUtil.getCurrentMillis());

        switch(houseStatusEditReqDTO.getRentTimeCode()){
            case "one_year" -> houseStatus.setRentEndTime(TimestampUtil.getYearLaterMillis(1L));
            case "half_year" -> houseStatus.setRentEndTime(TimestampUtil.getMonthsLaterMillis(6L));
            case "thirty_seconds" ->houseStatus.setRentEndTime(TimestampUtil.getSecondsLaterMillis(30L));
            default -> throw new ServiceException("出租市场错误，无法修改状态！");
        }


        houseStatusMapper.updateById(houseStatus);
        //更新缓存
        cacheHouse(house.getId());
    }




    /**
     * 从缓存中查询缓存详情
     * @param houseId
     * @return
     */
    private HouseDTO getCacheHouse(Long houseId) {

        if(null == houseId){
            return null;
        }

        HouseDTO houseDTO = null;
        try{
            String houseDTOStr = redisService.getCacheObject(HOUSE_PREFIX + houseId, String.class);

            if(StringUtils.isBlank(houseDTOStr)){
                return null;
            }

            houseDTO = JsonUtil.string2Obj(houseDTOStr, HouseDTO.class);

        }catch (Exception e){
            log.error("从缓存中获取房源信息异常:{}",houseId,e);
        }

        return houseDTO;
    }


    /**
     * 修改房屋标签
     * @param houseId
     * @param olgtTagHouses
     * @param newTagCodes
     */
    private void editTagHouses(Long houseId, List<TagHouse> olgtTagHouses,List<String> newTagCodes) {


        // houseId: 1
        // old：1 2 3 4 5
        // new: 3 4 5 6 7
        //找出你要删除的标签
        Set<String> oldTagCodes = olgtTagHouses.stream()
                .map(TagHouse::getTagCode)
                .collect(Collectors.toSet());

        List<String> deleteTagCodes = oldTagCodes.stream()
                .filter(tagCode -> !newTagCodes.contains(tagCode))
                .collect(Collectors.toList());



        //1.需要区别sql中in的区别
        // 删除需要删除的tagCodes 1 2
        if(!CollectionUtils.isEmpty(deleteTagCodes)){
            tagHouseMapper.delete(new LambdaQueryWrapper<TagHouse>()
                    .eq(TagHouse::getHouseId, houseId)
                    .in(TagHouse::getTagCode, deleteTagCodes));
        }


        //过滤出要新增的tagCodes 6 7
        List<TagHouse> newTagHouses = newTagCodes.stream()
                .filter(newTagCode -> !oldTagCodes.contains(newTagCode))
                .map(newTagCode ->{
                    TagHouse tagHouse = new TagHouse();
                    tagHouse.setTagCode(newTagCode);
                    tagHouse.setHouseId(houseId);
                    return tagHouse;
                }).collect(Collectors.toList());


        //新增要新增的tagCodes 6 7
        if(!CollectionUtils.isEmpty(newTagHouses)){
            tagHouseMapper.insert(newTagHouses);
        }

    }

    /**
     * 判断房屋标签是否需要改变
     * @param oldTagHouses
     * @param newTagCodes
     * @return
     */
    private boolean tagHouseNeedChange(List<TagHouse> oldTagHouses, List<String> newTagCodes) {

        List<String> oldTagCodes = oldTagHouses
                .stream()
                .map(TagHouse::getTagCode)
                .sorted()  //排序
                .collect(Collectors.toList());

        newTagCodes = newTagCodes.stream().sorted().collect(Collectors.toList());

        return !Objects.equals(oldTagCodes,newTagCodes);
    }

    /**
     * 判断是更新城市房源映射
     * @param oldHouse
     * @param newCityId
     * @return
     */
    private boolean cityHouseNeedChange(House oldHouse, Long newCityId) {

        return !Objects.equals(oldHouse.getCityId(), newCityId);

    }


    /**
     * 更新房源映射（Mysql+Redis）
     * @param houseId
     * @param oldCityId
     * @param newCityId
     * @param newCityName
     */
    private void editCityHouses(Long houseId,Long oldCityId,Long newCityId,String newCityName) {


        //1.删掉老的映射记录
        cityHouseMapper.delete(new LambdaQueryWrapper<CityHouse>()
                .eq(CityHouse::getHouseId, houseId)
                .eq(CityHouse::getCityId, oldCityId));

        //2.新增新的映射记录
        CityHouse cityHouse = new CityHouse();
        cityHouse.setCityId(newCityId);
        cityHouse.setCityName(newCityName);
        cityHouse.setHouseId(houseId);
        cityHouseMapper.insert(cityHouse);

        //更新缓存
        cacheCityHouses(2,houseId,oldCityId,newCityId);
    }


    private void cacheHouse(Long houseId) {

        //1.通过ID查询完整的房源信息
        //参数校验
        if(null == houseId){
            log.warn("要缓存的房源ID为空");
            return;
        }
        HouseDTO houseDTO = getHouseDTOById(houseId);

        if(null == houseDTO){
            log.warn("缓存房源信息时,查询房源错误");
            return;
        }

//      判断houseDTO是否为空~~
        cacheHouse(houseDTO);

    }


    /**
     * 查询房源的基本信息
     * @param houseId
     * @return
     */
    private HouseDTO getHouseDTOById(Long houseId) {


        if(null == houseId){
            log.error("要查询的房源信息为空");
            return null;
        }

        //1、查房源、状态、tagHouse关联关系、房东信息
        House house = houseMapper.selectById(houseId);
        if(null == house){
            log.error("查询房源失败，houseId{}",houseId);
            return null;
        }


        //查询房东的信息
        AppUser appUser = appUserMapper.selectById(house.getUserId());
        if(null == appUser){
            log.error("查询房东信息失败，houseId:{},userId:{}",houseId,house.getUserId());
            return null;
        }
        //查询房源状态的信息
        HouseStatus houseStatus = houseStatusMapper.selectOne(new LambdaQueryWrapper<HouseStatus>()
                .eq(HouseStatus::getHouseId, houseId));

        if(null == houseStatus){
            log.error("查询房源状态信息失败:{}",houseId);
            return null;
        }
        //查询房源标签的信息
        List<TagHouse> tagHouses = tagHouseMapper.selectList(new LambdaQueryWrapper<TagHouse>().eq(TagHouse::getHouseId, houseId));


        //2.组装完整的房源信息
        return convertToHouseDTO(house,houseStatus,appUser,tagHouses);

    }


    /**
     * 组装房源的基本信息
     * @param house
     * @param houseStatus
     * @param appUser
     * @param tagHouses
     * @return
     */
    private HouseDTO convertToHouseDTO(House house, HouseStatus houseStatus, AppUser appUser, List<TagHouse> tagHouses) {

        //1.校验
        if(null == house || null == houseStatus || null == appUser){
            log.warn("房源信息不完整");
            return null;
        }


        HouseDTO houseDTO = new HouseDTO();

//      复制其基本信息
        BeanUtils.copyProperties(house,houseDTO);
        BeanUtils.copyProperties(houseStatus,houseDTO);
        BeanUtils.copyProperties(appUser,houseDTO);

//      设置其不能复制的信息
        houseDTO.setArea(house.getArea().doubleValue());
        houseDTO.setPrice(house.getPrice().doubleValue());
        houseDTO.setLongitude(house.getLongitude().doubleValue());
        houseDTO.setLatitude(house.getLatitude().doubleValue());
        houseDTO.setImages(JsonUtil.string2List(house.getImages(),String.class));

        //设备信息的处理
        //DeviceDTO
        List<String> dataKeys = Arrays.stream(house.getDevices().split(","))
                .distinct()
                .collect(Collectors.toList());

        List<DictionaryDataDTO> deviceDataDTOs = sysDictionaryService.getDicDataByKeys(dataKeys);

        List<DeviceDTO> deviceDTOs= deviceDataDTOs.stream()
                .map(dataDTO->{
                    DeviceDTO deviceDTO = new DeviceDTO();
                    deviceDTO.setDeviceCode(dataDTO.getDataKey());
                    deviceDTO.setDeviceName(dataDTO.getValue());
                    return deviceDTO;
                }).collect(Collectors.toList());

        houseDTO.setDevices(deviceDTOs);

        //TagDTO
        List<String> tagCodes = tagHouses.stream()
                .map(TagHouse::getTagCode)
                .distinct()
                .collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(tagCodes)){
            List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<Tag>().in(Tag::getTagCode,  tagCodes));
            houseDTO.setTags(BeanCopyUtil.copyList(tags, TagDTO::new));
        }

        return houseDTO;

    }

    /**
     * 缓存HouseDTO完整信息
     * @param houseDTO
     * @param timeout
     */
    private void cacheHouse(HouseDTO houseDTO,Long timeout) {
        if (null == houseDTO) {
            log.warn("要缓存的房源详细信息为空！");
            return;
        }

        // 缓存
        try {
            redisService.setCacheObject(HOUSE_PREFIX + houseDTO.getHouseId(),
                    JsonUtil.obj2String(houseDTO),timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("缓存房源完整信息时发生异常，houseDTO:{}", JsonUtil.obj2String(houseDTO), e);
            // 对于房源完整信息，是否存在于redis，不需要强一致性。
            // 因为C端查询时，如果redis不存在，可以通过查MySQL获取到数据，让后再放入Redis。
            // throw e;
        }
    }


    private void cacheHouse(Long houseId,Long timeout) {
        if (null == houseId) {
            log.warn("要缓存的房源详细信息为空！");
            return;
        }

        // 缓存
        try {
            redisService.setCacheObject(HOUSE_PREFIX + houseId,
                    JsonUtil.obj2String(new HouseDTO()),timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("缓存房源完整信息时发生异常，houseId:{}", JsonUtil.obj2String(houseId), e);
            // 对于房源完整信息，是否存在于redis，不需要强一致性。
            // 因为C端查询时，如果redis不存在，可以通过查MySQL获取到数据，让后再放入Redis。
            // throw e;
        }
    }



    //缓存房源的信息
    private void cacheHouse(HouseDTO houseDTO) {
        if (null == houseDTO) {
            log.warn("要缓存的房源详细信息为空！");
            return;
        }

        // 缓存
        try {
            redisService.setCacheObject(HOUSE_PREFIX + houseDTO.getHouseId(),
                    JsonUtil.obj2String(houseDTO));
        } catch (Exception e) {
            log.error("缓存房源完整信息时发生异常，houseDTO:{}", JsonUtil.obj2String(houseDTO), e);
            // 对于房源完整信息，是否存在于redis，不需要强一致性。
            // 因为C端查询时，如果redis不存在，可以通过查MySQL获取到数据，让后再放入Redis。
            // throw e;
        }
    }

    /**
     * 新增TagHouse
     * @param houseId
     * @param tagCodes
     */
    private void addTagHouses(Long houseId, List<String> tagCodes) {
//      一个房源有多个房源标签
        List<TagHouse> tagHouses = tagCodes.stream()
                .map(tagCode ->{
                    TagHouse tagHouse = new TagHouse();
                    tagHouse.setTagCode(tagCode);
                    tagHouse.setHouseId(houseId);
                    return tagHouse;
                }).collect(Collectors.toList());
        
        tagHouseMapper.insert(tagHouses);
    }

    /**
     * 新增cityHouse
     * @param houseId
     * @param cityId
     * @param cityName
     */
    private void addCityHouse(Long houseId, Long cityId, String cityName) {

        CityHouse cityHouse = new CityHouse();
        cityHouse.setCityId(cityId);
        cityHouse.setCityName(cityName);
        cityHouse.setHouseId(houseId);
//      新增
        cityHouseMapper.insert(cityHouse);

//      进行redis缓存
//      一个城市在有多个房源,下面旨在缓存key:城市ID,value:房源ID
        cacheCityHouses(1,houseId, null,cityId);
    }

    private void cacheCityHouses(int op, Long houseId, Long oldCityId, Long newCityId) {

        try{

            if(1 == op){

                redisService.setCacheList(CITY_HOUSE_PREFIX + newCityId, Arrays.asList(houseId));
            }else if(2 == op){
//          删除旧城市下的房源ID
                redisService.removeForList(CITY_HOUSE_PREFIX+oldCityId,Arrays.asList(houseId));
//          新增新城市在的房源ID
                redisService.setCacheList(CITY_HOUSE_PREFIX + newCityId,Arrays.asList(houseId));
            }else{
                log.error("无效的操作:缓存城市房源关联信息");
            }

        }catch (Exception e){
            log.error("缓存城市下的房源列表发生异常，op:{}, houseId:{}, oldCityId:{}, newCityId:{}",
                    op, houseId, oldCityId, newCityId, e);
            // 注意这里抛出了异常，保证事务
            // 因为C端获取房源列表是以城市ID列表为主的，因此我们必须保证redis和mysql的数据的一致性！！！
            throw e;
        }

    }

    private void checkAddOrEditReq(HouseAddOrEditReqDTO houseAddOrEditReqDTO) {

        //1.校验房东信息
        AppUser appUser = appUserMapper.selectById(houseAddOrEditReqDTO.getUserId());
        if(null == appUser){
            throw new ServiceException("房东信息不存在", ResultCode.INVALID_PARA.getCode());
        }

        //2.校验地址信息（城市ID，区域ID）
        List<Long> regionIds = Arrays.asList(houseAddOrEditReqDTO.getRegionId(),houseAddOrEditReqDTO.getCityId());

        List<SysRegion> sysRegions = regionMapper.selectBatchIds(regionIds);
        if(regionIds.size() != sysRegions.size()){
            throw new ServiceException("传递的城市信息有误！",ResultCode.INVALID_PARA.getCode());
        }

        //3.校验标签码
        List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<Tag>().in(Tag::getTagCode,houseAddOrEditReqDTO.getTagCodes()));
        if(tags.size() != houseAddOrEditReqDTO.getTagCodes().size()){
            throw new ServiceException("传递的标签列表有误！",ResultCode.INVALID_PARA.getCode());
        }
        //4.设备码，房源基本信息
    }

}

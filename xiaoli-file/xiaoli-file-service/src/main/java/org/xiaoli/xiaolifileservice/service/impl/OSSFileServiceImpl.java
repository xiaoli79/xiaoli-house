package org.xiaoli.xiaolifileservice.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;
import org.xiaoli.xiaolifileservice.config.OSSProperties;
import org.xiaoli.xiaolifileservice.constants.OSSCustomConstants;
import org.xiaoli.xiaolifileservice.domain.dto.FileDTO;
import org.xiaoli.xiaolifileservice.domain.dto.SignDTO;
import org.xiaoli.xiaolifileservice.service.IFileService;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class OSSFileServiceImpl implements IFileService {

    @Autowired
    private OSSClient ossClient;

    @Autowired
    private OSSProperties ossProperties;

    @Override
    public FileDTO upload(MultipartFile file) {

        try{
            InputStream inputStream = file.getInputStream();
//          获取原始的文件名
            String originalFilename = file.getOriginalFilename();
//          获取文件的后缀
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
//          按格式生成文件名，避免不重复
            String objectName = ossProperties.getPathPrefix()+UUID.randomUUID()+"."+extName;

            ObjectMetadata objectMetadata = new ObjectMetadata();
//          设置权限
            objectMetadata.setObjectAcl(CannedAccessControlList.PublicRead);

//          创建PutObjectRequest对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperties.getBucketName(), objectName, inputStream, objectMetadata);

//          创建PutObject请求~~
            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);

//          判断是否正常请求~~
            if(putObjectResult== null || StringUtils.isBlank(putObjectResult.getRequestId())){
                log.error("上传oss异常putObjectResult未正常返回：{}",putObjectResult);
                throw new ServiceException(ResultCode.OSS_UPLOAD_FAILED);
            }

//          创建返回对象，存储相应的信息
            FileDTO sysFileDTO = new FileDTO();
            sysFileDTO.setUrl(ossProperties.getBaseUrl()+ objectName);
            sysFileDTO.setKey(objectName);
            sysFileDTO.setName(new File(objectName).getName());
            return sysFileDTO;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//  前端获取签名的方法
    @Override
    public SignDTO getSign() {

        try {
            //获取ak sk
            String accesskeyid = ossProperties.getAccessKeyId();
            String accesskeysecret = ossProperties.getAccessKeySecret();
            // 获取当前时间
            Instant now = Instant.now();
            //构建返回数据
            SignDTO signDTO = new SignDTO();
            signDTO.setHost(ossProperties.getBaseUrl());
            signDTO.setPathPrefix(ossProperties.getPathPrefix());

            // 步骤1：创建policy。
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> policy = new HashMap<>();

//          对时间进行格式化
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(OSSCustomConstants.SIGN_EXPIRE_TIME_FORMAT)
                    .withZone(java.time.ZoneOffset.UTC);

//          增加超时时间 ，根据nacos上面有相应的配置
            String expiration = formatter.format(now.plusSeconds(ossProperties.getExpre()));
            policy.put("expiration", expiration);

            List<Object> conditions = new ArrayList<>();

            Map<String, String> bucketCondition = new HashMap<>();
            bucketCondition.put("bucket", ossProperties.getBucketName());
            conditions.add(bucketCondition);

            Map<String, String> signatureVersionCondition = new HashMap<>();
            signatureVersionCondition.put("x-oss-signature-version", "OSS4-HMAC-SHA256");
            conditions.add(signatureVersionCondition);

            Map<String, String> credentialCondition = new HashMap<>();

            formatter = DateTimeFormatter.ofPattern(OSSCustomConstants.SIGN_DATE_FORMAT)
                    .withZone(java.time.ZoneOffset.UTC);
            String dateStr = formatter.format(now);
            String xOSSCredential = accesskeyid + "/" + dateStr + "/" + ossProperties.getRegion() + "/oss/aliyun_v4_request";

//          构建相应的返回数据
            signDTO.setXOSSCredential(xOSSCredential);
            credentialCondition.put("x-oss-credential", xOSSCredential); // 替换为实际的 access key id
            conditions.add(credentialCondition);

            Map<String, String> dateCondition = new HashMap<>();

            // 定义日期时间格式化器
            formatter = DateTimeFormatter.ofPattern(OSSCustomConstants.SIGN_REQUEST_TIME_FORMAT)
                    .withZone(java.time.ZoneOffset.UTC);

            // 格式化时间
            String xOSSDate = formatter.format(now);

//          构建相应的返回数据
            signDTO.setXOSSDate(xOSSDate);
            dateCondition.put("x-oss-date", xOSSDate);

            conditions.add(dateCondition);


//          设置文件大小限制
            conditions.add(Arrays.asList("content-length-range", ossProperties.getMinLen(), ossProperties.getMaxLen()));
//          状态码的限制
            conditions.add(Arrays.asList("eq", "$success_action_status", "200"));

            policy.put("conditions", conditions);

            String jsonPolicy = mapper.writeValueAsString(policy);

            // 步骤2：构造待签名字符串（StringToSign）。
            String policyBase64 = new String(Base64.encodeBase64(jsonPolicy.getBytes()));

//          构建相应的返回数据
            signDTO.setPolicy(policyBase64);

            // 步骤3：计算SigningKey。
            byte[] dateKey = hmacsha256(("aliyun_v4" + accesskeysecret).getBytes(), dateStr);
            byte[] dateRegionKey = hmacsha256(dateKey, ossProperties.getRegion());
            byte[] dateRegionServiceKey = hmacsha256(dateRegionKey, "oss");
            byte[] signingKey = hmacsha256(dateRegionServiceKey, "aliyun_v4_request");

            // 步骤4：计算Signature。
            byte[] result = hmacsha256(signingKey, policyBase64);
            String signature = BinaryUtil.toHex(result);
            signDTO.setSignature(signature);
            return signDTO;
        } catch (Exception e) {
            log.error("生成直传签名失败", e);
            throw new ServiceException(ResultCode.PRE_SIGN_URL_FAILED);
        }
    }



//  相应生成的算法
    public static byte[] hmacsha256(byte[] key, String data) {
        try {
            // 初始化HMAC密钥规格，指定算法为HMAC-SHA256并使用提供的密钥。
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");

            // 获取Mac实例，并通过getInstance方法指定使用HMAC-SHA256算法。
            Mac mac = Mac.getInstance("HmacSHA256");
            // 使用密钥初始化Mac对象。
            mac.init(secretKeySpec);

            // 执行HMAC计算，通过doFinal方法接收需要计算的数据并返回计算结果的数组。
            byte[] hmacBytes = mac.doFinal(data.getBytes());

            return hmacBytes;
        } catch (Exception e) {
            log.error("生成直传签名失败", e);
            throw new ServiceException(ResultCode.PRE_SIGN_URL_FAILED);
        }
    }


    public static void main(String[] args) {
        System.out.println(UUID.randomUUID());
    }
}

package org.xiaoli.xiaolifileservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xiaoli.xiaolicommoncore.utils.BeanCopyUtil;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolifileservice.domain.dto.FileDTO;
import org.xiaoli.xiaolifileservice.domain.dto.SignDTO;
import org.xiaoli.xiaolifileservice.domain.vo.FileVO;
import org.xiaoli.xiaolifileservice.domain.vo.SignVO;
import org.xiaoli.xiaolifileservice.service.IFileService;

@RestController
@Slf4j
public class FileController {


    @Autowired
    private IFileService fileService;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<FileVO> upload(MultipartFile file) {
       FileDTO fileDTO  = fileService.upload(file);
//     使用Bean拷贝工具类可以实现VO到DTO数据的转换
        FileVO fileVO = new FileVO();

//      将DTO的数据拷贝到fileVO中~~
        BeanCopyUtil.copyProperties(fileDTO, fileVO);

//      返回结果~~
        return R.ok(fileVO);
    }


    /**
     * 获取签名
     * @param file
     * @return
     */
    @GetMapping("/sign")
    public R<SignVO> sign(MultipartFile file) {

        SignDTO signDTO = fileService.getSign();
        SignVO signVO = new SignVO();
        BeanCopyUtil.copyProperties(signDTO, signVO);

        return R.ok(signVO);
    }
}

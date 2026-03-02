package org.xiaoli.xiaolifileservice.service;

import org.springframework.web.multipart.MultipartFile;
import org.xiaoli.xiaolifileservice.domain.dto.FileDTO;
import org.xiaoli.xiaolifileservice.domain.dto.SignDTO;

public interface IFileService {


    FileDTO upload(MultipartFile file);

    SignDTO getSign();
}

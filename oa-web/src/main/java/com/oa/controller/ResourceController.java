package com.oa.controller;

import com.oa.base.PublicResultConstant;
import com.oa.config.ResponseHelper;
import com.oa.config.ResponseModel;
import com.oa.util.ComUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author oa
 * @since on 2018/5/11.
 */
@RestController
@RequestMapping("/resource")
//不加入swagger ui里
@ApiIgnore
public class ResourceController {

    @PostMapping
    public ResponseModel uploadResource(@RequestParam("files")MultipartFile[] multipartFiles) throws Exception {
        List<String> filePaths = new ArrayList<>();
        if(!ComUtil.isEmpty(multipartFiles) && multipartFiles.length != 0) {
            for (MultipartFile multipartFile : multipartFiles) {
//                int fileType =  FileUtil.getFileType(multipartFile.getOriginalFilename());
//                filePaths.add(
//                        FileUtil.saveFile(multipartFile.getInputStream(), fileType, multipartFile.getOriginalFilename(), null)
//                );
            }
        }
        return ResponseHelper.buildResponseModel(filePaths);
    }

    @DeleteMapping
    public ResponseModel deleteResource(@RequestParam("filePaths") List<String> filePaths){
        if(!ComUtil.isEmpty(filePaths) && filePaths.size() !=0){
            for (String item: filePaths) {
//                if(!FileUtil.deleteUploadedFile(item)){
//                    return ResponseHelper.validationFailure(PublicResultConstant.ERROR);
//                }
            }
        }
        return ResponseHelper.buildResponseModel(filePaths);
    }

}

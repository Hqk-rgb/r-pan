package com.whf.pan.server.modules.file.mapper;

import com.whf.pan.server.modules.file.context.QueryFileListContext;
import com.whf.pan.server.modules.file.entity.UserFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whf.pan.server.modules.file.vo.UserFileVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 26570
* @description 针对表【r_pan_user_file(用户文件信息表)】的数据库操作Mapper
* @createDate 2023-10-28 15:45:30
* @Entity com.whf.pan.server.modules.file.entity.UserFile
*/
public interface UserFileMapper extends BaseMapper<UserFile> {

    List<UserFileVO> selectFileList(@Param("param") QueryFileListContext context);

}





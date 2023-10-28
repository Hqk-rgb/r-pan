package com.whf.pan.server.modules.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whf.pan.server.modules.log.entity.ErrorLog;
import com.whf.pan.server.modules.log.service.ErrorLogService;
import com.whf.pan.server.modules.log.mapper.ErrorLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 26570
* @description 针对表【r_pan_error_log(错误日志表)】的数据库操作Service实现
* @createDate 2023-10-28 15:46:51
*/
@Service
public class ErrorLogServiceImpl extends ServiceImpl<ErrorLogMapper, ErrorLog>
    implements ErrorLogService{

}





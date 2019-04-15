package com.addplus.server.consumer.config.system;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.model.base.ReturnDataSet;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.SimpleDateFormat;


/**
 * Created by 特大碗拉面 on 2017/10/23 0023.
 */
@ControllerAdvice
public class AddControllerAdvice {

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class,new CustomDateEditor(dateFormat,true));
    }

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ReturnDataSet errorHandler(Exception ex) {
        ReturnDataSet returnDataSet =new ReturnDataSet();
        returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_SERVICE);
        returnDataSet.setErrorInfo(ex.getMessage());
        return returnDataSet;
    }


    /**
     * 拦截捕捉自定义异常 ErrorException.class
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ErrorException.class)
    public ReturnDataSet myErrorHandler(ErrorException ex, HttpServletResponse response) throws Exception {
        ReturnDataSet returnDataSet =new ReturnDataSet();
        returnDataSet.setReturnCode(ex.getReturnCode());
        if(ex.getReturnInfo()!=null){
            returnDataSet.setDataSet(ex.getReturnInfo());
        }
        return returnDataSet;
    }

}

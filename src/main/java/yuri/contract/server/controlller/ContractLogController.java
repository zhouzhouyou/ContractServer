package yuri.contract.server.controlller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.model.ContractLog;
import yuri.contract.server.service.ContractLogService;
import yuri.contract.server.util.annotation.NeedToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

import lombok.Data;
import yuri.contract.server.util.response.ResponseFactory;
import yuri.contract.server.util.toExcel.ExcelUtil;

import java.sql.Date;

public class ContractLogController  extends BaseController{
    private final ContractLogService contractLogService;

    @Autowired
    public ContractLogController(HttpServletRequest request,ContractLogService contractLogService) {
        super(request);
        this.contractLogService=contractLogService;
    }

    @ApiOperation("查询所有日志")
    @PostMapping("/selectAllLog")
    @CrossOrigin
    @ResponseBody
    @NeedToken(function = NeedToken.GRANT)
    public ResponseEntity<List<ContractLog>> selectAllLog() {
        return contractLogService.selectAllLog();
    }

    @ApiOperation("查询")
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectLog")
    @NeedToken(function = NeedToken.GRANT)
    public ResponseEntity<List<ContractLog>> fuzzyQuery(@RequestBody LogInfo a, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(null);
        return contractLogService.selectLog(a.userName,a.fromTime,a.toTime);
    }

    @Data
    private static class LogInfo {
        /**
         * 操作人员
         */
        private String userName;

        /**
         * 起始时间
         */
        private Date fromTime;

        /**
         * 终止时间
         */
        private Date toTime;
    }

    @ApiOperation("模糊查询")
    @CrossOrigin
    @ResponseBody
    @PostMapping("/fuzzyQuery")
    @NeedToken(function = NeedToken.GRANT)
    public ResponseEntity<List<ContractLog>> fuzzyQuery(@RequestBody Query a, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(null);
        return contractLogService.fuzzyQuery(a.query);
    }

    @Data
    @ApiModel(description = "模糊查询请求")
    private static class Query {
        /**
         * 关键字
         */
        private String query;
    }


    @ApiOperation("转excel")
    @CrossOrigin
    @NeedToken(function = NeedToken.GRANT)
    @PostMapping("/export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //获取数据
        List<ContractLog> list = contractLogService.getLogList();//查询到了所有的用户

        //excel标题
        String[] title = {"id", "userName", "content", "time"};

        //excel文件名
        String fileName = "logBackup" + System.currentTimeMillis() + ".xls";

        //sheet名
        String sheetName = "log";

        String content[][] = new String[list.size()][title.length];
        for (int i = 0; i < list.size(); i++) {


            ContractLog obj = list.get(i);
            content[i][0] = obj.getId().toString();
            content[i][1] = obj.getUserName();
            content[i][2] = obj.getContent();
            content[i][3] = sdf.format(obj.getTime());
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

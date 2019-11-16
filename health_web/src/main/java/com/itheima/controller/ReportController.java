package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报表控制层
 *
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;


    @Reference
    private SetmealService setmealService;//套餐预约占比的饼图


    @Reference
    private ReportService reportService;// 页面需要的数据是 多种类型



    /**
     * 会员数量折线图
     */
    @RequestMapping(value = "/getMemberReport", method = RequestMethod.GET)
    public Result getMemberReport() {
        Map<String, Object> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);//得到一年前的时间
        //1.获取年月时间
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            String yearMonth = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
            months.add(yearMonth);
        }
        map.put("months", months);
        //2.根据年月时间 获取每月底累加会员数量
        List<Integer> memberCount = memberService.findMemberCountBeforeDate(months);
        map.put("memberCount", memberCount);
        //"months":["2019-01","2019-02","2019-03","2019-04"],
        //"memberCount":[3,4,8,10]
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    /**
     * 套餐预约占比饼形图
     */
    @RequestMapping(value = "/getSetmealReport", method = RequestMethod.GET)
    public Result getSetmealReport() {
        Map<String, Object> rsMap = new HashMap<>();
        Map<String, Object> getSetmealMap = setmealService.getSetmealReport();
        //套餐名称
        rsMap.put("setmealNames", getSetmealMap.get("setmealNames"));
        //套餐对应的预约数量
        rsMap.put("setmealCount", getSetmealMap.get("setmealCount"));
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, rsMap);
    }


    /**
     * @Path: com.itheima.controller.ReportController
     * @param
     * @return: com.itheima.entity.Result
     * @Author: Zzc
     * @Date: 2019/11/15 16:56
     * @Description: 会员男女比例占比饼图
    */
    @RequestMapping(value = "/getMemberSexReport", method = RequestMethod.GET)
    public Result getMemberSexReport() {
        Map<String, Object> rsMap = new HashMap<>();
        Map<String, Object> getMemberMapSex = memberService.getMemberSexReport();
        //套餐名称
        rsMap.put("MemberSex", getMemberMapSex.get("MemberSex"));
        //套餐对应的预约数量
        rsMap.put("MemberSexCount", getMemberMapSex.get("MemberSexCount"));
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, rsMap);
    }

    /**
     * @Path: com.itheima.controller.ReportController
     * @param
     * @return: com.itheima.entity.Result
     * @Author: Zzc
     * @Date: 2019/11/15 16:56
     * @Description: 年龄段占比饼图
     */
    @RequestMapping(value = "/getMemberAgeReport", method = RequestMethod.GET)
    public Result getMemberAgeReport() {
        Map<String, Object> rsMap = new HashMap<>();
        Map<String, Object> getMemberMapSex = memberService.getMemberAgeReport();
        //套餐名称
        rsMap.put("MemberAge", getMemberMapSex.get("MemberAge"));
        //套餐对应的预约数量
        rsMap.put("MemberAgeCount", getMemberMapSex.get("MemberAgeCount"));
        int x = 0;
        int y =0;
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, rsMap);
        //

    }


    /**
     * 获取运营数据统计报表
     */
    @RequestMapping(value = "/getBusinessReportData", method = RequestMethod.GET)
    public Result getBusinessReportData() {
        Map<String, Object> rsMap = null;
        try {
            rsMap = reportService.getBusinessReportData();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, rsMap);
    }

    /**
     * 导出运营数据统计报表
     */
    /*@RequestMapping(value = "/exportBusinessReport", method = RequestMethod.GET)
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //1.获取运营数据统计报表数据
            Map<String, Object> businessReportData = reportService.getBusinessReportData();
            //2.获取模板路径  File.separator 根据不同的系统返回相应目录符号
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            //3.获取Excel模板对象
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //4.将businessReportData数据填充到Excel模板对象中
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);//获取第一个工作簿
            Integer todayNewMember = (Integer) businessReportData.get("todayNewMember");
            Integer totalMember = (Integer) businessReportData.get("totalMember");
            Integer thisWeekNewMember = (Integer) businessReportData.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) businessReportData.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) businessReportData.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) businessReportData.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) businessReportData.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) businessReportData.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) businessReportData.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) businessReportData.get("thisMonthVisitsNumber");
            List<Map> mapList = (List<Map>)businessReportData.get("hotSetmeal");//热门套餐集合

            XSSFRow row = sheet.getRow(2);///获取第三行对象
            XSSFCell cell = row.getCell(5);//获取第六列对象
            cell.setCellValue((String)businessReportData.get("reportDate"));

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            //热门套餐
            int rownum = 12;//第13行
            if(mapList != null && mapList.size()>0){
                for (Map map : mapList) {
                    String name = (String)map.get("name");//套餐名称
                    String setmeal_count = (String)map.get("setmeal_count").toString();//预约数量
                    String proportion = (String)map.get("proportion").toString();//预约占比
                    String remark = (String)map.get("remark");//备注
                    XSSFRow dataRow = sheet.getRow(rownum);
                    XSSFCell cellName = dataRow.getCell(4);
                    cellName.setCellValue(name);
                    XSSFCell cellSetmealCount = dataRow.getCell(5);
                    cellSetmealCount.setCellValue(setmeal_count);
                    XSSFCell cellProportion = dataRow.getCell(6);
                    cellProportion.setCellValue(proportion);
                    XSSFCell cellRemark = dataRow.getCell(7);
                    cellRemark.setCellValue(remark);
                    rownum++;//遍历4次
                }
            }


            //5.通过输出流返回页面下载本地磁盘
            OutputStream outputStream = response.getOutputStream();
            //设置文件名 文件类型
            //文件类型 告知浏览器返回2007 excel
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //attachment:以附件的形式下载 filename=report.xlsx:指定下载文件名
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            xssfWorkbook.write(outputStream);
            //6.释放资源
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
        return null;
    }*/


    @RequestMapping(value = "/exportBusinessReport", method = RequestMethod.GET)
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //1.获取运营数据统计报表数据
            Map<String, Object> businessReportData = reportService.getBusinessReportData();
            //2.获取模板路径  File.separator 根据不同的系统返回相应目录符号
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            //3.获取Excel模板对象 XSSFWorkbook：2007  HSSFWorkbook：2003
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //通过第三方模板技术  excel模板对象（例子：改造${reportData}） 模板对应数据map
            //针对poi开发模板技术  jxl
            XLSTransformer transformer = new XLSTransformer();
            transformer.transformWorkbook(xssfWorkbook, businessReportData);
            //5.通过输出流返回页面下载本地磁盘
            OutputStream outputStream = response.getOutputStream();
            //设置文件名 文件类型
            //文件类型 告知浏览器返回2007 excel
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //attachment:以附件的形式下载 filename=report.xlsx:指定下载文件名
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            xssfWorkbook.write(outputStream);
            //6.释放资源
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
        return null;
    }
}

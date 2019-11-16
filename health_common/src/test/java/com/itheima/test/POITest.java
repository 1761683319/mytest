package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * excel入门案例
 * @author wangxin
 * @version 1.0
 */
public class POITest {

    /**
     * 方式一：读取excel方式
     * @throws IOException
     */
    //@Test
    public void readExcel01() throws IOException {
        //1.获取excel文件
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("C:\\Users\\admin\\Desktop\\read.xlsx");
        //2.获取工作簿 sheet页
        //XSSFSheet sheet1 = xssfWorkbook.getSheet("Sheet1");
        XSSFSheet sheet1 = xssfWorkbook.getSheetAt(0);
        //3.获取每一行记录
        for (Row row : sheet1) {
            //4.获取每一列数据
            //rows:每一行对象
            for (Cell cell : row) {
                //cell:每一列单元格
                System.out.println(cell.getStringCellValue());
            }
            System.out.println("获取每一行数据*************");
        }
        //5.资源关闭
        xssfWorkbook.close();
    }


    /**
     * 方式二：读取excel方式
     * @throws IOException
     */
    //@Test
    public void readExcel02() throws IOException {
        //1.获取excel文件
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("C:\\Users\\admin\\Desktop\\read.xlsx");
        //2.获取工作簿 sheet页
        //XSSFSheet sheet1 = xssfWorkbook.getSheet("Sheet1");
        XSSFSheet sheet1 = xssfWorkbook.getSheetAt(0);
        //得到sheet 最后一行行号 行号从0开始  for(int i = 0;i<=1;i++)
        int lastRowNum = sheet1.getLastRowNum();
        System.out.println(lastRowNum);
        //3.根据lastRowNum遍历行
        for(int i = 0;i<=lastRowNum;i++){
            XSSFRow row = sheet1.getRow(i);
            //根据行 获取每一列  下标从0开始 lastCellNum值最后一列值
            short lastCellNum = row.getLastCellNum(); //3
            for(int j = 0;j<lastCellNum;j++){
                System.out.println(row.getCell(j).getStringCellValue());
            }
            System.out.println("获取每一行数据*************");
        }
        //5.资源关闭
        xssfWorkbook.close();
    }

    /**
     *  向Excel文件写入数据
     */
   // @Test
    public void writeExcel() throws IOException {

        //1.创建excel对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //2.创建工作簿对象
        XSSFSheet xssfSheet = xssfWorkbook.createSheet("poi");
        //3.创建行对象
        XSSFRow titleRow = xssfSheet.createRow(0);//标题行
        //4.创建列对象
        titleRow.createCell(0).setCellValue("编号");
        titleRow.createCell(1).setCellValue("姓名");
        titleRow.createCell(2).setCellValue("年龄");

        //从数据库中查询结果集 list<T> list.size()
        XSSFRow dataRow = xssfSheet.createRow(1);
        dataRow.createCell(0).setCellValue("001");
        dataRow.createCell(1).setCellValue("老王");
        dataRow.createCell(2).setCellValue("18");
        //5.将excel通过输出流的方式写入磁盘
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\admin\\Desktop\\poi.xlsx");
        xssfWorkbook.write(fileOutputStream);
        //6.资源关闭
        fileOutputStream.flush();
        fileOutputStream.close();
        xssfWorkbook.close();

    }
}

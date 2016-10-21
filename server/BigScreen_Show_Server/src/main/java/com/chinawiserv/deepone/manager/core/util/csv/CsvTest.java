package com.chinawiserv.deepone.manager.core.util.csv;

import com.chinawiserv.deepone.manager.core.util.DateTime;
import com.chinawiserv.deepone.manager.core.util.MD5Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class CsvTest {

    public static void main(String... strings) {
        try {
            read(new File("V:\\files\\DSJ_SWDJQK-地税局 税务登记情况.csv"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void read(File file) throws Exception {
        if (file != null) {
            String fileName = file.getName();
            if (fileName != null && fileName.indexOf("-") > 0 && fileName.indexOf(".") > 0) {
                InputStream inputStream = new FileInputStream(file);
                int index = fileName.indexOf("-");
                String tableName = fileName.substring(0, index);
                String tableNameCN = fileName.substring(index + 1, fileName.indexOf("."));
                if (fileName.toLowerCase().endsWith("csv")) {
                    CsvReader reader = new CsvReader(inputStream, Charset.forName("GBK"));
                    reader.readHeaders();
                    reader.readHeaders();
                    String[] headersCN = reader.getHeaders();
                    reader.readHeaders();
                    String[] headersLE = reader.getHeaders();
                    reader.readHeaders();
                    String[] headersEN = reader.getHeaders();
                    if (headersCN != null && headersLE != null && headersEN != null &&
                            headersCN.length == headersLE.length && headersLE.length == headersEN.length) {
                        int length = headersCN.length;
                        StringBuffer sb = new StringBuffer();
                        sb.append("create table "+tableName+" (");
                        sb.append("    uuid varchar (36) not null comment '流水键',");
                        sb.append("    updatetime varchar(20) comment '上传时间',");
                        for (int i = 0 ; i < length ; i ++) {
                            sb.append("    "+headersEN[i]+" varchar("+headersLE[i]+") comment '"+headersCN[i]+"',");
                        }
                        sb.append("    primary key (uuid)");
                        sb.append(") comment '"+tableNameCN+"' ");

                        //执行 sql 语句，生成表！
                        System.out.println(sb.toString());
                        while (reader.readRecord()) {
                            String keys = "";
                            String values = "";
                            for (String header : headersEN) {
                                if (!"uuid".equalsIgnoreCase(header) && !"updatetime".equalsIgnoreCase(header)) {
                                    keys += header +  ",";
                                    values += reader.get(header) +  ",";
                                }
                            }
                            String uuid = MD5Util.getMD5String(values);
                            keys = keys + "uuid, updatetime";
                            values = values + "'"+uuid+"','"+ DateTime.getCurrentDate_YYYYMMDDHHMMSS()+"'";
                            String lastSql =  "insert into "+tableName + "(" + keys + ") values(" + values + ")";
                            System.out.println(lastSql);
                        }
                    }
                    else {
                        //文件中关于表的结构说明错误！
                    }
                }
                else {
                    //目前只支持CSV格式数据文件！
                }
            }
            else {
                //文件名正确格式：表英文名-表中文名.拓展名
            }
        }
        else {
            //文件为空，请先选择文件！
        }
    }
}

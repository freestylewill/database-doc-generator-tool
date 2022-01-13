package com.guigu.tool.database.doc.generator.doc;

import com.guigu.tool.database.doc.generator.bean.ColumnVo;
import com.guigu.tool.database.doc.generator.bean.TableVo;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WordGenerator
 *
 * @author zt
 * @version 2019/1/12 0012
 */
public class WordGenerator {

    private static Configuration configuration = null;

    public static void createDoc(String rootDir, String dbName, List<TableVo> list) {
        Map map = new HashMap();
        map.put("dbName", dbName);
        map.put("tables", list);
        try {
            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("utf-8");
            /** 加载模板 **/
            //这个方法在eclipse跑是OK 的 打jar包部署获取不到模版
            /*File file = ResourceUtils.getFile("classpath:templates");
            configuration.setDirectoryForTemplateLoading(file);*/
            //	  这个方法在eclipse跑和打jar包部署都可以获取到模版
            configuration.setClassForTemplateLoading(WordGenerator.class.getClass(), "/templates");
            Template template = configuration.getTemplate("database.html");
            String name = rootDir + File.separator + dbName + ".html";
            System.err.println(name);
            File file = new File(name);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
            template.process(map, writer);
            writer.close();
            new Html2DocConverter(name, rootDir + File.separator + dbName + ".doc")
                    .writeWordFile();
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }


    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<TableVo> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TableVo tableVo = new TableVo();
            tableVo.setTable("表" + i);
            tableVo.setComment("注释" + i);
            List<ColumnVo> columns = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                ColumnVo columnVo = new ColumnVo();
                columnVo.setName("name" + j);
                columnVo.setComment("注释" + j);
                columnVo.setKey("PRI");
                columnVo.setIsNullable("是");
                columnVo.setType("varchar(2");
                columns.add(columnVo);

            }
            tableVo.setColumns(columns);
            list.add(tableVo);
        }

        createDoc("D:\\tmp", "test", list);

    }

}

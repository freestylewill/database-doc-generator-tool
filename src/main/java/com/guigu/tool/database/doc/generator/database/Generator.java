package com.guigu.tool.database.doc.generator.database;

import com.guigu.tool.database.doc.generator.bean.ColumnVo;
import com.guigu.tool.database.doc.generator.bean.TableVo;
import com.guigu.tool.database.doc.generator.doc.WordGenerator;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * Generator
 *
 * @author zt
 * @version 2019/1/6 0006
 */
public abstract class Generator {
    private SimpleDataSource dataSource;
    protected Dao dao = null;
    protected String dbName;
    protected String docPath;

    protected String rootPath = "D:/tmp";

    public Generator(String dbName, SimpleDataSource dataSource) {
        this.dataSource = dataSource;
        dao = new NutDao(dataSource);
        int i = dbName.indexOf("?");
        if (i > -1) {
            dbName = dbName.substring(0, i);
        }
        System.err.println("dbName==========" + dbName);
        this.dbName = dbName;
        this.docPath = rootPath + File.separator + dbName + "-doc";
    }

    /**
     * 获取表结构数据
     *
     * @return
     */
    public abstract List<TableVo> getTableData();

    public void generateDoc() {
        File docDir = new File(docPath);
        if (docDir.exists()) {

            String str = "\n【温馨提示】 - 文件夹" + docPath + "已存在。 是否删除?(y 默认删除)\n";
            //throw new RuntimeException(str);
            System.out.print(str);

            Scanner sc = new Scanner(System.in);
            String dbType = sc.nextLine();
            if ("y".equals(dbType) || "".equals(dbType)) {
                docDir.delete();
            } else {
                return;
            }
        } else {
            docDir.mkdirs();
        }
        List<TableVo> list = getTableData();
        save2File(list);
        //保存word
//        WordGenerator.createDoc(dbName, list);
        WordGenerator.createDoc(docPath, dbName, list);

    }

    public void save2File(List<TableVo> tables) {
        saveSummary(tables);
        saveReadme(tables);
        for (TableVo tableVo : tables) {
            saveTableFile(tableVo);
        }
    }

    private void saveSummary(List<TableVo> tables) {
        StringBuilder builder = new StringBuilder("# Summary").append("\r\n").append("* [Introduction](README.md)")
                .append("\r\n");
        for (TableVo tableVo : tables) {
            String name = Strings.isEmpty(tableVo.getComment()) ? tableVo.getTable() : tableVo.getComment();
            builder.append("* [" + name + "](" + tableVo.getTable() + ".md)").append("\r\n");
        }
        try {
            Files.write(new File(docPath + File
                    .separator + "SUMMARY.md"), builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveReadme(List<TableVo> tables) {
        StringBuilder builder = new StringBuilder("# " + dbName + "数据库文档").append("\r\n");
        for (TableVo tableVo : tables) {
            builder.append("- [" + (Strings.isEmpty(tableVo.getComment()) ? tableVo.getTable() : tableVo.getComment())
                    + "]" +
                    "(" + tableVo
                    .getTable() + ".md)")
                    .append
                            ("\r\n");
        }
        try {
            Files.write(new File(docPath + File
                    .separator + "README.md"), builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveTableFile(TableVo table) {

        StringBuilder builder = new StringBuilder("# " + (Strings.isBlank(table.getComment()) ? table.getTable() : table
                .getComment()) + "(" + table.getTable() + ")").append("\r\n");
        builder.append("| 列名   | 类型   | KEY  | 可否为空 | 注释   |").append("\r\n");
        builder.append("| ---- | ---- | ---- | ---- | ---- |").append("\r\n");
        List<ColumnVo> columnVos = table.getColumns();
        for (int i = 0; i < columnVos.size(); i++) {
            ColumnVo column = columnVos.get(i);
            builder.append("|").append(column.getName()).append("|").append(column.getType()).append("|").append
                    (Strings.sNull(column.getKey())).append("|").append(column.getIsNullable()).append("|").append
                    (column.getComment()).append("|\r\n");
        }
        try {
            Files.write(new File(docPath + File
                    .separator + table.getTable() + ".md"), builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Record> getList(String sqlStr) {
        Sql sql = Sqls.create(sqlStr);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        List<Record> list = sql.getList(Record.class);
        return list;
    }
}

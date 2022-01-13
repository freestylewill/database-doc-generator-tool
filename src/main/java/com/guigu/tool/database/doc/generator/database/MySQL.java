package com.guigu.tool.database.doc.generator.database;
import com.guigu.tool.database.doc.generator.bean.ColumnVo;
import com.guigu.tool.database.doc.generator.bean.TableVo;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.SimpleDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseDocService
 *
 * @author zt
 * @version 2018/10/6 0006
 */
public class MySQL extends Generator {

    private final String URL = "jdbc:postgresql://192.168.3.160:10655/cibpm";
    private final String USER = "postgres";
    private final String PASSWORD = "888888";
    private final String DRIVER = "org.postgresql.Driver";
    private String tableName="";

    public Connection getConnection() throws Exception{
        Class.forName(DRIVER);
        Connection connection= DriverManager.getConnection(URL, USER, PASSWORD);
        return connection;
    }

//    private final String changeTableName = replaceUnderLineAndUpperCase(tableName);
//    private final String changeTableName = CamelCaseUtil.toCapitalizeCamelCase(tableName);

//    Connection connection = getConnection();
//    DatabaseMetaData databaseMetaData = connection.getMetaData();
//    ResultSet resultSet = databaseMetaData.getColumns(null,"%", tableName,"%");






    String sqlTables = "select table_name,table_comment from information_schema.tables where table_schema = '@dbname'" +
            " order by table_name asc";
    String sqlColumns = "select column_name,column_type,column_key,is_nullable,column_comment from information_schema" +
            ".columns where table_schema = '@dbname'  and table_name " +
            "='@tablename'";

    public MySQL(String dbName, SimpleDataSource dataSource) {
        super(dbName,dataSource);
    }
    @Override
    public List<TableVo> getTableData(){
        String sql = sqlTables.replace("@dbname",dbName);
        List<Record> list = getList(sql);
        List<TableVo> tables = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Record record = list.get(i);
            String table = record.getString("table_name");
            String comment =record.getString("table_comment");
            TableVo tableVo = getTableInfo(table,comment);
            tables.add(tableVo);
        }
        return tables;
    }
    public TableVo getTableInfo(String table,String comment){
        TableVo tableVo = new TableVo();
        tableVo.setTable(table);
        tableVo.setComment(comment);
        String sql = sqlColumns.replace("@dbname",dbName);
        sql = sql.replace("@tablename",table);
        List<Record> list =getList(sql);
        List<ColumnVo> columns = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Record record = list.get(i);
            ColumnVo column = new ColumnVo();
            column.setName(record.getString("column_name"));
            column.setType(record.getString("column_type"));
            column.setKey(record.getString("column_key"));
            column.setIsNullable(record.getString("is_nullable").equals("NO")?"否":"是");
            column.setComment(record.getString("column_comment"));
            columns.add(column);
        }
        tableVo.setColumns(columns);
        return tableVo;
    }

}
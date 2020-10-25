package com.casbaherpapp.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;

public class ExcelHelper {

    public static final String Tablename = "MyTable1";
    public static final String id = "_id";// 0 integer
    public static final String Nom= "NOM";// 1 text(String)
    public static final String Type= "TYPE";// 2 text(String)
    public static final String NUM = "NUM";// 3 text(String)
    public static final String wilaya = "WILAYA";// 3 text(String)

    public static final String zone = "COMMUNE";// 3 text(String)

    public static final String adresse = "ADRESSE";// 3 text(String)
    public static final String ID = "ID";// 3 text(String)
    public static final String longg = "LONGG";// 3 text(String)
    public static final String lat = "LAT";// 3 text(String)


    public static void insertExcelToSqlite(BDD bd, Sheet sheet) {
int i=0;
        for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext(); ) {
            Row row = rit.next();
if(i!=0){
            ContentValues contentValues = new ContentValues();
            row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
            row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
            row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
            row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
            row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
            row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
    row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
    row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
    row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);


    contentValues.put(ID, row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(Nom, row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(Type, row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(NUM, row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(wilaya, row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(zone, row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(adresse, row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
    contentValues.put(longg, row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
    contentValues.put(lat, row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());


    try {


                bd.insertpventelong(bd.getb(),Integer.parseInt(contentValues.getAsString(ID)),contentValues.getAsString(Nom),"0"+contentValues.getAsString(NUM),contentValues.getAsString(adresse),contentValues.getAsString(wilaya),contentValues.getAsString(Type)+"",contentValues.getAsString(zone),"0",bd.getID()+"",contentValues.getAsString(longg),contentValues.getAsString(lat));

            } catch (Exception ex) {
                Log.d("Exception in importing", ex.getMessage().toString());
            }
        }

        i++;}

    }
}
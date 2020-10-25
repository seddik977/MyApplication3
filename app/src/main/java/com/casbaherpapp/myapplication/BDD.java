package com.casbaherpapp.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BDD {

    public static final String KEY_STUID = "stuid";
    public static final String KEY_SUB1 = "subject_one";
    public static final String KEY_SUB2 = "subject_two";
    public static final String KEY_SUB3 = "subject_three";
    public static final String KEY_MARKS1 = "marks_one";
    public static final String KEY_MARKS2 = "marks_two";
    public static final String KEY_MARKS3 = "marks_three";
    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";

    private static final String DATABASE_NAME = "hand_to_hand";
    private static final String DATABASE_MARKSTABLE = "StudentMarks";
    private static final int DATABASE_VERSION = 5;
private int x=0,k=0, id=-1,cpt=0,cptp=0,cptliv=0,cptstock=0,countp=0,countliv=0,countstock=0;
    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;
    private int []id_lx,id_pv;

    private String[][] produitx,quantitex,quantite_ux,montant_bonx,endomagex,payementx,QVx,QVUx,IDPx,restex;


    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (oldVersion) {
                case 1:
                    db.execSQL("CREATE TABLE synch\n" +


                            "  (  veri default 0 \n" +


                            ");");
                    ContentValues cvvv = new ContentValues();
                    cvvv.put("veri", 0);
                    db.insertWithOnConflict("synch", null, cvvv, SQLiteDatabase.CONFLICT_IGNORE);

                case 2:  db.execSQL("CREATE TABLE distance\n" +


                        "  (  distance int default 0 \n" +


                        ");");


                case 3:  db.execSQL("CREATE TABLE promotion\n" +


                        "  (  id_promotion int , \n" +
                        "  date_debut date ,\n" +
                        "  date_fin date ,\n" +
                        "  nom_promotion TEXT ,\n" +
                        "  dsc_promotion TEXT ,\n" +
                        "  type_promotion TEXT ,\n" +
                        "  etat promotion \n" +

                        ");");

                    db.execSQL("CREATE TABLE promotion_produit\n" +


                            "  (  id int , \n" +
                            "  prix_reduction double ,\n" +
                            "  id_produit int ,\n" +
                            "  id_promotion int \n" +

                            ");");

                case 4 :
                    db.execSQL("CREATE TABLE product\n" +
                            "(id Integer NOT NULL, \n" +
                            "nom TEXT, \n" +
                            "famille TEXT, \n" +
                            "fardeau Integer, \n" +
                            "palette Integer, \n" +
                            "prix_usine REAL, \n" +
                            "quantite_u Integer \n" +


                            ");");

                    break;


            }

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
         /*   db.execSQL(" CREATE TABLE " + DATABASE_MARKSTABLE + " (" +
                    KEY_STUID + " TEXT PRIMARY KEY, " +
                    KEY_SUB1 + " TEXT NOT NULL, " +
                    KEY_SUB2 + " TEXT NOT NULL, " +
                    KEY_SUB3 + " TEXT NOT NULL, " +
                    KEY_MARKS1 + " INTEGER NOT NULL, " +
                    KEY_MARKS2 + " INTEGER NOT NULL, " +
                    KEY_MARKS3 + " INTEGER NOT NULL);"
            );*/
            db.execSQL("CREATE TABLE pvente\n" +
                    "(id Integer NOT NULL, \n" +
                    "  nom TEXT ,\n" +
                    "  num TEXT ,\n" +
                    "  branche TEXT ,\n" +
                    "  adresse TEXT ,\n" +
                    "  credit TEXT ,\n" +
                    "  longg VARCHAR(40) ,\n" +
                    "  lat VARCHAR(40) ,\n" +
                    "  zone TEXT ,\n" +
                    "  id_livreur TEXT ,\n" +
                    "  modifier TEXT ,\n" +
                    "  wilaya TEXT ,\n" +
                    "  type TEXT ,\n" +
                    "     UNIQUE (nom,zone)\n"+
                    ");");

            db.execSQL("CREATE TABLE produit\n" +
                    "(id Integer NOT NULL, \n" +
                    "  nom TEXT ,\n" +
                    "  prix_u TEXT ,\n" +
                    "  prix_uu TEXT ,\n" +
                    "  prix_f double ,\n" +
                    "  fardeaup TEXT ,\n" +
                    "  code_bar TEXT ,\n" +
                    "  quantite_u TEXT ,\n" +

                    "  quantite_f TEXT \n"+
                    ");");
            db.execSQL("CREATE TABLE product\n" +
                    "(id Integer NOT NULL, \n" +
                    "nom TEXT, \n" +
                    "famille TEXT, \n" +
                    "fardeau Integer, \n" +
                    "palette Integer, \n" +
                    "prix_usine REAL, \n" +
                    "quantite_u Integer \n" +


                    ");");
            db.execSQL("CREATE TABLE p\n" +
                    "(id Integer NOT NULL, \n" +
                    "  nom TEXT ,\n" +
                    "  prix_u TEXT ,\n" +
                    "  prix_f double \n" +

                    ");");

            db.execSQL("CREATE TABLE livraison\n" +
                    "( id Integer NOT NULL,\n" +
                    "  id_livreur integer, \n" +
                    "  id_pvente integer, \n" +
                    "  bon integer, \n" +
                    "  livreur TEXT ,\n" +
                    "    pvente TEXT ,\n" +
                    "    produit TEXT ,\n" +
                    "    type_p TEXT,\n" +
                    "    motif TEXT,\n" +
                    "    photo BLOB,\n" +
                    "    temps_p TEXT,\n" +
                    "  date TEXT NOT NULL,\n" +
                    "    heure TEXT NOT NULL,\n" +
                    "    heure_a TEXT ,\n" +
                    "    date_m TEXT,\n" +
                    "   payement double default 0,\n" +
                    "   reste double default 0,\n" +
                    "    user TEXT NOT NULL default 'non',\n" +
                    "    faite TEXT NOT NULL default 'non',\n" +
                    "    valide TEXT  default 'non',\n" +
                    "       UNIQUE (id_livreur,pvente,date,user)\n"+
                    ");");
           db.execSQL("CREATE TABLE produitv\n" +
                    "( id Integer NOT NULL,\n" +
                    "  id_livraison integer, \n" +
                    "  id_livreur integer, \n" +
                    "  id_pvente integer, \n" +
                    "  produit TEXT ,\n" +
                    "     quantite int not null,\n" +
                    "     quantite_u int not null default 0,\n" +
                    "    quantite_v int not null default 0,\n" +
                    "   quantite_u_v int not null default 0,\n" +
                    "    endomage int not null default 0,\n" +
                    "    montant_bon double,\n" +
                    "    date TEXT\n" +

                    ");");


            db.execSQL("CREATE TABLE location\n" +
                    "( id INT(11) NOT NULL,\n" +
                    "  longg VARCHAR(40) NOT NULL,\n" +
                    "  lat VARCHAR(40) NOT NULL,\n" +
                    "  date DATE,\n" +
                    "  heure VARCHAR(10) NOT NULL\n" +

                    "  );\n");

            db.execSQL(" CREATE TABLE " + "login" + " (" +
                    "id" + " INTEGER , " +
                    "user" + " TEXT not null , " +
                    "connecte" + " TEXT  default 'oui', " +
                    "branche" + " TEXT , " +
                    "password" + " INTEGER NOT NULL );"
            );
            db.execSQL("CREATE TABLE stockm\n" +
                    "(id Integer NOT NULL,\n" +
                    "  produit text ,\n" +
                    "     quantite_u int not null ,\n" +
                    "     quantite_f int not null ,\n" +
                    "    fardeau int not null ,\n" +

                    "     valeur double not null \n" +


                    ");");

            db.execSQL("CREATE TABLE seuil\n" +


                    "  (  seuil double default 0 \n" +


                    ");");

            db.execSQL("CREATE TABLE distance\n" +


                    "  (  distance int default 0 \n" +


                    ");");
            db.execSQL("CREATE TABLE notification\n" +


                    "  (  cpt default 0 \n" +


                    ");");

            db.execSQL("CREATE TABLE promotion\n" +


                    "  (  id_promotion int , \n" +
                    "  date_debut date ,\n" +
                    "  date_fin date ,\n" +
                    "  nom_promotion TEXT ,\n" +
                    "  dsc_promotion TEXT ,\n" +
                    "  type_promotion TEXT ,\n" +
                    "  etat promotion \n" +

                    ");");

            db.execSQL("CREATE TABLE promotion_produit\n" +


                    "  (  id int , \n" +
                    "  prix_reduction double ,\n" +
                    "  id_produit int ,\n" +
                    "  id_promotion int \n" +

                    ");");

            db.execSQL("CREATE TABLE synch\n" +


                    "  (  veri default 0 \n" +


                    ");");
            ContentValues cvvv = new ContentValues();
            cvvv.put("veri", 0);
            db.insertWithOnConflict("synch", null, cvvv, SQLiteDatabase.CONFLICT_IGNORE);


            ContentValues cv = new ContentValues();
            cv.put("id", 1);
            cv.put("user", "");
            cv.put("password", 0);
            cv.put("branche", "c");


            db.insertWithOnConflict("login", null, cv, SQLiteDatabase.CONFLICT_IGNORE);

          ;



            ContentValues cvv = new ContentValues();
            cvv.put("cpt", 0);



            db.insertWithOnConflict("notification", null, cvv, SQLiteDatabase.CONFLICT_IGNORE);
        }




    }

    public BDD(Context c) {
        ourContext = c;
    }


    public void dropliv(){
        ourDatabase.delete("produitv"," id_livraison in(select id from livraison where user=?)",new String [] {String.valueOf("non")});

        ourDatabase.delete("livraison","user=?",new String [] {String.valueOf("non")});

    }
    public void drop(){
        ourDatabase.execSQL("DROP TABLE IF EXISTS stockm" );
        ourDatabase.execSQL("DROP TABLE IF EXISTS produit" );
        ourDatabase.execSQL("DROP TABLE IF EXISTS p" );

        ourDatabase.execSQL("DROP TABLE IF EXISTS promotion" );
        ourDatabase.execSQL("DROP TABLE IF EXISTS promotion_produit" );
        ourDatabase.execSQL("DROP TABLE IF EXISTS product" );


        /*ourDatabase.execSQL("DROP TABLE livraison" );
        ourDatabase.execSQL("DROP TABLE produitv" );


        ourDatabase.execSQL("CREATE TABLE livraison\n" +
                "( id Integer NOT NULL,\n" +
                "  id_livreur integer, \n" +
                "  id_pvente integer, \n" +
                "  bon integer, \n" +
                "  livreur TEXT ,\n" +
                "    pvente TEXT ,\n" +
                "    produit TEXT ,\n" +
                "    type_p TEXT,\n" +
                "    motif TEXT,\n" +
                "    photo BLOB,\n" +
                "    temps_p TEXT,\n" +
                "  date TEXT NOT NULL,\n" +
                "    heure TEXT NOT NULL,\n" +
                "    heure_a TEXT ,\n" +
                "    date_m TEXT,\n" +
                "    user TEXT NOT NULL default 'non',\n" +
                "    faite TEXT NOT NULL default 'non',\n" +
                "    valide TEXT  default 'non',\n" +
                "       UNIQUE (id_livreur,pvente,date,user)\n"+
                ");");
        ourDatabase.execSQL("CREATE TABLE produitv\n" +
                "( id Integer NOT NULL,\n" +
                "  id_livraison integer, \n" +
                "  id_livreur integer, \n" +
                "  id_pvente integer, \n" +
                "  produit TEXT ,\n" +
                "     quantite int not null,\n" +
                "     quantite_u int not null default 0,\n" +
                "    quantite_v int not null default 0,\n" +
                "   quantite_u_v int not null default 0,\n" +
                "    endomage int not null default 0,\n" +
                "    montant_bon double,\n" +
                "   payement double default 0,\n" +
                "   reste double default 0,\n" +
                "    date TEXT,\n" +
                "UNIQUE (produit,date,id_pvente)\n"+
                ");");*/

        ourDatabase.execSQL("CREATE TABLE stockm\n" +
                "(id Integer NOT NULL,\n" +
                "  produit text ,\n" +
                "     quantite_u int not null ,\n" +
                "     quantite_f int not null ,\n" +
                "    fardeau int not null ,\n" +

                "     valeur double not null \n" +


                ");");



     ourDatabase.execSQL("CREATE TABLE promotion\n" +


                "  (  id_promotion int , \n" +
                "  date_debut date ,\n" +
                "  date_fin date ,\n" +
                "  nom_promotion TEXT ,\n" +
                "  dsc_promotion TEXT ,\n" +
                "  type_promotion TEXT ,\n" +
                "  etat promotion \n" +

                ");");

       ourDatabase.execSQL("CREATE TABLE promotion_produit\n" +


                "  (  id int , \n" +
                "  prix_reduction double ,\n" +
                "  id_produit int ,\n" +
                "  id_promotion int \n" +

                ");");

        ourDatabase.execSQL("CREATE TABLE produit\n" +
                "(id Integer NOT NULL, \n" +
                "  nom TEXT ,\n" +
                "  prix_u TEXT ,\n" +
                "  prix_uu TEXT ,\n" +
                "  prix_f double ,\n" +
                "  code_bar TEXT ,\n" +
                "  fardeaup TEXT ,\n" +
                "  quantite_u TEXT ,\n" +

                "  quantite_f TEXT \n"+
                ");");
        ourDatabase.execSQL("CREATE TABLE p\n" +
                "(id Integer NOT NULL, \n" +
                "  nom TEXT ,\n" +
                "  prix_u TEXT ,\n" +
                "  prix_f double \n" +

                ");");

        ourDatabase.execSQL("CREATE TABLE product\n" +
                "(id Integer NOT NULL, \n" +
                "nom TEXT, \n" +
                "famille TEXT, \n" +
                "fardeau Integer, \n" +
                "palette Integer, \n" +
                "prix_usine REAL, \n" +
                "quantite_u Integer \n" +



                ");");

    }
    public Cursor getAllProducts(){
        Cursor c =ourDatabase.rawQuery("select * from product",null);


        return c;
    }
    public Cursor getProductsFamillyName(){
        Cursor c =ourDatabase.rawQuery("SELECT DISTINCT p.famille from product as p",null);


        return c;}
    public void Insertproduct(int id,String nom,String famille,int fardeau,int palette,double prix_usine,int quantite_u) {
//        ourDatabase.delete("product","id=?",new String [] {String.valueOf(id)});//Developed by imad
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("nom", nom);
        cv.put("famille", famille);
        cv.put("fardeau", fardeau);
        cv.put("palette", palette);
        cv.put("prix_usine",prix_usine);
        cv.put("quantite_u",quantite_u);

        ourDatabase.insert("product", null, cv);


    }


    public void c(){
        ourDatabase.execSQL("DROP TABLE IF EXISTS login" );

        ourDatabase.execSQL("CREATE TABLE livraison\n" +
                "( id Integer NOT NULL,\n" +
                "  id_livreur integer, \n" +
                "  id_pvente integer, \n" +
                "  bon integer, \n" +
                "  livreur TEXT ,\n" +
                "    pvente TEXT ,\n" +
                "    produit TEXT ,\n" +
                "    type_p TEXT,\n" +
                "    motif TEXT,\n" +
                "    photo BLOB,\n" +
                "    temps_p TEXT,\n" +
                "  date TEXT NOT NULL,\n" +
                "    heure TEXT NOT NULL,\n" +
                "    heure_a TEXT ,\n" +
                "    date_m TEXT,\n" +
                "    user TEXT NOT NULL default 'non',\n" +
                "    faite TEXT NOT NULL default 'non',\n" +
                "    valide TEXT  default 'non'\n" +

                ");");
        ourDatabase.execSQL("CREATE TABLE produitv\n" +
                "( id Integer NOT NULL,\n" +
                "  id_livraison integer, \n" +
                "  id_livreur integer, \n" +
                "  id_pvente integer, \n" +
                "  produit TEXT ,\n" +
                "     quantite int not null,\n" +
                "     quantite_u int not null default 0,\n" +
                "    quantite_v int not null default 0,\n" +
                "   quantite_u_v int not null default 0,\n" +
                "    endomage int not null default 0,\n" +
                "    montant_bon double,\n" +
                "   payement double default 0,\n" +
                "   reste double default 0,\n" +
                "    date TEXT\n" +

                ");");

        ourDatabase.execSQL("CREATE TABLE stockm\n" +
                "(id Integer NOT NULL,\n" +
                "  produit text ,\n" +
                "     quantite_u int not null ,\n" +
                "     quantite_f int not null ,\n" +
                "     valeur double not null \n" +


                ");");

        ourDatabase.execSQL(" CREATE TABLE " + "login" + " (" +
                "id" + " INTEGER , " +
                "user" + " TEXT not null , " +
                "connecte" + " TEXT  default 'oui', " +
                "branche" + " TEXT  , " +
                "password" + " INTEGER NOT NULL );"
        );

       ourDatabase.execSQL("CREATE TABLE promotion\n" +


                "  (  id_promotion int , \n" +
                "  date_debut date ,\n" +
                "  date_fin date ,\n" +
                "  nom_promotion TEXT ,\n" +
                "  dsc_promotion TEXT ,\n" +
                "  type_promotion TEXT ,\n" +
                "  etat promotion \n" +

                ");");

        ourDatabase.execSQL("CREATE TABLE promotion_produit\n" +


                "  (  id int , \n" +
                "  prix_reduction double ,\n" +
                "  id_produit int ,\n" +
                "  id_promotion int \n" +

                ");");
        ourDatabase.execSQL("CREATE TABLE seuil\n" +


                "  (  seuil double default 0 \n" +


                ");");
        ourDatabase.execSQL("CREATE TABLE distance\n" +


                "  (  distance int default 0 \n" +


                ");");
        ContentValues cv = new ContentValues();
        cv.put("id", 2);
        cv.put("user", "");
        cv.put("password", 0);
        cv.put("branche", "");


        ourDatabase.insertWithOnConflict("login", null, cv, SQLiteDatabase.CONFLICT_IGNORE);

    }

public void dec(){
    ContentValues cv = new ContentValues();
    cv.put("connecte", "non");
   ourDatabase.update("login", cv,"id<>0",null );

}

    public void updatelonglat(int id,String longg,String lat){
        ContentValues cv = new ContentValues();
        cv.put("longg", longg);
        cv.put("lat", lat);
        ourDatabase.update("pvente", cv,"id="+id,null );

    }
    public void setseuil(double c){
        ourDatabase.delete("seuil",null,null);
        ContentValues cv = new ContentValues();
        cv.put("seuil", c);
        ourDatabase.insert("seuil", null,cv );

    }

    public void setdistance(double c){
        ourDatabase.delete("distance",null,null);
        ContentValues cv = new ContentValues();
        cv.put("distance", c);
        ourDatabase.insert("distance", null,cv );

    }


    public void updateproduitv(int id_pvente,int id_livraison,String date,String produit){
        ContentValues cv = new ContentValues();
        cv.put("id_livraison", id_livraison);
        ourDatabase.update("produitv", cv,"id_pvente="+id_pvente+" and date="+date+" and produit="+produit+" and id_livraison in(select id from livraison where user='oui') ",null );

    }

    public BDD open() throws SQLException {
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public long createInsert(String stuid, String subject1, String subject2,
                             String subject3, String marks1, String marks2, String marks3) {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put(KEY_STUID, stuid);
        cv.put(KEY_SUB1, subject1);
        cv.put(KEY_SUB2, subject2);
        cv.put(KEY_SUB3, subject3);
        cv.put(KEY_MARKS1, marks1);
        cv.put(KEY_MARKS2, marks2);
        cv.put(KEY_MARKS3, marks3);
        return ourDatabase.insert(DATABASE_MARKSTABLE, null, cv);

    }

    public long Insert(int id, String user, String mdp,String b) {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("user", user);
        cv.put("password", mdp);
        cv.put("branche",b );
        cv.put("connecte", "oui");


        return ourDatabase.update("login", cv,null,null );

    }


    public void insertveri(int id) {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put("veri", id);


        ourDatabase.execSQL("UPDATE synch SET veri=?  WHERE 1",
                new String [] {String.valueOf(id)});
    }


    public void addnoti(int id) {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put("cpt", id);



        ourDatabase.execSQL("UPDATE notification SET cpt=cpt+?  WHERE 1",
                new String [] {String.valueOf(id)});
    }

    public void zero() {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put("cpt", id);



        ourDatabase.execSQL("UPDATE notification SET cpt=0  WHERE 1");
    }

    public int getnoti(){

        Cursor c =ourDatabase.rawQuery("select cpt from notification",null);
        while (c.moveToNext()){

            int data = c.getInt(c.getColumnIndex("cpt"));
            return data;
        }
        if(c!= null)
            c.close();
        return 0;

    }

    public int getcountpventes(){

        Cursor c =ourDatabase.rawQuery("select count(id) as cpt from pvente",null);
        while (c.moveToNext()){

            int data = c.getInt(c.getColumnIndex("cpt"));
            return data;
        }
        if(c!= null)
            c.close();
        return 0;

    }


    public int getcountp(){

        Cursor c =ourDatabase.rawQuery("select count(id) as cpt from p",null);
        while (c.moveToNext()){

            int data = c.getInt(c.getColumnIndex("cpt"));
            return data;
        }
        if(c!= null)
            c.close();
        return 0;

    }



    public long InsertLocation(int id, String longg, String lat, String date, String heure) {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("longg", longg);
        cv.put("lat", lat);
        cv.put("date",date);
        cv.put("heure",heure);

        return ourDatabase.insertWithOnConflict("location", null, cv, SQLiteDatabase.CONFLICT_IGNORE);

    }


    public long InsertStock(int id, String longg, String lat,String date,String heure,int f) {
        ourDatabase.delete("stockm","produit=?",new String [] {String.valueOf(longg)});
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("produit", longg);
        cv.put("quantite_f", lat);
        cv.put("quantite_u",date);
        cv.put("valeur",heure);
        cv.put("fardeau",f);


        return ourDatabase.insertWithOnConflict("stockm", null, cv, SQLiteDatabase.CONFLICT_IGNORE);

    }

    /*
    public void InsertLiv(int id,int id_l,int bon, String pvente, String heure,String faite,String motif,String date,String valide,String date_m) {
        // TODO Auto-generated method stub

        Cursor c =ourDatabase.rawQuery("select * from livraison where id=? ",new String [] {String.valueOf(id_l)});
        if(c.getCount()==0){


        ContentValues cv = new ContentValues();
        cv.put("id", id_l);
        cv.put("id_livreur", id);
        cv.put("bon", bon);
        cv.put("pvente", pvente);
        cv.put("heure",heure);
        cv.put("faite",faite);
        cv.put("motif",motif);
        cv.put("date",date);
        ourDatabase.insert("livraison", null, cv);
        }
       else if(c.getString(c.getColumnIndex("date_m")).equals(date_m)){
            ContentValues cv = new ContentValues();
            cv.put("id_livreur", id);
            cv.put("pvente", pvente);
            cv.put("bon", bon);
            cv.put("heure",heure);
            cv.put("faite",faite);
            cv.put("motif",motif);
            cv.put("date",date);
            ourDatabase.update("livraison",  cv,"id="+id_l,null);

        }

    }*/
    public void InsertLiva(int id,int id_l,int idpv,int bon, String pvente, String heure,String faite,String motif,String date,String valide,String date_m,String user) {
        // TODO Auto-generated method stub

        ourDatabase.delete("livraison","id=?",new String [] {String.valueOf(id_l)});


        ContentValues cv = new ContentValues();
        cv.put("id", id_l);
        cv.put("id_livreur", id);
        cv.put("bon", bon);
        cv.put("pvente", pvente);
        cv.put("heure",heure);
        cv.put("faite",faite);
        cv.put("motif",motif);
        cv.put("date",date);
        cv.put("user",user);
        cv.put("id_pvente",idpv);


        ourDatabase.insert("livraison", null, cv);



    }


    public int InsertLiv(int id,int id_l,String idpv,int bon, String pvente, String heure,String faite,String motif,String date,String valide,String date_m,String payement,String reste,String user,String photo) {
        // TODO Auto-generated method stub
        Cursor c =ourDatabase.rawQuery("select id from livraison  order by id desc limit 1",null);
        int data=0;
        int idu=0;
        while (c.moveToNext()){

            data = c.getInt(c.getColumnIndex("id"));



        }
        if(c!= null)
            c.close();
        if(data>id_l)
            idu=data+1;
        else
            idu=id_l+1;

        ContentValues cvvv = new ContentValues();
        cvvv.put("id_livraison", idu);
        ourDatabase.update("produitv", cvvv,"id_livraison="+id_l+" and id_livraison in (select id from livraison where user='oui')",null );

        ContentValues cvv = new ContentValues();
        cvv.put("id", idu);
        ourDatabase.update("livraison", cvv,"id="+id_l+" and user='oui'",null );



            ContentValues cv = new ContentValues();
            cv.put("id", id_l);
            cv.put("id_livreur", id);
            cv.put("bon", bon);
        cv.put("id_pvente", idpv);

        cv.put("pvente", pvente);
            cv.put("heure",heure);
            cv.put("faite",faite);
            cv.put("motif",motif);
            cv.put("date",date);
        cv.put("user",user);
        cv.put("reste",reste);
        cv.put("payement",payement);
        cv.put("photo",photo);

        ourDatabase.insert("livraison", null, cv);

return id_l;

    }
    public void delete(int id){
        ourDatabase.delete("produitv","id_livraison=?",new String [] {String.valueOf(id)});

    }

    public void deletep(String id,int idl){
        ourDatabase.delete("produitv","produit=? and id_livraison=?",new String [] {String.valueOf(id),String.valueOf(idl)});

    }

    public void insertpventelong(String branche,int id,String nom, String num,String adresse,String wilaya,String type,String zone,String credit,String id_livreur,String longg,String lat) {
        // TODO Auto-generated method stub
        ContentValues cvv = new ContentValues();
        cvv.put("pvente", nom);
        ourDatabase.update("livraison", cvv,"id_pvente="+id,null );
        ourDatabase.delete("pvente","id=?",new String [] {String.valueOf(id)});
        insertveri(1);

        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("branche", branche);
        cv.put("num", num);
        cv.put("nom", nom);
        cv.put("adresse",adresse);
        cv.put("zone",zone);
        cv.put("credit",credit);
        cv.put("wilaya",wilaya);
        cv.put("type",type);
        cv.put("longg",longg);
        cv.put("lat",lat);
        if(Integer.parseInt(id_livreur)!=0)
            cv.put("id_livreur",id_livreur);



     ourDatabase.insertWithOnConflict("pvente", null, cv, SQLiteDatabase.CONFLICT_IGNORE);



    }

    public void insertpvente(String branche,int id,String nom, String num,String adresse,String wilaya,String type,String zone,String credit,String id_livreur) {
        // TODO Auto-generated method stub
        ContentValues cvv = new ContentValues();
        cvv.put("pvente", nom);
        ourDatabase.update("livraison", cvv,"id_pvente="+id,null );
        ourDatabase.delete("pvente","id=?",new String [] {String.valueOf(id)});
        insertveri(1);

        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("branche", branche);
        cv.put("num", num);
        cv.put("nom", nom);
        cv.put("adresse",adresse);
        cv.put("zone",zone);
        cv.put("credit",credit);
        cv.put("wilaya",wilaya);
        cv.put("type",type);
        if(Integer.parseInt(id_livreur)!=0)
        cv.put("id_livreur",id_livreur);



        ourDatabase.insert("pvente", null, cv);



    }


    public void insertpromotion(int id,String nom, String desc,String date_d,String date_fin,String type) {
        // TODO Auto-generated method stub


        ContentValues cv = new ContentValues();
        cv.put("id_promotion", id);
        cv.put("nom_promotion", nom);
        cv.put("dsc_promotion",desc);
        cv.put("date_debut",date_d);
        cv.put("date_fin",date_fin);
        cv.put("type_promotion",type);


        ourDatabase.insert("promotion", null, cv);



    }


    public void insertpromotionproduit(int id,String prix, int id_promotion,int id_produit) {
        // TODO Auto-generated method stub


        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("id_promotion", id_promotion);
        cv.put("id_produit",id_produit);
        cv.put("prix_reduction",prix);


        ourDatabase.insert("promotion_produit", null, cv);



    }
    public void insertpventeu(String branche,int id,String nom, String num,String adresse,String wilaya,String type,String zone,String credit,String id_livreur) {
        // TODO Auto-generated method stub
        ContentValues cvv = new ContentValues();
        cvv.put("pvente", nom);
        ourDatabase.update("livraison", cvv,"id_pvente="+id,null );
        ourDatabase.delete("pvente","id=?",new String [] {String.valueOf(id)});
        insertveri(1);

        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("branche", branche);
        cv.put("num", num);
        cv.put("nom", nom);
        cv.put("adresse",adresse);
        cv.put("zone",zone);
        cv.put("credit",credit);
        cv.put("wilaya",wilaya);
        cv.put("type",type);
        cv.put("modifier","oui");
        if(Integer.parseInt(id_livreur)!=0)
            cv.put("id_livreur",id_livreur);



        ourDatabase.insert("pvente", null, cv);



    }
    public void insertpventeelong(String branche,int id,String nom, String num,String adresse,String wilaya,String type,String zone,String credit,String id_livreur,String longg,String lat) {
        // TODO Auto-generated method stub
        ContentValues cvv = new ContentValues();
        cvv.put("pvente", nom);
        ourDatabase.update("livraison", cvv,"id_pvente="+id,null );
        ourDatabase.delete("pvente","id=?",new String [] {String.valueOf(id)});

        insertveri(1);

        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("branche", branche);
        cv.put("num", num);
        cv.put("nom", nom);
        cv.put("adresse",adresse);
        cv.put("zone",zone);
        cv.put("credit",credit);
        cv.put("wilaya",wilaya);
        cv.put("type",type);
        cv.put("longg",longg);
        cv.put("lat",lat);
        if(Integer.parseInt(id_livreur)!=0)
            cv.put("id_livreur",id_livreur);
        cv.put("modifier","oui");



        ourDatabase.insert("pvente", null, cv);



    }

    public void insertpventee(String branche,int id,String nom, String num,String adresse,String wilaya,String type,String zone,String credit,String id_livreur) {
        // TODO Auto-generated method stub
        ContentValues cvv = new ContentValues();
        cvv.put("pvente", nom);
        ourDatabase.update("livraison", cvv,"id_pvente="+id,null );
        ourDatabase.delete("pvente","id=?",new String [] {String.valueOf(id)});

        insertveri(1);

        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("branche", branche);
        cv.put("num", num);
        cv.put("nom", nom);
        cv.put("adresse",adresse);
        cv.put("zone",zone);
        cv.put("credit",credit);
        cv.put("wilaya",wilaya);
        cv.put("type",type);
        if(Integer.parseInt(id_livreur)!=0)
            cv.put("id_livreur",id_livreur);
        cv.put("modifier","oui");



        ourDatabase.insert("pvente", null, cv);



    }
    public void insertpventeee(String branche,int id,String nom, String num,String adresse,String wilaya,String type,String zone,String credit,String id_livreur) {
        // TODO Auto-generated method stub
        String modifier="si";
        Cursor c =ourDatabase.rawQuery("select modifier from pvente where id=?",new String [] {String.valueOf(id)});
        while (c.moveToNext()){
 if(c.getString(c.getColumnIndex("modifier"))!=null) {
if(c.getString(c.getColumnIndex("modifier")).equals("oui"))
            modifier = c.getString(c.getColumnIndex("modifier"));}

        }
        if(c!= null)
            c.close();
        insertveri(1);

        ContentValues cvv = new ContentValues();
        cvv.put("pvente", nom);
        ourDatabase.update("livraison", cvv,"id_pvente="+id,null );
        ourDatabase.delete("pvente","id=?",new String [] {String.valueOf(id)});

        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("branche", branche);
        cv.put("num", num);
        cv.put("nom", nom);
        cv.put("adresse",adresse);
        cv.put("zone",zone);
        cv.put("credit",credit);
        cv.put("wilaya",wilaya);
        cv.put("type",type);
        if(Integer.parseInt(id_livreur)!=0)
            cv.put("id_livreur",id_livreur);
        cv.put("modifier",modifier);



        ourDatabase.insert("pvente", null, cv);



    }
    public void insertpventeeelong(String branche,int id,String nom, String num,String adresse,String wilaya,String type,String zone,String credit,String id_livreur,String longg,String lat) {
        // TODO Auto-generated method stub
        String modifier="si";
        Cursor c =ourDatabase.rawQuery("select modifier from pvente where id=?",new String [] {String.valueOf(id)});
        while (c.moveToNext()){
            if(c.getString(c.getColumnIndex("modifier"))!=null) {
                if(c.getString(c.getColumnIndex("modifier")).equals("oui"))
                    modifier = c.getString(c.getColumnIndex("modifier"));}

        }
        if(c!= null)
            c.close();
        insertveri(1);

        ContentValues cvv = new ContentValues();
        cvv.put("pvente", nom);
        ourDatabase.update("livraison", cvv,"id_pvente="+id,null );
        ourDatabase.delete("pvente","id=?",new String [] {String.valueOf(id)});

        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("branche", branche);
        cv.put("num", num);
        cv.put("nom", nom);
        cv.put("adresse",adresse);
        cv.put("zone",zone);
        cv.put("credit",credit);
        cv.put("wilaya",wilaya);
        cv.put("type",type);
        cv.put("longg",longg);
        cv.put("lat",lat);


        if(Integer.parseInt(id_livreur)!=0)
            cv.put("id_livreur",id_livreur);
        cv.put("modifier",modifier);


        ourDatabase.insert("pvente", null, cv);



    }

    public void supprimer(int id){
        ourDatabase.delete("produitv","id_livraison=?",new String [] {String.valueOf(id)});

        ourDatabase.delete("livraison","id=?",new String [] {String.valueOf(id)});


    }
    public void supprimerpvente(int id){
        ourDatabase.delete("pvente","id=?",new String [] {String.valueOf(id)});



    }
    public void Insertproduitv(int id,int id_l,int id_livreur, String produit,String quantite, String quantite_u,String montant,String endomage,String QV,String QVU,String date) {
        // TODO Auto-generated method stub
       ourDatabase.delete("produitv","id_livraison=? and produit=?",new String [] {String.valueOf(id_livreur),produit});


        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("id_livraison", id_livreur);

        cv.put("date", date);

        cv.put("id_livreur", id_l);
        cv.put("produit", produit);
        cv.put("quantite", quantite);
        cv.put("quantite_u", quantite_u);
        cv.put("montant_bon", montant);
        cv.put("endomage", endomage);

        cv.put("quantite_v", QV);
        cv.put("quantite_u_v", QVU);
        ourDatabase.insert("produitv", null, cv);



    }

    public void Insertproduitvv(int id,int id_l,int id_livreur,int id_pvente, String produit,String quantite, String quantite_u,String montant,String payement,String endomage,String QV,String QVU,String reste) {
        // TODO Auto-generated method stub

        String  date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Cursor c =ourDatabase.rawQuery("select id from livraison where id=? and id is not null order by id desc limit 1",new String [] {String.valueOf(id_livreur)});
while (c.moveToNext()) {

    ContentValues cv = new ContentValues();
    cv.put("id", id);
    cv.put("id_livraison", id_livreur);
    cv.put("date", date);
    cv.put("id_pvente", id_pvente);

    cv.put("id_livreur", id_l);
    cv.put("produit", produit);
    cv.put("quantite", quantite);
    cv.put("quantite_u", quantite_u);
    cv.put("montant_bon", montant);
    cv.put("endomage", endomage);

    cv.put("quantite_v", QV);
    cv.put("quantite_u_v", QVU);

    ourDatabase.insert("produitv", null, cv);


}
    }


    public void Insertproduit(int id, String produit, String prix_u,String prix_f,String quantite_f,String codebar,String fardeau,String quantite_u,String prix_uu) {
        // TODO Auto-generated method stub

        ourDatabase.delete("produit","id=?",new String [] {String.valueOf(id)});


        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("nom", produit);
        cv.put("quantite_f", quantite_f);
        cv.put("prix_u", prix_u);
        cv.put("prix_uu", prix_uu);

        cv.put("prix_f", prix_f);
        cv.put("code_bar", codebar);
        cv.put("fardeaup", fardeau);
        cv.put("quantite_u", quantite_u);


        ourDatabase.insert("produit", null, cv);



    }
    public void Insertpro(int id, String nom, String prix_u,String prix_f) {
        // TODO Auto-generated method stub

        ourDatabase.delete("p","id=?",new String [] {String.valueOf(id)});


        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("nom", nom);
        cv.put("prix_u", prix_u);
        cv.put("prix_f", prix_f);



        ourDatabase.insert("p", null, cv);



    }

    public void synchronisatioLocation(){
        open();
        String  datte=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Cursor c =ourDatabase.rawQuery("select * from location where date=? group by longg,lat",new String [] {String.valueOf(datte)});
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));
            String longg = c.getString(c.getColumnIndex("longg"));

            String lat = c.getString(c.getColumnIndex("lat"));
            String date = c.getString(c.getColumnIndex("date"));
            String heure = c.getString(c.getColumnIndex("heure"));

            AndroidNetworking.post(DATA_INSERT_URL)

                    .addBodyParameter("action","locationA")
                    .addBodyParameter("lat", String.valueOf(lat))
                    .addBodyParameter("long", String.valueOf(longg))
                    .addBodyParameter("id", id+"")
                    .addBodyParameter("heure",heure)
                    .addBodyParameter("date",date)
                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if(response != null)
                                try {

                                    //SHOW RESPONSE FROM SERVER

                                    String responseString= response.get(0).toString();




                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Main.dismiss();
                                    Toast.makeText(ourContext, "Synch non reussie! Veuillez réessayer", Toast.LENGTH_SHORT).show();

                                }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Log.e( "BDD","Erreur");
                            Main.dismiss();
                            Toast.makeText(ourContext, "Synch non reussie! Veuillez réessayer", Toast.LENGTH_SHORT).show();


                        }


                    });



        }

        close();


    }

    public void synchronisatioLiv(){
        int id=0;
        open();
        Cursor c =ourDatabase.rawQuery("select id from login",null);
        while (c.moveToNext()){

            id  = c.getInt(c.getColumnIndex("id"));

        }

        close();

        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","supprimer synch")
                .addBodyParameter("id", id+"")
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null) {

                            //SHOW RESPONSE FROM SERVER

                            try {
                                String responseString= response.get(0).toString();



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            synchronisationpvente();

                        }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        Log.e( "BDD","2222222"+anError);
                        Main.dismiss();
                        Toast.makeText(ourContext, "Synch non reussie! Veuillez réessayer", Toast.LENGTH_SHORT).show();


                    }


                });


    }
    public void synchliv(){
        open();
        String  datte=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Cursor c =ourDatabase.rawQuery("select * from livraison where date=? and id_pvente not in (select id from pvente where modifier='oui' or modifier='si' )",new String [] {String.valueOf(datte)});
        final int count =c.getCount();
        final int i=0;
       if(count==0){
           x++;
           synchronisationproduitv();

       }

        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));
            int id_livreur = c.getInt(c.getColumnIndex("id_livreur"));

            String date_m="",temps_p="",heure_a="",heure="",photo="",idpv="",date="",reste="",payement="";
            String faite= c.getString(c.getColumnIndex("faite"));
            if(c.getString(c.getColumnIndex("date_m"))!=null)
                date_m = c.getString(c.getColumnIndex("date_m"));
            if(c.getString(c.getColumnIndex("date"))!=null)
                date = c.getString(c.getColumnIndex("date"));
            if(c.getString(c.getColumnIndex("payement"))!=null)
                payement = c.getString(c.getColumnIndex("payement"));
            if(c.getString(c.getColumnIndex("reste"))!=null)
                reste = c.getString(c.getColumnIndex("reste"));
            String motif = c.getString(c.getColumnIndex("motif"));
            if(c.getString(c.getColumnIndex("photo"))!=null)
                photo = c.getString(c.getColumnIndex("photo"));
            String user = c.getString(c.getColumnIndex("user"));
            String bon = c.getString(c.getColumnIndex("bon"));
            if(c.getString(c.getColumnIndex("id_pvente"))!=null)
                idpv = c.getString(c.getColumnIndex("id_pvente"));
            String pvente = c.getString(c.getColumnIndex("pvente"));
            if(c.getString(c.getColumnIndex("temps_p"))!=null)
                temps_p= c.getString(c.getColumnIndex("temps_p"));
            String valide = c.getString(c.getColumnIndex("valide"));
            if(c.getString(c.getColumnIndex("heure_a"))!=null)
                heure_a= c.getString(c.getColumnIndex("heure_a"));
            if(c.getString(c.getColumnIndex("heure"))!=null)
                heure= c.getString(c.getColumnIndex("heure"));

            cpt++;

            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action","update livraisonS")
                    .addBodyParameter("id", id+"")
                    .addBodyParameter("id_livreur", id_livreur+"")
                    .addBodyParameter("id_pvente", idpv+"")
                    .addBodyParameter("pvente", pvente+"")
                    .addBodyParameter("valide", valide+"")
                    .addBodyParameter("user", String.valueOf(user))
                    .addBodyParameter("faite",faite)
                    .addBodyParameter("photo",photo)
                    .addBodyParameter("temps_p",temps_p)
                    .addBodyParameter("heure",heure)
                    .addBodyParameter("heure_a",heure_a)
                    .addBodyParameter("bon",bon)
                    .addBodyParameter("motif",motif)
                    .addBodyParameter("date_m",date_m)
                    .addBodyParameter("date",date)
                    .addBodyParameter("payement",payement)
                    .addBodyParameter("reste",reste)
                    .addBodyParameter("cpt",cpt+"")



                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if(response != null) {

                                //SHOW RESPONSE FROM SERVER

                                try {
                                    String responseString= response.get(0).toString();


                                    if(responseString.contains("Unsuccessfull")){
                                        x=0;
                                        Main.dismiss();
                                        Toast.makeText(ourContext, "Synch non reussie! Veuillez réessayer", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        if(k==count-1){
                                            synchronisationproduitv();
                                            k=0;
                                        }

                                    }
                                    k++;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Log.e( "BDD","1111111111"+anError);
                            Main.dismiss();
                            Toast.makeText(ourContext, "Synch non reussie! Veuillez réessayer", Toast.LENGTH_SHORT).show();

                        }


                    });



        }

        close();
    }

    public void synchronisationproduitv(){
open();
        String  datte=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Cursor c =ourDatabase.rawQuery("select * from produitv where id_livraison in (select id from livraison where date=? and id_pvente not in (select id from pvente where modifier='oui' or modifier='si' ))",new String [] {String.valueOf(datte)});
countliv=c.getCount();
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));
            int id_livreur = c.getInt(c.getColumnIndex("id_livreur"));
            int id_livraison = c.getInt(c.getColumnIndex("id_livraison"));

            String quantite="",quantite_u="",quantite_v="",quantite_u_v="",reste="",produit="",endomage="",payement="",date="",idpv="",montant="";
            if(c.getString(c.getColumnIndex("quantite"))!=null)
                quantite= c.getString(c.getColumnIndex("quantite"));
            if(c.getString(c.getColumnIndex("quantite_u"))!=null)
                quantite_u= c.getString(c.getColumnIndex("quantite_u"));
            if(c.getString(c.getColumnIndex("quantite_v"))!=null)
                quantite_v= c.getString(c.getColumnIndex("quantite_v"));
            if(c.getString(c.getColumnIndex("quantite_u_v"))!=null)
                quantite_u_v= c.getString(c.getColumnIndex("quantite_u_v"));
            if(c.getString(c.getColumnIndex("endomage"))!=null)
                endomage= c.getString(c.getColumnIndex("endomage"));
            if(c.getString(c.getColumnIndex("id_pvente"))!=null)
                idpv = c.getString(c.getColumnIndex("id_pvente"));
            if(c.getString(c.getColumnIndex("date"))!=null)
                date= c.getString(c.getColumnIndex("date"));
            if(c.getString(c.getColumnIndex("produit"))!=null)
                produit= c.getString(c.getColumnIndex("produit"));
            if(c.getString(c.getColumnIndex("montant_bon"))!=null)
                montant= c.getString(c.getColumnIndex("montant_bon"));

            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action","update produitS")
                    .addBodyParameter("id", id+"")
                    .addBodyParameter("id_livreur", id_livreur+"")
                    .addBodyParameter("id_pvente", idpv+"")
                    .addBodyParameter("quantite", quantite+"")
                    .addBodyParameter("quantite_u", quantite_u+"")
                    .addBodyParameter("quantite_v", quantite_v)
                    .addBodyParameter("quantite_u_v",quantite_u_v)
                    .addBodyParameter("endomage",endomage)
                    .addBodyParameter("montant",montant)
                    .addBodyParameter("produit",produit)
                    .addBodyParameter("date",date)
                    .addBodyParameter("id_livraison",id_livraison+"")
                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if(response != null) {

                                //SHOW RESPONSE FROM SERVER

                                try {
                                    String responseString= response.get(0).toString();
                                    cptliv++;


                                    if(responseString.contains("Unsuccessfull")){
                                        x=0;
                                        cptliv=0;
                                        Main.dismiss();
                                        Toast.makeText(ourContext, "Synch non reussie! Veuillez réessayer", Toast.LENGTH_SHORT).show();

                                    }
                                    else{


                                    if(cptliv==countliv){
                                        x++;
                                        synchronisationstockm();
                                    }}

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Log.e( "BDD","2222222"+anError);
                            Main.dismiss();
                            Toast.makeText(ourContext, "Synch non reussie! Veuillez réessayer", Toast.LENGTH_SHORT).show();
                        }


                    });



        }

        if(c.getCount()==0){
x++;
            synchronisationstockm();
        }

close();
    }
    public void synchronisationpvente(){
open();

        Cursor c =ourDatabase.rawQuery("select * from pvente where modifier=? or modifier=? order by id desc",new String [] {String.valueOf("oui"),"si"});
       // Cursor c =ourDatabase.rawQuery("select * from pvente order by id desc limit 10",null);

        countp=c.getCount();

        if(countp==0){

            synchliv();
        }

        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));


            String branche="",zone="",num="",nom="",adresse="",wilaya="",commune="",id_livreur="",modifier="",longg="",lat="";
            if(c.getString(c.getColumnIndex("branche"))!=null)
                branche= c.getString(c.getColumnIndex("branche"));
            if(c.getString(c.getColumnIndex("zone"))!=null)
                zone= c.getString(c.getColumnIndex("zone"));
            if(c.getString(c.getColumnIndex("num"))!=null)
                num= c.getString(c.getColumnIndex("num"));
            if(c.getString(c.getColumnIndex("nom"))!=null)
                nom= c.getString(c.getColumnIndex("nom"));
            if(c.getString(c.getColumnIndex("adresse"))!=null)
                adresse= c.getString(c.getColumnIndex("adresse"));
            if(c.getString(c.getColumnIndex("wilaya"))!=null)
                wilaya= c.getString(c.getColumnIndex("wilaya"));
            if(c.getString(c.getColumnIndex("type"))!=null)
                commune= c.getString(c.getColumnIndex("type"));

            if(c.getString(c.getColumnIndex("id_livreur"))!=null)
                id_livreur= c.getString(c.getColumnIndex("id_livreur"));
            if(c.getString(c.getColumnIndex("modifier"))!=null)
                modifier= c.getString(c.getColumnIndex("modifier"));
            if(c.getString(c.getColumnIndex("longg"))!=null)
                longg= c.getString(c.getColumnIndex("longg"));
            if(c.getString(c.getColumnIndex("lat"))!=null)
                lat= c.getString(c.getColumnIndex("lat"));

            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action","mod pventee")
                    .addBodyParameter("id", id+"")
                    .addBodyParameter("branche", branche+"")
                    .addBodyParameter("zone", zone+"")
                    .addBodyParameter("nom", nom+"")
                    .addBodyParameter("lieu",adresse+"")
                    .addBodyParameter("wilaya",wilaya+"")
                    .addBodyParameter("type",commune+"")
                    .addBodyParameter("num",num+"")
                    .addBodyParameter("id_livreur",id_livreur+"")
                    .addBodyParameter("modifier",modifier+"")
                    .addBodyParameter("count",countp+"")
                    .addBodyParameter("longg",longg+"")
                    .addBodyParameter("lat",lat+"")


                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if(response != null) {

                                //SHOW RESPONSE FROM SERVER

                                try {
                                    String responseString= response.get(0).toString();
                                   cptp++;
                                    if(responseString.contains("SUS")){
                                        String oldid= response.get(1).toString();
                                        String newid= response.get(2).toString();
                                        ContentValues cve = new ContentValues();
                                        cve.put("id", newid);
                                        cve.put("modifier", "non");

                                        open();
                                        ourDatabase.update("pvente", cve,"id="+oldid,null );

                                        ContentValues cv = new ContentValues();
                                        cv.put("id_pvente", newid);
                                        ourDatabase.update("produitv", cv,"id_pvente="+oldid,null );

                                        ContentValues cvv = new ContentValues();
                                        cvv.put("id_pvente", newid);
                                        ourDatabase.update("livraison", cvv,"id_pvente="+oldid,null );
close();

                                    }

                                    if(responseString.contains("Unsuccessfull")){

                                        Toast.makeText(ourContext, "Un client existe déjà avec le meme numéro", Toast.LENGTH_SHORT).show();

                                    }




                                    if(cptp==countp){

                                        synchliv();

                                    }
                                } catch (JSONException e) {
                                    Main.dismiss();
                                    Toast.makeText(ourContext, "Synch non reussie! Veuillez réessayer", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }


                            }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Main.dismiss();
                            Toast.makeText(ourContext, "Synch non reussie! Veuillez réessayer", Toast.LENGTH_SHORT).show();
                        }


                    });



        }

        close();


    }
    public void synchronisationpventee(){
        open();

        Cursor c =ourDatabase.rawQuery("select * from pvente where modifier=? or modifier=? order by id desc",new String [] {String.valueOf("oui"),"si"});
        // Cursor c =ourDatabase.rawQuery("select * from pvente order by id desc limit 10",null);

        countp=c.getCount();



        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));


            String branche="",zone="",num="",nom="",adresse="",wilaya="",commune="",id_livreur="",modifier="",longg="",lat="";
            if(c.getString(c.getColumnIndex("branche"))!=null)
                branche= c.getString(c.getColumnIndex("branche"));
            if(c.getString(c.getColumnIndex("zone"))!=null)
                zone= c.getString(c.getColumnIndex("zone"));
            if(c.getString(c.getColumnIndex("num"))!=null)
                num= c.getString(c.getColumnIndex("num"));
            if(c.getString(c.getColumnIndex("nom"))!=null)
                nom= c.getString(c.getColumnIndex("nom"));
            if(c.getString(c.getColumnIndex("adresse"))!=null)
                adresse= c.getString(c.getColumnIndex("adresse"));
            if(c.getString(c.getColumnIndex("wilaya"))!=null)
                wilaya= c.getString(c.getColumnIndex("wilaya"));
            if(c.getString(c.getColumnIndex("type"))!=null)
                commune= c.getString(c.getColumnIndex("type"));

            if(c.getString(c.getColumnIndex("id_livreur"))!=null)
                id_livreur= c.getString(c.getColumnIndex("id_livreur"));
            if(c.getString(c.getColumnIndex("modifier"))!=null)
                modifier= c.getString(c.getColumnIndex("modifier"));
            if(c.getString(c.getColumnIndex("longg"))!=null)
                longg= c.getString(c.getColumnIndex("longg"));
            if(c.getString(c.getColumnIndex("lat"))!=null)
                lat= c.getString(c.getColumnIndex("lat"));

            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action","mod pventee")
                    .addBodyParameter("id", id+"")
                    .addBodyParameter("branche", branche+"")
                    .addBodyParameter("zone", zone+"")
                    .addBodyParameter("nom", nom+"")
                    .addBodyParameter("lieu",adresse+"")
                    .addBodyParameter("wilaya",wilaya+"")
                    .addBodyParameter("type",commune+"")
                    .addBodyParameter("num",num+"")
                    .addBodyParameter("id_livreur",id_livreur+"")
                    .addBodyParameter("modifier",modifier+"")
                    .addBodyParameter("count",countp+"")
                    .addBodyParameter("longg",longg+"")
                    .addBodyParameter("lat",lat+"")

                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if(response != null) {

                                //SHOW RESPONSE FROM SERVER

                                try {
                                    String responseString= response.get(0).toString();
                                    cptp++;
                                    if(responseString.contains("SUS")){
                                        String oldid= response.get(1).toString();
                                        String newid= response.get(2).toString();
                                        ContentValues cve = new ContentValues();
                                        cve.put("id", newid);
                                        cve.put("modifier", "non");

                                        open();
                                        ourDatabase.update("pvente", cve,"id="+oldid,null );

                                        ContentValues cv = new ContentValues();
                                        cv.put("id_pvente", newid);
                                        ourDatabase.update("produitv", cv,"id_pvente="+oldid,null );

                                        ContentValues cvv = new ContentValues();
                                        cvv.put("id_pvente", newid);
                                        ourDatabase.update("livraison", cvv,"id_pvente="+oldid,null );
                                        close();

                                    }

                                    if(responseString.contains("Unsuccessfull")){
                                        cptp=0;
                                        x=0;
                                        Main.dismiss();

                                    }
                                    else{



                                        if(cptp==countp){



                                        }}
                                } catch (JSONException e) {
                                    Main.dismiss();
                                    e.printStackTrace();
                                }


                            }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Log.e( "BDD","33333333"+anError);
                            Main.dismiss();
                        }


                    });



        }

        close();


    }
    public void synchronisationstockm(){


open();
        Cursor c =ourDatabase.rawQuery("select * from stockm",null);
        countstock=c.getCount();
        if(countstock==0)
            x++;
        int qu=0,qf=0,f=1;
        while(c.moveToNext()){
            if(c.getString(c.getColumnIndex("quantite_u"))!=null)

            qu = Integer.parseInt(c.getString(c.getColumnIndex("quantite_u")));
            if(c.getString(c.getColumnIndex("quantite_f"))!=null)

                qf = Integer.parseInt(c.getString(c.getColumnIndex("quantite_f")));
            if(c.getString(c.getColumnIndex("fardeau"))!=null)

            f = Integer.parseInt(c.getString(c.getColumnIndex("fardeau")));

            qu=qf*f+qu;


            int id = c.getInt(c.getColumnIndex("id"));

            String produit="";
            if(c.getString(c.getColumnIndex("produit"))!=null)
                produit= c.getString(c.getColumnIndex("produit"));





            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action","update stockmm")
                    .addBodyParameter("id", id+"")
                    .addBodyParameter("quantite", qu+"")
                    .addBodyParameter("produit", produit+"")
                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if(response != null) {

                                //SHOW RESPONSE FROM SERVER

                                try {
                                    String responseString= response.get(0).toString();

                                    cptstock++;
                                    if(responseString.contains("Unsuccessfull")){
                                        x=0;
                                        Main.dismiss();
                                        Toast.makeText(ourContext, "Synch non reussie! Veuillez réessayer", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                    if(cptstock==countstock){
                                    x++;
                                    if(x>=2){
                                        Main.dismiss();
                                        Toast.makeText(ourContext, "Synch reussie", Toast.LENGTH_SHORT).show();
                                     open();
                                        insertveri(0);
                                     close();

                                    }}}
                                } catch (JSONException e) {
                                    Main.dismiss();
                                    Toast.makeText(ourContext, "Synch non reussie! Veuillez réessayer", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }


                            }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Log.e( "BDD","44444444"+anError);
                            Main.dismiss();
                            Toast.makeText(ourContext, "Synch non reussie! Veuillez réessayer", Toast.LENGTH_SHORT).show();

                        }


                    });



        }

        if(c.getCount()==0){

            if(x>=2){
                Main.dismiss();
                Toast.makeText(ourContext, "Synch reussie", Toast.LENGTH_SHORT).show();
                open();
                insertveri(0);
                close();


            }
        }
        close();


    }
    public int getID(){

        Cursor c =ourDatabase.rawQuery("select id from login",null);
while (c.moveToNext()){

    int data = c.getInt(c.getColumnIndex("id"));
    return data;
}
        if(c!= null)
            c.close();

return 0;

    }

    public int getidpvente(int id){

        Cursor c =ourDatabase.rawQuery("select id_pvente from livraison where id=?",new String [] {String.valueOf(id)});
        while (c.moveToNext()){

            int data = c.getInt(c.getColumnIndex("id_pvente"));
            return data;
        }
        if(c!= null)
            c.close();
        return 0;

    }

    public String getmodifier(int id){

        Cursor c =ourDatabase.rawQuery("select modifier from pvente where id=?",new String [] {String.valueOf(id)});
        while (c.moveToNext()){

            String data = c.getString(c.getColumnIndex("modifier"));
            return data;
        }
        if(c!= null)
            c.close();
        return "";

    }


    public String getuser(){

        Cursor c =ourDatabase.rawQuery("select user from login where connecte='oui'",null);
        while (c.moveToNext()){

            String data = c.getString(c.getColumnIndex("user"));

            return data;
        }
        if(c!= null)
            c.close();
        return "";

    }

    public String getpassword(){
        Cursor c =ourDatabase.rawQuery("select password from login  where connecte='oui'",null);
        while (c.moveToNext()){

            String data = String.valueOf(c.getInt(c.getColumnIndex("password")));


            return data;
        }
        if(c!= null)
            c.close();
        return "";

    }


    public String gettype(int id){
        Cursor c =ourDatabase.rawQuery("select type from pvente  where id=?",new String [] {String.valueOf(id)});
        while (c.moveToNext()){

            String data = String.valueOf(c.getString(c.getColumnIndex("type")));


            return data;
        }
        if(c!= null)
            c.close();
        return "";

    }
    public String getcommune(String id){
        String data="";
        Cursor c =ourDatabase.rawQuery("select zone from pvente  where id=?",new String [] {String.valueOf(id)});
        while (c.moveToNext()){

           data = String.valueOf(c.getString(c.getColumnIndex("zone")));



        }
        if(c!= null)
            c.close();

        return data;

    }

    public boolean getmodifier(String id){
        String data="";
        Cursor c =ourDatabase.rawQuery("select modifier from pvente  where id=?",new String [] {String.valueOf(id)});
        while (c.moveToNext()){

            data = String.valueOf(c.getString(c.getColumnIndex("modifier")));
            if(data.equals("oui"))
                return true;


        }
        if(c!= null)
            c.close();

        return false;

    }

    public boolean getfaite(String id){
        String data="";
        Cursor c =ourDatabase.rawQuery("select faite from livraison  where id=?",new String [] {String.valueOf(id)});
        while (c.moveToNext()){

            data = String.valueOf(c.getString(c.getColumnIndex("faite")));
            if(data.equals("oui"))
                return true;


        }
        if(c!= null)
            c.close();

        return false;

    }
    public String getseuil(){
        Cursor c =ourDatabase.rawQuery("select seuil from seuil ",null);
        while (c.moveToNext()){

            String data = String.valueOf(c.getInt(c.getColumnIndex("seuil")));


            return data;
        }
        if(c!= null)
            c.close();

        return "";

    }


    public double getpromotion(int id,String type) {

        String datte = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        if (type.equals("Grossiste")){
            Cursor c = ourDatabase.rawQuery("SELECT prix_reduction as promotion,id_produit FROM promotion as pp,promotion_produit as p where date_debut<=?<=date_fin and (type_promotion=0 or type_promotion=1)  and pp.id_promotion=p.id_promotion and p.id_produit=?", new String[]{String.valueOf(datte),String.valueOf(id)});
        while (c.moveToNext()) {

            Double data = c.getDouble(c.getColumnIndex("promotion"));


            return data;
        }
        if (c != null)
            c.close();
    }
        else {
            Cursor c = ourDatabase.rawQuery("SELECT prix_reduction as promotion,id_produit FROM promotion as pp,promotion_produit as p where date_debut<=?<=date_fin and (type_promotion=0 or type_promotion=2)  and pp.id_promotion=p.id_promotion and p.id_produit=?", new String[]{String.valueOf(datte),String.valueOf(id)});
            while (c.moveToNext()) {

                Double data = c.getDouble(c.getColumnIndex("promotion"));


                return data;
            }
            if (c != null)
                c.close();
        }

        return 0;

    }



    public String getdistance(){
        Cursor c =ourDatabase.rawQuery("select distance from distance",null);
        while (c.moveToNext()){

            String data = String.valueOf(c.getInt(c.getColumnIndex("distance")));


            return data;
        }
        if(c!= null)
            c.close();

        return "";

    }
    public int getpvente1(){

        String raw = String.format("SELECT count(*) FROM %s ", "pvente",null);
        Cursor c = ourDatabase.rawQuery(raw, null);
        try {
            return (c.moveToFirst()) ? c.getInt(0) : 0;
        } finally {
            c.close();
        }
    }

    public int getveri(){
        Cursor c =ourDatabase.rawQuery("select veri from synch ",null);
        while (c.moveToNext()){

         int data = c.getInt(c.getColumnIndex("veri"));


            return data;
        }
        if(c!= null)
            c.close();
        return 0;

    }


    public String getb(){
        Cursor c =ourDatabase.rawQuery("select branche from login  where connecte='oui'",null);
        while (c.moveToNext()){

            String data = String.valueOf(c.getString(c.getColumnIndex("branche")));


            return data;
        }
        if(c!= null)
            c.close();
        return "";

    }

    public String getuserR(){

        Cursor c =ourDatabase.rawQuery("select user from login where connecte='non'",null);
        while (c.moveToNext()){

            String data = c.getString(c.getColumnIndex("user"));

            return data;
        }
        if(c!= null)
            c.close();

        return "";

    }

    public String getpasswordD(){
        Cursor c =ourDatabase.rawQuery("select password from login  where connecte='non'",null);
        while (c.moveToNext()){

            String data = String.valueOf(c.getInt(c.getColumnIndex("password")));


            return data;
        }
        if(c!= null)
            c.close();

        return "";

    }
    public Cursor getLiv(int id){
        String  date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Cursor c =ourDatabase.rawQuery("select * from livraison where id_livreur=? and date=? and user='non' and valide='non' and id in (select id_livraison from produitv where id_livraison in (select id from livraison where date=?)) order by date,bon asc",new String [] {String.valueOf(id),String.valueOf(date),date});


        return c;

    }
    public Cursor getuser(int id){


        Cursor c =ourDatabase.rawQuery("select user from livraison where  id=? ",new String [] {String.valueOf(id)});


        return c;

    }

    public Cursor getlongglat(int id){


        Cursor c =ourDatabase.rawQuery("select p.id,p.longg,p.lat,p.nom,p.num from pvente as p,livraison as l where l.id=? and l.id_pvente=p.id ",new String [] {String.valueOf(id)});


        return c;

    }
    public Cursor getLiva(int id){
        String  date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Cursor c =ourDatabase.rawQuery("select * from livraison where id_livreur=? and date=? and user='oui' and valide='non' order by bon desc",new String [] {String.valueOf(id),String.valueOf(date)});


        return c;

    }
    public Cursor getLivv(int id,int idp){
        String  date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Cursor c =ourDatabase.rawQuery("select * from livraison where user='non' and id_livreur=? and id_pvente in (select * from produitv where id_pvente=?  ) and reste>0 and id in (select id_livraison from produitv)  ",new String [] {String.valueOf(id),String.valueOf(idp)});


        return c;

    }
    public Cursor getbonliv(int id){

        Cursor c =ourDatabase.rawQuery("select * from livraison where id_livreur=? and bon is not null and id is not null order by id desc limit 1",new String [] {String.valueOf(id)});


        return c;

    }
    public Cursor getp(int id){

        Cursor c =ourDatabase.rawQuery("select * from produitv where id_livreur=? and id is not null order by id desc limit 1",new String [] {String.valueOf(id)});


        return c;

    }
    public Cursor getLivv(int id){
        String  date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Cursor c =ourDatabase.rawQuery("select * from livraison where id_livreur=? and date=? and user='oui' and valide='non'",new String [] {String.valueOf(id),String.valueOf(date)});


        return c;

    }
    public Cursor getproduitv(int id){
        String  date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Cursor c =ourDatabase.rawQuery("select * from produitv where id_livraison=? order by id asc",new String [] {String.valueOf(id)});


        return c;

    }
    public Cursor getproduit(){

        Cursor c =ourDatabase.rawQuery("select p.id,p.nom,s.quantite_u,s.quantite_f,p.code_bar,p.prix_f,p.prix_uu,p.fardeaup from produit as p,stockm as s where p.nom=s.produit ",null);


        return c;

    }

    public Cursor gettop(){

        Cursor c =ourDatabase.rawQuery("select p.id,p.nom,sum(payement) as somme from pvente as p,livraison as l where p.id=l.id_pvente group by l.id_pvente order by somme desc limit 100 ",null);


        return c;

    }
    public void affiche(){

        Cursor c =ourDatabase.rawQuery("select * from produitv ",null);

        while (c.moveToNext()){
        }
        if(c!= null)
            c.close();


    }
    public Cursor getpr(){

        Cursor c =ourDatabase.rawQuery("select * from p order by id desc limit 30",null);


        return c;

    }
    public Cursor getpvente(){

        Cursor c =ourDatabase.rawQuery("select * from pvente order by id desc limit 0,30 ",null);


        return c;

    }

    public Cursor getpvente(int id){

        Cursor c =ourDatabase.rawQuery("select * from pvente where id="+id,null);


        return c;

    }
    public Cursor getpventes(int index){

        Cursor c =ourDatabase.rawQuery("select * from pvente order by id desc limit "+index+",30 ",null);


        return c;

    }


    public Cursor getpventessearch(String index){
index=index.toLowerCase();
        Cursor c =ourDatabase.rawQuery("select * from pvente where lower(nom) like '%"+index+"%' or lower(num) like '%"+index+"%'  or lower(id) like '%"+index+"%'  order by id desc limit 0,30 ",null);


        return c;

    }



    public Cursor getprindex(int index){

        Cursor c =ourDatabase.rawQuery("select * from p order by id desc limit "+index+",30 ",null);


        return c;

    }


    public Cursor getprsearch(String index){
        index=index.toLowerCase();
        Cursor c =ourDatabase.rawQuery("select * from p where lower(nom) like '%"+index+"%'  order by id desc limit 0,30 ",null);


        return c;

    }



    public Cursor getpventeMod(){

        Cursor c =ourDatabase.rawQuery("select * from pvente where modifier='oui' ",null);


        return c;

    }
    public Cursor getpventee(){

        Cursor c =ourDatabase.rawQuery("select * from pvente order by id desc limit 1 ",null);


        return c;

    }

    public Cursor getmag(int id){
        String  date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Cursor c =ourDatabase.rawQuery("select * from stockm where id="+id+" and (quantite_f>0 OR quantite_u>0)",null);


        return c;

    }
    public Cursor getmagg(int id){
        String  date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Cursor c =ourDatabase.rawQuery("select produit,quantite_f*fardeau+quantite_u as quantite from stockm where id="+id,null);


        return c;

    }


    public void updateLivA(String user,String bon,String idp,int id_l,String quantite_v,String quantite_u_v,String payement,String endomage,String reste,String produit,int id_livreur,int u,String montant){


        insertveri(1);


        ContentValues cv = new ContentValues();
        String  date=new SimpleDateFormat("yyyy-MM-dd_HH:mm").format(new Date());
        cv.put("motif", "");
        cv.put("photo", "");
        cv.put("temps_p", "");
        cv.put("valide", "non");
        cv.put("payement", payement);
        cv.put("bon", bon);

        cv.put("reste", reste);

        cv.put("faite", "oui");
        cv.put("date_m", date);
        ourDatabase.update("livraison",  cv,"id="+id_l,null);


        Cursor x =ourDatabase.rawQuery("select * from stockm where  produit=\""+produit+"\"",null);
        int qu=0,qf=0,f=1;
        while(x.moveToNext()){

            qu = Integer.parseInt(x.getString(x.getColumnIndex("quantite_u")));
            qf = Integer.parseInt(x.getString(x.getColumnIndex("quantite_f")));

            f = Integer.parseInt(x.getString(x.getColumnIndex("fardeau")));

            qu=qf*f+qu;

        }
        if(x!= null)
            x.close();
        Cursor c =ourDatabase.rawQuery("select * from produitv where id="+idp,null);
        int uu=0,qv=0;
        double p=0,pu=0;
        while(c.moveToNext()){

            int quu = Integer.parseInt(c.getString(c.getColumnIndex("quantite_u_v")));
             qv = Integer.parseInt(c.getString(c.getColumnIndex("quantite_v")));

            uu=(qv*f)+quu;

        }
        if(c!= null)
            c.close();

        ContentValues cc = new ContentValues();
        int R=qu+uu-u;
        int res=(int)((qu/f)+(uu/f)-(u/f));
            cc.put("quantite_u", R-(((int) R/f)*f));
        cc.put("quantite_f", ((int)R/f));



        ourDatabase.update("stockm", cc,"produit=\""+produit+"\"",null );

        ContentValues cvv = new ContentValues();
        cvv.put("quantite_v", quantite_v);
        cvv.put("quantite_u_v", quantite_u_v);
        cvv.put("endomage", endomage);
        cvv.put("montant_bon", montant);

        ourDatabase.update("produitv", cvv,"id="+idp,null );


    }
    public void updateLivB(String user,String idp,int id_l,String quantite_v,String quantite_u_v,String payement,String endomage,String reste,String produit,int id_livreur,int u,String motif){
        insertveri(1);
        ContentValues cv = new ContentValues();
        String  date=new SimpleDateFormat("yyyy-MM-dd_HH:mm").format(new Date());
        cv.put("motif", motif);
        cv.put("valide", "non");
        cv.put("faite", "oui");
        cv.put("date_m", date);
        cv.put("payement", 0);
        cv.put("reste", 0);

        ourDatabase.update("livraison",  cv,"id="+id_l,null);


        Cursor x =ourDatabase.rawQuery("select * from stockm where  produit=\""+produit+"\"",null);
        int qu=0,qf=0,f=1;
        while(x.moveToNext()){

            qu = Integer.parseInt(x.getString(x.getColumnIndex("quantite_u")));
            qf = Integer.parseInt(x.getString(x.getColumnIndex("quantite_f")));

            f = Integer.parseInt(x.getString(x.getColumnIndex("fardeau")));

            qu=qf*f+qu;

        }
        if(x!= null)
            x.close();
        Cursor c =ourDatabase.rawQuery("select * from produitv where id="+idp,null);
        int uu=0,qv=0;
        double p=0,pu=0;
        while(c.moveToNext()){

            int quu = Integer.parseInt(c.getString(c.getColumnIndex("quantite_u_v")));
            qv = Integer.parseInt(c.getString(c.getColumnIndex("quantite_v")));

            uu=(qv*f)+quu;

        }
        if(c!= null)
            c.close();

        ContentValues cc = new ContentValues();
        int R=qu+uu-u;
        int res=(int)((qu/f)+(uu/f)-(u/f));
        cc.put("quantite_u", R-(res*f));
        cc.put("quantite_f", res);



        ourDatabase.update("stockm", cc,"produit=\""+produit+"\"",null );

        ContentValues cvv = new ContentValues();
        cvv.put("quantite_v", quantite_v);
        cvv.put("quantite_u_v", quantite_u_v);
        cvv.put("endomage", endomage);

        ourDatabase.update("produitv", cvv,"id="+idp,null );


    }


    public void updateLivC(String user,String idp,int id_l,String quantite_v,String quantite_u_v,String payement,String endomage,String reste,String produit,int id_livreur,int u,String motif,String photo,String temps){
        insertveri(1);
        ContentValues cv = new ContentValues();
        String  date=new SimpleDateFormat("yyyy-MM-dd_HH:mm").format(new Date());
        cv.put("motif", motif);
        cv.put("photo", photo);
        cv.put("temps_p", temps);
        cv.put("valide", "non");
        cv.put("faite", "oui");
        cv.put("date_m", date);
        cv.put("payement", 0);
        cv.put("reste", 0);

        ourDatabase.update("livraison",  cv,"id="+id_l,null);


        Cursor x =ourDatabase.rawQuery("select * from stockm where  produit=\""+produit+"\"",null);
        int qu=0,qf=0,f=1;
        while(x.moveToNext()){

            qu = Integer.parseInt(x.getString(x.getColumnIndex("quantite_u")));
            qf = Integer.parseInt(x.getString(x.getColumnIndex("quantite_f")));

            f = Integer.parseInt(x.getString(x.getColumnIndex("fardeau")));

            qu=qf*f+qu;

        }
        if(x!= null)
            x.close();
        Cursor c =ourDatabase.rawQuery("select * from produitv where id="+idp,null);
        int uu=0,qv=0;
        double p=0,pu=0;
        while(c.moveToNext()){

            int quu = Integer.parseInt(c.getString(c.getColumnIndex("quantite_u_v")));
            qv = Integer.parseInt(c.getString(c.getColumnIndex("quantite_v")));

            uu=(qv*f)+quu;

        }

        if(c!= null)
            c.close();
        ContentValues cc = new ContentValues();
        int R=qu+uu-u;
        int res=(int)((qu/f)+(uu/f)-(u/f));
        cc.put("quantite_u", R-(res*f));
        cc.put("quantite_f", res);



        ourDatabase.update("stockm", cc,"produit=\""+produit+"\"",null );

        ContentValues cvv = new ContentValues();
        cvv.put("quantite_v", quantite_v);
        cvv.put("quantite_u_v", quantite_u_v);
        cvv.put("endomage", endomage);

        ourDatabase.update("produitv", cvv,"id="+idp,null );


    }



    public void annuler(int id,String idp,int id_livreur,String produit){
        insertveri(1);

        ContentValues cv = new ContentValues();
        String  date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        cv.put("motif", "");
        cv.put("photo", "");
        cv.put("temps_p", "");
        cv.put("valide", "non");
        cv.put("faite", "non");
        cv.put("date_m", "");
        cv.put("reste", "0");
        cv.put("payement", "0");


        ourDatabase.update("livraison",  cv,"id="+id,null);


        Cursor x =ourDatabase.rawQuery("select * from stockm where  produit=\""+produit+"\"",null);
        int qu=0,qf=0,f=1;
        while(x.moveToNext()){

            qu = Integer.parseInt(x.getString(x.getColumnIndex("quantite_u")));
            qf = Integer.parseInt(x.getString(x.getColumnIndex("quantite_f")));

            f = Integer.parseInt(x.getString(x.getColumnIndex("fardeau")));

            qu=qf*f+qu;

        }
        if(x!= null)
            x.close();
        Cursor c =ourDatabase.rawQuery("select * from produitv where id="+idp,null);
        int uu=0,qv=0;
        double p=0,pu=0;
        while(c.moveToNext()){

            int quu = Integer.parseInt(c.getString(c.getColumnIndex("quantite_u_v")));
            qv = Integer.parseInt(c.getString(c.getColumnIndex("quantite_v")));

            uu=(qv*f)+quu;

        }
        if(c!= null)
            c.close();
        ContentValues cc = new ContentValues();
        int R=qu+uu;
        int res=(int)((qu/f)+(uu/f));
        cc.put("quantite_u", R-(res*f));
        cc.put("quantite_f", res);

        ourDatabase.update("stockm", cc,"produit=\""+produit+"\"",null );

        ContentValues cvv = new ContentValues();
        cvv.put("quantite_v", "0");
        cvv.put("quantite_u_v", "0");
        cvv.put("endomage", "0");

        ourDatabase.update("produitv", cvv,"id="+idp,null );


    }
}
package epiphany_soft.wtw.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

import epiphany_soft.wtw.DataBase.DataBaseContract.AgendaContract;
import epiphany_soft.wtw.DataBase.DataBaseContract.HorarioContract;
import epiphany_soft.wtw.Negocio.Horario;

import static epiphany_soft.wtw.DataBase.DataBaseContract.CalificacionContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.CanalContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.CapituloContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.DiaContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.DiaHorarioContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.EmisoraContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.EmiteContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.GeneroContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.PeliculaContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.ProgramaContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.SerieContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.TemporadaContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.UsuarioContract;


/**
 * Created by Camilo on 23/03/2016.
 */
public class DataBaseConnection {
    DataBaseHelper miDBHelper;
    public DataBaseConnection(Context context){
        miDBHelper = new DataBaseHelper(context);
    }

    public Cursor consultarAllGeneros(){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + GeneroContract.COLUMN_NAME_GENERO_ID+","+
                            GeneroContract.COLUMN_NAME_GENERO_NOMBRE+" ";
            query+= "FROM "+ GeneroContract.TABLE_NAME;
            Cursor c=db.rawQuery(query,null);
            return c;
        }
        return null;
    }
    public Cursor consultarPeliculaLikeNombre(String nombre){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + ProgramaContract.COLUMN_NAME_PROGRAMA_ID + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_SINOPSIS + ","+
                            ProgramaContract.COLUMN_NAME_PROGRAMA_ANIO_ESTRENO + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_PAIS_ORIGEN + " ";
            query +=
                    "FROM " + ProgramaContract.TABLE_NAME + " JOIN " +
                            PeliculaContract.TABLE_NAME +
                            " ON " + PeliculaContract.COLUMN_NAME_PELICULA_ID + "="
                            + ProgramaContract.COLUMN_NAME_PROGRAMA_ID + " ";
           query +=
                    "WHERE " + ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + " LIKE \'%"+nombre+"%\'";
            Cursor c = db.rawQuery(query, null);
            return c;
        }
        else return null;
    }
    /**@param nombre es el nombre con el que se realizará la búsqueda de la película
    * @return la pelicula cuyo nombre es nombre*/
    public Cursor consultarPeliculaPorNombre(String nombre){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " +ProgramaContract.TABLE_NAME+"."+ProgramaContract.COLUMN_NAME_PROGRAMA_ID + "," +
                    ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + "," +
                    ProgramaContract.COLUMN_NAME_PROGRAMA_SINOPSIS + ","+
                            GeneroContract.COLUMN_NAME_GENERO_NOMBRE +","+
                    ProgramaContract.COLUMN_NAME_PROGRAMA_ANIO_ESTRENO + "," +
                    ProgramaContract.COLUMN_NAME_PROGRAMA_PAIS_ORIGEN + "," +
                    AgendaContract.COLUMN_NAME_USUARIO_ID+" ";
            query +=
                    "FROM " + ProgramaContract.TABLE_NAME+" NATURAL JOIN "+
                            GeneroContract.TABLE_NAME+ " JOIN " +
                            PeliculaContract.TABLE_NAME +
                            " ON " + PeliculaContract.COLUMN_NAME_PELICULA_ID + "="
                            +ProgramaContract.TABLE_NAME+"."+ProgramaContract.COLUMN_NAME_PROGRAMA_ID +
                            " LEFT OUTER JOIN "+
                            AgendaContract.TABLE_NAME +" ON "+ProgramaContract.TABLE_NAME+"."+
                            ProgramaContract.COLUMN_NAME_PROGRAMA_ID+"="+AgendaContract.TABLE_NAME+"."+
                            AgendaContract.COLUMN_NAME_PROGRAMA_ID+" ";
            query +=
                    "WHERE " + ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE +"=\'"+nombre+"\'";
            Cursor c = db.rawQuery(query, null);
            return c;
        }
        else return null;
    }

    public boolean insertarGenero(String nombre){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(GeneroContract.COLUMN_NAME_GENERO_NOMBRE, nombre);
            long rowid=db.insert(GeneroContract.TABLE_NAME, null, values);
            if (rowid>0) return true;
        }
        return false;
    }

    public Cursor getGenerosNoUsados(){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT "+ GeneroContract.COLUMN_NAME_GENERO_ID+","+
                            GeneroContract.COLUMN_NAME_GENERO_NOMBRE+" ";
            query+=
                    "FROM "+ GeneroContract.TABLE_NAME+" ";
            query+=
                    "WHERE "+ GeneroContract.COLUMN_NAME_GENERO_ID + " NOT IN ("+
                            "SELECT "+ ProgramaContract.COLUMN_NAME_GENERO_ID+" FROM "+
                            ProgramaContract.TABLE_NAME+")";
            Cursor c = db.rawQuery(query, null);
            return c;
        }
        else return null;
    }

    public boolean eliminarGenero(int id){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            String query= GeneroContract.COLUMN_NAME_GENERO_ID+"=?";
            int numDel=db.delete(GeneroContract.TABLE_NAME, query, new String[]{Integer.toString(id)});
            if (numDel>0) return true;
        }
        return false;
    }

    public int consultarId_Programa (String nombre ) {
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + ProgramaContract.COLUMN_NAME_PROGRAMA_ID +" ";
            query +=
                    "FROM " + ProgramaContract.TABLE_NAME+" ";

            query +=
                    "WHERE " + ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + "=\'" + nombre + "\'";
            Cursor c = db.rawQuery(query, null);
            if (c.moveToNext()){
                return c.getInt(c.getColumnIndex(ProgramaContract.COLUMN_NAME_PROGRAMA_ID));
            }
        }
        return -1;
    }

    public boolean insertarPrograma( int id_gen, String nombre, String sinopsis, int anio, String pais) {
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ProgramaContract.COLUMN_NAME_GENERO_ID, id_gen);
            values.put(ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE, nombre);
            values.put(ProgramaContract.COLUMN_NAME_PROGRAMA_SINOPSIS, sinopsis);
            values.put(ProgramaContract.COLUMN_NAME_PROGRAMA_ANIO_ESTRENO, anio);
            values.put(ProgramaContract.COLUMN_NAME_PROGRAMA_PAIS_ORIGEN, pais);
            long rowide= db.insert(ProgramaContract.TABLE_NAME, null, values);
            if (rowide > 0) return true;
        }
        return false;

    }

    public boolean insertarPelicula(int  id) {
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(PeliculaContract.COLUMN_NAME_PELICULA_ID, id);
            long rowide= db.insert(PeliculaContract.TABLE_NAME, null, values);
            if (rowide > 0) return true;
        }
        return false;
    }

    public boolean insertarSerie(int  id) {
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SerieContract.COLUMN_NAME_SERIE_ID, id);
            long rowide= db.insert(SerieContract.TABLE_NAME, null, values);
            if (rowide > 0) return true;
        }
        return false;
    }

    public boolean actualizarPrograma(int id_gen, String nombre, String sinopsis, int anio, String pais){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ProgramaContract.COLUMN_NAME_GENERO_ID, id_gen);
            values.put(ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE,nombre);
            values.put(ProgramaContract.COLUMN_NAME_PROGRAMA_SINOPSIS, sinopsis);
            values.put(ProgramaContract.COLUMN_NAME_PROGRAMA_ANIO_ESTRENO, anio);
            values.put(ProgramaContract.COLUMN_NAME_PROGRAMA_PAIS_ORIGEN, pais);

            String query= ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE+"=?";
            int rowid=db.update(ProgramaContract.TABLE_NAME, values, query, new String[]{nombre});
            if (rowid>0) return true;
        }
        return false;
    }

    public Cursor consultarSerieLikeNombre(String nombre){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + ProgramaContract.COLUMN_NAME_PROGRAMA_ID + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_SINOPSIS + ","+
                            ProgramaContract.COLUMN_NAME_PROGRAMA_ANIO_ESTRENO + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_PAIS_ORIGEN + " ";
            query +=
                    "FROM " + ProgramaContract.TABLE_NAME + " JOIN " +
                            SerieContract.TABLE_NAME +
                            " ON " + SerieContract.COLUMN_NAME_SERIE_ID + "="
                            + ProgramaContract.COLUMN_NAME_PROGRAMA_ID + " ";
            query +=
                    "WHERE " + ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + " LIKE \'%"+nombre+"%\'";
            Cursor c = db.rawQuery(query, null);
            return c;
        }
        else return null;
    }

    public Cursor consultarSeriePorNombre(String nombre){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + ProgramaContract.TABLE_NAME+"."+ProgramaContract.COLUMN_NAME_PROGRAMA_ID + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_SINOPSIS + ","+
                            GeneroContract.COLUMN_NAME_GENERO_NOMBRE +","+
                            ProgramaContract.COLUMN_NAME_PROGRAMA_ANIO_ESTRENO + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_PAIS_ORIGEN + ","+
                            AgendaContract.COLUMN_NAME_USUARIO_ID+" ";
            query +=
                    "FROM " + ProgramaContract.TABLE_NAME+" LEFT OUTER JOIN "+
                            AgendaContract.TABLE_NAME +" ON "+ProgramaContract.TABLE_NAME+"."+
                            ProgramaContract.COLUMN_NAME_PROGRAMA_ID+"="+AgendaContract.TABLE_NAME+"."+
                            AgendaContract.COLUMN_NAME_PROGRAMA_ID+" NATURAL JOIN "+
                            GeneroContract.TABLE_NAME+ " JOIN " +
                            SerieContract.TABLE_NAME +
                            " ON " + SerieContract.COLUMN_NAME_SERIE_ID + "="
                            +ProgramaContract.TABLE_NAME+"."+ProgramaContract.COLUMN_NAME_PROGRAMA_ID +" ";
            ;
            query +=
                    "WHERE " + ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE +"=\'"+nombre+"\'";
            Cursor c = db.rawQuery(query, null);
            return c;
        }
        else return null;
    }

    public boolean insertarCapitulo(int id_cap, String nombreCap, int id_temp, int id_ser){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CapituloContract.COLUMN_NAME_CAPITULO_ID, id_cap);
            values.put(CapituloContract.COLUMN_NAME_CAPITULO_NOMBRE,nombreCap);
            values.put(CapituloContract.COLUMN_NAME_TEMPORADA_ID, id_temp);
            values.put(CapituloContract.COLUMN_NAME_SERIE_ID, id_ser);

            long rowide= db.insert(CapituloContract.TABLE_NAME, null, values);
            if (rowide > 0) return true;
        }
        return false;
    }
/*
    public Cursor getTemporadasDeSerie(int idSerie){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + TemporadaContract.COLUMN_NAME_TEMPORADA_ID + " ";
            query +=
                    "FROM " + TemporadaContract.TABLE_NAME+" ";
            query +=
                    "WHERE " + TemporadaContract.COLUMN_NAME_PROGRAMA_ID +"=? ";
            Cursor c = db.rawQuery(query, new String[]{Integer.toString(idSerie)});
            return c;
        }
        else return null;
    }*/

    public Cursor getTemporadasDeSerie(int idSerie){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + TemporadaContract.TABLE_NAME+"."+ TemporadaContract.COLUMN_NAME_TEMPORADA_ID + ","
                    +"COUNT("+ CapituloContract.TABLE_NAME+"."+ CapituloContract.COLUMN_NAME_TEMPORADA_ID+") AS cuenta ";
            query +=
                    "FROM " + TemporadaContract.TABLE_NAME+" LEFT OUTER JOIN "+ CapituloContract.TABLE_NAME +
                            " ON "+ TemporadaContract.TABLE_NAME+"."+ TemporadaContract.COLUMN_NAME_TEMPORADA_ID
                            +"="+ CapituloContract.TABLE_NAME+"."+ CapituloContract.COLUMN_NAME_TEMPORADA_ID+" AND "
                            + TemporadaContract.TABLE_NAME+"."+ TemporadaContract.COLUMN_NAME_PROGRAMA_ID+"="
                            + CapituloContract.TABLE_NAME+"."+ CapituloContract.COLUMN_NAME_SERIE_ID+" ";
            query +=
                    "WHERE " + TemporadaContract.TABLE_NAME+"."+ TemporadaContract.COLUMN_NAME_PROGRAMA_ID +"=? ";
            query+=
                    "GROUP BY "+ TemporadaContract.TABLE_NAME+"."+ TemporadaContract.COLUMN_NAME_TEMPORADA_ID;
            Cursor c = db.rawQuery(query, new String[]{Integer.toString(idSerie)});
            return c;
        }
        else return null;
    }


    public boolean actualizarCapitulo(int id_cap_old, int id_cap_new, String nombreCap, int id_temp, int id_ser){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CapituloContract.COLUMN_NAME_CAPITULO_ID, id_cap_new);
            values.put(CapituloContract.COLUMN_NAME_CAPITULO_NOMBRE,nombreCap);
            values.put(CapituloContract.COLUMN_NAME_TEMPORADA_ID, id_temp);
            values.put(CapituloContract.COLUMN_NAME_SERIE_ID, id_ser);

            String query= CapituloContract.COLUMN_NAME_SERIE_ID+"=? AND "
                    + CapituloContract.COLUMN_NAME_TEMPORADA_ID+"=? AND "
                    + CapituloContract.COLUMN_NAME_CAPITULO_ID+"=?";
            String[] compare = new String[]{Integer.toString(id_ser),Integer.toString(id_temp),Integer.toString(id_cap_old)};
            try{
                int rowid=db.update(CapituloContract.TABLE_NAME, values, query, compare);
                if (rowid>0) return true;
            } catch (Exception e){
                //No se hace nada, y luego retorna falso
            }
        }
        return false;
    }

    public Cursor consultarCapitulosPorTemporada(int idTemporada, int idSerie){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query = "SELECT " + CapituloContract.COLUMN_NAME_CAPITULO_ID +","+
                    CapituloContract.COLUMN_NAME_CAPITULO_NOMBRE+" ";
            query +=
                    "FROM " + CapituloContract.TABLE_NAME+" ";
            query +=
                    "WHERE "+ CapituloContract.COLUMN_NAME_TEMPORADA_ID+"=? AND " + CapituloContract.COLUMN_NAME_SERIE_ID +"=? ";
            query += "ORDER BY "+ CapituloContract.COLUMN_NAME_CAPITULO_ID;

            Cursor c = db.rawQuery(query, new String[]{Integer.toString(idTemporada),Integer.toString(idSerie)});
            return c;
        }
        else return null;
    }

    public Cursor consultarInfoCapitulo(int idTemporada, int idSerie, int idCap){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query = "SELECT " + CapituloContract.COLUMN_NAME_CAPITULO_ID +","+
                    CapituloContract.COLUMN_NAME_CAPITULO_NOMBRE+" ";
            query +=
                    "FROM " + CapituloContract.TABLE_NAME+" ";
            query +=
                    "WHERE "+ CapituloContract.COLUMN_NAME_TEMPORADA_ID+"=? AND "
                            + CapituloContract.COLUMN_NAME_SERIE_ID +"=? AND "
                            + CapituloContract.COLUMN_NAME_CAPITULO_ID+"=?";
            Cursor c = db.rawQuery(query, new String[]{Integer.toString(idTemporada),Integer.toString(idSerie),
            Integer.toString(idCap)});
            return c;
        }
        else return null;
    }

    public boolean insertarTemporada(int id_serie, int  idTemporada){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TemporadaContract.COLUMN_NAME_PROGRAMA_ID, id_serie);
            values.put(TemporadaContract.COLUMN_NAME_TEMPORADA_ID, idTemporada);

            long rowide= db.insert(TemporadaContract.TABLE_NAME, null, values);
            if (rowide > 0) return true;
        }
        return false;
    }

    public Cursor consultarSesion(String nombreUsu, String password){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query = "SELECT " + UsuarioContract.COLUMN_NAME_USUARIO_ID +","+
                    UsuarioContract.COLUMN_NAME_USUARIO_NOMBRE+" ";
            query +=
                    "FROM " + UsuarioContract.TABLE_NAME+" ";
            query +=
                    "WHERE "+ UsuarioContract.COLUMN_NAME_USUARIO_NOMBRE+"=? AND "
                            + UsuarioContract.COLUMN_NAME_USUARIO_PASSWORD+"=?";
            Cursor c = db.rawQuery(query, new String[]{nombreUsu, password});
            return c;
        }
        else return null;
    }

    public boolean AgregarUsuario(String nombre, String contrasenia){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(UsuarioContract.COLUMN_NAME_USUARIO_NOMBRE, nombre);
            values.put(UsuarioContract.COLUMN_NAME_USUARIO_PASSWORD, contrasenia);
            long rowid=db.insert(UsuarioContract.TABLE_NAME, null, values);
            if (rowid>0) return true;
        }
        return false;
    }


    public boolean actualizarUsuario(String nombre, String contrasenia){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(UsuarioContract.COLUMN_NAME_USUARIO_NOMBRE, nombre);
            values.put(UsuarioContract.COLUMN_NAME_USUARIO_PASSWORD,contrasenia);
            String query= UsuarioContract.COLUMN_NAME_USUARIO_NOMBRE+"=?";
            int rowid=db.update(UsuarioContract.TABLE_NAME, values, query, new String[]{nombre});
            if (rowid>0) return true;
        }
        return false;
    }

    public Cursor consultarCalificacion(int idUsuario, int idPrograma){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query = "SELECT " + CalificacionContract.COLUMN_NAME_VALOR_CALIFICACION+" ";
            query +=
                    "FROM " + CalificacionContract.TABLE_NAME+" ";
            query +=
                    "WHERE "+ CalificacionContract.COLUMN_NAME_USUARIO_ID+"=? AND "
                            + CalificacionContract.COLUMN_NAME_PROGRAMA_ID +"=? ";
            Cursor c = db.rawQuery(query, new String[]{Integer.toString(idUsuario), Integer.toString(idPrograma)});
            return c;
        }
        else return null;
    }

    public boolean insertarCalificacion(int idUsuario, int idPrograma, float calificacion){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CalificacionContract.COLUMN_NAME_USUARIO_ID, idUsuario);
            values.put(CalificacionContract.COLUMN_NAME_PROGRAMA_ID, idPrograma);
            values.put(CalificacionContract.COLUMN_NAME_VALOR_CALIFICACION,calificacion);
            long rowid=db.insert(CalificacionContract.TABLE_NAME, null, values);
            if (rowid>0) return true;
        }
        return false;
    }

    public boolean actualizarCalificacion(int idUsuario, int idPrograma, float calificacion){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CalificacionContract.COLUMN_NAME_USUARIO_ID, idUsuario);
            values.put(CalificacionContract.COLUMN_NAME_PROGRAMA_ID, idPrograma);
            values.put(CalificacionContract.COLUMN_NAME_VALOR_CALIFICACION, calificacion);

            String query = CalificacionContract.COLUMN_NAME_USUARIO_ID+"=? AND "
                    + CalificacionContract.COLUMN_NAME_PROGRAMA_ID +"=? ";
            String[] compare = new String[]{Integer.toString(idUsuario), Integer.toString(idPrograma)};
            try {
                int rowid = db.update(CalificacionContract.TABLE_NAME, values, query, compare);
                if (rowid > 0) return true;
            } catch (Exception e) {
                //No se hace nada, y luego retorna falso
            }
        }
        return false;
    }

    public Cursor consultarAllEmisoras(){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query = "SELECT " + EmisoraContract.COLUMN_NAME_EMISORA_NOMBRE+","
                    + EmisoraContract.COLUMN_NAME_EMISORA_ID+" ";
            query +=
                    "FROM " + EmisoraContract.TABLE_NAME+" ";
            Cursor c = db.rawQuery(query, null);
            return c;
        }
        else return null;
    }

    public boolean insertarCanal(String nombreCanal){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CanalContract.COLUMN_NAME_CANAL_ID,nombreCanal);
            long rowid=db.insert(CanalContract.TABLE_NAME, null, values);
            if (rowid>0) return true;
        }
        return false;
    }

    public boolean insertarEmite(String nombreCanal,int idEmisor, Integer numCanal){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EmiteContract.COLUMN_NAME_CANAL_ID,nombreCanal);
            values.put(EmiteContract.COLUMN_NAME_EMISORA_ID,idEmisor);
            values.put(EmiteContract.COLUMN_NAME_CANAL_NUMERO,numCanal);
            long rowid=db.insert(EmiteContract.TABLE_NAME, null, values);
            if (rowid>0) return true;
        }
        return false;
    }

    public Cursor consultarCanalesDePrograma(int idPrograma){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query = "SELECT " + HorarioContract.COLUMN_NAME_CANAL_ID+" ";
            query +=
                    "FROM " + HorarioContract.TABLE_NAME+" ";
            query +=
                    "WHERE " + HorarioContract.COLUMN_NAME_PROGRAMA_ID+"=?";
            Cursor c = db.rawQuery(query, new String[]{Integer.toString(idPrograma)});
            return c;
        }
        else return null;
    }

    public boolean insertarHorario(Horario h){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(HorarioContract.COLUMN_NAME_CANAL_ID,h.getNombreCanal());
            values.put(HorarioContract.COLUMN_NAME_PROGRAMA_ID,h.getIdPrograma());
            values.put(HorarioContract.COLUMN_NAME_RELACION_HORA,h.getHora());
            long rowid=db.insert(HorarioContract.TABLE_NAME, null, values);
            if (rowid>0) return true;
        }
        return false;
    }

    public int getHorarioId(int idPrograma, String idCanal){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query = "SELECT " + HorarioContract.COLUMN_NAME_RELACION_ID+" ";
            query +=
                    "FROM " + HorarioContract.TABLE_NAME+" ";
            query +=
                    "WHERE " + HorarioContract.COLUMN_NAME_PROGRAMA_ID+"=? AND "+
            HorarioContract.COLUMN_NAME_CANAL_ID+"=?";
            Cursor c = db.rawQuery(query, new String[]{Integer.toString(idPrograma),idCanal});
            int count = c.getCount();
            if (count>0) {
                c.moveToNext();
                return c.getInt(c.getColumnIndex(HorarioContract.COLUMN_NAME_RELACION_ID));
            }
        }
        return 0;
    }

    public boolean eliminarHorario(int idHorario){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            String query= HorarioContract.COLUMN_NAME_RELACION_ID+"=?";
            int numDel=db.delete(HorarioContract.TABLE_NAME, query, new String[]{Integer.toString(idHorario)});
            if (numDel>0) return true;
        }
        return false;
    }

    public Cursor getHorariosPrograma(int idPrograma){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + HorarioContract.TABLE_NAME+"."+ HorarioContract.COLUMN_NAME_PROGRAMA_ID+","+
                            HorarioContract.TABLE_NAME+"."+ HorarioContract.COLUMN_NAME_RELACION_ID+","+
                            HorarioContract.TABLE_NAME+"."+ HorarioContract.COLUMN_NAME_RELACION_HORA+","+
                            CanalContract.TABLE_NAME+"."+ CanalContract.COLUMN_NAME_CANAL_ID+" ";
            query +=
                    "FROM " + CanalContract.TABLE_NAME + " LEFT OUTER JOIN "+
                    HorarioContract.TABLE_NAME+" ON "+ CanalContract.TABLE_NAME+"."+
                    CanalContract.COLUMN_NAME_CANAL_ID+"="+
                    HorarioContract.TABLE_NAME+"."+ HorarioContract.COLUMN_NAME_CANAL_ID+
                    " AND "+ HorarioContract.TABLE_NAME+"."+ HorarioContract.COLUMN_NAME_PROGRAMA_ID + "=?";
            Cursor c = db.rawQuery(query, new String[]{Integer.toString(idPrograma)});
            return c;
        }
        else return null;
    }
  /*  public boolean actualizarUsuario1(int id, String nombre, String contrasenia){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(UsuarioContract.COLUMN_NAME_USUARIO_ID, id);
            values.put(UsuarioContract.COLUMN_NAME_USUARIO_NOMBRE, nombre);
            values.put(UsuarioContract.COLUMN_NAME_USUARIO_PASSWORD,contrasenia);
            String query= UsuarioContract.COLUMN_NAME_USUARIO_ID+"=?";
            int rowid=db.update(UsuarioContract.TABLE_NAME, values, query, new String[]{nombre});
            if (rowid>0) return true;
        }
        return false;
    }

    public Cursor consultarNombreUser(String nombre){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + UsuarioContract.COLUMN_NAME_USUARIO_NOMBRE+" ";

            query+= "FROM "+ UsuarioContract.TABLE_NAME;
            query +=
                    "WHERE " + UsuarioContract.COLUMN_NAME_USUARIO_NOMBRE +nombre;
            Cursor c = db.rawQuery(query, null);
            return c;
        }
        else return null;
    }
    */

    public Cursor consultarCanalLikeNombre(String nombre){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + CanalContract.COLUMN_NAME_CANAL_ID + " ";
            query +=
                    "FROM " + CanalContract.TABLE_NAME + " ";
            query +=
                    "WHERE " + CanalContract.COLUMN_NAME_CANAL_ID + " LIKE \'%"+nombre+"%\'";
            Cursor c = db.rawQuery(query, null);
            return c;
        }
        else return null;
    }

    // todas las consultas  de canal


    // consultar datos del canal

    public Cursor consultarNombreEmisoras(String nombre_canal){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + EmiteContract.COLUMN_NAME_EMISORA_ID + "," +
                            EmiteContract.COLUMN_NAME_CANAL_ID + "," +
                            EmiteContract.COLUMN_NAME_CANAL_NUMERO+ ","+
                            EmisoraContract.COLUMN_NAME_EMISORA_NOMBRE+" ";
            query +=
                    "FROM " + EmiteContract.TABLE_NAME+" NATURAL JOIN "+
                            EmisoraContract.TABLE_NAME+ " ";

            query +=
                    "WHERE " + EmiteContract.COLUMN_NAME_CANAL_ID +"=\'"+nombre_canal+"\'";
            Cursor c = db.rawQuery(query, null);
            return c;
        }
        else return null;
    }



    public Cursor consultarEmisorasDeCanal(String  nombre_canal){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + EmisoraContract.TABLE_NAME+"."+ EmiteContract.COLUMN_NAME_EMISORA_ID + ","
                            + EmiteContract.TABLE_NAME+"."+ EmiteContract.COLUMN_NAME_CANAL_ID + ","
                    + EmiteContract.TABLE_NAME+"."+ EmiteContract.COLUMN_NAME_CANAL_NUMERO+ ","
                            + EmisoraContract.TABLE_NAME+"."+ EmisoraContract.COLUMN_NAME_EMISORA_NOMBRE+" ";
            query +=
                    "FROM " + EmisoraContract.TABLE_NAME+"  LEFT OUTER  JOIN "+ EmiteContract.TABLE_NAME+
                            " ON "+ EmisoraContract.TABLE_NAME+"."+ EmisoraContract.COLUMN_NAME_EMISORA_ID
                            +"="+ EmiteContract.TABLE_NAME+"."+ EmiteContract.COLUMN_NAME_EMISORA_ID+" AND "+
                            EmiteContract.TABLE_NAME+"."+ EmiteContract.COLUMN_NAME_CANAL_ID +"=? ";
            Cursor c = db.rawQuery(query, new String[]{nombre_canal});
            return c;
        }
        else return null;
    }
    //eliminar de la tabla canal   y de la tabla emite

    public boolean eliminarCanal(String  NombreCanal){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            String query= CanalContract.COLUMN_NAME_CANAL_ID+"=?";
            int numDel=db.delete(CanalContract.TABLE_NAME, query, new String[]{(NombreCanal)});
            if (numDel>0) return true;
        }
        return false;
    }

    public boolean eliminarEmite(String  nombreCanal, int idEmisora){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            String query= EmiteContract.COLUMN_NAME_CANAL_ID+"=? AND "+
                    EmiteContract.COLUMN_NAME_EMISORA_ID+"=?";
            int numDel=db.delete(EmiteContract.TABLE_NAME, query, new String[]{nombreCanal,Integer.toString(idEmisora)});
            if (numDel>0) return true;
        }
        return false;
    }


// actualizar canal


    public boolean actualizarCanal(String  NombreCanal_old, String NombreCanal_new){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CanalContract.COLUMN_NAME_CANAL_ID, NombreCanal_new) ;

            String query= CanalContract.COLUMN_NAME_CANAL_ID+"=?";
            String[] compare = new String[]{(NombreCanal_old)};
            try{
                int rowid=db.update(CanalContract.TABLE_NAME, values, query, compare);
                if (rowid>0) return true;
            } catch (Exception e){
                //No se hace nada, y luego retorna falso
            }
        }
        return false;
    }

//actualizar emite
    public boolean actualizarEmite(String nombrecanal_old, String nombrecanal_new, int idEmisor, Integer numCanal){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EmiteContract.COLUMN_NAME_CANAL_ID,nombrecanal_new);
            values.put(EmiteContract.COLUMN_NAME_EMISORA_ID,idEmisor);
            values.put(EmiteContract.COLUMN_NAME_CANAL_NUMERO,numCanal);

            String query= EmiteContract.COLUMN_NAME_CANAL_ID+"=? AND "
                    + EmiteContract.COLUMN_NAME_EMISORA_ID+"=? ";
            String[] compare = new String[]{(nombrecanal_old),Integer.toString(idEmisor)};
            try{
                int rowid=db.update(EmiteContract.TABLE_NAME, values, query, compare);
                if (rowid>0) return true;
            } catch (Exception e){
                //No se hace nada, y luego retorna falso
            }
        }
        return false;
    }

    public boolean insertarFavorito(int idUsuario, int idPrograma){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(AgendaContract.COLUMN_NAME_USUARIO_ID, idUsuario);
            values.put(AgendaContract.COLUMN_NAME_PROGRAMA_ID, idPrograma);
            long rowid=db.insert(AgendaContract.TABLE_NAME, null, values);
            if (rowid>0) return true;
        }
        return false;
    }

    public boolean eliminarFavorito(int idUsuario, int idPrograma){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            String query= AgendaContract.COLUMN_NAME_USUARIO_ID+"=? AND "+
                    AgendaContract.COLUMN_NAME_PROGRAMA_ID+"=?";
            int numDel=db.delete(AgendaContract.TABLE_NAME, query,
                    new String[]{Integer.toString(idUsuario), Integer.toString(idPrograma)});
            if (numDel>0) return true;
        }
        return false;
    }

    public Cursor consultarPeliculasAndFavoritos(String nombre, int idUsuario){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + ProgramaContract.TABLE_NAME+"."+ProgramaContract.COLUMN_NAME_PROGRAMA_ID + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_SINOPSIS + ","+
                            ProgramaContract.COLUMN_NAME_PROGRAMA_ANIO_ESTRENO + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_PAIS_ORIGEN + ","+
                    AgendaContract.COLUMN_NAME_USUARIO_ID+" ";
            query +=
                    "FROM " + ProgramaContract.TABLE_NAME + " JOIN " +
                            PeliculaContract.TABLE_NAME +
                            " ON " + PeliculaContract.COLUMN_NAME_PELICULA_ID + "="
                            + ProgramaContract.TABLE_NAME+"."+ProgramaContract.COLUMN_NAME_PROGRAMA_ID + " LEFT OUTER JOIN "+
                            AgendaContract.TABLE_NAME+" ON "+AgendaContract.TABLE_NAME+"."+
                            AgendaContract.COLUMN_NAME_PROGRAMA_ID+"="+ ProgramaContract.TABLE_NAME+"."+
                            ProgramaContract.COLUMN_NAME_PROGRAMA_ID+" AND "+AgendaContract.TABLE_NAME+"."+
                            AgendaContract.COLUMN_NAME_USUARIO_ID+"=? ";
            query +=
                    "WHERE " + ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + " LIKE \'%"+nombre+"%\'";
            Cursor c = db.rawQuery(query, new String[]{Integer.toString(idUsuario)});
            return c;
        }
        else return null;
    }

    public Cursor consultarSeriesAndFavoritos(String nombre, int idUsuario){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + ProgramaContract.TABLE_NAME+"."+ProgramaContract.COLUMN_NAME_PROGRAMA_ID + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_SINOPSIS + ","+
                            ProgramaContract.COLUMN_NAME_PROGRAMA_ANIO_ESTRENO + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_PAIS_ORIGEN + ","+
                            AgendaContract.COLUMN_NAME_USUARIO_ID+" ";
            query +=
                    "FROM " + ProgramaContract.TABLE_NAME + " JOIN " +
                            SerieContract.TABLE_NAME +
                            " ON " + SerieContract.COLUMN_NAME_SERIE_ID + "="
                            + ProgramaContract.TABLE_NAME+"."+ProgramaContract.COLUMN_NAME_PROGRAMA_ID + " LEFT OUTER JOIN "+
                            AgendaContract.TABLE_NAME+" ON "+AgendaContract.TABLE_NAME+"."+
                            AgendaContract.COLUMN_NAME_PROGRAMA_ID+"="+ ProgramaContract.TABLE_NAME+"."+
                            ProgramaContract.COLUMN_NAME_PROGRAMA_ID+" AND "+AgendaContract.TABLE_NAME+"."+
                            AgendaContract.COLUMN_NAME_USUARIO_ID+"=? ";
            query +=
                    "WHERE " + ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + " LIKE \'%"+nombre+"%\'";
            Cursor c = db.rawQuery(query, new String[]{Integer.toString(idUsuario)});
            return c;
        }
        else return null;
    }


    public Cursor consultarPeliculasDeAgenda(String nombre, int idUsuario){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + ProgramaContract.TABLE_NAME+"."+ProgramaContract.COLUMN_NAME_PROGRAMA_ID + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_SINOPSIS + ","+
                            ProgramaContract.COLUMN_NAME_PROGRAMA_ANIO_ESTRENO + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_PAIS_ORIGEN + ","+
                            AgendaContract.COLUMN_NAME_USUARIO_ID+" ";
            query +=
                    "FROM " + ProgramaContract.TABLE_NAME + " JOIN " +
                            PeliculaContract.TABLE_NAME +
                            " ON " + PeliculaContract.COLUMN_NAME_PELICULA_ID + "="
                            + ProgramaContract.TABLE_NAME+"."+ProgramaContract.COLUMN_NAME_PROGRAMA_ID + " JOIN "+
                            AgendaContract.TABLE_NAME+" ON "+AgendaContract.TABLE_NAME+"."+
                            AgendaContract.COLUMN_NAME_PROGRAMA_ID+"="+ ProgramaContract.TABLE_NAME+"."+
                            ProgramaContract.COLUMN_NAME_PROGRAMA_ID+" AND "+AgendaContract.TABLE_NAME+"."+
                            AgendaContract.COLUMN_NAME_USUARIO_ID+"=? ";
            query +=
                    "WHERE " + ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + " LIKE \'%"+nombre+"%\'";
            Cursor c = db.rawQuery(query, new String[]{Integer.toString(idUsuario)});
            return c;
        }
        else return null;
    }



    public Cursor consultarSeriesDeAgenda(String nombre, int idUsuario){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + ProgramaContract.TABLE_NAME+"."+ProgramaContract.COLUMN_NAME_PROGRAMA_ID + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_SINOPSIS + ","+
                            ProgramaContract.COLUMN_NAME_PROGRAMA_ANIO_ESTRENO + "," +
                            ProgramaContract.COLUMN_NAME_PROGRAMA_PAIS_ORIGEN + ","+
                            AgendaContract.COLUMN_NAME_USUARIO_ID+" ";
            query +=
                    "FROM " + ProgramaContract.TABLE_NAME + " JOIN " +
                            SerieContract.TABLE_NAME +
                            " ON " + SerieContract.COLUMN_NAME_SERIE_ID + "="
                            + ProgramaContract.TABLE_NAME+"."+ProgramaContract.COLUMN_NAME_PROGRAMA_ID + " JOIN "+
                            AgendaContract.TABLE_NAME+" ON "+AgendaContract.TABLE_NAME+"."+
                            AgendaContract.COLUMN_NAME_PROGRAMA_ID+"="+ ProgramaContract.TABLE_NAME+"."+
                            ProgramaContract.COLUMN_NAME_PROGRAMA_ID+" AND "+AgendaContract.TABLE_NAME+"."+
                            AgendaContract.COLUMN_NAME_USUARIO_ID+"=? ";
            query +=
                    "WHERE " + ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE + " LIKE \'%"+nombre+"%\'";
            Cursor c = db.rawQuery(query, new String[]{Integer.toString(idUsuario)});
            return c;
        }
        else return null;
    }

    public boolean insertarRelacionDiaHorario(int rel_id, int dia){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DataBaseContract.DiaHorarioContract.COLUMN_NAME_RELACION_ID, rel_id);
            values.put(DataBaseContract.DiaHorarioContract.COLUMN_NAME_DIA_ID, dia);
            long rowid=db.insert(DataBaseContract.DiaHorarioContract.TABLE_NAME, null, values);
            if (rowid>0) return true;
        }
        return false;
    }
 // actualizar Horario , por ahora no lo voy a hacer.

    public boolean actualizarHorario(String nombrecanal_old, String nombrecanal_new, int idEmisor, Integer numCanal){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if (miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EmiteContract.COLUMN_NAME_CANAL_ID,nombrecanal_new);
            values.put(EmiteContract.COLUMN_NAME_EMISORA_ID,idEmisor);
            values.put(EmiteContract.COLUMN_NAME_CANAL_NUMERO,numCanal);

            String query= EmiteContract.COLUMN_NAME_CANAL_ID+"=? AND "
                    + EmiteContract.COLUMN_NAME_EMISORA_ID+"=? ";
            String[] compare = new String[]{(nombrecanal_old),Integer.toString(idEmisor)};
            try{
                int rowid=db.update(EmiteContract.TABLE_NAME, values, query, compare);
                if (rowid>0) return true;
            } catch (Exception e){
                //No se hace nada, y luego retorna falso
            }
        }
        return false;
    }

    public Cursor getDia(){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " +  DataBaseContract.DiaContract.COLUMN_NAME_ID_DIA+" ";
            query +=
                    "FROM " +DataBaseContract.DiaContract.TABLE_NAME+ "";

          /*  query +=
                    "WHERE " + ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE +"=\'"+nombre+"\'";*/
            Cursor c = db.rawQuery(query, null);
            return c;
        }
        else return null;
    }


    public Cursor consultarHorarioDia(int idHorario){
        try {
            miDBHelper.createDataBase();
        } catch (IOException e) {
        }
        if(miDBHelper.checkDataBase()) {
            SQLiteDatabase db = miDBHelper.getReadableDatabase();
            String query =
                    "SELECT " + DiaContract.TABLE_NAME+"."+ DiaContract.COLUMN_NAME_ID_DIA+","+
                            DiaHorarioContract.TABLE_NAME+"."+DiaHorarioContract.COLUMN_NAME_RELACION_ID+" ";
            query +=
                    "FROM " + DiaContract.TABLE_NAME+" LEFT OUTER JOIN "+ DiaHorarioContract.TABLE_NAME +
                            " ON "+ DiaContract.TABLE_NAME+"."+ DiaContract.COLUMN_NAME_ID_DIA
                            +"="+ DiaHorarioContract.TABLE_NAME+"."+ DiaHorarioContract.COLUMN_NAME_DIA_ID+" AND "
                            +DiaHorarioContract.TABLE_NAME+"."+ DiaHorarioContract.COLUMN_NAME_RELACION_ID +"=? ";
            query+=
                    "ORDER BY "+ DiaContract.TABLE_NAME+"."+ DiaContract.COLUMN_NAME_ID_DIA;

            Cursor c = db.rawQuery(query, new String[]{Integer.toString(idHorario)});
            return c;
        }
        else return null;
    }

}

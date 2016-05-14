package epiphany_soft.wtw.DataBase;

import android.provider.BaseColumns;

/**
 * Created by Camilo on 23/03/2016.
 */
/*Clase contrato que guarda las definiciones de la base de datos*/
public final class DataBaseContract {
    public DataBaseContract(){}

    public static abstract class ProgramaContract implements BaseColumns {
        public static final String TABLE_NAME = "Programa";
        public static final String COLUMN_NAME_PROGRAMA_ID = "Pro_Identificador";
        public static final String COLUMN_NAME_GENERO_ID = "Gen_Identificador";
        public static final String COLUMN_NAME_PROGRAMA_NOMBRE = "Pro_Nombre";
        public static final String COLUMN_NAME_PROGRAMA_SINOPSIS = "Pro_Sinopsis";
        public static final String COLUMN_NAME_PROGRAMA_ANIO_ESTRENO = "Pro_AnioEstreno";
        public static final String COLUMN_NAME_PROGRAMA_PAIS_ORIGEN = "Pro_PaisOrigen";
    }

    public static abstract class PeliculaContract implements BaseColumns {
        public static final String TABLE_NAME = "Pelicula";
        public static final String COLUMN_NAME_PELICULA_ID = "Pel_Id";
    }

    public static abstract class SerieContract implements BaseColumns {
        public static final String TABLE_NAME = "Serie";
        public static final String COLUMN_NAME_SERIE_ID = "Ser_Id";
    }

    public static abstract class CapituloContract implements BaseColumns {
        public static final String TABLE_NAME = "Capitulo";
        public static final String COLUMN_NAME_TEMPORADA_ID = "Tem_Identificador";
        public static final String COLUMN_NAME_CAPITULO_ID = "Cap_Identificador";
        public static final String COLUMN_NAME_SERIE_ID = "Ser_Id";
        public static final String COLUMN_NAME_CAPITULO_NOMBRE = "Cap_Nombre";
    }

    public static abstract class GeneroContract implements BaseColumns {
        public static final String TABLE_NAME = "Genero";
        public static final String COLUMN_NAME_GENERO_ID = "Gen_Identificador";
        public static final String COLUMN_NAME_GENERO_NOMBRE = "Gen_Nombre";
    }

    public static abstract class TemporadaContract implements BaseColumns {
        public static final String TABLE_NAME = "Temporada";
        public static final String COLUMN_NAME_PROGRAMA_ID = "Ser_Id";
        public static final String COLUMN_NAME_TEMPORADA_ID = "Tem_Identificador";
    }

    public static abstract class UsuarioContract implements BaseColumns {
        public static final String TABLE_NAME = "Usuario";
        public static final String COLUMN_NAME_USUARIO_ID = "Usu_Identificador";
        public static final String COLUMN_NAME_USUARIO_NOMBRE = "Usu_Nombre";
        public static final String COLUMN_NAME_USUARIO_PASSWORD = "Usu_Contrasenia";
    }

    public static abstract class CalificacionContract implements BaseColumns {
        public static final String TABLE_NAME = "Calificacion";
        public static final String COLUMN_NAME_USUARIO_ID = "Usu_Identificador";
        public static final String COLUMN_NAME_PROGRAMA_ID = "Pro_Identificador";
        public static final String COLUMN_NAME_VALOR_CALIFICACION = "Cal_Valor";
    }

    public static abstract class CanalContract implements BaseColumns {
        public static final String TABLE_NAME = "Canal";
        public  static final String COLUMN_NAME_CANAL_ID = "Can_Nombre";
    }

    public static abstract class EmiteContract implements BaseColumns {
        public static final String TABLE_NAME = "Emite";
        public  static final String COLUMN_NAME_EMISORA_ID = "Emisora_Id";
        public  static final String COLUMN_NAME_CANAL_ID = "Can_Nombre";
        public static final String COLUMN_NAME_CANAL_NUMERO = "Can_Numero";
    }

    public static abstract class EmisoraContract implements BaseColumns {
        public static final String TABLE_NAME = "Emisora";
        public  static final String COLUMN_NAME_EMISORA_ID = "Emisora_Id";
        public  static final String COLUMN_NAME_EMISORA_NOMBRE = "Emisora_Nombre";
    }

    /*TODO: Implementarlo en la BD. Todavía no se pueden usar. La definición puede variar*/
    public static abstract class AgendaContract implements BaseColumns {
        public static final String TABLE_NAME = "Agendar";
        public static final String COLUMN_NAME_USUARIO_ID = "Usu_Identificador";
        public static final String COLUMN_NAME_PROGRAMA_ID = "Pro_Identificador";
    }

    public static abstract class RecordatorioContract implements BaseColumns {
        public static final String TABLE_NAME = "Recordatorio";
        public static final String COLUMN_NAME_USUARIO_ID = "Usu_Identificador";
        public static final String COLUMN_NAME_PROGRAMA_ID = "Pro_Identificador";
        public static final String COLUMN_NAME_RECORDATORIO_ID = "Rec_Identiicador";
        public static final String COLUMN_NAME_RECORDATORIO_FECHA = "Rec_Fecha";
        public static final String COLUMN_NAME_RECORDATORIO_HORA = "Rec_Hora";
    }


    public static abstract class HorarioContract implements BaseColumns {
        public static final String TABLE_NAME = "Horario";
        public static final String COLUMN_NAME_RELACION_ID = "Rel_Identificador";
        public static final String COLUMN_NAME_PROGRAMA_ID = "Pro_Identificador";
        public static final String COLUMN_NAME_CANAL_ID = "Can_Nombre";
        public static final String COLUMN_NAME_RELACION_HORA = "Rel_Hora";
    }

    public static abstract class DiaHorarioContract implements BaseColumns {
        public static final String TABLE_NAME = "DiaHorario";
        public static final String COLUMN_NAME_RELACION_ID = "Rel_Identificador";//id_horario
        public static final String COLUMN_NAME_DIA_ID = "Id_Dia";

    }

    public static abstract class DiaContract implements BaseColumns {
        public static final String TABLE_NAME = "Dia";
        public static final String COLUMN_NAME_ID_DIA = "Id_Dia";

    }
}

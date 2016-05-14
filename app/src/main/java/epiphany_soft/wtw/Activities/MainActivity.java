package epiphany_soft.wtw.Activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import epiphany_soft.wtw.Negocio.Sesion;
import epiphany_soft.wtw.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getUserInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_inicio) {
            Intent i = new Intent(this.getBaseContext(),ActivityInicioSesion.class);
            this.startActivity(i);
        }
        else if (id == R.id.nav_registrar_programa) {
            Intent i = new Intent(this.getBaseContext(), ActivityRegistrarPrograma.class);
            this.startActivity(i);
        }
        else if (id == R.id.nav_consultar_series) {
            Intent i = new Intent(this.getBaseContext(), ActivityConsultarPrograma.class);
            this.startActivity(i);
        }
        else if (id == R.id.nav_registrar_canal) {
            Intent i = new Intent(this.getBaseContext(), ActivityAgregarCanal.class);
            this.startActivity(i);
        }
        else if (id == R.id.nav_consultar_canal) {
            Intent i = new Intent(this.getBaseContext(), ActivityConsultarCanal.class);
            this.startActivity(i);
        }
        else if (id == R.id.nav_registrar_genero) {
            Intent i = new Intent(this.getBaseContext(), ActivityAgregarGenero.class);
            this.startActivity(i);
        }
        else if (id == R.id.nav_eliminar_genero) {
            Intent i = new Intent(this.getBaseContext(), ActivityEliminarGenero.class);
            this.startActivity(i);
        }
        else if (id == R.id.nav_actualizar_user) {
            Intent i = new Intent(this.getBaseContext(), ActivityModificarUsuario.class);
            this.startActivity(i);
        }
        else if (id == R.id.nav_consultar_peliculas_agenda) {
            Intent i = new Intent(this.getBaseContext(), ActivityConsultarProgramasAgenda.class);
            this.startActivity(i);
        }
        else if (id == R.id.nav_cerrar) {
            this.cerrarSesion();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cerrarSesion(){
        Sesion.getInstance().refresh();
        hideWhenNoSesion();
        deleteUserInfo();
    }

    private void deleteUserInfo() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.file_user_info), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.key_id_usuario_session), -1);
        editor.putString(getString(R.string.key_nombre_usuario_session), null);
        editor.putBoolean(getString(R.string.key_active_session), false);
        editor.commit();
    }

    private void getUserInfo(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.file_user_info), MODE_PRIVATE);
        int idUsuario = sharedPref.getInt(getString(R.string.key_id_usuario_session), -1);
        String nombreUsuario = sharedPref.getString(getString(R.string.key_nombre_usuario_session), null);
        boolean isActiva=sharedPref.getBoolean(getString(R.string.key_active_session), false);
        Sesion.getInstance().setIdUsuario(idUsuario);
        Sesion.getInstance().setNombreUsuario(nombreUsuario);
        Sesion.getInstance().setActiva(isActiva);
    }


    public void hideWhenNoSesion(){
        if (!Sesion.getInstance().isActiva()){
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().clear();
            getMenuInflater().inflate(R.menu.activity_main_drawer_no_session, navigationView.getMenu());
        }
    }

    public void showWhenSesion(){
        if (Sesion.getInstance().isActiva()){
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().clear();
            getMenuInflater().inflate(R.menu.activity_main_drawer, navigationView.getMenu());
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        hideWhenNoSesion();
        showWhenSesion();
    }
}

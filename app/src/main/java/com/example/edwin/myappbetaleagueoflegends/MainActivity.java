package com.example.edwin.myappbetaleagueoflegends;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.edwin.myappbetaleagueoflegends.request.ApiRequest;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Button buttonSearch;
    private EditText editTextSummoner;
    private RequestQueue queue;
    private ApiRequest request;
    private ProgressBar pbLoader;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = MySingleton.getInstance(this).getRequestQueue();
        request = new ApiRequest(queue,this);



        buttonSearch = (Button) findViewById(R.id.buttonSearchSummoner);
        editTextSummoner = (EditText) findViewById(R.id.editTextSearchSummoner);
        pbLoader = (ProgressBar) findViewById(R.id.progressBarSearch);
        handler = new Handler();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayoutIntro);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        NavigationView nvDrawerPrincipal = (NavigationView) findViewById(R.id.nvPrincipal);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpDrawerContent(nvDrawerPrincipal);


        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String summonername = editTextSummoner.getText().toString().trim();

                if(summonername.length() > 0){
                    pbLoader.setVisibility(View.VISIBLE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            request.checkPlayerName(summonername,new ApiRequest.CheckPlayerCallback() {
                                @Override
                                public void onSuccess(String name, long id) {

                                    pbLoader.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(getApplicationContext(),HistoriqueActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("NAME",name);
                                    extras.putLong("ID",id);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }

                                @Override
                                public void dontExist(String message) {
                                    pbLoader.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(String message) {
                                    pbLoader.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, 2000);

                }else{
                    Toast.makeText(MainActivity.this,"Ingrese Usuario",Toast.LENGTH_SHORT).show();
                }

                //Toast.makeText(MainActivity.this,"Search Usuario => "+ summonername,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectItemDrawer(MenuItem menuItem){
        android.support.v4.app.Fragment myFragment = null;
        Class fragmentClass;

        switch (menuItem.getItemId()){
            case R.id.itemChampion:
                fragmentClass = ChampionFragment.class;
                break;
            case R.id.itemObjects:
                fragmentClass = ObjectFragment.class;
                break;
            case R.id.itemSearch:
                fragmentClass = SearchFragment.class;
                break;
            case R.id.itemSettings:
                fragmentClass = SettingFragment.class;
                break;
            default:
                fragmentClass = ChampionFragment.class;
        }

        try{
            myFragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent,myFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();

    }

    private void setUpDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });

    }



}

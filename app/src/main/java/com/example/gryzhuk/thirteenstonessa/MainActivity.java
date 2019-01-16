package com.example.gryzhuk.thirteenstonessa;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import static com.example.gryzhuk.thirteenstonessa.ThirteenStones.getGameFromJSON;
import static com.example.gryzhuk.thirteenstonessa.ThirteenStones.getJSONStringFrom;
import static com.example.gryzhuk.thirteenstonessa.Utils.showInfoDialog;

public class MainActivity extends AppCompatActivity {

    private ThirteenStones mCurrentGame;
    private TextView statusBar;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusBar = (TextView)findViewById(R.id.tv_status_bar);
        view = (View)findViewById(R.id.activity_main);
        setupToolbar();
        setupFAB();
        initializeAndStartGame();
    }

    private void initializeAndStartGame() {
        mCurrentGame = new ThirteenStones();
        updateStatusBar();
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialog (MainActivity.this, getString(R.string.info_title),mCurrentGame.getRules() );
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_game) {
            startNextNewGame();
        }
        else if (id == R.id.action_about){
            showInfoDialog(MainActivity.this,"About","Hana Ryzhuk");
        }

        return super.onOptionsItemSelected(item);
    }

    public void pick123(View view) {
        try{
            Button b = (Button)view;
            int bText = Integer.parseInt(b.getText().toString());
            mCurrentGame.takeTurn(bText);
            updateStatusBar();
            if (mCurrentGame.isGameOver()){
                showInfoDialog(MainActivity.this,"Game Over!","The winner is player #"+mCurrentGame.getCurrentOrWinningPlayerNumberOneOrTwo());
            }
        }
        catch(Exception ex){
         Snackbar.make(view, "Ooops wrong", Snackbar.LENGTH_SHORT).show();}
    }

    public void updateStatusBar(){
        int Players =  mCurrentGame.getCurrentOrWinningPlayerNumberOneOrTwo();
        int Stones =  mCurrentGame.getPileCurrent();
        statusBar.setText(mCurrentGame.getStatusBarText());
    }

    public void startNextNewGame(){
        mCurrentGame.startGame();
        updateStatusBar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String json=
        getJSONStringFrom(mCurrentGame);
        outState.putString("JSON",json);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        String st=savedInstanceState.getString("JSON");
        mCurrentGame = getGameFromJSON (savedInstanceState.getString(st));
        updateStatusBar();
    }

}

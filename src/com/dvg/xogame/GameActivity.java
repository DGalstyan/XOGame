package com.dvg.xogame;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class GameActivity extends Activity {
	private static SharedPreferences mSharedPreferences;
	private GLSurfaceView mGLView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		final Button new_game_btn = (Button) findViewById(R.id.new_game);
		final Button load_game_btn = (Button) findViewById(R.id.load_saves);
		final Button resumeGamebtn = (Button) findViewById(R.id.resume);
		new_game_btn.setOnClickListener(listenerGame);
		load_game_btn.setOnClickListener(listenerGame);
		resumeGamebtn.setOnClickListener(listenerGame);
		mSharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		boolean resume = mSharedPreferences.getBoolean("back", false);
		if(resume)
		{
			resumeGamebtn.setVisibility(View.VISIBLE);
		}else{
			resumeGamebtn.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	OnClickListener listenerGame = new View.OnClickListener() {
		public void onClick(View v) {
			final Button btn = (Button) v;			
			Intent intent = new Intent(GameActivity.this, MainGame.class);
			if (btn.getId() == R.id.new_game) {
				finish();
				intent.putExtra("game", "1");
				startActivity(intent);
			} else if (btn.getId() == R.id.load_saves) {
				String saved = mSharedPreferences.getString("saved_game", null);
				if (saved == null) {
					Toast.makeText(getApplicationContext(), "You dont have a saved game.", Toast.LENGTH_SHORT).show();
				} else {
					finish();
					intent.putExtra("game", "2");
					startActivity(intent);
				}
			} else if (btn.getId() == R.id.resume) {
				finish();
				intent.putExtra("game", "3");
				startActivity(intent);
			}
		}
	};

}

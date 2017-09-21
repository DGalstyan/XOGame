package com.dvg.xogame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainGame extends Activity implements OnTouchListener {
	//Interface for accessing and modifying preference data.
	private static SharedPreferences mSharedPreferences = null;
	private AdView mAdView = null;
	private ImageButton b1 = null;
	private ImageButton b2 = null;
	private ImageButton b3 = null;
	private ImageButton b4 = null;
	private ImageButton b5 = null;
	private ImageButton b6 = null;
	private ImageButton b7 = null;
	private ImageButton b8 = null;
	private ImageButton b9 = null;

	private ImageView img = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		/** Called when the activity is first created. */
		img = (ImageView) findViewById(R.id.imagecross);
		img.setOnTouchListener(this);

		findViewById(R.id.layout_id).setOnDragListener(new GameDragListener());

		b1 = (ImageButton) findViewById(R.id.btn1);
		b2 = (ImageButton) findViewById(R.id.btn2);
		b3 = (ImageButton) findViewById(R.id.btn3);
		b4 = (ImageButton) findViewById(R.id.btn4);
		b5 = (ImageButton) findViewById(R.id.btn5);
		b6 = (ImageButton) findViewById(R.id.btn6);
		b7 = (ImageButton) findViewById(R.id.btn7);
		b8 = (ImageButton) findViewById(R.id.btn8);
		b9 = (ImageButton) findViewById(R.id.btn9);

		b1.setOnDragListener(new GameDragListener());
		b2.setOnDragListener(new GameDragListener());
		b3.setOnDragListener(new GameDragListener());
		b4.setOnDragListener(new GameDragListener());
		b5.setOnDragListener(new GameDragListener());
		b6.setOnDragListener(new GameDragListener());
		b7.setOnDragListener(new GameDragListener());
		b8.setOnDragListener(new GameDragListener());
		b9.setOnDragListener(new GameDragListener());

		mSharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		Intent intent = getIntent();
		String game = intent.getStringExtra("game");

		if (game.equalsIgnoreCase("1")) {
			newGame();
		} else if (game.equalsIgnoreCase("2")) {
			savedGame();
		} else if (game.equalsIgnoreCase("3")) {
			resumeGame();
		}
		// Added ads in the button
		
		mAdView = new AdView(this);
		mAdView.setAdSize(AdSize.BANNER);
		mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id));
		LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

	
		layout.addView(mAdView, layoutParams);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(
				AdRequest.DEVICE_ID_EMULATOR).build();

		mAdView.loadAd(adRequest);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void resumeGame() {
		buttonClickable(true);
		String saved = mSharedPreferences.getString("resume_game", null);
		String savArray[] = saved.split(",");
		int index = 0;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				gameArray[i][j] = Integer.parseInt(savArray[index]);
				loadSaved(i, j, Integer.parseInt(savArray[index]));
				index++;
			}
		if ((gameType == 1) && (count % 2 != 0))
			runGame();
	}

	@Override
	public void onBackPressed() {
		String savedGame = arrayToString(gameArray);
		Editor e = mSharedPreferences.edit();
		e.putString("saved_game", savedGame);
		e.putBoolean("back", false);
		e.remove("resume_game");
		e.commit();
		finish();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:			
				String savedGame = arrayToString(gameArray);
				Editor e = mSharedPreferences.edit();
				e.putBoolean("back", true);
				e.putString("resume_game", savedGame);
				e.commit();
				finish();
				Intent intent = new Intent(MainGame.this, GameActivity.class);
				startActivity(intent);
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void savedGame() {		
		buttonClickable(true);
		String saved = mSharedPreferences.getString("saved_game", null);
		String savArray[] = saved.split(",");
		int index = 0;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				gameArray[i][j] = Integer.parseInt(savArray[index]);
				loadSaved(i, j, Integer.parseInt(savArray[index]));
				index++;
			}
		if ((gameType == 1) && (count % 2 != 0))
			runGame();

	}

	public void loadSaved(int x, int y, int value) {
		final ImageButton ib_tmp = (ImageButton) findViewById(R.id.btn1);
		int ib_id = ib_tmp.getId();
		if ((x == 0) && (y == 0)) {

		} else {
			if (x == 0)
				ib_id -= y;
			else if (x == 1)
				ib_id += (3 - y);
			else if (x == 2)
				ib_id += (6 - y);
		}
		final ImageButton ib = (ImageButton) findViewById(ib_id);
		if (value == 1)
			ib.setImageResource(dotTemplate);
		else if (value == 2)
			ib.setImageResource(crossTemplate);
	}

	public void newGame() {
		buttonClickable(true);
		// reset the array.
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				gameArray[i][j] = 0;

		/* *********************************************************
		 * Initiates the computer's chance during start of the game, as well as
		 * when there is a win / loose and the next chance is for the computer.
		 * *********************************************************
		 */
		if ((gameType == 1) && (count % 2 != 0))
			runGame();
	}

	private void buttonClickable(boolean b) {
		// Re-enable the Click-able property of buttons.
		b1.setClickable(b);
		b2.setClickable(b);
		b3.setClickable(b);
		b4.setClickable(b);
		b5.setClickable(b);
		b6.setClickable(b);
		b7.setClickable(b);
		b8.setClickable(b);
		b9.setClickable(b);
	}

	public void onPress(ImageButton ibutton) {
		
		
		// Button inactive for further clicks until a result is obtained.
		ibutton.setClickable(false);

		if (soundEnabled) {
			MediaPlayer mp = MediaPlayer.create(getApplicationContext(),
					R.raw.faild);
			mp.start();
		}
		// Increment Count on clicking the button.
		count++;

		if ((count % 2 != 0) && (gameType == 0)) {
			player = 1;
			ibutton.setImageResource(crossTemplate);
		} else if ((count % 2 == 0) || (gameType == 1)) {
			player = 2; //  player.
			ibutton.setImageResource(crossTemplate);
		}

		// afterMove function to check the result and decide.
		afterMove(ibutton);
	}

	public void afterMove(ImageButton ib) {
		CharSequence positionStr = "";
		int pos = 0;
		boolean result = false;

		positionStr = (CharSequence) ib.getTag();
		pos = (int) positionStr.charAt(0) - 48;
		if (player == 1) {
			if (pos < 4)
				gameArray[0][pos - 1] = 1;
			else if (pos < 7)
				gameArray[1][(pos - 1) % 3] = 1;
			else if (pos < 10)
				gameArray[2][(pos - 1) % 3] = 1;
		} else {
			if (pos < 4)
				gameArray[0][pos - 1] = 2;
			else if (pos < 7)
				gameArray[1][(pos - 1) % 3] = 2;
			else if (pos < 10)
				gameArray[2][(pos - 1) % 3] = 2;
		}

		result = resuletCheck(player);

		if (result == true) {
			if (player == 1) {
				if (gameType == 0) {
					resuletMessage("Congrats.  wins !!");
				} else {
					resuletMessage("You Loose !!");
				}
			} else {
				if (gameType == 0) {
					resuletMessage("Congrats.  wins !!");
				} else {
					resuletMessage("Congrats. You Win !!");
				}
			}
			return;

		} else if ((result == false) && isFull()) {
			resuletMessage("    Game Draw !    ");
			return;
		}
		if ((gameType == 1) && (player == 2) && (result == false))
			runGame();
		
	}

	/**
	 * Shows the result of the game in a separate dialog.
	 */
	public boolean resuletMessage(CharSequence message) {		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Editor e = mSharedPreferences.edit();
						e.putBoolean("back", false);
						e.commit();

						finish();
						Intent intent = new Intent(MainGame.this,
								GameActivity.class);
						startActivity(intent);
					}
				});
		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
		return true;
	}

	/**
	 * Checks the result after each move.
	 * 
	 * @return True is any player has won.
	 */
	public boolean resuletCheck(int player_local) {
		boolean win = true;
		int k = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (gameArray[i][j] != player_local) { // check with player
														// number.
					win = false;
					break;
				}
			}
			if (win == true) {
				return true;
			}
			win = true;
		} // row loop.

		win = true; // resetting win to true.

		// checking for vertical condition only.
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (gameArray[j][i] != player_local) {
					win = false;
					break;
				}
			} // column loop.
			if (win == true) {
				return true;
			}
			win = true;
		} // row loop.

		win = true; // reset win to true.

		// check for diagonal condition 1.
		for (int i = 0; i < 3; i++)
			if (gameArray[i][k++] != player_local) {
				win = false;
				break;
			}

		if (win == true) {
			return true;
		}

		k = 2;
		win = true;

		for (int i = 0; i < 3; i++)
			if (gameArray[i][k--] != player_local) {
				win = false;
				break;
			}

		if (win == true) {
			return true;
		}

		return false;
	}

	/**
	 * Check the array 'gameArray' and returns the result.
	 * 
	 * @return True if array is full.
	 */
	public boolean isFull() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (gameArray[i][j] == 0)
					return false;
		return true;
	}

	public void runGame() {
		player = 1;
		count++;
		arrayCheck();
		if (moveWin() == true)
			return;
		else if (moveBlock() == true)
			return;
		else {
			getMap();
			move();
		}
	}

	public void arrayCheck() {

		for (int i = 0; i < 8; i++)
			finishedArray[i][0] = finishedArray[i][1] = 0;

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (gameArray[i][j] == 1) // 1 = player 1 : computer
					finishedArray[i][0] += 1;
				else if (gameArray[i][j] == 2) // 2 = player 2 : player
					finishedArray[i][1] += 1;

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (gameArray[j][i] == 1)
					finishedArray[i + 3][0] += 1;
				else if (gameArray[j][i] == 2)
					finishedArray[i + 3][1] += 1;
		int k = 0;
		for (int i = 0; i < 3; i++) {
			if (gameArray[i][k] == 1)
				finishedArray[6][0] += 1;
			else if (gameArray[i][k] == 2)
				finishedArray[6][1] += 1;
			k++;
		}

		k = 2;
		for (int i = 0; i < 3; i++) {
			if (gameArray[i][k] == 1)
				finishedArray[7][0] += 1;
			else if (gameArray[i][k] == 2)
				finishedArray[7][1] += 1;
			k--;
		}

	}

	public boolean moveWin() {
		boolean flag = false;
		int i, k = 0;
		for (i = 0; i < 8; i++)
			if ((finishedArray[i][0] == 2) && (finishedArray[i][1] == 0)) {
				flag = true;
				break;
			}

		if (flag == true) {
			if (i < 3) {
				for (int j = 0; j < 3; j++)
					if (gameArray[i][j] == 0) {
						play(i, j);
						return true;
					}
			} else if (i < 6) {
				for (int j = 0; j < 3; j++)
					if (gameArray[j][i - 3] == 0) {
						play(j, (i - 3));
						return true;
					}
			} else if (i == 6) {
				for (int j = 0; j < 3; j++) {
					if (gameArray[j][k] == 0) {
						play(j, k);
						return true;
					}
					k++;
				}
			} else if (i == 7) {
				k = 2;
				for (int j = 0; j < 3; j++) {
					if (gameArray[j][k] == 0) {
						play(j, k);
						return true;
					}
					k--;
				}
			}
		}
		return false;
	}

	public void play(int x, int y) {
		final ImageButton tempButon = (ImageButton) findViewById(R.id.btn1);
		int butId = tempButon.getId();
		if ((x == 0) && (y == 0)) {
		} else {
			if (x == 0)
				butId -= y;
			else if (x == 1)
				butId += (3 - y);
			else if (x == 2)
				butId += (6 - y);
		}
		final ImageButton ib = (ImageButton) findViewById(butId);

		ib.setImageResource(dotTemplate);

		ib.setClickable(false);

		afterMove(ib);
	}

	/**
	 * best move calculation : the f_e_map is traversed to see the highest
	 * numbered (x, y) position and the move is made.
	 */
	public void move() {
		int highest = 0, k = 0;
		int pos[][] = { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 },
				{ 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } };
		int random_index = 0;
		int x = 0, y = 0;

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (mapingArray[i][j] > highest)
					highest = mapingArray[i][j];

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (mapingArray[i][j] == highest) {
					pos[k][0] = i;
					pos[k][1] = j;
					k++;
				}

		// get a random index ( <= k ).
		random_index = ((int) (Math.random() * 10)) % (k);
		x = pos[random_index][0];
		y = pos[random_index][1];

		play(x, y);
	}

	public boolean moveBlock() {
		boolean flag = false;
		int i, k = 0;

		for (i = 0; i < 8; i++)
			if ((finishedArray[i][0] == 0) && (finishedArray[i][1] == 2)) {
				flag = true;
				break;
			}

		if (flag == true) {

			if (i < 3) {

				for (int j = 0; j < 3; j++)
					if (gameArray[i][j] == 0) {
						play(i, j);
						return true;
					}
			} else if (i < 6) {
				for (int j = 0; j < 3; j++)
					if (gameArray[j][i - 3] == 0) {
						play(j, (i - 3));
						return true;
					}
			} else if (i == 6) {
				for (int j = 0; j < 3; j++) {
					if (gameArray[j][k] == 0) {
						play(j, k);
						return true;
					}
					k++;
				}
			} else if (i == 7) {
				k = 2;
				for (int j = 0; j < 3; j++) {
					if (gameArray[j][k] == 0) {
						play(j, k);
						return true;
					}
					k--;
				}
			}
		}
		return false;
	}

	/**
	 * Creates a friend and enemy map, based on all available moves and the
	 * current position of the game.
	 */
	public void getMap() {
		int k = 0;

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				mapingArray[i][j] = 1;

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if ((gameArray[i][j] == 1) || (gameArray[i][j] == 2))
					mapingArray[i][j] = 0;

		for (int i = 0; i < 8; i++) {
			if (((finishedArray[i][0] == 1) && (finishedArray[i][1] == 0))
					|| ((finishedArray[i][0] == 0) && (finishedArray[i][1] == 1)))
				if (i < 3) {
					for (int j = 0; j < 3; j++)
						if (mapingArray[i][j] != 0)
							mapingArray[i][j] += 1;
				} else if (i < 6) {
					for (int j = 0; j < 3; j++)
						if (mapingArray[j][i - 3] != 0)
							mapingArray[j][i - 3] += 1;
				} else if (i == 6) {
					k = 0;
					for (int m = 0; m < 3; m++) {
						if (mapingArray[m][k] != 0)
							mapingArray[m][k] += 1;
						k++;
					}
				} else if (i == 7) {
					k = 2;
					for (int m = 0; m < 3; m++) {
						if (mapingArray[m][k] != 0)
							mapingArray[m][k] += 1;
						k--;
					}
				}
		}
	}

	public static String arrayToString(int[][] a) {
		String aString = "";
		int column;
		int row;

		for (row = 0; row < a.length; row++) {
			for (column = 0; column < a[0].length; column++) {
				if (aString.equals("")) {
					aString += a[row][column];
				} else {
					aString += "," + a[row][column];
				}
			}
		}

		return aString;
	}

	private int count = 0; // to count the number of moves made.
	private int player = 1; // sets the player no. to 1 by default.
	private int gameType = 1;// default 1 : 1 : h Vs Comp
	private int crossTemplate = R.drawable.red_3;
	private int dotTemplate = R.drawable.blue_3;
	private int gameArray[][] = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
	private int finishedArray[][] = { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 },
			{ 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }; // analysis_arr
	int mapingArray[][] = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
	private boolean soundEnabled = true;
	

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		int action = event.getAction();

		if (action == MotionEvent.ACTION_DOWN) {
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
			view.startDrag(null, shadowBuilder, view, 0);
			return true;
		}
		return false;
	}

	/**
	 * Register a drag event listener callback object for this View. The
	 * parameter is an implementation of
	 * {@link android.view.View.OnDragListener}.
	 */
	class GameDragListener implements OnDragListener {
		Drawable green = getResources().getDrawable(R.color.green);
		Drawable red = getResources().getDrawable(R.color.red);
		Drawable normalShape = getResources().getDrawable(
				R.drawable.default_button);

		@Override
		public boolean onDrag(View v, DragEvent event) {

			// Handles each of the expected events
			switch (event.getAction()) {

			// signal for the start of a drag and drop operation.
			case DragEvent.ACTION_DRAG_STARTED:
				// do nothing
				break;

			// the drag point has entered the bounding box of the View
			case DragEvent.ACTION_DRAG_ENTERED: // change the shape of the view
				if (v instanceof ImageButton) {
					ImageButton img = (ImageButton) v;
					if (img.isClickable()) {
						v.setBackground(green);
					} else {
						v.setBackground(red);
					}
				}
				break;

			// the user has moved the drag shadow outside the bounding box of
			// the View
			case DragEvent.ACTION_DRAG_EXITED:
				if (v instanceof ImageButton) {
					v.setBackground(normalShape); // change the shape of the
													// view back to normal
				}
				break;

			// drag shadow has been released,the drag point is within the
			// bounding box of the View
			case DragEvent.ACTION_DROP:
				// if the view is the bottomlinear, we accept the drag item

				if (v == b1) {
					if (b1.isClickable()) {
						onPress(b1);
					}
				} else if (v == b2) {
					if (b2.isClickable())
						onPress(b2);
				} else if (v == b3) {
					if (b3.isClickable())
						onPress(b3);
				} else if (v == b4) {
					if (b4.isClickable())
						onPress(b4);
				} else if (v == b5) {
					if (b5.isClickable())
						onPress(b5);
				} else if (v == b6) {
					if (b6.isClickable())
						onPress(b6);
				} else if (v == b7) {
					if (b7.isClickable())
						onPress(b7);
				} else if (v == b8) {
					if (b8.isClickable())
						onPress(b8);
				} else if (v == b9) {
					if (b9.isClickable())
						onPress(b9);
				} else {
					break;
				}
				break;

			// the drag and drop operation has concluded.
			case DragEvent.ACTION_DRAG_ENDED:
				if (v instanceof ImageButton) {
					v.setBackground(normalShape);
				}

			default:
				break;
			}
			return true;
		}
	}

}

package com.dellno1.picscramble;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.assent.Assent;
import com.afollestad.assent.AssentCallback;
import com.afollestad.assent.PermissionResultSet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Unbinder unbinder;
    List<ResponseObject.ItemObject> itemObjectList = new ArrayList<>();
    List<String> questionList = new ArrayList<>();
    public static boolean isGameStarted = false;
    private int questionPosition = 0;

    @BindView(R.id.timeCountDown)
    TextView timeCountDown;

    @BindView(R.id.gameLayout)
    LinearLayout gameLayout;

    @BindView(R.id.picLayout)
    LinearLayout picLayout;

    @BindView(R.id.view)
    View view;

    @BindView(R.id.imageView)
    ImageView imageView;

    GameHandler gameHandler;

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        checkAppPermissions();
    }

    private void checkAppPermissions() {
        Assent.setActivity(this, this);
        if (!Assent.isPermissionGranted(Assent.WRITE_EXTERNAL_STORAGE)) {
            Assent.requestPermissions(new AssentCallback() {
                @Override
                public void onPermissionResult(PermissionResultSet result) {
                    if (!result.isGranted(Assent.WRITE_EXTERNAL_STORAGE)) {
                        //noinspection ConstantConditions
                        Snackbar.make(findViewById(android.R.id.content), "App Needs Storage Permission. Please click 'Ask' to give permission.",
                                Snackbar.LENGTH_INDEFINITE).setAction("Ask", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkAppPermissions();
                            }
                        }).show();
                    } else {
                        showRules();
                    }
                }
            }, 69, Assent.WRITE_EXTERNAL_STORAGE);
        } else {
            showRules();
        }
    }

    private void showRules() {
        new AlertDialog.Builder(this)
                .setTitle("Rules")
                .setMessage("1. You will have 15 seconds to memorize the images.\n" +
                        "2. You will be given image to guess the position in grid.\n" +
                        "3. Game will be ended once all the images are guesses.\n" +
                        "4. You have maximum of 9 chances to guess for an image.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadPictures();
                    }
                }).create().show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Assent.setActivity(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing())
            Assent.setActivity(this, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Assent.handleResult(permissions, grantResults);
    }

    @SuppressWarnings("ConstantConditions")
    private void loadPictures() {
        picLayout.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.pic_scramble));
        progressDialog.setMessage(getString(R.string.loading_pictures));
        progressDialog.show();
        RestService.Service service = RestService.getRestService().create(RestService.Service.class);
        Call<ResponseObject> call = service.getPictures();
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    ResponseObject responseObject = response.body();
                    if (responseObject.getItems().size() > 9) {
                        for (int i = 0; i < 9; i++) {
                            responseObject.getItems().get(i).setFlipped(true);
                            itemObjectList.add(responseObject.getItems().get(i));
                            questionList.add(responseObject.getItems().get(i).getMedia().getM());
                        }
                    }
                    Collections.shuffle(itemObjectList);
                    Collections.shuffle(questionList);
                    progressDialog.dismiss();
                    setGameLayout();
                    startTimer();
                } else {
                    progressDialog.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), R.string.something_went_wrong,
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), t.getMessage(),
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setGameLayout() {
        gameHandler = new GameHandler(this, itemObjectList, gameLayout, new OnAnswered() {
            @Override
            public void answer() {
                if (questionPosition < 9) {
                    //noinspection ConstantConditions
                    Snackbar.make(findViewById(android.R.id.content), "You answered it right. Here is next Image.",
                            Snackbar.LENGTH_LONG).show();
                    showImage();
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Congrats")
                            .setMessage("You finished game.")
                            .setCancelable(false)
                            .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    itemObjectList.clear();
                                    questionList.clear();
                                    questionPosition = 0;
                                    loadPictures();
                                }
                            }).create().show();
                }
            }
        });
        gameHandler.build();
    }

    private void startTimer() {
        timeCountDown.setVisibility(View.VISIBLE);
        isGameStarted = false;
        // countdown timer for 15 seconds to start game.
        new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                timeCountDown.setText(String.format(Locale.getDefault(), "Game will start in %d Seconds", millisUntilFinished / 1000));
            }

            public void onFinish() {
                isGameStarted = true;
                timeCountDown.setVisibility(View.GONE);
                startGame();
            }
        }.start();
    }

    private void startGame() {
        view.setVisibility(View.GONE);
        picLayout.setVisibility(View.VISIBLE);
        showImage();
    }

    private void showImage() {
        for (ResponseObject.ItemObject itemObject : itemObjectList) {
            itemObject.setFlipped(false);
        }
        gameHandler.reset();
        Picasso.with(this)
                .load(questionList.get(questionPosition))
                .resize((int) getResources().getDimension(R.dimen._300sdp),
                        (int) getResources().getDimension(R.dimen._300sdp))
                .centerCrop()
                .into(imageView);
        gameHandler.question(questionList.get(questionPosition));
        questionPosition++;
    }

    public interface OnAnswered {
        void answer();
    }
}

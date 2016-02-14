package com.ckc.ersin.bebetrack;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goebl.david.Webb;
import com.goebl.david.WebbException;

import org.json.JSONException;
import org.json.JSONObject;

class AccountManager {
    String pin;
    String token;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
public class MainActivity extends AppCompatActivity {

    AccountManager accountManager;

    EditText phoneField;
    EditText internationalCodeField;

    TextView pinTextView;
    TextView tokenTextView;

    Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        pinTextView = (TextView) findViewById(R.id.textView2);
        tokenTextView = (TextView) findViewById(R.id.textView6);
        phoneField = (EditText) findViewById(R.id.editText);
        internationalCodeField = (EditText) findViewById(R.id.editText2);

        accountManager = new AccountManager();
        testButton = (Button) findViewById(R.id.button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String urlString = "http://bebetrack.com/api/create";
                        try {
                            testButton.setEnabled(false);
                            String internationalCode = "01";
                            String phone = "1234567890";
                            if (phoneField != null) {
                                phone = phoneField.getText().toString();
                            }
                            if (internationalCodeField != null) {
                                internationalCode = internationalCodeField.getText().toString();;
                            }
                            JSONObject params = new JSONObject();
                            params.put("phone", phone);
                            params.put("internationalCode", internationalCode);
                            JSONObject response = Webb.create().post(urlString)
                                    .useCaches(false)
                                    .header(Webb.HDR_CONTENT_TYPE, Webb.APP_JSON)
                                    .body(params)
                                    .connectTimeout(30000)
                                    .ensureSuccess()
                                    .asJsonObject()
                                    .getBody();
                            Log.w("response", response.toString());
                            accountManager.setPin(response.get("pin").toString());
                            accountManager.setToken(response.get("token").toString());
                            setResultText();
                        } catch (JSONException e) {
                            setErrorText(e.getMessage());
                            e.printStackTrace();
                        } catch (WebbException e) {
                            setErrorText(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }).start();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onTestButtonClicked() {
        //
    }

    protected void setResultText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                testButton.setEnabled(true);
                pinTextView.setText(accountManager.getPin());
                tokenTextView.setText(accountManager.getToken());
            }
        });
    }

    protected void setErrorText(final String errorText) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                testButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), errorText, Toast.LENGTH_LONG).show();
            }
        });
    }
}

package edu.usc.eventme;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class BottomsheetDetails extends AppCompatActivity {

    private Button back;
    private Button register;
    private TextView eventName;
    private TextView location;
    private TextView date;
    private TextView time;
    private TextView cost;
    private TextView sponser;
    private TextView parking;
    private TextView eventType;
    private TextView description;
    private TextView numUser;
    private ImageView photo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        EventList results = (EventList) getIntent().getSerializableExtra("Events");
        int p = getIntent().getIntExtra("position",0);
        Event ev = results.getEventList().get(p);
        eventName = findViewById(R.id.eName);
        eventName.setText(ev.getEventTitle());
        location = findViewById(R.id.eLocation);
        location.setText("Location: "+ev.getLocation());
        date = findViewById(R.id.eDate);
        date.setText("Date: "+ev.getStartDate()+" to "+ev.getEndDate());
        time = findViewById(R.id.eTime);
        time.setText(ev.getStartTime()+" to "+ev.getEndTime());
        cost = findViewById(R.id.eCost);
        cost.setText("Cost: "+ev.getCost());
        sponser = findViewById(R.id.sponceringOrganization);
        sponser.setText(ev.getSponsoringOrganization());
        parking = findViewById(R.id.parking);
        if(ev.getParking())
            parking.setText("Parking Available: Yes");
        else
            parking.setText("Parking Available: No");
        eventType = findViewById(R.id.eType);
        eventType.setText("Category: "+ev.getCategory());
        description = findViewById(R.id.description);
        description.setText("Description: "+ev.getDescription());
        numUser = findViewById(R.id.numUser);
        numUser.setText("Number of Registered Users: "+String.valueOf(ev.getNumUser()));
        photo = findViewById(R.id.eImage);
        Picasso.get().load(ev.getPhotoURL()).resize(240,130).into(photo);

        back = findViewById(R.id.backToResult);
        Intent toResults = new Intent(this,EventBoxes.class);
        toResults.putExtra("searchResult",results);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String conflicted = checkConflict(ev);
                if(conflicted!="")
                {
                    LayoutInflater inflater = (LayoutInflater)
                            getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window tolken
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    Button no = findViewById(R.id.no);
                    Button yes = findViewById(R.id.yes);
                    // dismiss the popup window when touched
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                    yes.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            ev.addUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            int users = ev.getNumUser();
                            db.collection("events").document(ev.getId()).update("numUser",users);
                            userRegisterUpdate();
                            showMessage("Register Successfully!");
                        }
                    });
                }
                else{
                    ev.addUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    int users = ev.getNumUser();
                    db.collection("events").document(ev.getId()).update("numUser",users);
                    userRegisterUpdate();
                    showMessage("Register Successfully!");
                }

            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private String checkConflict(Event ev){
        String title="";
        return title;
    }

    private void userRegisterUpdate(){

    }
}

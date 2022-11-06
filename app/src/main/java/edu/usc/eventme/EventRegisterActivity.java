package edu.usc.eventme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class EventRegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private ImageView eventPhoto, backButton;
    private Button registerButton;
    private Toolbar toolbar;
    private TextView eventName, numRegistered, costNType, location, date, time;
    private TextView eventOrganization, eventDescription, eventParking, conflictMessage;
    private ProgressBar eventProgressBar;

    private String eventID;
    private Event event;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        EventList results = (EventList) getIntent().getSerializableExtra("Events");
        int p = getIntent().getIntExtra("position",0);
        event = results.getEventList().get(p);
        eventID = event.getId();

        toolbar = findViewById(R.id.toolbar);
        eventPhoto = findViewById(R.id.eventPhoto);
        registerButton = findViewById(R.id.eventRegisterButton);
        eventName = findViewById(R.id.eventName);
        numRegistered = findViewById(R.id.numRegistered);
        costNType = findViewById(R.id.costNType);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        eventOrganization = findViewById(R.id.eventOrgnization);
        eventDescription = findViewById(R.id.eventDescription);
        eventParking = findViewById(R.id.eventParking);
        conflictMessage = findViewById(R.id.conflictMessage);
        time = findViewById(R.id.time);
        eventProgressBar = findViewById(R.id.eventProgressBar);
        backButton = findViewById(R.id.backButton);

        getUser();
        //getEvent(eventID);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUser();
        //getEvent(eventID);
    }

//    private boolean getEvent(String eventID) {
//        DocumentReference docRef = db.collection("events").document(eventID);
//        if (!eventID.isEmpty()) {
//            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    event = documentSnapshot.toObject(Event.class);
//                    if (user != null) {
//                        updateUI();
//                    }
//                }
//
////                @Override
////                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                    if (task.isSuccessful()) {
////                        DocumentSnapshot document = task.getResult();
////                        if (document.exists()) {
////                            event = document.toObject(Event.class);
////                        } else {
////                            Log.i("Activity", "not ready");
////                        }
////                    } else {
////                        showMessage(task.getException().getMessage());
////                    }
////                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    showMessage(e.getMessage());
//                }
//            });
//            if (event != null) {
//                return true;
//            }
//        } else {
//            return false;
//        }
//        return false;
//    }

    private boolean getUser() {
        if (currentUser != null) {
            DocumentReference docRef = db.collection("users").document(currentUser.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user = documentSnapshot.toObject(User.class);
                    if (event != null) {
                        //eventProgressBar.setVisibility(View.GONE);
                        updateUI();
                    }
                }
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            user = document.toObject(User.class);
//                        } else {
//                            Log.i("Activity", "not ready");
//                        }
//                    } else {
//                        showMessage(task.getException().getMessage());
//                    }
//                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showMessage(e.getMessage());
                }
            });
            if (user != null) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    private void updateUI() {
        eventProgressBar.setVisibility(View.GONE);
        eventName.setText(event.getEventTitle());
        eventOrganization.setText(event.getSponsoringOrganization());
        costNType.setText(event.getCost()+" · " + event.getCategory());
        location.setText(event.getLocation());
        date.setText(event.getStartDate()+" to "+ event.getEndDate());
        time.setText(event.getStartTime()+" to "+event.getEndTime());
        numRegistered.setText(event.getNumUser()+" People Registered");
        eventDescription.setText(event.getDescription());
        Picasso.get().load(event.getPhotoURL()).fit().centerCrop().into(eventPhoto);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(event.getParking()) {
            eventParking.setText("There are parking lots near the event");
        } else {
            eventParking.setText("No parking lot available near the Event. Please take public transportation.");
        }

        if (user != null) {
            if (user.eventExist(eventID)) {
                updateToUnregister();
            } else {
                updateToRegister();
            }
        } else {
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mainActivity = new Intent(view.getContext(), LoginActivity.class);
                    startActivity(mainActivity);
                }
            });
        }
    }

    private void updateToRegister() {
        registerButton.setText("Register");
        registerButton.setBackgroundColor(this.getResources().getColor(R.color.orange_theme, this.getTheme()));
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventRegister();
            }
        });
    }

    private void updateToUnregister() {
        registerButton.setText("Unregister");
        registerButton.setBackgroundColor(this.getResources().getColor(R.color.red_theme, this.getTheme()));
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventUnregister();
            }
        });
        //showMessage("exist already");
    }

    private void eventRegister() {
        eventProgressBar.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);
        event.addUser();
        DocumentReference docRef = db.collection("events").document(eventID);
        docRef.update("numUser", event.getNumUser()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                numRegistered.setText(event.getNumUser()+" People Registered");
                user.registerEvent(event);
                updateUser();
                updateToUnregister();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventProgressBar.setVisibility(View.GONE);
                registerButton.setEnabled(true);
                showMessage("update in server failed, please try again");
                user.removeEvent(event);
                event.lessUser();
            }
        });
    }

    private void eventUnregister() {
        eventProgressBar.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);
        event.lessUser();
        DocumentReference docRef = db.collection("events").document(eventID);
        docRef.update("numUser", event.getNumUser()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                numRegistered.setText(event.getNumUser()+" People Registered");
                user.removeEvent(event);
                updateUser();
                updateToRegister();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventProgressBar.setVisibility(View.GONE);
                registerButton.setEnabled(true);
                showMessage("update in server failed, please try again");
                user.registerEvent(event);
                event.addUser();
            }
        });
    }

    private void updateUser() {
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.update("userEventList", user.getUserEventList()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                eventProgressBar.setVisibility(View.GONE);
                registerButton.setEnabled(true);
                showMessage("Succeed");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventProgressBar.setVisibility(View.GONE);
                registerButton.setEnabled(true);
                showMessage("update in server failed, please try again");
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
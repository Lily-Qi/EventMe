package edu.usc.eventme;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ImageView userPhoto;
    private TextView userName, userEmail, userBirthday;
    private Button logOutBtn, showEventButton;
    private FirebaseFirestore db;
    ExploreFragment exploreFragment = new ExploreFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                return;
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


    @Override
    public void onResume() {
        super.onResume();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        userName = profileView.findViewById(R.id.userName);
        userBirthday = profileView.findViewById(R.id.userBirthday);
        userEmail = profileView.findViewById(R.id.userEmail);
        userPhoto = profileView.findViewById(R.id.userPhoto);
        logOutBtn = profileView.findViewById(R.id.logoutButton);
        showEventButton = profileView.findViewById(R.id.showEventButton);
        currentUser = mAuth.getCurrentUser();


        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    DocumentReference docRef = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    User user = document.toObject(User.class);
                                    userBirthday.setText(user.getUserBirthday());
                                    userName.setText(user.getUserName());
                                    userEmail.setText(user.getUserEmail());
                                    Picasso.get().load(user.getUserPhoto()).into(userPhoto);
                                    logOutBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent mainActivity = new Intent(getContext(), LoginActivity.class);
                                            mAuth.signOut();
                                            startActivity(mainActivity);
                                        }
                                    });
                                    showEventButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent mainActivity = new Intent(getContext(), EventRegisterActivity.class);
                                            startActivity(mainActivity);
                                        }
                                    });
                                } else {
                                    Log.i("Activity", "not ready");
                                }
                            } else {
                                showMessage(task.getException().getMessage());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage(e.getMessage());
                        }
                    });

                } else {
                    userPhoto.getLayoutParams().width = 1;
                    userPhoto.setVisibility(View.INVISIBLE);
                    profileView.findViewById(R.id.eventView).getLayoutParams().height = 0;
                    profileView.findViewById(R.id.breaker2).getLayoutParams().height = 0;
                    userName.setText("Log in\nRegister events you like");
                    userName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    userBirthday.setText("");
                    userEmail.setText("");
                    logOutBtn.setText("Log In");
                    logOutBtn.setBackgroundColor(getActivity().getResources().getColor(R.color.orange_theme, getContext().getTheme()));
                    logOutBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent mainActivity = new Intent(getContext(), LoginActivity.class);
                            startActivity(mainActivity);
                        }
                    });
                }
            }
        });
        return profileView;
    }

    //show message
    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }



}
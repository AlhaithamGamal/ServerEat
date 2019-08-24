package com.example.servereat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servereat.common.Common;
import com.example.servereat.holders.FoodViewHolder;
import com.example.servereat.interfaces.ItemClickListener;
import com.example.servereat.models.Category;
import com.example.servereat.models.Food;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FoodActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManger;
    FloatingActionButton fab;
    FirebaseDatabase db;
    DatabaseReference foodRef;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseRecyclerAdapter searchAdapter;
    Food newFood;
    EditText nameFood, descFood, priceFood, discountFood;
    Button uploadFoodB, selectFoodB;
    RelativeLayout rootLayout;
    Uri saveUri;

    String categoryId = "";
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    List<String> suggestLis = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        db = FirebaseDatabase.getInstance();
        foodRef = db.getReference("Food");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManger = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManger);
        rootLayout = findViewById(R.id.rootelement);
        fab = findViewById(R.id.btnCart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFoodDialog();
            }
        });

        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");

        }
        if (!categoryId.isEmpty()) {
            loadListFood(categoryId);
        }
        materialSearchBar = findViewById(R.id.search_bar);
        materialSearchBar.setHint("Enter your food....");
        // materialSearchBar.setSpeechMode(false);
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestLis);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<String>();
                for (String search : suggestLis) {

                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled) {
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }


    private void loadSuggest() {

        foodRef.orderByChild("menuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Food item = ds.getValue(Food.class);
                            suggestLis.add(item.getName());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void showAddFoodDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodActivity.this);
        alertDialog.setTitle("Add New Food");
        alertDialog.setMessage("Please fill the following information");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_food_layout = inflater.inflate(R.layout.add_food_layout, null);
        uploadFoodB = add_food_layout.findViewById(R.id.btnUpload);
        selectFoodB = add_food_layout.findViewById(R.id.btnSselect);
        nameFood = add_food_layout.findViewById(R.id.edt_name_food);
        descFood = add_food_layout.findViewById(R.id.edt_name_description);
        discountFood = add_food_layout.findViewById(R.id.edt_discount_food);
        priceFood = add_food_layout.findViewById(R.id.edt_price_food);
        alertDialog.setView(add_food_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        uploadFoodB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameFood.getText().length() == 0 || descFood.getText().length() == 0 || priceFood.getText().length() == 0 || discountFood.getText().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Please complete enetering data", Toast.LENGTH_LONG).show();

                } else
                    uploadImage();

            }
        });
        selectFoodB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chooseImage();

            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (newFood != null) {
                    foodRef.push().setValue(newFood);
                    Snackbar.make(rootLayout, "New food" + newFood.getName() + "was added", Snackbar.LENGTH_SHORT)
                            .show();
                    loadSuggest();
                } else {
                    Snackbar.make(rootLayout, "CHECK FOR ENTERING DATA!!", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        alertDialog.show();

    }

    private void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>
                (Food.class, R.layout.food_item, FoodViewHolder.class, foodRef.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {

                foodViewHolder.food_name.setText(food.getName());
                Picasso.get().load(food.getImage())
                        .into(foodViewHolder.food_image);
//               foodViewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//
//                    }
//                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void uploadImage() {
        if (saveUri != null) {
            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageRef.child("images/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded!!!", Toast.LENGTH_LONG).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newFood = new Food();
                                    newFood.setName(nameFood.getText().toString());
                                    newFood.setDescription(descFood.getText().toString());
                                    newFood.setDiscount(discountFood.getText().toString());
                                    newFood.setPrice(priceFood.getText().toString());
                                    newFood.setMenuId(categoryId);
                                    newFood.setImage(uri.toString());

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed Uploading!!!" + e.getMessage(), Toast.LENGTH_LONG).show();


                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mProgressDialog.setMessage("Uploaded" + progress + "%");
                }
            });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Common.PICK_IMAGE_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            saveUri = data.getData();
            selectFoodB.setText("Image selected");


        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if ((item.getTitle().equals(Common.UPDATE))) {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {
            deleteDialog(adapter.getRef(item.getOrder()).getKey());


        }
        return super.onContextItemSelected(item);

    }

    private void deleteDialog(String key) {
        foodRef.child(key).removeValue();
        Toast.makeText(getApplicationContext(), "Item deleted !!!", Toast.LENGTH_LONG).show();
        loadSuggest(); //for searching

    }

    private void showUpdateDialog(final String key, final Food item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodActivity.this);
        alertDialog.setTitle("Update Category");
        alertDialog.setMessage("Please fill the following information");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_food_layout = inflater.inflate(R.layout.add_food_layout, null);
        uploadFoodB = add_food_layout.findViewById(R.id.btnUpload);
        selectFoodB = add_food_layout.findViewById(R.id.btnSselect);
        nameFood = add_food_layout.findViewById(R.id.edt_name_food);
        priceFood = add_food_layout.findViewById(R.id.edt_price_food);
        descFood = add_food_layout.findViewById(R.id.edt_name_description);
        discountFood = add_food_layout.findViewById(R.id.edt_discount_food);
        nameFood.setText(item.getName());
        priceFood.setText(item.getPrice());
        discountFood.setText(item.getDiscount());
        descFood.setText(item.getDescription());
        alertDialog.setView(add_food_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        uploadFoodB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameFood.getText().length() == 0 || descFood.getText().length() == 0 || priceFood.getText().length() == 0 || discountFood.getText().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Please complete enetering data", Toast.LENGTH_LONG).show();

                } else

                    changeImage(item);

            }
        });
        selectFoodB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();

            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setName(nameFood.getText().toString());
                item.setDescription(descFood.getText().toString());
                item.setPrice(priceFood.getText().toString());
                item.setDiscount(discountFood.getText().toString());
                foodRef.child(key).setValue(item);
                loadSuggest();
                Snackbar.make(rootLayout, "New food" + newFood.getName() + "was edited", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        alertDialog.show();

    }

    private void changeImage(final Food item) {
        if (saveUri != null) {
            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageRef.child("images/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded!!!", Toast.LENGTH_LONG).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    item.setImage(uri.toString());

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed Uploading!!!" + e.getMessage(), Toast.LENGTH_LONG).show();


                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mProgressDialog.setMessage("Uploaded" + progress + "%");
                }
            });
        }


    }

    //-------------search bar
    private void startSearch(CharSequence text) {

        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item, FoodViewHolder.class, foodRef.orderByChild("name").equalTo(text.toString())) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.food_name.setText(food.getName());
                Picasso.get().load(food.getImage())
                        .into(foodViewHolder.food_image);
                final Food local = food;



            }

        };
        recyclerView.setAdapter(searchAdapter);
    }
}

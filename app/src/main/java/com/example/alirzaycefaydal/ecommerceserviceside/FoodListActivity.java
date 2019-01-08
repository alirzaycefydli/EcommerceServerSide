package com.example.alirzaycefaydal.ecommerceserviceside;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alirzaycefaydal.ecommerceserviceside.Interface.ItemClickListener;
import com.example.alirzaycefaydal.ecommerceserviceside.Models.Category;
import com.example.alirzaycefaydal.ecommerceserviceside.Models.Food;
import com.example.alirzaycefaydal.ecommerceserviceside.ViewHolder.FoodViewHolder;
import com.example.alirzaycefaydal.ecommerceserviceside.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class FoodListActivity extends AppCompatActivity {

    //widgets
    private RecyclerView recyclerView;
    private FloatingActionButton fab;


    private EditText add_food_name, add_food_description, add_food_price, add_food_discount;
    private Button select_food_button, add_food_button;

    //vars
    private DatabaseReference foodRef;
    private StorageReference storageReference;
    private String categoryId = "";
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    private static final int REQUEST_IMAGE_PICK = 123;
    private Uri saveUri;
    private Food newFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        foodRef = FirebaseDatabase.getInstance().getReference().child("Foods");
        storageReference = FirebaseStorage.getInstance().getReference();

        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        fab = findViewById(R.id.fab_food);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddFoodDialog();
            }
        });

        if (!categoryId.isEmpty()) {
            loadListFood(categoryId);
        }
    }

    private void showAddFoodDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(FoodListActivity.this);
        dialog.setTitle("Add new Food");
        dialog.setMessage("Please fill up");

        View view = LayoutInflater.from(FoodListActivity.this).inflate(R.layout.add_new_food_layout, null);

        select_food_button = view.findViewById(R.id.new_food_select);
        add_food_button = view.findViewById(R.id.new_food_update);
        add_food_name = view.findViewById(R.id.new_food_name);
        add_food_description = view.findViewById(R.id.new_food_description);
        add_food_discount = view.findViewById(R.id.new_food_discount);
        add_food_price = view.findViewById(R.id.new_food_price);


        select_food_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        add_food_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        dialog.setView(view);
        dialog.setIcon(R.drawable.ic_cart);


        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (newFood != null) {
                    foodRef.push().setValue(newFood);
                }
            }
        });

        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        dialog.show();


    }

    private void loadListFood(String categoryId) {

        Query categoryQuery = foodRef.getRef();


        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(categoryQuery, Food.class)
                .setLifecycleOwner(this)
                .build();

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {

                holder.txtMenuName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.foodImageView);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);

                return new FoodViewHolder(view);
            }
        };

    }

    private void chooseImage() {
        askPermission();
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(FoodListActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(FoodListActivity.this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }
    }


    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(FoodListActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(FoodListActivity.this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(FoodListActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_PICK);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_PICK);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_IMAGE_PICK) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), REQUEST_IMAGE_PICK);

            } else {
                Toast.makeText(FoodListActivity.this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {


            saveUri = data.getData();
            select_food_button.setText("Image selected");

        }
    }

    private void uploadImage() {
        if (saveUri != null) {

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            newFood = new Food();
                            newFood.setName(add_food_name.getText().toString());
                            newFood.setImage(uri.toString());
                            newFood.setDescription(add_food_description.getText().toString());
                            newFood.setDiscount(add_food_discount.getText().toString());
                            newFood.setPrice(add_food_price.getText().toString());
                            newFood.setName(add_food_name.getText().toString());

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(FoodListActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(HomeActivity.UPDATE)) {

            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));

        } else if (item.getTitle().equals(HomeActivity.DELETE)) {

            showDeleteDialog(adapter.getRef(item.getOrder()).getKey());

        }
        return super.onContextItemSelected(item);
    }


    private void showDeleteDialog(String key) {

        foodRef.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(FoodListActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FoodListActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showUpdateDialog(final String key, final Food item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(FoodListActivity.this);
        dialog.setTitle("Edit Food");
        dialog.setMessage("Please fill up");

        View view = LayoutInflater.from(FoodListActivity.this).inflate(R.layout.add_new_food_layout, null);

        select_food_button = view.findViewById(R.id.new_food_select);
        add_food_button = view.findViewById(R.id.new_food_update);
        add_food_name = view.findViewById(R.id.new_food_name);
        add_food_description = view.findViewById(R.id.new_food_description);
        add_food_discount = view.findViewById(R.id.new_food_discount);
        add_food_price = view.findViewById(R.id.new_food_price);

        add_food_name.setText(item.getName());
        add_food_description.setText(item.getDescription());
        add_food_discount.setText(item.getDiscount());
        add_food_price.setText(item.getPrice());

        select_food_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        add_food_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        dialog.setView(view);
        dialog.setIcon(R.drawable.ic_cart);


        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (newFood != null) {

                    item.setName(add_food_name.getText().toString());
                    item.setDescription(add_food_description.getText().toString());
                    item.setPrice(add_food_price.getText().toString());
                    item.setDiscount(add_food_discount.getText().toString());
                    foodRef.child(key).setValue(item);
                }
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void changeImage(final Food item) {

        if (saveUri != null) {

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

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

                    Toast.makeText(FoodListActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

package com.swgroup.alexandria;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;
import com.swgroup.alexandria.data.database.ShelfEntry;
import com.swgroup.alexandria.data.internal.DataScratcher;
import com.swgroup.alexandria.data.internal.FileUtil;
import com.swgroup.alexandria.databinding.ActivityMainBinding;
import com.swgroup.alexandria.ui.ShelfViewModel;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    //insecure state
    private ShelfViewModel shelfViewModel;
    //end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //PERMISSION REQUEST
        ActivityResultLauncher<String> requestPermissionReadExt =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (!isGranted) {
                        Toast.makeText(this, R.string.ToastMsgStoragePermissionDenied, Toast.LENGTH_LONG).show();
                    }
                });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionReadExt.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        ActivityResultLauncher<String> requestPermissionWriteExt =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (!isGranted) {
                        Toast.makeText(this, R.string.ToastMsgStoragePermissionDenied, Toast.LENGTH_LONG).show();
                    }
                });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionWriteExt.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        //END PERMISSION REQUEST

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_favorites, R.id.navigation_books, R.id.navigation_audiobooks, R.id.navigation_comics)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.fabAddEntry.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/epub+zip");
            startActivityForResult(intent, 1);
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.titlebar_menu, menu);
        return true;
    }

    public void onActivityResult(int requestcode, int resulCode, Intent data) {
        super.onActivityResult(requestcode,resulCode,data);
        if (resulCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            } Uri uri = data.getData();
            System.out.println(uri.toString());
            try {
                File inputFile = FileUtil.from(MainActivity.this, uri);
                //Log.d("file", "File...:::: uti - "+inputFile .getPath()+" file -" + inputFile + " : " + inputFile .exists());
                FileUtil.writeToSDFile( MainActivity.this, inputFile);
                //data scratching
                ShelfEntry shelfEntry = new ShelfEntry();
                DataScratcher.getMetaDataFromEpub(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/Alexandria/", inputFile.getName(),shelfEntry);
                // end
                // rename inputfile as title of the book
                //String [] parts = inputFile.getName().split("\\.");
                //inputFile.renameTo(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/Alexandria/"+shelfEntry.title+"."+parts[parts.length-1]));
                //end
                //insecure state
                shelfViewModel = new ViewModelProvider(this).get(ShelfViewModel.class);
                shelfViewModel.insert(shelfEntry);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
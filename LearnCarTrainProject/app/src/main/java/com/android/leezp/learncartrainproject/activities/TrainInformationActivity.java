package com.android.leezp.learncartrainproject.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.leezp.learncartrainproject.GlideApp;
import com.android.leezp.learncartrainproject.R;
import com.android.leezp.learncartrainproject.entities.DriverEntity;
import com.android.leezp.learncartrainproject.interfaces.NetEvent;
import com.android.leezp.learncartrainproject.net.data.NetDriverData;
import com.android.leezp.learncartrainproject.presenter.TrainInformationPresenter;
import com.android.leezp.learncartrainproject.utils.ProjectConstant;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TrainInformationActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, AMapLocationListener, NetEvent {
    private ImageView back;
    private CircleImageView head;
    private EditText name;
    private RadioGroup sexGroup;
    private TextView evaluate;
    private EditText unitPriceDetails;
    private TextView taughtPeople;
    private TextView phone;
    private EditText identify;
    private TextView place;
    private EditText information;
    private ImageView identifyImage;
    private ImageView trainerImage;
    private TextView managerBtn;
    private LinearLayout chooseImageWays;
    private TextView camera;
    private TextView photos;
    private LinearLayout loading;
    private TrainInformationPresenter presenter;
    private DriverEntity driverEntity;
    private boolean fromLoginOrRegister;
    private float startX;
    private float startY;
    private int imageType = 0;
    private String imageAbsolutePath;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_information);
        back = findViewById(R.id.activity_train_information_back);
        head = findViewById(R.id.activity_train_information_head);
        name = findViewById(R.id.activity_train_information_name);
        sexGroup = findViewById(R.id.activity_train_information_sex_group);
        evaluate = findViewById(R.id.activity_train_information_evaluate);
        unitPriceDetails = findViewById(R.id.activity_train_information_unit_price_details);
        taughtPeople = findViewById(R.id.activity_train_information_taught_people);
        phone = findViewById(R.id.activity_train_information_phone);
        identify = findViewById(R.id.activity_train_information_identify);
        place = findViewById(R.id.activity_train_information_place);
        information = findViewById(R.id.activity_train_information_information);
        identifyImage = findViewById(R.id.activity_train_information_identify_image);
        trainerImage = findViewById(R.id.activity_train_information_trainer_image);
        managerBtn = findViewById(R.id.activity_train_information_manager_btn);
        chooseImageWays = findViewById(R.id.activity_train_information_choose_image_ways);
        camera = findViewById(R.id.activity_train_information_camera);
        photos = findViewById(R.id.activity_train_information_photos);
        loading = findViewById(R.id.activity_train_information_loading);

        presenter = new TrainInformationPresenter();
        presenter.onCreate();
        presenter.attachEvent(this);

        locationClient = new AMapLocationClient(getApplicationContext());
        locationClient.setLocationListener(this);
        locationOption = new AMapLocationClientOption();
        locationOption.setOnceLocation(true);
        locationOption.setOnceLocationLatest(true);


        Intent intent = getIntent();
        loadData(intent);

        back.setOnClickListener(this);
        head.setOnClickListener(this);
        identifyImage.setOnClickListener(this);
        trainerImage.setOnClickListener(this);
        chooseImageWays.setOnTouchListener(this);
        camera.setOnClickListener(this);
        photos.setOnClickListener(this);
        place.setOnClickListener(this);
        managerBtn.setOnClickListener(this);
    }

    private void loadData(Intent intent) {
        Serializable serializable = intent.getSerializableExtra("driver");
        if (serializable == null) {
            showMessage("错误打开本页面，请重新启动本应用！");
            return;
        }
        driverEntity = ((DriverEntity) serializable);
        phone.setText(driverEntity.getPhone());
        //以下需根据是否来自注册页来初始化界面信息
        fromLoginOrRegister = intent.getBooleanExtra("fromLoginOrRegister", false);
        if (fromLoginOrRegister) {
            managerBtn.setText("上传");
        } else {
            loadNetImage(ProjectConstant.SERVER_ADDRESS + driverEntity.getHead_url(), R.drawable.activity_train_information_default_head, head);
            loadNetImage(ProjectConstant.SERVER_ADDRESS + driverEntity.getIdentify_image_url(), R.drawable.activity_train_information_default_identify_image, identifyImage);
            loadNetImage(ProjectConstant.SERVER_ADDRESS + driverEntity.getTrainer_image_url(), R.drawable.activity_train_information_default_trainer_image, trainerImage);
            name.setText(driverEntity.getName());
            ((RadioButton) sexGroup.getChildAt(driverEntity.getSex())).setChecked(true);
            evaluate.setText("评分：" + driverEntity.getEvaluate());
            unitPriceDetails.setText(String.format("%.2f", driverEntity.getUnit_price()));
            taughtPeople.setText("已教人数：" + driverEntity.getTaught_people());
            identify.setText(driverEntity.getIdentify_number());
            place.setText(driverEntity.getPlace());
            place.setTextColor(getResources().getColor(R.color.color_train_information_text_color));
            information.setText(driverEntity.getInformation());
            managerBtn.setText("更新");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public static void startTrainInformationActivity(Activity activity, DriverEntity driverEntity, boolean fromLoginOrRegister) {
        Intent intent = new Intent(activity, TrainInformationActivity.class);
        intent.putExtra("driver", driverEntity);
        intent.putExtra("fromLoginOrRegister", fromLoginOrRegister);
        activity.startActivity(intent);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载服务器上的图片
     *
     * @param url        **图片地址**
     * @param errorResId **加载失败显示图片**
     * @param view       **加载的图片显示在具体的控件上**
     */
    private void loadNetImage(String url, int errorResId, ImageView view) {
        GlideApp.with(this)
                .load(url)
                .placeholder(R.drawable.image_loading_black)
                .error(errorResId)
                .into(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_train_information_back:
                if (fromLoginOrRegister) {
                    showMessage("请完善个人信息，否则无法正确使用本系统！");
                } else {
                    HomePagerActivity.startHomePager(this, driverEntity, false);
                }
                break;
            case R.id.activity_train_information_head:
                showChooseImageWays();
                imageType = 0;
                break;
            case R.id.activity_train_information_identify_image:
                showChooseImageWays();
                imageType = 1;
                break;
            case R.id.activity_train_information_trainer_image:
                showChooseImageWays();
                imageType = 2;
                break;
            case R.id.activity_train_information_camera:
                chooseImageWay(v);
                break;
            case R.id.activity_train_information_photos:
                chooseImageWay(v);
                break;
            case R.id.activity_train_information_place:
                locationClient.setLocationOption(locationOption);
                locationClient.startLocation();
                loading.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_train_information_manager_btn:
                if (name.getText().toString() == null || name.getText().toString().equals("")) {
                    showMessage("姓名未填写！");
                    return;
                }
                if (unitPriceDetails.getText().toString() == null || unitPriceDetails.getText().toString().equals("")) {
                    showMessage("单位价未填写！");
                    return;
                }
                if (identify.getText().toString() == null || identify.getText().toString().equals("")) {
                    showMessage("身份证未填写！");
                    return;
                }
                if (information.getText().toString() == null || information.getText().toString().equals("")) {
                    showMessage("个人信息未填写！");
                    return;
                }
                if (place.getText().toString().contains("所在城市")) {
                    showMessage("请点击获取城市信息！");
                    return;
                }
                DriverEntity entity = new DriverEntity();
                entity.setId(driverEntity.getId());
                entity.setPhone(driverEntity.getPhone());
                entity.setPassword(driverEntity.getPassword());
                entity.setName(name.getText().toString());
                entity.setUnit_price(Float.valueOf(unitPriceDetails.getText().toString()));
                entity.setIdentify_number(identify.getText().toString());
                entity.setInformation(information.getText().toString());
                entity.setPlace(place.getText().toString());
                for (int i = 0; i < sexGroup.getChildCount(); ++i) {
                    if (((RadioButton) sexGroup.getChildAt(i)).isChecked()) {
                        entity.setSex(i);
                        break;
                    }
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(entity));
                presenter.uploadOrRenewTrainerInformation(body);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startX = event.getX();
            startY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            if (Math.abs(startX - x) < 20 && startY - y < 20) {
                chooseImageWays.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_down);
                chooseImageWays.startAnimation(animation);
                return true;
            }
        }
        return false;
    }

    private void showChooseImageWays() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_up);
        chooseImageWays.startAnimation(animation);
        chooseImageWays.setVisibility(View.VISIBLE);
    }

    private void chooseImageWay(View view) {
        switch (view.getId()) {
            case R.id.activity_train_information_camera:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
                } else {
                    takeCamera();
                }
                break;
            case R.id.activity_train_information_photos:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1002);
                } else {
                    takePhoto();
                }
                break;
        }
        chooseImageWays.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //根据请求参数判断
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takeCamera();
            } else {
                showMessage("请打开相机的权限");
            }
        } else if (requestCode == 1002) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                showMessage("请打开读取相册的权限");
            }
        }
    }

    private void takePhoto() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 1003);
    }

    private void takeCamera() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "uploadImage";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        if (imageType == 0) {
            path += File.separator + "head.png";
        } else if (imageType == 1) {
            path += File.separator + "identify.png";
        } else if (imageType == 2) {
            path += File.separator + "trainer.png";
        }
        imageAbsolutePath = path;
        File imageFile = new File(imageAbsolutePath);
        if (imageFile.exists()) {
            imageFile.delete();
        }
        try {
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this, "com.leezp.learncarproject.fileprovider", imageFile);
        } else {
            imageUri = Uri.fromFile(imageFile);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1004);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1004 && resultCode == RESULT_OK) {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(driverEntity.getId()));
            map.put("phone", driverEntity.getPhone());
            map.put("password", driverEntity.getPassword());
            map.put("requestCode", String.valueOf(imageType));
            if (imageType == 0) {
                head.setImageBitmap(BitmapFactory.decodeFile(imageAbsolutePath));
            } else if (imageType == 1) {
                identifyImage.setImageBitmap(BitmapFactory.decodeFile(imageAbsolutePath));
            } else if (imageType == 2) {
                trainerImage.setImageBitmap(BitmapFactory.decodeFile(imageAbsolutePath));
            }
            File file = new File(imageAbsolutePath);
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            presenter.uploadOrRenewImage(map, body);
        } else if (requestCode == 1003 && resultCode == RESULT_OK) {
            resolvePhotos(data);
        }
    }

    private void resolvePhotos(Intent data) {
        //4.4为分界线，两种方法处理图片
        if (Build.VERSION.SDK_INT >= 19) {
            handleImageOnKitkat(data);
        } else {
            handleImageBeforeKitKat(data);
        }
    }

    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(driverEntity.getId()));
            map.put("phone", driverEntity.getPhone());
            map.put("password", driverEntity.getPassword());
            map.put("requestCode", String.valueOf(imageType));
            if (imageType == 0) {
                head.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            } else if (imageType == 1) {
                identifyImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            } else if (imageType == 2) {
                trainerImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            }
            File file = new File(imagePath);
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            presenter.uploadOrRenewImage(map, body);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        loading.setVisibility(View.GONE);
        if (aMapLocation == null) {
            showMessage("没有获取到地址！");
            return;
        }
        if (aMapLocation.getErrorCode() != 0) {
            showMessage("错误码：" + aMapLocation.getErrorCode() + "错误信息：" + aMapLocation.getErrorInfo());
            return;
        }
        place.setText(aMapLocation.getCity());
        place.setTextColor(getResources().getColor(R.color.color_train_information_text_color));
    }

    @Override
    public void onSuccess(Object object) {
        if (object instanceof NetDriverData) {
            NetDriverData driverData = (NetDriverData) object;
            if (driverData.getState() == 1001 && driverData.getDriver() != null) {
                presenter.storageDriver(driverData.getDriver());
                if (fromLoginOrRegister) {
                    HomePagerActivity.startHomePager(this, driverData.getDriver(), true);
                } else {
                    HomePagerActivity.startHomePager(this, driverData.getDriver(), false);
                }
                return;
            }
            if ((driverData.getState() == 1002 && driverData.getDriver() != null)
                    || (driverData.getState() == 1004 && driverData.getDriver() != null)
                    || (driverData.getState() == 1006 && driverData.getDriver() != null)) {
                driverEntity = driverData.getDriver();
                presenter.storageDriver(driverData.getDriver());
            }
            showMessage(driverData.getMessage());
        } else {
            showMessage("个人信息管理界面数据解析异常!");
        }
    }

    @Override
    public void onError(String message) {
        showMessage(message);
    }
}

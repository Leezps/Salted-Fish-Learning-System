package com.android.leezp.learncarproject.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leezp.learncarlib.GlideApp;
import com.android.leezp.learncarlib.activity.BaseActivity;
import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.adapter.PersonalProcessAdapter;
import com.android.leezp.learncarproject.db.PersonalInformationDB;
import com.android.leezp.learncarproject.entity.ActivityConstantEntity;
import com.android.leezp.learncarproject.entity.DriverProcessEntity;
import com.android.leezp.learncarproject.entity.StudentEntity;
import com.android.leezp.learncarproject.presenter.PersonalInformationPresenter;
import com.android.leezp.learncarproject.utils.TypeTransform;
import com.android.leezp.learncarproject.utils.event.NetEvent;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PersonalInformationActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, OnItemClickListener {

    //照相机的权限请求码
    private final int cameraPermission = 3001;
    //相册的权限请求码
    private final int photosPermission = 3002;
    //上传头像的标志
    private final int headUpload = 3003;
    //上传身份证的标志
    private final int identifyUpload = 3004;
    //照相机Intent请求参数
    private final int cameraRequestParam = 3005;
    //相册Intent请求参数
    private final int photoRequestParam = 3006;

    //检验上传图片类型的标志位
    private int imageFlag = 0;


    private ImageView back;
    private TextView title;
    private ImageView head;
    private LinearLayout imagePart;
    private TextView chooseCamera;
    private ImageView identifyImage;
    private TextView choosePhoto;
    private EditText name;
    private TextView sex;
    private LinearLayout sexPart;
    private TextView sexMan;
    private TextView sexWoman;
    private EditText age;
    private TextView phone;
    private EditText identify;
    private TextView process;
    private LinearLayout processPart;
    private RecyclerView processRecyler;
    private TextView upload;

    private float startX;
    private float startY;
    private Uri imageUri;
    //照片绝对路径
    private String imageAbsolutePath;
    private PersonalProcessAdapter adapter;
    private PersonalInformationPresenter presenter;
    private NetEvent event = new NetEvent() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof PersonalInformationDB) {
                PersonalInformationDB informationDB = (PersonalInformationDB) object;
                if ((informationDB.getState() == 1001 && informationDB.getStudent() != null) || informationDB.getState() == 1002 || informationDB.getState() == 1004) {
                    StudentEntity student = informationDB.getStudent();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("personalInformation", new Gson().toJson(student));
                    editor.apply();
                    showMessage("上传成功！");
                    if (student.getName() != null && !student.getName().equals("") &&
                            student.getIdentifyImageUrl() != null && !student.getIdentifyImageUrl().equals("") &&
                            student.getHeadUrl() != null && !student.getHeadUrl().equals("")) {
                        editor.apply();
                        if (openMode == ActivityConstantEntity.registerPersonalInformation) {
                            HomePagerActivity.startHomePagerActivity(PersonalInformationActivity.this);
                        }
                    }
                } else {
                    showMessage(informationDB.getMessage());
                }
            } else {
                showMessage("数据格式解析有误！");
            }
        }

        @Override
        public void onError(String message) {
            showMessage(message);
        }
    };
    private List<DriverProcessEntity> data = null;
    private SharedPreferences preferences;
    private StudentEntity studentEntity;
    private int openMode;

    @Override
    protected void initVariables() {
        ActivityCollector.addActivity(this);
        presenter = new PersonalInformationPresenter();
        presenter.onCreate();
        presenter.attachEvent(event);

        preferences = getSharedPreferences(ActivityConstantEntity.personalInformation_sharePreferences_name, MODE_PRIVATE);
        String information = preferences.getString("personalInformation", null);
        if (information != null) {
            studentEntity = new Gson().fromJson(information, StudentEntity.class);
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_information);
        back = ((ImageView) findViewById(R.id.activity_personal_information_back));
        title = ((TextView) findViewById(R.id.activity_personal_information_title));
        head = ((ImageView) findViewById(R.id.activity_personal_information_head));
        identifyImage = ((ImageView) findViewById(R.id.activity_personal_information_identify_image));
        imagePart = ((LinearLayout) findViewById(R.id.activity_personal_information_image_part));
        chooseCamera = ((TextView) findViewById(R.id.activity_personal_information_image_camera));
        choosePhoto = ((TextView) findViewById(R.id.activity_personal_information_image_photos));
        name = ((EditText) findViewById(R.id.activity_personal_information_name));
        sex = ((TextView) findViewById(R.id.activity_personal_information_sex));
        sexPart = ((LinearLayout) findViewById(R.id.activity_personal_information_sex_part));
        sexMan = ((TextView) findViewById(R.id.activity_personal_information_sex_man));
        sexWoman = ((TextView) findViewById(R.id.activity_personal_information_sex_woman));
        age = ((EditText) findViewById(R.id.activity_personal_information_age));
        phone = ((TextView) findViewById(R.id.activity_personal_information_phone));
        identify = ((EditText) findViewById(R.id.activity_personal_information_identify));
        process = ((TextView) findViewById(R.id.activity_personal_information_process));
        processPart = ((LinearLayout) findViewById(R.id.activity_personal_information_process_part));
        processRecyler = ((RecyclerView) findViewById(R.id.activity_personal_information_process_part_recycler));
        upload = ((TextView) findViewById(R.id.activity_personal_information_upload));

        processRecyler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        processRecyler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new PersonalProcessAdapter(data);
        adapter.addOnItemClickListener(this);
        processRecyler.setAdapter(adapter);


        back.setOnClickListener(this);
        head.setOnClickListener(this);
        identifyImage.setOnClickListener(this);
        chooseCamera.setOnClickListener(this);
        choosePhoto.setOnClickListener(this);
        imagePart.setOnTouchListener(this);
        sex.setOnClickListener(this);
        sexPart.setOnTouchListener(this);
        sexMan.setOnClickListener(this);
        sexWoman.setOnClickListener(this);
        process.setOnClickListener(this);
        processPart.setOnTouchListener(this);
        upload.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        Intent intent = getIntent();
        String title_str = intent.getStringExtra("title");
        if (title_str != null) {
            title.setText(title_str);
        }
        openMode = intent.getIntExtra("openMode", 0);
        if (openMode == ActivityConstantEntity.registerPersonalInformation) {
            back.setClickable(false);
            phone.setText(studentEntity.getPhone());
        } else if (openMode == ActivityConstantEntity.normalPersonalInformation) {
            back.setClickable(true);
            name.setText(studentEntity.getName());
            if (studentEntity.getSex() == 0) {
                sex.setText("男");
            } else if (studentEntity.getSex() == 1) {
                sex.setText("女");
            }
            sex.setTextColor(getResources().getColor(R.color.color_black));
            sex.setTag(studentEntity.getSex());
            age.setText(String.valueOf(studentEntity.getAge()));
            phone.setText(studentEntity.getPhone());
            identify.setText(studentEntity.getIdentifyNumber());
            List<DriverProcessEntity> entities = DataSupport.select("title")
                    .where("process_order=?", String.valueOf(studentEntity.getDriverProcess()))
                    .find(DriverProcessEntity.class);
            if (entities.size() > 0) {
                process.setText(entities.get(0).getTitle());
                process.setTag(studentEntity.getDriverProcess());
                process.setTextColor(getResources().getColor(R.color.color_black));
            }
            requestImage(ActivityConstantEntity.SERVER_ADDRESS + studentEntity.getHeadUrl(), R.drawable.personal_information_default_head, head);
            requestImage(ActivityConstantEntity.SERVER_ADDRESS + studentEntity.getIdentifyImageUrl(), R.drawable.personal_information_identify_image, identifyImage);
        }
        data = DataSupport.findAll(DriverProcessEntity.class);
        adapter.notifyDataChange(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_personal_information_back:
                ActivityCollector.removeActivity(this);
                presenter.onDestroy();
                finish();
                break;
            case R.id.activity_personal_information_head:
                imageFlag = headUpload;
                imagePart.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_personal_information_image_camera:
                chooseImageUpload(view);
                break;
            case R.id.activity_personal_information_identify_image:
                imageFlag = identifyUpload;
                imagePart.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_personal_information_image_photos:
                chooseImageUpload(view);
                break;
            case R.id.activity_personal_information_sex:
                sexPart.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_personal_information_sex_man:
                sex.setText(sexMan.getText());
                sex.setTag(0);
                sex.setTextColor(getResources().getColor(R.color.color_black));
                sexPart.setVisibility(View.GONE);
                break;
            case R.id.activity_personal_information_sex_woman:
                sex.setText(sexWoman.getText());
                sex.setTag(1);
                sex.setTextColor(getResources().getColor(R.color.color_black));
                sexPart.setVisibility(View.GONE);
                break;
            case R.id.activity_personal_information_process:
                processPart.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_personal_information_upload:
                int studentId = studentEntity.getStudentId();
                String phone_str = phone.getText().toString();
                String password_str = studentEntity.getPassword();
                String name_str = name.getText().toString();
                Object sex_object = sex.getTag();
                String age_str = age.getText().toString();
                String identify_str = identify.getText().toString();
                Object process_object = process.getTag();
                if (phone_str == null || password_str == null ||
                        name_str == null || sex_object == null ||
                        age_str == null || identify_str == null ||
                        process_object == null || phone_str.length() != 11 ||
                        name_str.length() > 5 || !TypeTransform.canStrToInt(String.valueOf(sex_object)) ||
                        age_str.length() > 2 || !TypeTransform.canStrToInt(age_str) ||
                        identify_str.length() != 18 || !TypeTransform.canStrToInt(String.valueOf(process_object))) {
                    showMessage("上传数据格式有误，请仔细审核！");
                } else {
                    StudentEntity entity = new StudentEntity();
                    entity.setStudentId(studentId);
                    entity.setPhone(phone_str);
                    entity.setPassword(password_str);
                    entity.setName(name_str);
                    entity.setSex(Integer.valueOf(String.valueOf(sex_object)));
                    entity.setAge(Integer.valueOf(age_str));
                    entity.setIdentifyNumber(identify_str);
                    entity.setDriverProcess(Integer.valueOf(String.valueOf(process_object)));
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(entity));
                    presenter.uploadPersonalInformation(body);
                }
                break;
        }
    }

    /**
     * 启动对应的个人信息Activity
     *
     * @param activity 传入启动的Activity
     * @param title    传入此Activity的标题
     */
    public static void startPersonalInformationActivity(Activity activity, String title, int openMode) {
        Intent intent = new Intent(activity, PersonalInformationActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("openMode", openMode);
        activity.startActivity(intent);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            startX = motionEvent.getX();
            startY = motionEvent.getY();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (Math.abs(startX - x) < 20 && startY - y < 20) {
                switch (view.getId()) {
                    case R.id.activity_personal_information_image_part:
                        imagePart.setVisibility(View.GONE);
                        break;
                    case R.id.activity_personal_information_sex_part:
                        sexPart.setVisibility(View.GONE);
                        break;
                    case R.id.activity_personal_information_process_part:
                        processPart.setVisibility(View.GONE);
                        break;
                }
                return true;
            }
        }
        return false;
    }

    private void chooseImageUpload(View view) {
        switch (view.getId()) {
            case R.id.activity_personal_information_image_camera:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, cameraPermission);
                } else {
                    takeCamera();
                }
                break;
            case R.id.activity_personal_information_image_photos:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, photosPermission);
                } else {
                    takePhoto();
                }
                break;
        }
        imagePart.setVisibility(View.GONE);
    }

    /**
     * 相册中选择照片
     */
    private void takePhoto() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, photoRequestParam);
    }

    /**
     * 调用照相机拍照
     */
    private void takeCamera() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "uploadImage";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        if (imageFlag == headUpload) {
            path += File.separator + "head.png";
        } else if (imageFlag == identifyUpload) {
            path += File.separator + "identify.png";
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
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this, "com.leezp.learncarproject.fileprovider", imageFile);
        } else {
            imageUri = Uri.fromFile(imageFile);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, cameraRequestParam);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //根据请求参数判断
        if (requestCode == cameraPermission) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takeCamera();
            } else {
                showMessage("请打开相机的权限");
            }
        } else if (requestCode == photosPermission) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                showMessage("请打开读取相册的权限");
            }
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == cameraRequestParam && resultCode == RESULT_OK) {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(studentEntity.getStudentId()));
            map.put("phone", studentEntity.getPhone());
            map.put("password", studentEntity.getPassword());
            if (imageFlag == headUpload) {
                map.put("requestCode", "0");
                head.setImageBitmap(BitmapFactory.decodeFile(imageAbsolutePath));
            } else if (imageFlag == identifyUpload) {
                map.put("requestCode", "1");
                identifyImage.setImageBitmap(BitmapFactory.decodeFile(imageAbsolutePath));
            }
            File file = new File(imageAbsolutePath);
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            presenter.uploadImage(map, body);
        } else if (requestCode == photoRequestParam && resultCode == RESULT_OK) {
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

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
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
            map.put("id", String.valueOf(studentEntity.getStudentId()));
            map.put("phone", studentEntity.getPhone());
            map.put("password", studentEntity.getPassword());
            if (imageFlag == headUpload) {
                map.put("requestCode", "0");
                head.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            } else if (imageFlag == identifyUpload) {
                map.put("requestCode", "1");
                identifyImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            }
            File file = new File(imagePath);
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            presenter.uploadImage(map, body);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        DriverProcessEntity entity = data.get(position);
        if (entity == null) {
            Object object = view.getTag();
            if (object != null) {
                String netId = String.valueOf(object);
                List<DriverProcessEntity> entities = DataSupport.select("process_order", "title")
                        .where("netId=?", netId)
                        .find(DriverProcessEntity.class);
                if (entities.size() > 0) {
                    entity = entities.get(0);
                }
            }
        }
        if (entity != null) {
            process.setText(entity.getTitle());
            process.setTextColor(getResources().getColor(R.color.color_black));
            process.setTag(entity.getProcess_order());
        }
        processPart.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (openMode == ActivityConstantEntity.normalPersonalInformation) {
            super.onBackPressed();
        } else if (openMode == ActivityConstantEntity.registerPersonalInformation) {
            showMessage("上传完正确个人信息才可返回主页，否则无法正确使用本应用！");
        }
    }

    private void requestImage(String url, int drawableId, ImageView image) {
        GlideApp.with(this)
                .load(url)
                .placeholder(R.drawable.image_loading_black)
                .error(drawableId)
                .into(image);
    }
}

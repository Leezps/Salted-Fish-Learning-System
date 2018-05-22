package com.android.leezp.learncarproject.entity;

/**
 * 用于存储Activity中需要的常量信息
 * <p>
 * Created by Leezp on 2018/3/16 0016.
 */

public class ActivityConstantEntity {

    //服务器的地址
    public static final String SERVER_ADDRESS = "http://192.168.43.207:8080/NoobStudyCarWeb/";

    //WelComeActivity中的SharePreferences名称
    public static final String welcomeActivity_sharePreferences_name = "WelcomeSharePreferences";

    //个人数据的SharePreferences名称
    public static final String personalInformation_sharePreferences_name = "PersonalInformationSharePreferences";

    //代码中付款方式的id
    public static final int personalMenuPaymentWay = 10001;

    //代码中学车路线的id
    public static final int personalMenuDriverTrail = 10002;

    //代码中消息中心的id
    public static final int personalMenuMessageCenter = 10003;

    //代码中帮助的id
    public static final int personalMenuHelp = 10004;

    //注册时上传个人信息
    public static final int registerPersonalInformation = 10005;

    //平时打开个人信息
    public static final int normalPersonalInformation = 10006;

    //考试秘籍是否第一次请求网络
    public volatile static boolean examFirstRequestNet = true;

}

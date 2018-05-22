package com.android.leezp.learncarproject.db.manager;

import com.android.leezp.learncarproject.db.BaiduMapDB;
import com.android.leezp.learncarproject.db.CompleteOrderDB;
import com.android.leezp.learncarproject.db.DriverProcessDB;
import com.android.leezp.learncarproject.db.DriverShowDB;
import com.android.leezp.learncarproject.db.ExamCheatsDB;
import com.android.leezp.learncarproject.db.HelpDB;
import com.android.leezp.learncarproject.db.LoginDB;
import com.android.leezp.learncarproject.db.MessageCenterDB;
import com.android.leezp.learncarproject.db.PaymentWayDB;
import com.android.leezp.learncarproject.db.PersonalInformationDB;
import com.android.leezp.learncarproject.db.PublishOrderDB;
import com.android.leezp.learncarproject.db.RegisterDB;
import com.android.leezp.learncarproject.db.RelationCompleteOrderDB;
import com.android.leezp.learncarproject.utils.retrofit.BaiduMapRetrofit;
import com.android.leezp.learncarproject.utils.retrofit.BookPublishOrderRetrofit;
import com.android.leezp.learncarproject.utils.retrofit.CompleteOrderRetrofit;
import com.android.leezp.learncarproject.utils.retrofit.DriverProcessRetrofit;
import com.android.leezp.learncarproject.utils.retrofit.ExamCheatsRetrofit;
import com.android.leezp.learncarproject.utils.retrofit.HelpRetrofit;
import com.android.leezp.learncarproject.utils.retrofit.LoginRetrofit;
import com.android.leezp.learncarproject.utils.retrofit.MessageCenterRetrofit;
import com.android.leezp.learncarproject.utils.retrofit.OrderDriverRetrofit;
import com.android.leezp.learncarproject.utils.retrofit.PaymentWayRetrofit;
import com.android.leezp.learncarproject.utils.retrofit.PersonalInformationRetrofit;
import com.android.leezp.learncarproject.utils.retrofit.RegisterRetrofit;
import com.android.leezp.learncarproject.utils.retrofit.RelationCompleteOrderRetrofit;
import com.android.leezp.learncarproject.utils.retrofit.base.BaseRetrofit;
import com.google.gson.GsonBuilder;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class DataBaseManager {

    private volatile static DataBaseManager manager = null;

    public static DataBaseManager getManager() {
        if (manager == null) {
            synchronized (DataBaseManager.class) {
                if (manager == null) {
                    manager = new DataBaseManager();
                }
            }
        }
        return manager;
    }

    /**
     * 登录界面的数据请求操作
     *
     * @param phone    手机号
     * @param password 密码
     * @return
     */
    public Observable<LoginDB> getStudentLogin(String phone, String password) {
        return ((LoginRetrofit) BaseRetrofit.getInstance().getRetrofit(LoginRetrofit.class))
                .getStudentLogin(phone, password);
    }

    /**
     * 获取验证码
     *
     * @param code  验证码
     * @param phone 手机号
     * @return
     */
    public Observable<RegisterDB> getVerification(String code, String phone) {
        return ((RegisterRetrofit) BaseRetrofit.getInstance().getRetrofit(RegisterRetrofit.class))
                .getVerification(code, phone);
    }

    /**
     * 注册学员
     *
     * @param params 注册需要的相关信息
     * @return
     */
    public Observable<RegisterDB> regisetrStudent(Map<String, String> params) {
        return ((RegisterRetrofit) BaseRetrofit.getInstance().getRetrofit(RegisterRetrofit.class))
                .regisetrStudent(params);
    }

    /**
     * 上传头像或身份证图片
     *
     * @param params 验证信息
     * @param image  图片
     * @return
     */
    public Observable<PersonalInformationDB> uploadImage(Map<String, String> params, RequestBody image) {
        return ((PersonalInformationRetrofit) BaseRetrofit.getInstance().getRetrofit(PersonalInformationRetrofit.class))
                .uploadImage(params, image);
    }

    /**
     * 上传个人信息
     *
     * @param information 上传个人信息
     * @return
     */
    public Observable<PersonalInformationDB> uploadPersonalInformation(RequestBody information) {
        return ((PersonalInformationRetrofit) BaseRetrofit.getInstance().getRetrofit(PersonalInformationRetrofit.class))
                .uploadPersonalInformation(information);
    }

    /**
     * 获取学车流程
     */
    public Observable<DriverProcessDB> requestDriverProcess() {
        return ((DriverProcessRetrofit) BaseRetrofit.getInstance().getRetrofit(DriverProcessRetrofit.class))
                .requestDriverProcess();
    }

    /**
     * 获取考试秘籍
     *
     * @param processOrder 学车流程
     * @return
     */
    public Observable<ExamCheatsDB> requestExamCheats(String processOrder) {
        return ((ExamCheatsRetrofit) BaseRetrofit.getInstance().getRetrofit(ExamCheatsRetrofit.class))
                .requestExamCheats(processOrder);
    }

    /**
     * 获取帮助
     *
     * @return
     */
    public Observable<HelpDB> getHelp() {
        return ((HelpRetrofit) BaseRetrofit.getInstance().getRetrofit(HelpRetrofit.class))
                .getHelp();
    }

    /**
     * 获取消息中心的消息
     *
     * @param params 请求参数
     * @return
     */
    public Observable<MessageCenterDB> getMessageCenter(Map<String, String> params) {
        return ((MessageCenterRetrofit) BaseRetrofit.getInstance().getRetrofit(MessageCenterRetrofit.class))
                .getMessageCenter(params);
    }

    /**
     * 移除消息中心的消息
     *
     * @param params 请求参数
     * @return
     */
    public Observable<MessageCenterDB> removeOrUpdateMessage(Map<String, String> params) {
        return ((MessageCenterRetrofit) BaseRetrofit.getInstance().getRetrofit(MessageCenterRetrofit.class))
                .removeOrUpdateMessage(params);
    }

    /**
     * 查询、添加、删除支付方式
     */
    public Observable<PaymentWayDB> operationPaymentWay(Map<String, String> params) {
        return ((PaymentWayRetrofit) BaseRetrofit.getInstance().getRetrofit(PaymentWayRetrofit.class))
                .operationPaymentWay(params);
    }

    /**
     * 查询完成订单
     */
    public Observable<CompleteOrderDB> getCompleteOrders(String requestCode, String role, String id, String phone, String password) {
        return ((CompleteOrderRetrofit) BaseRetrofit.getInstance().getRetrofit(CompleteOrderRetrofit.class))
                .getCompleteOrders(requestCode, role, id, phone, password);
    }

    /**
     * 请求删除完成订单
     */
    public Observable<CompleteOrderDB> requestRemoveCompleteOrder(Map<String, String> params) {
        return ((CompleteOrderRetrofit) BaseRetrofit.getInstance().getRetrofit(CompleteOrderRetrofit.class))
                .requestRemoveCompleteOrder(params);
    }

    /**
     * 通过发布订单id获取完成订单
     */
    public Observable<CompleteOrderDB> getCompOrderByPublishOrder(String requestCode, String publishOrderId) {
        return ((CompleteOrderRetrofit) BaseRetrofit.getInstance().getRetrofit(CompleteOrderRetrofit.class))
                .getCompOrderByPublishOrder(requestCode, publishOrderId);
    }

    /**
     * 根据完成订单信息获取相关用户信息
     */
    public Observable<RelationCompleteOrderDB> getRelationCompleteOrder(String role, String orderID, String driverID, String studentID) {
        return ((RelationCompleteOrderRetrofit) BaseRetrofit.getInstance().getRetrofit(RelationCompleteOrderRetrofit.class))
                .getRelationCompleteOrder(role, orderID, driverID, studentID);
    }

    /**
     * 通过手机定位的地址获取教练订单信息
     */
    public Observable<DriverShowDB> findDriverOrderByPlace(String place) {
        return ((OrderDriverRetrofit) BaseRetrofit.getInstance().getRetrofit(OrderDriverRetrofit.class))
                .findDriverOrderByPlace(place);
    }

    /**
     * 通过教练id获取教练发布订单
     */
    public Observable<PublishOrderDB> getPublishOrderByDriverID(String role, String code, String driverID) {
        return ((OrderDriverRetrofit) BaseRetrofit.getInstance().getRetrofit(OrderDriverRetrofit.class))
                .getPublishOrderByDriverID(role, code, driverID);
    }

    /**
     * 通过经纬度在百度上获取城市名
     */
    public Observable<BaiduMapDB> getPlaceByLatAndLng(String location) {
        return new Retrofit.Builder()
                .baseUrl("http://api.map.baidu.com/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(BaiduMapRetrofit.class)
                .getPlaceByLatAndLng("json", location);
    }

    /**
     * 预定发布的订单
     */
    public Observable<PublishOrderDB> studentBookOrder(Map<String, String> params) {
        return ((BookPublishOrderRetrofit) BaseRetrofit.getInstance().getRetrofit(BookPublishOrderRetrofit.class))
                .studentBookOrder(params);
    }
}

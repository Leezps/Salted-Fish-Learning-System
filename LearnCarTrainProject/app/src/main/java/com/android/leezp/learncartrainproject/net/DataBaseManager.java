package com.android.leezp.learncartrainproject.net;

import com.android.leezp.learncartrainproject.net.data.NetBookSuccessOrderData;
import com.android.leezp.learncartrainproject.net.data.NetDriverData;
import com.android.leezp.learncartrainproject.net.data.NetHelpData;
import com.android.leezp.learncartrainproject.net.data.NetMessageCenterData;
import com.android.leezp.learncartrainproject.net.data.NetPublishOrderData;
import com.android.leezp.learncartrainproject.net.data.NetRelationBookSuccessOrderData;
import com.android.leezp.learncartrainproject.net.retrofit.BookSuccessOrderRetrofit;
import com.android.leezp.learncartrainproject.net.retrofit.HelpRetrofit;
import com.android.leezp.learncartrainproject.net.retrofit.HomePagerRetrofit;
import com.android.leezp.learncartrainproject.net.retrofit.LoginOrRegisterRetrofit;
import com.android.leezp.learncartrainproject.net.retrofit.MessageCenterRetrofit;
import com.android.leezp.learncartrainproject.net.retrofit.TrainInformationRetrofit;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
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
     * 教练登陆
     *
     * @param phone    手机号
     * @param password 密码
     * @return 教练信息
     */
    public Observable<NetDriverData> loginDriver(String phone, String password) {
        return ((LoginOrRegisterRetrofit) BaseRetrofit.getInstance().getRetrofit(LoginOrRegisterRetrofit.class))
                .loginDriver(phone, password);
    }

    /**
     * 教练获取注册验证码
     *
     * @param code  **请求码**
     * @param phone **手机号**
     * @return  **返回对应教练的验证码的处理消息**
     */
    public Observable<NetDriverData> registerGetVerification(String code, String phone) {
        return ((LoginOrRegisterRetrofit) BaseRetrofit.getInstance().getRetrofit(LoginOrRegisterRetrofit.class))
                .registerGetVerification(code, phone);
    }

    /**
     * 注册教练账号
     *
     * @param params    **教练端注册需要的参数**
     * @return  **返回对应教练处理的消息**
     */
    public Observable<NetDriverData> registerDriver(Map<String, String> params) {
        return ((LoginOrRegisterRetrofit) BaseRetrofit.getInstance().getRetrofit(LoginOrRegisterRetrofit.class))
                .registerDriver(params);
    }

    /**
     * 获取教练发布的订单
     *
     * @param role     角色
     * @param code     请求码
     * @param driverID 教练id
     * @return 发布订单的返回信息
     */
    public Observable<NetPublishOrderData> getPublishOrderByDriverID(String role, String code, String driverID) {
        return ((HomePagerRetrofit) BaseRetrofit.getInstance().getRetrofit(HomePagerRetrofit.class))
                .getPublishOrderByDriverID(role, code, driverID);
    }

    /**
     * 教练发布订单
     *
     * @param params **发布订单所需参数**
     * @return **返回对应网络的信息对象**
     */
    public Observable<NetPublishOrderData> publishOrder(Map<String, String> params) {
        return ((HomePagerRetrofit) BaseRetrofit.getInstance().getRetrofit(HomePagerRetrofit.class))
                .publishOrder(params);
    }

    /**
     * 上传或更新图片
     *
     * @param params **请求所需带的参数**
     * @param image  **上传的图片**
     * @return **返回对应的教练信息**
     */
    public Observable<NetDriverData> uploadOrRenewImage(Map<String, String> params, RequestBody image) {
        return ((TrainInformationRetrofit) BaseRetrofit.getInstance().getRetrofit(TrainInformationRetrofit.class))
                .uploadOrRenewImage(params, image);
    }

    /**
     * 上传或更新图片
     *
     * @param information **上传的数据**
     * @return **返回对应的教练信息**
     */
    public Observable<NetDriverData> uploadOrRenewTrainerInformation(RequestBody information) {
        return ((TrainInformationRetrofit) BaseRetrofit.getInstance().getRetrofit(TrainInformationRetrofit.class))
                .uploadOrRenewTrainerInformation(information);
    }

    /**
     * 获取应用的帮助信息
     *
     * @return **返回对应的帮助数据**
     */
    public Observable<NetHelpData> getApplicationHelp() {
        return ((HelpRetrofit) BaseRetrofit.getInstance().getRetrofit(HelpRetrofit.class))
                .getApplicationHelp();
    }

    /**
     * 获取该教练的消息中心
     *
     * @param driverID **教练ID**
     * @param phone    **手机号**
     * @param password **密码**
     * @return **返回消息中心的消息**
     */
    public Observable<NetMessageCenterData> getMessageCenter(String driverID, String phone, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("code", "0");
        params.put("role", "1");
        params.put("id", driverID);
        params.put("phone", phone);
        params.put("password", password);
        return ((MessageCenterRetrofit) BaseRetrofit.getInstance().getRetrofit(MessageCenterRetrofit.class))
                .getOrUpdateOrRemoveMessage(params);
    }

    /**
     * 教练消息打开更新
     *
     * @param messageID **消息ID**
     */
    public Observable<NetMessageCenterData> updateMessageCenter(String driverID, String phone, String password, String messageID) {
        Map<String, String> params = new HashMap<>();
        params.put("code", "2");
        params.put("role", "1");
        params.put("id", driverID);
        params.put("phone", phone);
        params.put("password", password);
        params.put("message_id", messageID);
        return ((MessageCenterRetrofit) BaseRetrofit.getInstance().getRetrofit(MessageCenterRetrofit.class))
                .getOrUpdateOrRemoveMessage(params);
    }

    /**
     * 移除消息中心消息
     */
    public Observable<NetMessageCenterData> removeMessageCenter(String driverID, String phone, String password, String messageID) {
        Map<String, String> params = new HashMap<>();
        params.put("code", "1");
        params.put("role", "1");
        params.put("id", driverID);
        params.put("phone", phone);
        params.put("password", password);
        params.put("message_id", messageID);
        return ((MessageCenterRetrofit) BaseRetrofit.getInstance().getRetrofit(MessageCenterRetrofit.class))
                .getOrUpdateOrRemoveMessage(params);
    }

    /**
     * 获取预约成功订单
     *
     * @param driverID  **学员ID**
     * @param phone **手机号**
     * @param password  **密码**
     * @return  **返回预约成功订单数据**
     */
    public Observable<NetBookSuccessOrderData> getBookSuccessOrder(String driverID, String phone, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("code", "0");
        params.put("role", "1");
        params.put("id", driverID);
        params.put("phone", phone);
        params.put("password", password);
        return ((BookSuccessOrderRetrofit) BaseRetrofit.getInstance().getRetrofit(BookSuccessOrderRetrofit.class))
                .getOrRemoveBookSuccessOrder(params);
    }

    /**
     * 删除预约成功订单
     */
    public Observable<NetBookSuccessOrderData> removeBookSuccessOrder(String driverID, String phone, String password, String orderID, String removeMessage) {
        Map<String, String> params = new HashMap<>();
        params.put("code", "1");
        params.put("role", "1");
        params.put("id", driverID);
        params.put("phone", phone);
        params.put("password", password);
        params.put("order_id", orderID);
        params.put("remove_message", removeMessage);
        return ((BookSuccessOrderRetrofit) BaseRetrofit.getInstance().getRetrofit(BookSuccessOrderRetrofit.class))
                .getOrRemoveBookSuccessOrder(params);
    }

    /**
     * 获取预约成功订单的学员信息
     */
    public Observable<NetRelationBookSuccessOrderData> getRelationBookSuccessOrder(String orderID, String driverID, String studentID) {
        Map<String, String> params = new HashMap<>();
        params.put("role", "1");
        params.put("orderID", orderID);
        params.put("driverID", driverID);
        params.put("studentID", studentID);
        return ((BookSuccessOrderRetrofit) BaseRetrofit.getInstance().getRetrofit(BookSuccessOrderRetrofit.class))
                .getRelationBookSuccessOrder(params);
    }
}

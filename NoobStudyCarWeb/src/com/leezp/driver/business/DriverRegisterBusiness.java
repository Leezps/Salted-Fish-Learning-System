package com.leezp.driver.business;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.leezp.driver.business.interfaces.DriverRegisterBusinessInterface;
import com.leezp.driver.dao.DriverDao;
import com.leezp.driver.entity.DriverEntity;

public class DriverRegisterBusiness implements DriverRegisterBusinessInterface {
	private DriverDao driver = new DriverDao();
	static final String product = "Dysmsapi";
    static final String domain = "dysmsapi.aliyuncs.com";

    static final String accessKeyId = "LTAIbPVsftIlVVZo";
    static final String accessKeySecret = "NpHdDHSRcw4TotcmUTYN6Eh2Pm8TcY";
    
	@Override
	public boolean isExist(String phone) {
		DriverEntity entity = driver.findDriverByPhone(phone);
		if(entity != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int register(String phone, String password) {
		int rowNums = driver.insertBaseDriver(phone, password);
		if(rowNums > 0) {
			int id = driver.findDriverByPhone(phone).getId();
			return id;
		} else {
			return -1;
		}
	}

	@Override
	public SendSmsResponse sendMessageVerification(String phone, String verification) throws ClientException {
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName("œÃ”„—ß≥µ");
        request.setTemplateCode("SMS_131055040");
        request.setTemplateParam("{\"code\":\""+verification+"\"}");

        request.setOutId(verification);

        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
	}

	@Override
	public QuerySendDetailsResponse getSendMessageDetail(String phone, String bizId) throws ClientException {
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        request.setPhoneNumber(phone);
        request.setBizId(bizId);
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        
        request.setPageSize(10L);
        request.setCurrentPage(1L);

        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
	}

	@Override
	public int changeDriverPassword(String phone, String password) {
		return driver.updateDriverPassword(phone, password);
	}

	@Override
	public DriverEntity getDriverInformation(String phone) {
		return driver.findDriverByPhone(phone);
	}

}

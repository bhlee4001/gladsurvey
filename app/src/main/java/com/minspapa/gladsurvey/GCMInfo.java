package com.minspapa.gladsurvey;

/**
 * Created by Administrator on 2016-10-29.
 */

public class GCMInfo {

    /**
     * 서버 URL
     */
    //public static final String SERVER_URL = "http://minspapa.iptime.org:8096";
    public static final String SERVER_URL = "http://218.157.134.40:8080";
    //public static final String SERVER_URL = "http://192.168.42.245:8096";
    //public static final String SERVER_URL = "http://192.168.0.59:8096";

    public static final String RESOURCE_1 = "/admin/ba/b2/0100/getQueRealInitPopupPage.do";  //초기 페이지
    public static final String RESOURCE_2 = "/admin/ba/b2/0100/getQueRealPreviewPopupPage.do";  //이용동의
    public static final String RESOURCE_3 = "/admin/ba/b2/0100/getQueRealRegisterPopupPage.do";  //설문답변

    /**
     * Project Id registered to use GCM.
     * 단말 등록을 위한 필요한 ID
     */
    //public static final String PROJECT_ID = "1004588246065";
    public static final String PROJECT_ID = "524451208949";

    /**
     * Google API Key generated for service access
     * 서버 : 푸시 메시지 전송을 위해 필요한 KEY
     */
    //public static final String GOOGLE_API_KEY = "AIzaSyCxgSpw5aHnyVE8_MhnxWji5qK8Cjl6zzQ";
    public static final String GOOGLE_API_KEY = "AIzaSyCS30blHBoGXlJu8j-lptoHchTXmxthcQk";

    /**
     * Registration ID for this device
     * 단말 등록 후 수신한 등록 ID
     */
    public static String RegistrationId = "";
}

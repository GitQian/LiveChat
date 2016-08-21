package com.livechat.chat.utils;

/**
 * 常量
 * Created by Administrator on 2016/3/18.
 */
public class Constant {

    public final static int PHOTO_CAMERA_CODE = 300;// 拍照
    public final static int PHOTO_LOCALHOST_CODE = 400;// 本地选择
    public static final int PHOTO_CROP_PICTURE = 500;// 被裁剪的图片
    public final static int CONTENT_CODE = 700;// 意见反馈
    public final static int COUNTY_CODE = 900;// 国家
    public final static int IMAGE_OPEN_CODE = 1000;// 打开图片标记
    public final static int PAGE_ZERO = 0;// 第一页
    public final static int PAGE_ONE = 1;// 第二页
    public final static int PAGE_TWO = 2;// 第三页
    public final static int PAGE_THREE = 3;// 第四页
    public final static int THEME_HOLO_LIGHT = 3;// 主题样式
    public final static int ACCOUNT_CODE = 100;// 账号码
    public final static int PASSWORD_CODE = 200;// 密码码
    public final static int iLoginState_CODE = 3000;// 登陆状态码
    public final static int OnWays_Code = 4000;// 那种登陆方式
    public final static int ChannelId_Code = 5000;// 百度推送的ID
    public final static int ItemId_Code = 6000;// 后台推送的ID
    public final static String Token = "89F0B2B7E1D34DE498F5C17669718495";// Token Key用于融云的
    public final static String APPKEY = "110c022e2d025";// 短信SDK得到的APPKEY
    public final static String APPSECRET = "b16b4e384f73a3c0b7e65685db9d80d1";// 短信SDK得到的APPSECRET
    public final static int SUBMIT_SUCCESS_CODE = 200;// 验证码提交成功
    public final static int SUBMIT_FAILURE_ERROR = 404;// 验证码提交失败
    public final static int GET_CODE_SUCCESS_CODE = 201;// 获取验证码成功
    public final static int COUNTRIES_LIST_CODE = 100;// 国家列表

    public final static int SET_TEXT_TYPING_TITLE = 10000;// 正在输入
    public final static int SET_VOICE_TYPING_TITLE = 20000;// 正在讲话
    public final static int SET_TARGETID_TITLE = 30000;// 初始化状态

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /**
     * 部分路径(IP、端口)
     */
    public final static String BASE_URL = "http://www.e8chat.com";// http://61.56.86.21:80
    /**
     * 用户登录
     */
    public final static String sLoginUrl = BASE_URL + "/mobile/appuser/login.action";
    /**
     * 用户登出
     */
    public final static String sLogOutUrl = BASE_URL + "/mobile/appuser/logout.action";
    /**
     * 修改用户信息
     */
    public final static String sUpdateUserInfoUrl = BASE_URL + "/mobile/appuser/update.action";
    /**
     * 注册用户
     */
    public final static String sRegisterUrl = BASE_URL + "/mobile/appuser/register.action";
    /**
     * 重置用户密码(修改用户密码)
     */
    public final static String sResetUserPwdUrl = BASE_URL + "/mobile/appuser/resetPassword.action";
    /**
     * 获取客服列表
     */
    public final static String sFetchCustomersUrl = BASE_URL + "/mobile/user/allCustomer.action";
    /**
     * 素材接口
     * itemId    CommonUtil.getUserLoginInfo(getContext(), Constant.ItemId_Code)
     * sign   CommonUtil.md5Encryption("0" + Constant.Token)
     */
    public final static String sFetchMaterialUrl = BASE_URL + "/mobile/newsitem/getItem.action";
    /**
     * 素材下拉格子里面的URL
     */
    public final static String sMenuListUrl = BASE_URL + "/mobile/newsmenu/menuList.action";
    /**
     * 资讯里面的URL</br>
     * page  第几页（从1开始）
     * rows  几条数据
     * sign  CommonUtil.md5Encryption(CommonUtil.getUserLoginInfo(getActivity(),Constant.ACCOUNT_CODE) + Constant.Token)
     * account  86 2013427740478（登录账号）CommonUtil.getUserLoginInfo(getActivity(),Constant.ACCOUNT_CODE)
     */
    public final static String sInformationUrl = BASE_URL + "/mobile/newsitem/getItemByPage";
    /**
     * 提交反馈的信息
     */
    public final static String sCommitFeedbackUrl = BASE_URL + "/mobile/feedback/commit.action";
    /**
     * 上传图片的
     */
    public final static String sUploadImageUrl = BASE_URL + "/upload/to/headerImage.action";
    /**
     * "其他"接口
     */
    public final static String sOtherMenuListUrl = BASE_URL + "/mobile/appmenu/menuList.action";

}

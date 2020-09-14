package com.java.zhengkw.utils;

import android.content.Context;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsShareActivity{

    public Context mm;

    public NewsShareActivity(Context m){
        mm=m;
    }
    public void showShare(String title,String text,String url,String picture) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        //titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        oks.setImageUrl(picture);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(title);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        //启动分享GUI
        oks.show(mm);
    }
}

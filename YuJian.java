package Xp.YuJian.App;

import Xp.YuJian.App.Hook.SdkHook;
import Xp.YuJian.App.Hook.修改内部时间;
import Xp.YuJian.App.Hook.修改内部时间戳;
import Xp.YuJian.App.Hook.网站拦截;
import Xp.YuJian.App.Utis.RootCloak;
import Xp.YuJian.App.光遇.Hook.三服配置区;
import Xp.YuJian.App.光遇.Hook.国服Hook;
import Xp.YuJian.App.光遇.Hook.国际服Hook;
import Xp.YuJian.App.光遇.Hook.应用伪装与隐藏Root;
import Xp.YuJian.App.光遇.Hook.数据修改;
import Xp.YuJian.App.光遇.Hook.防抓包屏蔽Hook;
import Xp.YuJian.App.光遇.启动弹窗功能选择;
import Xp.YuJian.App.光遇.框架热更新;
import Xp.YuJian.App.容器.双开助手;
import Xp.YuJian.App.容器.微双开分身;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import Xp.YuJian.App.光遇.Hook.国服特殊Hook;

public class YuJian implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    private 防抓包屏蔽Hook sslInstance;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.bfire.da.nui")) {
            三服配置区.热更新类名 = "com.bfire.da.nui.lop01kvl.MainActivity";
            微双开分身 微双开分身 = new 微双开分身();
            微双开分身.handleLoadPackage(lpparam);

            框架热更新 框架热更新 = new 框架热更新();
            框架热更新.handleLoadPackage(lpparam);
        } else if (lpparam.packageName.equals("com.excelliance.dualaid")) {
            三服配置区.热更新类名 = "com.excelliance.kxqp.ui.MainActivity";

            双开助手 双开助手 = new 双开助手();
            双开助手.handleLoadPackage(lpparam);
            框架热更新 框架热更新 = new 框架热更新();
            框架热更新.handleLoadPackage(lpparam);
        } else if (lpparam.packageName.equals("com.excean.maid")) {
            三服配置区.热更新类名 = "com.excean.maid.icg52ewf.wlm36fm33krbf";

            框架热更新 框架热更新 = new 框架热更新();
            框架热更新.handleLoadPackage(lpparam);
        } else if (lpparam.packageName.equals("com.tencent.mmi")) {
            三服配置区.热更新类名 = "com.rv2k.eqr.vzs2moh2.gc9vrp6nrv3u";

            框架热更新 框架热更新 = new 框架热更新();
            框架热更新.handleLoadPackage(lpparam);
        } else if (lpparam.packageName.equals("com.tgc.sky.android")) {
			三服配置区.bao = "com.tgc.sky.android";
            三服配置区.URL = "https://sky.app98.cn/";
            三服配置区.公告地址 = 三服配置区.URL + "app/xpgg.php"; 
            三服配置区.更新检测 = 三服配置区.URL + "app/loaderg.php";
            三服配置区.二进制名称 = "sky_国际遇见.so";

            三服配置区.游戏版本 = "0.24.8 (253326)";
            三服配置区.游戏版本号 = 253326;
            国际服Hook 国际服 = new 国际服Hook();
			国际服.handleLoadPackage(lpparam);
        } 
		else {
			国服Hook 国服 = new 国服Hook();
			国服.handleLoadPackage(lpparam);
			网站拦截 网站拦截 = new 网站拦截();
			网站拦截.handleLoadPackage(lpparam);
			
			SdkHook SDK = new SdkHook();
			SDK.handleLoadPackage(lpparam);
		}
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        sslInstance = new 防抓包屏蔽Hook();
        sslInstance.initZygote(startupParam);
    }
}



package com.ly.imart.maxim.scan.utils;


import android.app.Activity;
import android.text.TextUtils;

import com.ly.imart.maxim.common.utils.ToastUtil;
import com.ly.imart.maxim.scan.view.ScanResultActivity;

/**
 * Description:扫描结果处理工具类
 */

public class ScanResultUtil {

    /**
     * 处理扫描后的结果
     *
     * @param scanMessage 扫描信息
     */
    public void dealScanResult(Activity activity, String scanMessage) {
        if (TextUtils.isEmpty(scanMessage)) {
            ToastUtil.showTextViewPrompt("未识别到二维码，请重试");
            activity.finish();
            return;
        }
        ScanResultActivity.openScanResult(activity, scanMessage);
        activity.finish();
    }
}

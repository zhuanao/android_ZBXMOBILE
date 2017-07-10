package com.zbxn.activity.schedule;

import android.content.Context;

import com.zbxn.bean.RepeatNewTaskEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.mvp.AbsBasePresenter;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.frame.mvp.base.ControllerCenter;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * Created by GISirFive on 2016/8/3.
 */
public class RemindNewTackPresenter extends AbsBasePresenter<ControllerCenter> {

    public RemindNewTackPresenter(AbsToolbarActivity activity) {
        super(activity);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {
        switch (code) {
        }

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {
        switch (code) {
        }
    }

    /**
     * 提醒
     */
    public void save(Context context, final ICustomListener listener) {
        mController.loading().show("正在提交...");

        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaScheduleRule/findPrecedeType.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<RepeatNewTaskEntity>().parse(json, RepeatNewTaskEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    if (!StringUtils.isEmpty(mResult.getRows())) {
                        List<RepeatNewTaskEntity> list = mResult.getRows();

                        listener.onCustomListener(0, list, 0);
                    } else {
                        MyToast.showToast( mResult.getMsg());
                    }
                    mController.loading().hide();
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast( message);
                    mController.loading().hide();
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(map);
    }
}

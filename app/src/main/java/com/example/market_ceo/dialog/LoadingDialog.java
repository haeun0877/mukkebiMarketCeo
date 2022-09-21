package com.example.market_ceo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.widget.ImageView;

import com.example.market_ceo.MainActivity;
import com.example.market_ceo.R;

public class LoadingDialog extends Dialog {
    private static LoadingDialog m_instance = null;
    private static Context m_context = null;
    private static Boolean m_isInit = false;


    public static LoadingDialog setInstance(Context context) {
        if (m_isInit) {
            if (m_instance == null) {
                m_context = context;
                m_instance = new LoadingDialog(m_context);
            }
        }
        return m_instance;
    }

    public static void init(Boolean isInit) {
        m_isInit = isInit;
    }

    public static Boolean isAvail() {
        return m_instance != null && m_isInit;
    }

    public static void showProgress() {
        if (m_instance != null && !((Activity) m_context).isFinishing())
            m_instance.dismiss();
        if (m_instance != null)
            m_instance.show();
    }

    public static void dismissProgress() {
        if (m_instance != null && !((Activity) m_context).isFinishing())
            m_instance.dismiss();
    }

    public LoadingDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 지저분한(?) 다이얼 로그 제목을 날림
        setContentView(R.layout.dialog_loading); // 다이얼로그에 박을 레이아웃
    }

    @Override
    public void show() {
        if (m_context == null || ((Activity) m_context).isFinishing() || !(m_context instanceof MainActivity))
            return;

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);

        AnimationDrawable drawable = (AnimationDrawable) ((ImageView) findViewById(R.id.iv_loading)).getDrawable();
        if (drawable != null && !drawable.isRunning())
            drawable.start();

        super.show();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (m_context != null && !((Activity) m_context).isFinishing() && m_instance.isShowing()) {
                    m_instance.dismiss();
                }
            }
        }, 1000 * 10);

//        new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                if (m_context != null && !((Activity) m_context).isFinishing() && m_instance.isShowing()) {
//                    m_instance.dismiss();
//                }
//                return false;
//            }
//        }).sendEmptyMessageDelayed(0, 1000 * 10);
    }
}

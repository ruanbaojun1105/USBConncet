package com.hwx.usbconnect.usbconncet.ui.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.hwx.usbconnect.usbconncet.MsgInterface;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.bean.MessageTalk;
import com.hwx.usbconnect.usbconncet.ui.activity.SimpleFragment;
import com.hwx.usbconnect.usbconncet.ui.activity.UsbMainActivity;
import com.hwx.usbconnect.usbconncet.ui.adapter.TalkMulItemAdapter;
import com.hwx.usbconnect.usbconncet.ui.widget.CustomFooterView;
import com.hwx.usbconnect.usbconncet.ui.widget.ScollLinearLayoutManager;
import com.hwx.usbconnect.usbconncet.ui.widget.SmileyHeaderView;
import com.hwx.usbconnect.usbconncet.ui.widget.StateButton;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;
import com.joanzapata.iconify.widget.IconTextView;
import com.xw.repo.XEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TalkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TalkFragment extends SimpleFragment {

    @BindView(R.id.talk_edit)
    XEditText talkEdit;
    @BindView(R.id.talk_sends)
    StateButton talkSends;
    @BindView(R.id.recycleview)
    RecyclerView talkRecycleview;
    @BindView(R.id.talk_setname)
    IconTextView talkSetname;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    private TalkMulItemAdapter itemAdapter;

    public TalkFragment() {
    }

    public static TalkFragment newInstance() {
        TalkFragment fragment = new TalkFragment();
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_talk;
    }

    @Override
    protected void initEventAndData() {
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((UsbMainActivity) mActivity).setBackMsgCall(new MsgInterface() {
            @Override
            public void getBackMsg(String name, String msg) {
                refreshList(msg);
            }
        });
        talkRecycleview.smoothScrollToPosition((((UsbMainActivity) mActivity).getMsgList()).size());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (itemAdapter != null && talkRecycleview != null) {
            List<MessageTalk> list = ((UsbMainActivity) mActivity).getMsgList();
            itemAdapter.setNewData(list);
            talkRecycleview.smoothScrollToPosition(list.size());
        }
    }

    private void initView() {
        itemAdapter = new TalkMulItemAdapter(((UsbMainActivity) mActivity).getMsgList());
        ScollLinearLayoutManager manager = new ScollLinearLayoutManager(mContext);
        //manager.setSpeedSlow(0.3f);
        itemAdapter.openLoadAnimation();
        TextView head = new TextView(mContext);
        head.setTextColor(Color.GRAY);
        head.setText(R.string.adatewq);
        head.setGravity(Gravity.CENTER);
        itemAdapter.addHeaderView(head);
        talkRecycleview.setLayoutManager(manager);
        talkRecycleview.setAdapter(itemAdapter);
        talkSends.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                String text = talkEdit.getText().toString();
                if (TextUtils.isEmpty(text))
                    return;
                String name = ((UsbMainActivity) mActivity).getName();
                ((UsbMainActivity) mActivity).sendMessage(name + ":" + text);
                talkEdit.setText("");
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(talkEdit.getWindowToken(), 0);
            }
        });
        talkSetname.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                ((UsbMainActivity) mActivity).showDialog();
            }
        });
        setRefreshView(mContext,xrefreshview);
    }

    public static void setRefreshView(final Context context, final XRefreshView view){
        view.setPullLoadEnable(true);
        view.setPinnedTime(0);
        view.setCustomHeaderView(new SmileyHeaderView(context));
        view.setCustomFooterView(new CustomFooterView(context));
        view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                view.stopRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                view.stopLoadMore();
            }
        });
    }


    void refreshList(final String message) {
        LogUtils.e("get msg:" + message);
        if (talkRecycleview != null && itemAdapter != null) {
            talkRecycleview.post(new Runnable() {
                @Override
                public void run() {
                    if (talkRecycleview.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && !talkRecycleview.isComputingLayout()) {
                        itemAdapter.notifyDataSetChanged();
                        talkRecycleview.smoothScrollToPosition(itemAdapter.getData().size());
                    } else {
                        itemAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }


    /*@Deprecated
    class MessageBackReciver extends BroadcastReceiver {
        void regist() {
            IntentFilter mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(TcpAIDLService.HEART_BEAT_ACTION);
            mIntentFilter.addAction(TcpAIDLService.MESSAGE_ACTION);
            LocalBroadcastManager.getInstance(mContext).registerReceiver(this, mIntentFilter);
        }

        @Override
        public void onReceive(Context context, final Intent intent) {
            String action = intent.getAction();
            if (action.equals(TcpAIDLService.HEART_BEAT_ACTION)) {
            } else {
                String message = intent.getStringExtra("message");
                refreshList(message);
            }
        }
    }*/
}

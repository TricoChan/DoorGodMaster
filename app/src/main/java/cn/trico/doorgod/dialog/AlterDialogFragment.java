package cn.trico.doorgod.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.trico.doorgod.R;

public class AlterDialogFragment extends DialogFragment {

    public interface CallBack {
        void catchData(String data);
    }

    public void setCallback(CallBack callback){
        this.callback = callback;
    }

    public void catchData(String data){
        callback.catchData(data);
    }

    private CallBack callback;
    private String catchText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.alter_title_input, null);
        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null) {
                            EditText et_alter_name = (EditText) view.findViewById(R.id.et_alter_name);
                            catchText = et_alter_name.getText().toString();
                            if(catchText.isEmpty() || catchText.length() == 0){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"未输入字符",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }else if(catchText.length() > 4){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"请输入小于4个字符",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            callback.catchData(catchText);
                        }
                    }
                });
        builder.setView(view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "AlertDialogFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
}

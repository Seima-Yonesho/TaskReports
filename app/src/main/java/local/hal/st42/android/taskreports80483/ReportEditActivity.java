package local.hal.st42.android.taskreports80483;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import local.hal.st42.android.taskreports80483.dataaccess.Report;
import local.hal.st42.android.taskreports80483.viewmodel.ReportEditViewModel;

/**
 * ST42 Android 課題6 作業報告管理アプリ
 *
 * 第2画面・第4画面表示用アクティビティクラス
 * 新規登録
 * 編集
 * 管理データ項目のうちシステム管理項目以外を入力できるようにする
 *
 */
public class ReportEditActivity extends AppCompatActivity {
    /**
     * 新規登録モードか更新モードかを表すフィールド。
     */
    private int _mode = Consts.MODE_INSERT;
    /**
     * 更新モードの際、現在表示しているリスト情報のデータベース上の主キー値。
     */
    private int _idNo = 0;
    /**
     * レポート情報編集モデルオブジェクト。
     */
    private ReportEditViewModel _reportEditViewModel;
    /*
    * 時間入力のデフォルト値
     */
    private final String defaultInputTime = "00:00";
    /*
    * 更新モードの際、現在表示しているレポートの登録日時。
     */
    private Timestamp _insertDate = new Timestamp(System.currentTimeMillis());

    private int activity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_edit);

        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        ViewModelProvider provider = new ViewModelProvider(ReportEditActivity.this, factory);
        _reportEditViewModel = provider.get(ReportEditViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        //スピナー
        Spinner spinner = findViewById(R.id.spWorkKinds);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Consts.CATEGORY);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                spinner.setTag(position);
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        _mode  = intent.getIntExtra("mode", Consts.MODE_INSERT);

        if(_mode == Consts.MODE_INSERT) {
            TextView tvTitle = findViewById(R.id.tvTitle);
            tvTitle.setText(R.string.tv_title_insert);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            TextView etInputDate = findViewById(R.id.etDate);
            etInputDate.setText(sdf.format(calendar.getTime()));
            TextView etStartTime = findViewById(R.id.etStartTime);
            etStartTime.setText(defaultInputTime);
            TextView etEndTime = findViewById(R.id.etEndTime);
            etEndTime.setText(defaultInputTime);
        }
        else {
            _idNo    = intent.getIntExtra("idNo", 0);
            Report report = _reportEditViewModel.getReport(_idNo);
            EditText etInputNote = findViewById(R.id.etInputNote);
            etInputNote.setText(report.workin);
            TextView etDate = findViewById(R.id.etDate);
            etDate.setText(report.workdate);
            TextView etStartTime = findViewById(R.id.etStartTime);
            etStartTime.setText(report.starttime);
            TextView etEndTime = findViewById(R.id.etEndTime);
            etEndTime.setText(report.endtime);
            Spinner spinner1 = findViewById(R.id.spWorkKinds);
            spinner1.setSelection(report.workkind);
            _insertDate = report.insertAt;
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(_mode == Consts.MODE_INSERT) {
            inflater.inflate(R.menu.menu_options_report_add, menu);
        }
        else {
            inflater.inflate(R.menu.menu_options_report_edit, menu);
        }
        return true;
    }

    /**
     * オプションメニュー
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int      itemId      = item.getItemId();
        switch(itemId) {
            case R.id.menuDelete:   // 削除ボタン
                DeleteConfirmDialogFragment dialog = new DeleteConfirmDialogFragment(_reportEditViewModel);
                Bundle extras = new Bundle();
                extras.putInt("id", _idNo);
                dialog.setArguments(extras);
                FragmentManager manager = getSupportFragmentManager();
                dialog.show(manager, "DeleteDialogFragment");
                return true;
            case R.id.menuEdit: // 登録・編集ボタン
                TextView etDate = findViewById(R.id.etDate);
                String strDate = etDate.getText().toString();
                TextView etStartTime = findViewById(R.id.etStartTime);
                String strStartTime = etStartTime.getText().toString();
                TextView etEndTime = findViewById(R.id.etEndTime);
                String strEndTime = etEndTime.getText().toString();
                if(strDate.equals("")) {
                    Toast.makeText(ReportEditActivity.this, R.string.msg_input_date, Toast.LENGTH_SHORT).show();
                }
                else if(strStartTime.equals(defaultInputTime)){
                    Toast.makeText(ReportEditActivity.this, R.string.msg_input_start_time, Toast.LENGTH_SHORT).show();
                }
                else if(strEndTime.equals(defaultInputTime)){
                    Toast.makeText(ReportEditActivity.this, R.string.msg_input_end_time, Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean checkTime = checkTime(strStartTime, strEndTime);
                    if(checkTime){
                        Toast.makeText(ReportEditActivity.this, R.string.msg_input_time, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        EditText etInputContent = findViewById(R.id.etInputNote);
                        String inputContent = etInputContent.getText().toString();
                        Spinner spinner = findViewById(R.id.spWorkKinds);
                        int workkind = (Integer) spinner.getTag();
                        long result = 0;
                        Report report = new Report();
                        report.workdate = strDate;
                        report.starttime = strStartTime;
                        report.endtime = strEndTime;
                        report.workkind = workkind;
                        report.workin = inputContent;
                        report.updatedAt = new Timestamp(System.currentTimeMillis());
                        if(_mode == Consts.MODE_INSERT) {
                            report.insertAt = new Timestamp(System.currentTimeMillis());
                            result = _reportEditViewModel.insert(report);
                        }
                        else {
                            report.insertAt = _insertDate;
                            report.id = _idNo;
                            result = _reportEditViewModel.update(report);
                        }
                        if(result <= 0) {
                            Toast.makeText(ReportEditActivity.this, R.string.msg_save_err, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(_mode == Consts.MODE_EDIT){
                                onClickSaveAndBackButton();
                            }
                            finish();
                        }
                    }
                }
                return true;
            case android.R.id.home:
                if(_mode == Consts.MODE_EDIT){
                    onClickSaveAndBackButton();
                }
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onClickSaveAndBackButton(){
        Intent intentOld = getIntent();
        activity = intentOld.getIntExtra("activity", Consts.DetailActivity);
        _idNo = intentOld.getIntExtra("idNo", _idNo);
        Intent intent;
        if(activity == Consts.DetailActivity){
            intent = new Intent(getApplicationContext(), ReportDetailActivity.class);
            intent.putExtra("idNo", _idNo);
            startActivity(intent);
        }
        return;
    }

    /**
     * 日付選択ダイアログ表示ボタンが押されたときのイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void showDatePickerDialog(View view) {
        int nowYear       = 0;
        int nowMonth      = 0;
        int nowDayOfMonth = 0;

        TextView etDate = findViewById(R.id.etDate);
        String strDate = etDate.getText().toString();

        nowYear       = Integer.parseInt(strDate.substring(0, 4));
        nowMonth      = Integer.parseInt(strDate.substring(5, 7)) -1;
        nowDayOfMonth = Integer.parseInt(strDate.substring(8, 10));

        DatePickerDialog dialog = new DatePickerDialog(ReportEditActivity.this, new DatePickerDialogDateSetListener(), nowYear, nowMonth, nowDayOfMonth);
        dialog.show();
    }

    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            TextView etDate = findViewById(R.id.etDate);
            Calendar calendar   = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            etDate.setText(sdf.format(calendar.getTime()));
        }
    }

    public void showStartTimePickerDialog(View view){
        int nowHour = 0;
        int nowMinute = 0;
        TextView etStartTime = findViewById(R.id.etStartTime);
        String strStartTime = etStartTime.getText().toString();
        nowHour = Integer.parseInt(strStartTime.substring(0, 2));
        nowMinute = Integer.parseInt(strStartTime.substring(3, 5));
        TimePickerDialog dialog = new TimePickerDialog(ReportEditActivity.this, new StartTimePickerDialogTimeSetListener(), nowHour, nowMinute, false);
        dialog.show();
    }

    private class StartTimePickerDialogTimeSetListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute){
            TextView etStartTime = findViewById(R.id.etStartTime);
            Calendar calendar   = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aaa");
            etStartTime.setText(sdf.format(calendar.getTime()));
        }
    }

    public void showEndTimePickerDialog(View view){
        int nowHour = 0;
        int nowMinute = 0;
        TextView etEndTime = findViewById(R.id.etEndTime);
        String strEndTime = etEndTime.getText().toString();
        nowHour = Integer.parseInt(strEndTime.substring(0, 2));
        nowMinute = Integer.parseInt(strEndTime.substring(3, 5));
        TimePickerDialog dialog = new TimePickerDialog(ReportEditActivity.this, new EndTimePickerDialogTimeSetListener(), nowHour, nowMinute, false);
        dialog.show();
    }

    private class EndTimePickerDialogTimeSetListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute){
            TextView etEndTime = findViewById(R.id.etEndTime);
            Calendar calendar   = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aaa");
            etEndTime.setText(sdf.format(calendar.getTime()));
        }
    }

    private boolean checkTime(String startTime, String endTime){
        Boolean errFlg = false;
        int hourOfStartTime = Integer.parseInt(startTime.substring(0, 2));
        int minuteOfStartTime = Integer.parseInt(startTime.substring(3, 5));
        int hourOfEndTime = Integer.parseInt(endTime.substring(0, 2));
        int minuteOfEndTime = Integer.parseInt(endTime.substring(3, 5));
        if(hourOfStartTime > hourOfEndTime){
            errFlg = true;
        }
        else if(hourOfStartTime == hourOfEndTime && minuteOfStartTime >= minuteOfEndTime){
            errFlg = true;
        }
        return errFlg;
    }
}

package local.hal.st42.android.taskreports80483;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import local.hal.st42.android.taskreports80483.dataaccess.Report;
import local.hal.st42.android.taskreports80483.viewmodel.ReportDetailViewModel;

/**
 * ST42 Android 課題6 作業報告管理アプリ
 *
 * 第3画面表示用アクティビティクラス
 * 管理しているデータ項目全てを表示する
 *
 */
public class ReportDetailActivity extends AppCompatActivity {
    /**
     * 現在表示しているリスト情報のデータベース上の主キー値。
     */
    private int _idNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        ViewModelProvider provider = new ViewModelProvider(ReportDetailActivity.this, factory);
        ReportDetailViewModel _reportDetailViewModel = provider.get(ReportDetailViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        _idNo    = intent.getIntExtra("idNo", 0);
        Report report = _reportDetailViewModel.getReport(_idNo);
        TextView tvWorkin = findViewById(R.id.tvWorkIn);
        tvWorkin.setText(report.workin);
        TextView tvWorkDate = findViewById(R.id.tvWorkDate);
        tvWorkDate.setText(report.workdate);
        TextView tvStartTime = findViewById(R.id.tvStartTime);
        tvStartTime.setText(report.starttime);
        TextView tvEndTime = findViewById(R.id.tvEndTime);
        tvEndTime.setText(report.endtime);
        TextView tvWorkCategory = findViewById(R.id.tvWorkCategory);
        tvWorkCategory.setText(Consts.CATEGORY[report.workkind]);
        TextView tvInsertDate = findViewById(R.id.tvInsertDate);
        tvInsertDate.setText(String.valueOf(report.insertAt));
        TextView tvUpdateDate = findViewById(R.id.tvUpdateDate);
        tvUpdateDate.setText(String.valueOf(report.updatedAt));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_report_detail, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case R.id.menuEdit: // 登録・編集ボタン
                Intent intent = new Intent(getApplicationContext(), ReportEditActivity.class);
                intent.putExtra("mode", Consts.MODE_EDIT);
                intent.putExtra("idNo", _idNo);
                intent.putExtra("activity", Consts.DetailActivity);
                startActivity(intent);
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package local.hal.st42.android.taskreports80483;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import local.hal.st42.android.taskreports80483.dataaccess.Report;
import local.hal.st42.android.taskreports80483.viewmodel.ReportListViewModel;

/**
 * ST42 Android 課題6 作業報告管理アプリ
 *
 * 第1画面表示用アクティビティクラス
 * 作業報告リストを表示
 * スクローリングアクティビティで構成
 * 作業報告新規登録のFABを設置
 * 作業種類によるリストの絞り込みが可能
 *
 * @author Seima Yonesho
 */
public class MainActivity extends AppCompatActivity {

    /**
     * リサイクラービューを表すフィールド。
     */
    private RecyclerView _rvReport;
    /**
     * メニューリストの種類を表すフィールド。
     */
    private int _menuCategory;
    /**
     * リサイクラービューで利用するアダプタオブジェクト。
     */
    private ReportListAdapter _adapter;
    /**
     * レポート情報リストビューモデルオブジェクト。
     */
    private ReportListViewModel _reportListViewModel;
    /**
     * レポート情報リストLiveDataオブジェクト。
     */
    private LiveData<List<Report>> _reportListLiveData;
    /**
     * タスク情報リストLiveData変更時に対応するオブザーバーオブジェクト。
     */
    private ReportListObserver _reportListObserver;

    /**
     * プレファレンスファイル名を表す定数フィールド。
     */
    private static final String PREFS_NAME = "ReportFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * ツールバー操作
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbarLayout);
        toolbarLayout.setTitle(getString(R.string.app_name));
        toolbarLayout.setExpandedTitleColor(Color.WHITE);
        toolbarLayout.setCollapsedTitleTextColor(Color.LTGRAY);

        _rvReport = findViewById(R.id.rvReport);
        LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this);
        _rvReport.setLayoutManager(layout);
        DividerItemDecoration decoration = new DividerItemDecoration(MainActivity.this, layout.getOrientation());
        _rvReport.addItemDecoration(decoration);
        List<Report> reportList = new ArrayList<>();
        _adapter = new ReportListAdapter(reportList);
        _rvReport.setAdapter(_adapter);

        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        ViewModelProvider provider = new ViewModelProvider(MainActivity.this, factory);
        _reportListViewModel = provider.get(ReportListViewModel.class);
        _reportListObserver = new ReportListObserver();
        _reportListLiveData = new MutableLiveData<>();

        SharedPreferences settings      = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        _menuCategory = settings.getInt("WorkCategory", Consts.ALL);

        createRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_activity_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuListOptionTitle = menu.findItem(R.id.menuListOptionTitle);
        switch(_menuCategory) {
            case Consts.ALL:
                menuListOptionTitle.setTitle(R.string.menu_list_all);
                break;
            case Consts.DEVELOP:
                menuListOptionTitle.setTitle(R.string.menu_list_develop);
                break;
            case Consts.MEETING:
                menuListOptionTitle.setTitle(R.string.menu_list_meeting);
                break;
            case Consts.DOCUMENT:
                menuListOptionTitle.setTitle(R.string.menu_list_document);
                break;
            case Consts.SUPPORT:
                menuListOptionTitle.setTitle(R.string.menu_list_support);
                break;
            case Consts.DESIGN:
                menuListOptionTitle.setTitle(R.string.menu_list_design);
                break;
            case Consts.OTHER:
                menuListOptionTitle.setTitle(R.string.menu_list_other);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor   = settings.edit();

        boolean returnVal = true;
        int     itemId    = item.getItemId();
        switch(itemId) {
            case R.id.menuListOptionAll:
                _menuCategory = Consts.ALL;
                editor.putInt("WorkCategory", _menuCategory);
                invalidateOptionsMenu();
                break;
            case R.id.menuListOptionDevelop:
                _menuCategory = Consts.DEVELOP;
                editor.putInt("WorkCategory", _menuCategory);
                invalidateOptionsMenu();
                break;
            case R.id.menuListOptionMeeting:
                _menuCategory = Consts.MEETING;
                editor.putInt("WorkCategory", _menuCategory);
                invalidateOptionsMenu();
                break;
            case R.id.menuListOptionDocument:
                _menuCategory = Consts.DOCUMENT;
                editor.putInt("WorkCategory", _menuCategory);
                invalidateOptionsMenu();
                break;
            case R.id.menuListOptionSupport:
                _menuCategory = Consts.SUPPORT;
                editor.putInt("WorkCategory", _menuCategory);
                invalidateOptionsMenu();
                break;
            case R.id.menuListOptionDesign:
                _menuCategory = Consts.DESIGN;
                editor.putInt("WorkCategory", _menuCategory);
                invalidateOptionsMenu();
                break;
            case R.id.menuListOptionOther:
                _menuCategory = Consts.OTHER;
                editor.putInt("WorkCategory", _menuCategory);
                invalidateOptionsMenu();
                break;
            default:
                returnVal = super.onOptionsItemSelected(item);
        }
        if(returnVal) {
            createRecyclerView();
        }
        editor.commit();
        return returnVal;
    }

    /**
     * 新規ボタンが押されたときのイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void onFabAddClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), ReportEditActivity.class);
        intent.putExtra("mode", Consts.MODE_INSERT);
        startActivity(intent);
    }

    /**
     * リスト画面表示用のデータを生成するメソッド。
     * フィールド_onlyImportantの値に合わせて生成するデータを切り替える。
     */
    private void createRecyclerView() {
        _reportListLiveData.removeObserver(_reportListObserver);
        _reportListLiveData = _reportListViewModel.getReportList(_menuCategory);
        _reportListLiveData.observe(MainActivity.this, _reportListObserver);
    }

    /**
     * ビューモデル中のメモ情報リストに変更があった際に、画面の更新を行う処理が記述されたクラス。
     */
    private class ReportListObserver implements Observer<List<Report>> {
        @Override
        public void onChanged(List<Report> reportList) {
            _adapter.changeReportList(reportList);
        }
    }

    /**
     * リサイクラービューで利用するビューホルダクラス。
     */
    private class ReportViewHolder extends RecyclerView.ViewHolder {
        /**
         * レポートID
         */
        public Integer id;
        /**
         * レポートタイトル表示用TextViewフィールド。
         */
        public TextView _tvRowTitle;
        /**
         * レポート日付表示用TextViewフィールド
         */
        public TextView _tvRowDate;
        /**
         * レポートのボタン
         */
        public Button _btRowEdit;

        /**
         * コンストラクタ。
         *
         * @param itemView リスト1行分の画面部品。
         */
        public ReportViewHolder(View itemView) {
            super(itemView);
            _tvRowTitle = itemView.findViewById(R.id.tvRowTitle);
            _tvRowDate = itemView.findViewById(R.id.tvRowDate);
            _btRowEdit = itemView.findViewById(R.id.btRowEdit);
        }

    }

    /**
     * リサイクラービューで利用するアダプタクラス。
     */
    private class ReportListAdapter extends RecyclerView.Adapter<ReportViewHolder> {
        /**
         * リストデータを表すフィールド。
         */
        private List<Report> _listData;

        /**
         * コンストラクタ。
         *
         * @param listData
         */
        public ReportListAdapter(List<Report> listData) {
            _listData = listData;
        }

        @Override
        public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View row = inflater.inflate(R.layout.row, parent, false);
            row.setOnClickListener(new ListItemClickListener());
            ReportViewHolder holder = new ReportViewHolder(row);
            return new ReportViewHolder(row);
        }

        @Override
        public void onBindViewHolder(ReportViewHolder holder, int position) {
            Report item = _listData.get(position);
            holder.id = item.id;
            holder._tvRowTitle.setText(Consts.CATEGORY[item.workkind]);
            holder._btRowEdit.setTag(item.id);
            holder._btRowEdit.setOnClickListener(new OnEditButtonClickListener());
            holder._tvRowDate.setText(item.workdate);
            TextView title = (TextView) holder._tvRowTitle;
            title.setTextColor(Color.parseColor(Consts.COLOR[item.workkind]));
            LinearLayout row = (LinearLayout) holder._tvRowTitle.getParent().getParent();
            row.setTag(item.id);
        }

        @Override
        public int getItemCount() {
            return _listData.size();
        }

        /**
         * 内部で保持しているリストデータを丸ごと入れ替えるメソッド。
         *
         * @param listData 新しいリストデータ。
         */
        public void changeReportList(List<Report> listData) {
            _listData = listData;
            notifyDataSetChanged();
        }
    }

    /**
     * リストをタップした時の処理が記述されたメンバクラス。
     */
    private class ListItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int idNo = (int) view.getTag();
            Intent intent = new Intent(getApplicationContext(), ReportDetailActivity.class);
            intent.putExtra("idNo", idNo);
            startActivity(intent);
        }
    }

    /*
    * 編集ボタンをタップした時の処理が記述されたメンバクラス。
     */
    private class OnEditButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            Button btRowEdit = (Button) view;
            int idNo = (Integer) btRowEdit.getTag();
            Intent intent = new Intent(getApplicationContext(), ReportEditActivity.class);
            intent.putExtra("mode", Consts.MODE_EDIT);
            intent.putExtra("idNo", idNo);
            intent.putExtra("activity", Consts.MainActivity);
            startActivity(intent);
        }
    }
}
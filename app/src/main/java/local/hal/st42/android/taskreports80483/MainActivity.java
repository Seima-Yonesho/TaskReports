package local.hal.st42.android.taskreports80483;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
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
    private ReportListViewModel _taskListViewModel;
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
            case R.id.menuListOptionInComplete:
                _menuCategory = Consts.INCOMPLETE;
                editor.putInt("TASK", _menuCategory);
                invalidateOptionsMenu();
                break;
            case R.id.menuListOptionAll:
                _menuCategory = Consts.ALL;
                editor.putInt("TASK", _menuCategory);
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
}
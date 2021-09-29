package local.hal.st42.android.taskreports80483.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import local.hal.st42.android.taskreports80483.Consts;
import local.hal.st42.android.taskreports80483.dataaccess.AppDatabase;
import local.hal.st42.android.taskreports80483.dataaccess.Report;
import local.hal.st42.android.taskreports80483.dataaccess.ReportDAO;

public class ReportListViewModel extends AndroidViewModel {
    /**
     * データベースオブジェクト。
     */
    private AppDatabase _db;

    /**
     * コンストラクタ。
     *
     * @param application アプリケーションオブジェクト。
     */
    public ReportListViewModel(Application application) {
        super(application);
        _db = AppDatabase.getDatabase(application);
    }

    /**
     * レポート情報リストを取得するメソッド。
     *
     * @param menuCategory
     * @return レポート情報リストに関連するLiveDateオブジェクト
     */
    public LiveData<List<Report>> getReportList(int menuCategory) {
        ReportDAO reportDAO = _db.reportDAO();
        LiveData<List<Report>> reportList;

        if (menuCategory == Consts.ALL){
            reportList = reportDAO.findAll();
        }
        else {
            reportList = reportDAO.findByWorkCategory(menuCategory);
        }

        return reportList;
    }
}

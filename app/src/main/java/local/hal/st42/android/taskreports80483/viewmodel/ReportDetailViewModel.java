package local.hal.st42.android.taskreports80483.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import local.hal.st42.android.taskreports80483.dataaccess.AppDatabase;
import local.hal.st42.android.taskreports80483.dataaccess.Report;
import local.hal.st42.android.taskreports80483.dataaccess.ReportDAO;

public class ReportDetailViewModel extends AndroidViewModel {
    /**
     * データベースオブジェクト。
     */
    private AppDatabase _db;

    /**
     * コンストラクタ。
     *
     * @param application アプリケーションオブジェクト。
     */
    public ReportDetailViewModel(Application application) {
        super(application);
        _db = AppDatabase.getDatabase(application);
    }

    /**
     * 引数の主キーに該当するレポート情報を取得するメソッド。
     *
     * @param id 主キー値。
     * @return 引数に該当するレポートオブジェクト。該当データが存在しない場合は、空のレポートオブジェクト。
     */
    public Report getReport(int id) {
        ReportDAO reportDAO = _db.reportDAO();
        ListenableFuture<Report> future = reportDAO.findByPK(id);
        Report report = new Report();
        try {
            report = future.get();
        }
        catch(ExecutionException ex) {
            Log.e("ReportEditViewModel", "データ取得処理失敗", ex);
        }
        catch(InterruptedException ex) {
            Log.e("ReportEditViewModel", "データ削除処理失敗", ex);
        }
        return report;
    }
}

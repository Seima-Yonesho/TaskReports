package local.hal.st42.android.taskreports80483;

public class Consts {
    /**
     * 新規登録モードを表す定数フィールド。
     */
    static final int MODE_INSERT = 11;
    /**
     * 更新モードを表す定数フィールド。
     */
    static final int MODE_EDIT = 12;
    /**
     * 実装を表す定数フィールド。
     */
    public static final int DEVELOP = 0;
    /**
     * 打ち合わせを表す定数フィールド。
     */
    public static final int MEETING = 1;
    /**
     * 資料作成を表す定数フィールド。
     */
    public static final int DOCUMENT = 2;
    /**
     * 顧客対応を表す定数フィールド。
     */
    public static final int SUPPORT = 3;
    /**
     * 設計を表す定数フィールド。
     */
    public static final int DESIGN = 4;
    /**
     * その他を表す定数フィールド。
     */
    public static final int OTHER = 5;
    /**
     * 全タスクを表す定数フィールド。
     */
    public static final int ALL = 6;
    /*
    * 作業種類を格納した定数配列
     */
    static final String[] CATEGORY = new String[]{"実装", "打ち合わせ", "資料作成", "顧客対応", "設計", "その他"};
    /*
    * 作業種類の色を格納した定数配列
     */
    static final String[] COLOR = new String[]{"#AEB404", "#0404B4", "#088A29", "#0489B1", "#B404AE", "#585858"};

    static final int MainActivity = 21;

    static final int DetailActivity = 22;
}

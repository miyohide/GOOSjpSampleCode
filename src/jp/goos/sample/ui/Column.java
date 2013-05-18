package jp.goos.sample.ui;

// テーブル内のカラムを表現する
public enum Column {
    ITEM_IDENTIFIER,
    LAST_PRICE,
    LAST_BID,
    SNIPER_STATUS;
    
    public static Column at(int offset) {
        return values()[offset];
    }

}

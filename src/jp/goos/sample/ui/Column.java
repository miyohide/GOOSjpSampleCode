package jp.goos.sample.ui;

// テーブル内のカラムを表現する

import actionsniper.SniperSnapshot;

public enum Column {
    ITEM_IDENTIFIER {
        @Override
        public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.itemId;
        }
    },
    LAST_PRICE {
        @Override
        public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.lastPrice;
        }
    },
    LAST_BID {
        @Override
        public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.lastBid;
        }
    },
    SNIPER_STATE {
        @Override
        public Object valueIn(SniperSnapshot snapshot) {
            // SnipersTableModelの外からステータスのテキスト文言を得るためにtextForをstatic宣言にしている
            return SnipersTableModel.textFor(snapshot.state);
        }
    };
    
    public static Column at(int offset) {
        return values()[offset];
    }

    abstract public Object valueIn(SniperSnapshot snapshot);
}

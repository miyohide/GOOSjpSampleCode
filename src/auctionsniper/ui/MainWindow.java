package auctionsniper.ui;

import auctionsniper.Item;
import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import auctionsniper.util.Announcer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class MainWindow extends JFrame {

    public static final String SNIPER_STATUS_NAME = "sniper status";
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String SNIPERS_TABLE_NAME = "Snipers Table";
    public static final String APPLICATION_TITLE = "Auction Sniper";
    public static final String NEW_ITEM_ID_NAME = "item id";
    public static final String NEW_ITEM_STOP_PRICE_NAME = "stop price";
    public static final String JOIN_BUTTON_NAME = "join button";
    private final Announcer<UserRequestListener> userRequests =
            Announcer.to(UserRequestListener.class);

    public MainWindow(SniperPortfolio portfolio) {
        super(APPLICATION_TITLE);
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable(portfolio), makeControls());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable, JPanel controls) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(controls, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable(SniperPortfolio portfolio) {
        SnipersTableModel model = new SnipersTableModel();
        portfolio.addPortfolioListener(model);
        final JTable snipersTable = new JTable(model);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }

    private JPanel makeControls() {
        JPanel controls = new JPanel(new FlowLayout());
        // 商品IDのためのテキストフィールド
        final JTextField itemIdField = new JTextField();
        itemIdField.setColumns(25);
        itemIdField.setName(NEW_ITEM_ID_NAME);
        controls.add(itemIdField);
        // 逆指値のためのテキストフィールド
        final JFormattedTextField stopPriceField = new JFormattedTextField();
        stopPriceField.setColumns(10);
        stopPriceField.setName(NEW_ITEM_STOP_PRICE_NAME);
        controls.add(stopPriceField);

        // 「Join Auction」ボタン
        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);

        joinAuctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Item item = new Item(itemId(), stopPrice());
                userRequests.announce().joinAuction(item);
            }

            private String itemId() {
                return itemIdField.getText();
            }

            private int stopPrice() {
                String stopPriceString = stopPriceField.getText();
                if (stopPriceString.equals("")) {
                    return Integer.MAX_VALUE;
                } else {
                    return Integer.valueOf(stopPriceString);
                }
            }
        });
        controls.add(joinAuctionButton);

        return controls;
    }

//    public void sniperStatusChanged(SniperSnapshot sniperSnapshot) {
//        snipers.sniperStateChanged(sniperSnapshot);
//    }
    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userRequests.addListener(userRequestListener);
    }
}

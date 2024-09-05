import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;

public class GameMenuBar extends MenuBar {
    private Callbacks mCallbacks;
    private Menu scoreMenu;

    /**回调接口*/
    public interface Callbacks {
        void afterRestart();//重新开始
        void afterResetGridSize(int cols,int rows);//重设表格尺寸
        void afterGetMoreScoreInfo();//获取更详细的分数信息
    }

    public GameMenuBar(Application application) {//application供回调方法使用
        mCallbacks=(Callbacks)application;
        //Game菜单
        Menu gameMenu=new Menu("游戏");//游戏
        MenuItem restartMenuItem=new MenuItem("重新开始");//重新开始
        restartMenuItem.setOnAction(e->mCallbacks.afterRestart());
        MenuItem exitMenuItem=new MenuItem("退出");//退出
        exitMenuItem.setOnAction(e->Platform.exit());
        gameMenu.getItems().addAll(restartMenuItem,exitMenuItem);

        //Setting菜单
        Menu settingMenu=new Menu("设置");//设置
        ToggleGroup tg=new ToggleGroup();//组

        RadioMenuItem r44MenuItem=new RadioMenuItem("尺寸:4x4");
        r44MenuItem.setOnAction(e->mCallbacks.afterResetGridSize(4,4));
        RadioMenuItem r55MenuItem=new RadioMenuItem("尺寸:5x5");
        r55MenuItem.setOnAction(e->mCallbacks.afterResetGridSize(5,5));
        RadioMenuItem r66MenuItem=new RadioMenuItem("尺寸:6x6");
        r66MenuItem.setOnAction(e->mCallbacks.afterResetGridSize(6 ,6));

        r44MenuItem.setToggleGroup(tg);
        r55MenuItem.setToggleGroup(tg);
        r66MenuItem.setToggleGroup(tg);
        settingMenu.getItems().addAll(r44MenuItem,r55MenuItem,r66MenuItem);
        r44MenuItem.setSelected(true);//默认选中4x4

        //Info菜单
        Menu infoMenu=new Menu("信息");//信息
        MenuItem helpMenuItem=new MenuItem("帮助");//帮助
        helpMenuItem.setOnAction(e->{
            Alert alert=new Alert(AlertType.INFORMATION);
            alert.setTitle(alert.getAlertType().toString());
            alert.setContentText("操作方式:\n"+
                    "向上滑动:键W\n"+
                    "向下滑动:键S\n"+
                    "向左滑动:键A\n"+
                    "向右滑动:键D\n"+
                    "\n游戏规则:\n"+
                    "相同数字的卡片在靠拢、相撞时会合并\n"+
                    "在操作中合并的卡片会以红色边框凸显\n尽可能获得更大的数字!");
            alert.show();
        });
        MenuItem aboutUsMenuItem=new MenuItem("关于我们");//关于我们
        aboutUsMenuItem.setOnAction(e->{
            Alert alert=new Alert(AlertType.INFORMATION);
            alert.setTitle(alert.getAlertType().toString());
            alert.setContentText("作者:软工2304\n感谢你的游玩!");
            alert.show();
        });
        infoMenu.getItems().addAll(helpMenuItem,aboutUsMenuItem);

        //Record菜单
        Menu recordMenu=new Menu("记录");//记录
        MenuItem historyScoreMenuItem=new MenuItem("历史分数");//历史分数
        historyScoreMenuItem.setOnAction(e->{
            Alert alert=new Alert(AlertType.INFORMATION);
            alert.setTitle(alert.getAlertType().toString());
            alert.setContentText("");
            alert.show();
        });
        recordMenu.getItems().addAll(historyScoreMenuItem);


        //Score菜单
        scoreMenu=new Menu("分数");//分数
        MenuItem moreScoreInfo=new MenuItem("更多分数信息");//更多分数信息
        moreScoreInfo.setOnAction(e->mCallbacks.afterGetMoreScoreInfo());
        scoreMenu.getItems().addAll(moreScoreInfo);
        getMenus().addAll(gameMenu,settingMenu,infoMenu,recordMenu,scoreMenu);
    }

    /**获取分数菜单*/
    public Menu getScoreMenu() {
        return scoreMenu;
    }
}
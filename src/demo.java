import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class demo extends Application implements GameMenuBar.Callbacks,CardMatrixPane.Callbacks {
    private BorderPane borderPane;
    private GameMenuBar menuBar;
    private CardMatrixPane cardMatrixPane;

    @Override
    public void start(Stage primaryStage) {
        borderPane=new BorderPane();
        Scene scene=new Scene(borderPane,1000,600);

        //Top菜单栏
        menuBar=new GameMenuBar(this);//创建菜单栏,并传入Application供调用
        borderPane.setTop(menuBar);//顶部

        //Center2048卡片矩阵
        cardMatrixPane=new CardMatrixPane(this);
        cardMatrixPane.setPadding(new Insets(5,5,5,5));//外边距
        borderPane.setCenter(cardMatrixPane);//中心

        primaryStage.setTitle("2048");
        primaryStage.setScene(scene);
        primaryStage.show();

        startGame();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    /**开始游戏*/
    private void startGame() {
        cardMatrixPane.requestFocus();//添加焦点
        cardMatrixPane.createKeyListener();//添加键盘监听
        afterScore();
    }

    @Override
    public void afterRestart() {
        cardMatrixPane.restartMatrix();
    }

    @Override
    public void afterResetGridSize(int cols,int rows) {
        cardMatrixPane=new CardMatrixPane(cols,rows,this);
        cardMatrixPane.setPadding(new Insets(5,5,5,5));//外边距
        borderPane.setCenter(cardMatrixPane);
        startGame();
//		cardMatrixPane.testColors();//颜色测试
    }

    @Override
    public void afterScore() {
        menuBar.getScoreMenu().setText("分数: "+cardMatrixPane.getScore());
    }

    @Override
    public void afterGetMoreScoreInfo() {
        int[] temp=cardMatrixPane.getMcQuantities();
        Alert alert=new Alert(AlertType.INFORMATION);
        alert.setTitle(alert.getAlertType().toString());
        alert.setContentText(
                "4的合并次数: 		"+temp[0]+"\n"+
                        "8的合并次数: 		"+temp[1]+"\n"+
                        "16的合并次数: 		"+temp[2]+"\n"+
                        "32的合并次数: 		"+temp[3]+"\n"+
                        "64的合并次数: 		"+temp[4]+"\n"+
                        "128的合并次数:		"+temp[5]+"\n"+
                        "256的合并次数:		"+temp[6]+"\n"+
                        "512的合并次数:		"+temp[7]+"\n"+
                        "1024的合并次数: 	"+temp[8]+"\n"+
                        "2048的合并次数: 	"+temp[9]+"\n"+
                        "4096的合并次数: 	"+temp[10]+"\n"+
                        "8192的合并次数: 	"+temp[11]+"\n"+
                        "16384的合并次数: 	"+temp[12]+"\n"+
                        "32768的合并次数: 	"+temp[13]+"\n"+
                        "65536的合并次数: 	"+temp[14]+"\n");
        alert.show();
    }
}

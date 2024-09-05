import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
public class CardMatrixPane extends StackPane{//卡片矩阵
    private Callbacks mCallbacks;
    private int cols;//矩阵列数
    private int rows;//矩阵行数
    private GridPane gridPane;
    private  cardPane[][] cps;//定义矩阵
    private long score = 0;//初始化分数
    private int[] mcQuantities=new int[15];

    public interface Callbacks{
        void afterRestart();

        void afterResetGridSize(int cols, int rows);

        void afterScore();//分数的变化
    }

    public CardMatrixPane(Application application){
        this(4,4,application);//默认4乘4
    }

    public CardMatrixPane(int cols,int rows,Application application){
        mCallbacks=(Callbacks)application;
        this.cols=cols;
        this.rows=rows;
        init();
        getChildren().add(gridPane);
    }

    public long getScore() {
        return score;
    }

    public int[] getMcQuantities() {
        return mcQuantities;
    }

    private  void init(){
        initGridpane();
        createRandomNumber();
    }

    private void initGridpane(){
        gridPane=new GridPane();
        widthProperty().addListener(ov->setGridSizeAndCardFont());//宽度变化,更新边长和字号
        heightProperty().addListener(ov->setGridSizeAndCardFont());//高度变化,更新边长和字号
        //单元格间隙
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        //绘制每个单元格
        cps=new cardPane[cols][rows];
        for(int i=0;i<cols;i++) {//遍历卡片矩阵的列
            for(int j=0;j<rows;j++) {//遍历卡片矩阵的行
                cardPane card=new cardPane(0);
                gridPane.add(card, i, j);
                cps[i][j]=card;
            }
        }
    }

    /*设置GridPane的边长,其内部单元格的尺寸和CardPane的字号*/
    private void setGridSizeAndCardFont(){
        double w=widthProperty().get();
        double h=heightProperty().get();
        double min= Math.min(w, h);
        gridPane.setMaxWidth(min);
        gridPane.setMaxHeight(min);
        for(int i=0;i<cols;i++) {//遍历卡片矩阵的列
            for(int j=0;j<rows;j++) {//遍历卡片矩阵的行
                cardPane card=cps[i][j];
                card.getLabel().setFont(new Font((min/14)/cols*4));//设置显示数字的尺寸
                card.setPrefWidth(min-5*(cols-1));//设置单元格内cardPane的宽度,否则它会随其内容变化,进而影响单元格宽度
                card.setPrefHeight(min-5*(rows-1));//设置单元格内cardPane的高度,否则它会随其内容变化,进而影响单元格高度
            }
        }
    }
    public void createKeyListener(){
        setOnKeyPressed(e->{
            cardPane maxCard = getMaxCard();
            if(maxCard.getType()==16){//最大数字
                Alert alert=new Alert(AlertType.INFORMATION);
                alert.setTitle(alert.getAlertType().toString());
                alert.setContentText("恭喜你,游戏的最大数字为"+maxCard.getNumber()+",可在菜单栏选择重新开始\n"+
                        "事实上,我们还尚未准备比"+maxCard.getNumber()+"更大的数字卡片,终点已至");
                alert.show();
                return;
            }
            KeyCode kc=e.getCode();
            switch(kc) {
                case UP:
                case W:
                    goUp();//↑
                    break;
                case DOWN:
                case S:
                    goDown();//↓
                    break;
                case LEFT:
                case A:
                    goLeft();//←
                    break;
                case RIGHT:
                case D:
                    goRight();//→
                    break;
                default:
                    return;//未定义的操作
            }
            redrawAllCardsAndResetIsMergeAndSetScore();//重绘所有的卡片,并重设合并记录,更新分数
            boolean isFull=!createRandomNumber();//生成新的随机数字卡片,并判满,这包含了生成数字后满的情况
            if(isFull) {//矩阵已满,可能已经游戏结束
                boolean testOpe=false;//是否还能进行横向或竖向操作
                testOpe|=testUp();//还能进行竖向操作
                testOpe|=testLeft();//还能进行横向操作
                if(!testOpe) {//游戏结束
                    Alert alert=new Alert(AlertType.INFORMATION);
                    alert.setTitle(alert.getAlertType().toString());
                    alert.setContentText("游戏结束,本次最大数字为"+maxCard.getNumber()+",可在菜单栏选择重新开始\n");
                    alert.show();
                }
            }
        });
    }

    private void goUp() {
        boolean mergeOrMove;
        do {
            mergeOrMove=false;
            for (int i=0;i<cols;i++){
                for(int j=1;j<rows;j++){
                    cardPane card=cps[i][j];
                    cardPane preCard = cps[i][j-1];
                    boolean isChanged=card.tryMerge(preCard);
                    mergeOrMove|=isChanged;
                }
            }
        }
        while (mergeOrMove);
    }
    private boolean testUp() {
        for(int i=0;i<cols;i++) {//遍历卡片矩阵的列
            for(int j=1;j<rows;j++) {//从第二行起向下,遍历卡片矩阵的行
                cardPane card=cps[i][j];
                cardPane preCard=cps[i][j-1];//前一个卡片
                if(card.canMerge(preCard)) {
                    return true;//能
                }
            }
        }
        return false;//不能
    }

    private boolean testLeft() {
        for(int i=1;i<cols;i++) {//遍历卡片矩阵的列
            for(int j=0;j<rows;j++) {//从第二行起向下,遍历卡片矩阵的行
                cardPane card=cps[i][j];
                cardPane preCard=cps[i-1][j];//前一个卡片
                if(card.canMerge(preCard)) {
                    return true;//能
                }
            }
        }
        return false;//不能
    }

    private void goDown() {
        boolean mergeOrMove;//矩阵的这次操作的一次遍历中是否存在移动或合并
        do {
            mergeOrMove=false;//初始为false
            for(int i=0;i<cols;i++) {//遍历卡片矩阵的列
                for(int j=rows-2;j>=0;j--) {//从倒数第二行起向上,遍历卡片矩阵的行
                    cardPane card=cps[i][j];
                    cardPane preCard=cps[i][j+1];//前一个卡片
                    boolean isChanged=card.tryMerge(preCard);//记录两张卡片间是否进行了移动或合并
                    mergeOrMove|=isChanged;//只要有一次移动或合并记录,就记存在为true
                }
            }
        }while(mergeOrMove);//如果存在移动或合并,就可能需要再次遍历,继续移动或合并
    }

    private void goLeft(){
        boolean mergeOrMove;
        do {
            mergeOrMove=false;
            for (int i=1;i<cols;i++){
                for(int j=0;j<rows;j++){
                    cardPane card=cps[i][j];
                    cardPane preCard = cps[i-1][j];
                    boolean isChanged=card.tryMerge(preCard);
                    mergeOrMove|=isChanged;
                }
            }
        }
        while (mergeOrMove);
    }
    private void goRight(){
        boolean mergeOrMove;
        do {
            mergeOrMove=false;
            for (int i=cols-2;i>=0;i--){
                for(int j=0;j<rows;j++){
                    cardPane card=cps[i][j];
                    cardPane preCard = cps[i+1][j];
                    boolean isChanged=card.tryMerge(preCard);
                    mergeOrMove|=isChanged;
                }
            }
        }
        while (mergeOrMove);
    }
    private void redrawAllCardsAndResetIsMergeAndSetScore() {
        for(int i=0;i<cols;i++) {//遍历卡片矩阵的列
            for(int j=0;j<rows;j++) {//遍历卡片矩阵的行
                cardPane card=cps[i][j];
                card.draw();
                if(card.isMerge()) {//这张卡片合并过
                    score+=card.getNumber();//计入分数
                    mcQuantities[card.getType()-2]++;//相应的合并过的卡片数字数量+1
                    card.setMerge(false);
                }
            }
        }
        mCallbacks.afterScore();
    }
    private  cardPane getMaxCard(){
        cardPane maxCard=new cardPane();
        for (int i=0;i<cols;i++){
            for (int j=0;j<rows;j++){
                cardPane card = cps[i][j];
                if(card.getType()>maxCard.getType()){
                    maxCard=card;
                }
            }
        }
        return maxCard;
    }
    public boolean createRandomNumber(){
        List<cardPane> voidCards=new ArrayList<>();
        for(int i=0;i<cols;i++) {//遍历卡片矩阵的列
            for(int j=0;j<rows;j++) {//遍历卡片矩阵的行
                cardPane card=cps[i][j];
                if(card.getType()==0) {//是空卡片
                    voidCards.add(card);//添加到列表中
                }
            }
        }
        int length= voidCards.size();
        if(length==0){
            return false;
        }
        int type;
        int index=(int)(Math.random()*5);//0,1,2,3,4
        if(index!=0) {//4/5概率
            type=1;//number=2
        }else {//1/5概率
            type=2;//number=4
        }
        int voidCardIndex=(int)(Math.random()*length);
        cardPane card=voidCards.get(voidCardIndex);
        card.setType(type);//更新type,生成数字
        card.draw();//重绘此卡片
        if(length==1) {//只有一个空卡片,矩阵生成数字后满
            return false;
        }

        return true;
    }
    public void restartMatrix() {
        for(int i=0;i<cols;i++) {//遍历卡片矩阵的列
            for(int j=0;j<rows;j++) {//遍历卡片矩阵的行
                cardPane card=cps[i][j];
                card.setType(0);
                card.draw();//重绘
            }
        }
        score=0;//重设分数
        mcQuantities=new int[15];//重设合并过的卡片数字数量
        mCallbacks.afterScore();
        createRandomNumber();
    }
}























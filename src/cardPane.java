import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
/*数字卡片类*/
public class cardPane extends BorderPane {
    private static final int RecAngle = 5;
    private int type;//类型
    private boolean merge = false;
    private Rectangle r;//矩形
    private Label l;//数字标签m
    public cardPane(){
        this(0);
    }
    public cardPane(int type){
        this.type = type;
        r = new Rectangle();
        r.widthProperty().bind(this.widthProperty());//矩形的宽度绑定单元格宽度
        r.heightProperty().bind(this.heightProperty());//矩形的高度绑定单元格高度
        r.setArcHeight(RecAngle);
        r.setArcWidth(RecAngle);//圆角宽高
        r.setStroke(Color.BLACK);
        r.setStrokeWidth(3);//边框宽度
        l = new Label("65536");
        setCenter(l);
        draw();
    }

    public Label getLabel(){
        return l;
    }

    public void setType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setMerge(boolean merge) {
        this.merge = merge;
    }

    public boolean isMerge() {
        return merge;
    }

    public void draw(){
        if(merge){
            r.setStroke(Color.RED);//如果合并为红色
        }else {
            r.setStroke(Color.BLACK);
        }
        r.setFill(cardColor.CC[type]);
        drawNumber();
    }
    public boolean canMerge(cardPane card){
        if (type==0){//空卡片
            return false;
        }
        if(card.type==0){//可以向空卡片移动
            return true;
        }
        return type==card.getType()&&!merge&&!card.isMerge();//不能二次合并
    }

    public boolean tryMerge(cardPane card){
        boolean canMerge=canMerge(card);
        if(canMerge){
            if(card.getType()==0){
                card.setType(type);//移动数字
                card.setMerge(merge);
                this.toVoid();
            }else{
                card.setType(card.getType()+1);//合并数字
                card.setMerge(true);
                this.toVoid();
            }
        }
        return canMerge;
    }
    private  void toVoid(){
        type = 0;
        merge = false;
    }
    public void drawNumber(){
        if(type==0){
            l.setText("");
        }else {
            l.setText(""+getNumber());
        }
    }

    public int getNumber(){
        return (int)Math.pow(2,type);
    }

    @Override
    public String toString(){
        return "[type="+type+", merge="+merge+"]";
    }
}

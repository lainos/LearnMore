package learnmore.yzx.com.learnmore.fruitannotation;

/**
 * Created by laino on 2017/6/21.
 */

public class Apple {

    @FruitName("Apple")
    private String appleName;

    @FruitColor(fruitColor = FruitColor.Color.BLUE)
    private String appleColor;


    public String getAppleColor() {
        return appleColor;
    }

    public void setAppleColor(String appleColor) {
        this.appleColor = appleColor;
    }


    public String getAppleName() {
        return appleName;
    }

    public void setAppleName(String appleName) {
        this.appleName = appleName;
    }
}

package learnmore.yzx.com.learnmore;


/**
 * Created by laino on 2017/5/17.
 */

public class LoginUtil {

    private String userName;
    private String passWord;
    public int loginType;

    public LoginUtil(String userName) {
        this.userName = userName;
    }

    public LoginUtil(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    private void runLogin() {
    }

    public void cancelLogin() {

    }

}

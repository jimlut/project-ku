package org.d3ifcool.timework;


public class Account {
    private  String mUsername;
    private  int mImage ;
    private  int mIsLogin;
    private String mQuote;


    /**Constructor of account
     *
     * @param mUsername is username for account
     * @param mImage is to save avatar image
     * @param mIsLogin is to check if user has been register
     * @param mQuote is to save quote from user
     */

    public Account(String mUsername, int mImage, int mIsLogin, String mQuote) {
        this.mUsername = mUsername;
        this.mImage = mImage;
        this.mIsLogin = mIsLogin;
        this.mQuote = mQuote;
    }



    public String getmUsername() {
        return mUsername;
    }

    public int getmImage() {
        return mImage;
    }

    public int getmIsLogin() {
        return mIsLogin;
    }

    public String getQuote() {
        return mQuote;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
    }

    public void setQuote(String quote) {
        this.mQuote = quote;
    }
}

package atc.Model;

public class Token {
    private boolean access;

    public Token(boolean access) {
        this.access = access;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }
}

package comp4920.mytummyisgrowling;

import java.io.Serializable;

public class RegisterResponse implements Serializable {

    private boolean error;
    private int id;
    private String errorMessage;

    public boolean getError() {
        return error;
    }


    public int getId() {
        return id;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}



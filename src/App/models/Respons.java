package App.models;

import App.helpers.database.annotations.DBCol;

public class Respons {
    @DBCol
    private Object answer;

    public Object getAnswer() {
        return answer;
    }
}

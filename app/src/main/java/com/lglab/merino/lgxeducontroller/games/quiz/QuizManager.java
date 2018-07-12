package com.lglab.merino.lgxeducontroller.games.quiz;

public class QuizManager {

    private static QuizManager instance = null;

    public static QuizManager getInstance() {
        if(instance == null)
            instance = new QuizManager();
        return instance;
    }

    private Quiz quiz;
    private boolean hasStarted = false;

    public void startQuiz(Quiz quiz){
        this.quiz = quiz;
    }

    public Quiz getQuiz() {
        return quiz;
    }
}

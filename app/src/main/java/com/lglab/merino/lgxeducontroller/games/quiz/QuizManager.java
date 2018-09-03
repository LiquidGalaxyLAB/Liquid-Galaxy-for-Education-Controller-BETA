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

    public boolean hasAnsweredAllQuestions() {
        boolean allAnswered = true;
        for(int i = 0; allAnswered && i < quiz.questions.size(); i++)
            allAnswered = quiz.questions.get(i).selectedAnswer != 0;

        return allAnswered;
    }

    public int correctAnsweredQuestionsCount() {
        int total = 0;
        for(int i = 0; i < quiz.questions.size(); i++)
            total += quiz.questions.get(i).selectedAnswer == quiz.questions.get(i).correctAnswer ? 1 : 0;
        return total;
    }
}

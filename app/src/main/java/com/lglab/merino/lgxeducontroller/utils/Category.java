package com.lglab.merino.lgxeducontroller.utils;

import com.lglab.merino.lgxeducontroller.games.quiz.Quiz;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Category extends ExpandableGroup<Quiz> {

    public long id;

    public Category(long id, String title, List<Quiz> items) {
        super(title, items);

        this.id = id;
    }
}

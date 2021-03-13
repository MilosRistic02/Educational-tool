package nl.tudelft.oopp.demo.controllers;

import java.util.Comparator;
import java.util.Date;
import nl.tudelft.oopp.demo.entities.Question;

public class QuestionComparator implements Comparator<Question> {
    @Override
    public int compare(Question o1, Question o2) {
        int isAnsweredComparison = compare(o2.isAnswered(), o1.isAnswered());
        if (isAnsweredComparison != 0) return isAnsweredComparison;

        int scoreComparison = compare(o2.getScore(), o1.getScore());
        // If scores are not the same, return the higher one first.
        if (scoreComparison != 0) {
            return scoreComparison;
        } else {
            return compare(o2.getCreationDate(), o1.getCreationDate());
        }
    }

    private int compare(boolean a, boolean b) {
        if (a && !b) {
            return -1;
        } else if (!a && b) {
            return 1;
        } else {
            return 0;
        }
    }

    private int compare(Date a, Date b) {
        return a.compareTo(b);
    }

    private int compare(int a, int b) {
        if (a < b) {
            return -1;
        } else if (a > b) {
            return 1;
        } else {
            return 0;
        }
    }
}

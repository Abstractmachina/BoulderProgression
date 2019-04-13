package android.taole.boulderprogression;

import android.widget.Toast;

public class Problem
{

    private Style style;
    private int numberOfMoves;
    private int angle;
    private int grade;

    final static private int INVALIDNUMCODE = -9999;

    Problem()
    {
    }

    Problem(int grade, Style style, int angle, int numberOfMoves)
    {
        this.grade = grade;
        this.style = style;

        if (!setAngle(angle))
        {
            this.angle = INVALIDNUMCODE;
        }

        if (!setNumberOfMoves(numberOfMoves))
        {
            this.numberOfMoves = INVALIDNUMCODE;
        }
    }


    //GETTERS SETTERS

    public Style getStyle() {return style;}
    public boolean setAngle(int angle)
    {
        if (angle > 90 || angle < -45) return false;
        this.angle = angle;
        return true;
    }

    public boolean setNumberOfMoves(int numberOfMoves)
    {
        if (numberOfMoves < 1) return false;

        this.numberOfMoves = numberOfMoves;
        return true;
    }

    public int getGrade()
    {
        return grade;
    }

    public int getAngle()
    {
        return angle;
    }

    public int getNumberOfMoves()
    {
        return numberOfMoves;
    }
}


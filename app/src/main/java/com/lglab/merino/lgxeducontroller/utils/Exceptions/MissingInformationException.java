package com.lglab.merino.lgxeducontroller.utils.Exceptions;

import com.lglab.merino.lgxeducontroller.R;
import android.content.Context;

public class MissingInformationException extends Exception {

    private String exception;

    public MissingInformationException(String msg) {
        super(msg);
        this.exception = msg;
    }

    @Override
    public String toString() {
        return "Missing the following input: " + this.exception;
    }
}

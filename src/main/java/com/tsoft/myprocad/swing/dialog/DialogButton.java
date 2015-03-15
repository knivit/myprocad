package com.tsoft.myprocad.swing.dialog;

import com.tsoft.myprocad.l10n.L10;

public enum DialogButton {
    OK(L10.get(L10.DIALOG_OK_BUTTON)),
    CANCEL(L10.get(L10.DIALOG_CANCEL_BUTTON)),
    YES(L10.get(L10.DIALOG_YES_BUTTON)),
    NO(L10.get(L10.DIALOG_NO_BUTTON)),
    SAVE(L10.get(L10.DIALOG_SAVE_BUTTON)),
    CLOSE(L10.get(L10.DIALOG_CLOSE_BUTTON)),
    ADD(L10.get(L10.DIALOG_ADD_BUTTON));

    private String text;

    DialogButton(String text) { this.text = text; }

    public String toString() { return text; }
}

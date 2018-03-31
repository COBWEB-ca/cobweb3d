package cobwebutil.swing;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.text.*;

public class JFormattedTextFieldPlus extends JFormattedTextField {

    private static final Color ERROR_BACKGROUND_COLOR = new Color(255, 215, 215);
    private static final Color ERROR_FOREGROUND_COLOR = null;

    private Color fBackground, fForeground;

    /**
     * Create a new {@code ImprovedFormattedTextField} instance which will use {@code aFormat} for the
     * validation of the user input.
     *
     * @param aFormat The format. May not be {@code null}
     */
    public JFormattedTextFieldPlus(Format aFormat) {
        //use a ParseAllFormat as we do not want to accept user input which is partially valid
        super(new ParseAllFormat(aFormat));
        setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        updateBackgroundOnEachUpdate();
        //improve the caret behavior
        //see also http://tips4java.wordpress.com/2010/02/21/formatted-text-field-tips/
        addFocusListener(new MousePositionCorrectorListener());
    }

    /**
     * Create a new {@code ImprovedFormattedTextField} instance which will use {@code aFormat} for the
     * validation of the user input. The field will be initialized with {@code aValue}.
     *
     * @param aFormat The format. May not be {@code null}
     * @param aValue  The initial value
     */
    public JFormattedTextFieldPlus(Format aFormat, Object aValue) {
        this(aFormat);
        setValue(aValue);
    }

    private void updateBackgroundOnEachUpdate() {
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateBackground();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateBackground();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateBackground();
            }
        });
    }

    /**
     * Update the background color depending on the valid state of the current input. This provides
     * visual feedback to the user
     */
    private void updateBackground() {
        boolean valid = validContent();
        if (ERROR_BACKGROUND_COLOR != null) {
            setBackground(valid ? fBackground : ERROR_BACKGROUND_COLOR);
        }
        if (ERROR_FOREGROUND_COLOR != null) {
            setForeground(valid ? fForeground : ERROR_FOREGROUND_COLOR);
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        fBackground = getBackground();
        fForeground = getForeground();
    }

    private boolean validContent() {
        AbstractFormatter formatter = getFormatter();
        if (formatter != null) {
            try {
                formatter.stringToValue(getText());
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setValue(Object value) {
        boolean validValue = true;
        //before setting the value, parse it by using the format
        try {
            AbstractFormatter formatter = getFormatter();
            if (formatter != null) {
                formatter.valueToString(value);
            }
        } catch (ParseException e) {
            validValue = false;
            updateBackground();
        }
        //only set the value when valid
        if (validValue) {
            int old_caret_position = getCaretPosition();
            super.setValue(value);
            setCaretPosition(Math.min(old_caret_position, getText().length()));
        }
    }

    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        //do not let the formatted text field consume the enters. This allows to trigger an OK button by
        //pressing enter from within the formatted text field
        if (validContent()) {
            return super.processKeyBinding(ks, e,
                    condition, pressed) && ks != KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        } else {
            return super.processKeyBinding(ks, e,
                    condition, pressed);
        }
    }

    private static class MousePositionCorrectorListener extends FocusAdapter {
        @Override
        public void focusGained(FocusEvent e) {
            /* After a formatted text field gains focus, it replaces its text with its
             * current value, formatted appropriately of course. It does this after
             * any focus listeners are notified. We want to make sure that the caret
             * is placed in the correct position rather than the dumb default that is
             * before the 1st character ! */
            final JTextField field = (JTextField) e.getSource();
            final int dot = field.getCaret().getDot();
            final int mark = field.getCaret().getMark();
            if (field.isEnabled() && field.isEditable()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // Only set the caret if the textfield hasn't got a selection on it
                        if (dot == mark) {
                            field.getCaret().setDot(dot);
                        }
                    }
                });
            }
        }
    }

    /**
     * <p>Decorator for a {@link Format Format} which only accepts values which can be completely parsed
     * by the delegate format. If the value can only be partially parsed, the decorator will refuse to
     * parse the value.</p>
     */
    public static class ParseAllFormat extends Format {
        private final Format fDelegate;

        /**
         * Decorate <code>aDelegate</code> to make sure if parser everything or nothing
         *
         * @param aDelegate The delegate format
         */
        public ParseAllFormat(Format aDelegate) {
            fDelegate = aDelegate;
        }

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return fDelegate.format(obj, toAppendTo, pos);
        }

        @Override
        public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
            return fDelegate.formatToCharacterIterator(obj);
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            int initialIndex = pos.getIndex();
            Object result = fDelegate.parseObject(source, pos);
            if (result != null && pos.getIndex() < source.length()) {
                int errorIndex = pos.getIndex();
                pos.setIndex(initialIndex);
                pos.setErrorIndex(errorIndex);
                return null;
            }
            return result;
        }

        @Override
        public Object parseObject(String source) throws ParseException {
            //no need to delegate the call, super will call the parseObject( source, pos ) method
            return super.parseObject(source);
        }
    }
}
package com.longforus.mvpautocodeplus.ui;

import com.intellij.ide.actions.ElementCreator;
import com.intellij.ide.actions.TemplateKindCombo;
import com.intellij.lang.LangBundle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import com.intellij.util.PlatformIcons;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateFileDialog extends DialogWrapper {
    private JTextField myNameField;
    private TemplateKindCombo myKindCombo;
    private JPanel myPanel;
    private JLabel myUpDownHint;
    private JLabel myKindLabel;
    private JLabel myNameLabel;

    private ElementCreator myCreator;
    private InputValidator myInputValidator;

    protected CreateFileDialog(@NotNull Project project) {
        super(project, true);
        // TODO: 2018/6/25 报空
        myKindLabel.setLabelFor(myKindCombo);
        myKindCombo.registerUpDownHint(myNameField);
        myUpDownHint.setIcon(PlatformIcons.UP_DOWN_ARROWS);
        setTemplateKindComponentsVisible(false);
        init();
    }

    public static Builder createDialog(@NotNull final Project project) {
        final CreateFileDialog dialog = new CreateFileDialog(project);
        return new BuilderImpl(dialog, project);
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (myInputValidator != null) {
            final String text = myNameField.getText().trim();
            final boolean canClose = myInputValidator.canClose(text);
            if (!canClose) {
                String errorText = LangBundle.message("incorrect.name");
                if (myInputValidator instanceof InputValidatorEx) {
                    String message = ((InputValidatorEx) myInputValidator).getErrorText(text);
                    if (message != null) {
                        errorText = message;
                    }
                }
                return new ValidationInfo(errorText, myNameField);
            }
        }
        return super.doValidate();
    }

    protected JTextField getNameField() {
        return myNameField;
    }

    protected TemplateKindCombo getKindCombo() {
        return myKindCombo;
    }

    protected JLabel getKindLabel() {
        return myKindLabel;
    }

    protected JLabel getNameLabel() {
        return myNameLabel;
    }

    private String getEnteredName() {
        final JTextField nameField = getNameField();
        final String text = nameField.getText().trim();
        nameField.setText(text);
        return text;
    }

    @Override
    protected JComponent createCenterPanel() {
        return myPanel;
    }

    @Override
    protected void doOKAction() {
        if (myCreator != null && myCreator.tryCreate(getEnteredName()).length == 0) {
            return;
        }
        super.doOKAction();
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return getNameField();
    }

    public void setTemplateKindComponentsVisible(boolean flag) {
        myKindCombo.setVisible(flag);
        myKindLabel.setVisible(flag);
        myUpDownHint.setVisible(flag);
    }

    public interface Builder {
        Builder setTitle(String title);

        Builder setValidator(InputValidator validator);

        Builder addKind(@NotNull String kind, @Nullable Icon icon, @NotNull String templateName);

        @Nullable
        <T extends PsiElement> T show(@NotNull String errorTitle, @Nullable String selectedItem, @NotNull FileCreator<T> creator);

        @Nullable
        Map<String, String> getCustomProperties();
    }

    public interface FileCreator<T> {

        @Nullable
        T createFile(@NotNull String name, @NotNull String templateName);

        @NotNull
        String getActionName(@NotNull String name, @NotNull String templateName);

        boolean startInWriteAction();
    }

    private static class BuilderImpl implements Builder {
        private final CreateFileDialog myDialog;
        private final Project myProject;

        public BuilderImpl(CreateFileDialog dialog, Project project) {
            myDialog = dialog;
            myProject = project;
        }

        @Override
        public Builder setTitle(String title) {
            myDialog.setTitle(title);
            return this;
        }

        @Override
        public Builder addKind(@NotNull String name, @Nullable Icon icon, @NotNull String templateName) {
            myDialog.getKindCombo().addItem(name, icon, templateName);
            if (myDialog.getKindCombo().getComboBox().getItemCount() > 1) {
                myDialog.setTemplateKindComponentsVisible(true);
            }
            return this;
        }

        @Override
        public Builder setValidator(InputValidator validator) {
            myDialog.myInputValidator = validator;
            return this;
        }

        @Override
        public <T extends PsiElement> T show(@NotNull String errorTitle, @Nullable String selectedTemplateName, @NotNull final FileCreator<T> creator) {
            final Ref<SmartPsiElementPointer<T>> created = Ref.create(null);
            myDialog.getKindCombo().setSelectedName(selectedTemplateName);
            myDialog.myCreator = new ElementCreator(myProject, errorTitle) {

                @Override
                protected PsiElement[] create(String newName) {
                    T element = creator.createFile(myDialog.getEnteredName(), myDialog.getKindCombo().getSelectedName());
                    if (element != null) {
                        created.set(SmartPointerManager.getInstance(myProject).createSmartPsiElementPointer(element));
                        return new PsiElement[] { element };
                    }
                    return PsiElement.EMPTY_ARRAY;
                }

                @Override
                public boolean startInWriteAction() {
                    return creator.startInWriteAction();
                }

                @Override
                protected String getActionName(String newName) {
                    return creator.getActionName(newName, myDialog.getKindCombo().getSelectedName());
                }
            };

            myDialog.show();
            if (myDialog.getExitCode() == OK_EXIT_CODE) {
                SmartPsiElementPointer<T> pointer = created.get();
                return pointer == null ? null : pointer.getElement();
            }
            return null;
        }

        @Nullable
        @Override
        public Map<String, String> getCustomProperties() {
            return null;
        }
    }
}
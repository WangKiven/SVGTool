package com.sckiven.svgtool.xutils;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.SourceTreeToPsiMap;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.sckiven.svgtool.common.android.Definitions;
import com.sckiven.svgtool.common.android.Utils;
import com.sckiven.svgtool.common.android.model.Element;
import com.sckiven.svgtool.xutils.form.EntryList;
import com.sckiven.svgtool.xutils.form.iface.ICancelListener;
import com.sckiven.svgtool.xutils.form.iface.IConfirmListener;
import com.sckiven.svgtool.xutils.info.IXUtils;
import kotlin.reflect.jvm.internal.impl.descriptors.annotations.KotlinTarget;
import kotlin.reflect.jvm.internal.impl.load.kotlin.KotlinClassFinder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;

public class XutilsInjectAction extends BaseGenerateAction implements IConfirmListener, ICancelListener {
    protected JFrame mDialog;
    protected static final Logger log = Logger.getInstance(XutilsInjectAction.class);

    public XutilsInjectAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    public XutilsInjectAction() {
        super(null);
    }

    @Override
    protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
//        return super.isValidForFile(project, editor, file);
        /*if (!(file instanceof PsiJavaFile) {
            return false;
        } else if (file instanceof PsiCompiledElement) {
            return false;
        } else {
            PsiClass targetClass = this.getTargetClass(editor, file);
            return targetClass != null && this.isValidForClass(targetClass);
        }*/
        /*if (!(file instanceof PsiJavaFile || file.getName().endsWith(".kt"))) {
            return false;
        } else if (file instanceof PsiCompiledElement) {
            return false;
        } else {
            String cn = file.getClass().getName();
            int offset = editor.getCaretModel().getOffset();
            PsiElement element = file.findElementAt(offset);
            if (element == null) {
                KotlinTarget
                return false;
            } else {
                PsiClass target = PsiTreeUtil.getParentOfType(element, PsiClass.class);
                if (target instanceof SyntheticElement) {
                    return false;
                }
            }



            PsiClass targetClass = this.getTargetClass(editor, file);
            if (targetClass == null || !isValidForClass(targetClass)) {
                return false;
            }
        }

        IXUtils butterKnife = XUtilsFactory.findXUtilsForPsiElement(project, file);

        return (butterKnife != null && super.isValidForFile(project, editor, file) && Utils.getLayoutFileFromCaret(editor, file) != null);*/

        return true;
    }

    /*@Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        actionPerformedImpl(project, editor);
//        super.actionPerformed(e);
    }*/

    /*@Override
    public void update(AnActionEvent e) {
//        super.update(e);
        //Get required data keys
        final Project project = e.getData(CommonDataKeys.PROJECT);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        //Set visibility only in case of existing project and editor and if some text in the editor is selected
        e.getPresentation().setVisible((project != null && editor != null
                && editor.getSelectionModel().hasSelection()));
    }*/

    @Override
    public void actionPerformedImpl(@NotNull Project project, Editor editor) {
//        super.actionPerformedImpl(project, editor);
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        PsiFile layout = Utils.getLayoutFileFromCaret(project, editor, file);

        if (layout == null) {
            Utils.showErrorNotification(project, "No layout found");
            return; // no layout found
        }

        log.info("Layout file: " + layout.getVirtualFile());

        ArrayList<Element> elements = Utils.getIDsFromLayout(layout);
        if (!elements.isEmpty()) {
            showDialog(project, editor, elements);
//            Messages.showMessageDialog(project, "OK，开始弹窗啦", "提示", Messages.getInformationIcon());
        } else {
            Utils.showErrorNotification(project, "No IDs found in layout");
        }
    }

    protected void showDialog(Project project, Editor editor, ArrayList<Element> elements) {
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (file == null) {
            return;
        }
        PsiClass clazz = getTargetClass(editor, file);
        KotlinClassFinder

        final IXUtils xutils = XUtilsFactory.findXUtilsForPsiElement(project, file);
        if (clazz == null || xutils == null) {
            return;
        }

        // get parent classes and check if it's an adapter
        boolean createHolder = false;
        PsiReferenceList list = clazz.getExtendsList();
        if (list != null) {
            for (PsiJavaCodeReferenceElement element : list.getReferenceElements()) {
                if (Definitions.adapters.contains(element.getQualifiedName())) {
                    createHolder = true;
                }
            }
        }

        // get already generated injections
        ArrayList<String> ids = new ArrayList<String>();
        PsiField[] fields = clazz.getAllFields();
        String[] annotations;
        String id;

        for (PsiField field : fields) {
            annotations = field.getFirstChild().getText().split(" ");

            for (String annotation : annotations) {
                id = Utils.getInjectionID(xutils, annotation.trim());
                if (!Utils.isEmptyString(id)) {
                    ids.add(id);
                }
            }
        }

        EntryList panel = new EntryList(project, editor, elements, ids, createHolder, this, this);

        mDialog = new JFrame();
        mDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mDialog.getRootPane().setDefaultButton(panel.getConfirmButton());
        mDialog.getContentPane().add(panel);
        mDialog.pack();
        mDialog.setLocationRelativeTo(null);
        mDialog.setVisible(true);
    }

    @Override
    public void onCancel() {
        closeDialog();
    }

    @Override
    public void onConfirm(Project project, Editor editor, ArrayList<Element> elements, String fieldNamePrefix, boolean createHolder, boolean splitOnclickMethods) {
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (file == null) {
            return;
        }
        PsiFile layout = Utils.getLayoutFileFromCaret(editor, file);

        closeDialog();


        if (Utils.getInjectCount(elements) > 0 || Utils.getClickCount(elements) > 0) { // generate injections
            new InjectWriter(file, getTargetClass(editor, file), "Generate Injections", elements, layout.getName(), fieldNamePrefix, createHolder, splitOnclickMethods).execute();
        } else { // just notify user about no element selected
            Utils.showInfoNotification(project, "No injection was selected");
        }
    }

    protected void closeDialog() {
        if (mDialog == null) {
            return;
        }

        mDialog.setVisible(false);
        mDialog.dispose();
    }
}

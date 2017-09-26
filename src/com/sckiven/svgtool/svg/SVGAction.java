package com.sckiven.svgtool.svg;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.impl.DirectoryInfoImpl;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.ui.docking.DockContainer;

import javax.swing.*;

/**
 *
 * Created by kiven on 2016/10/16.
 */
public class SVGAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        /*String userName = askForName(project);
        sayHello(project, userName);*/

        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(anActionEvent.getDataContext());
        if (file == null) {
            Messages.showMessageDialog(project, "请选择svg文件或svg文件所在的文件夹", "提示", Messages.getInformationIcon());
        } else if (file.isDirectory()) {
            PsiDirectory directory = (PsiDirectory) PsiManager.getInstance(project).findDirectory(file);
            PsiFile[] files = directory.getFiles();
            if (files == null || files.length == 0) {
                Messages.showMessageDialog(project, "您选择的空文件夹", "提示", Messages.getInformationIcon());
            } else {
                JDialog dialog = new DialogSVGShow();
                dialog.pack();
                dialog.setVisible(true);
            }
        } else {
            if (file.getName().endsWith(".xml")) {
//                Messages.showMessageDialog(project, "您选择的是xml文件", "提示", Messages.getInformationIcon());
                XmlFile xmlFile = (XmlFile) PsiManager.getInstance(project).findFile(file);

            } else {
                Messages.showMessageDialog(project, "请选择svg文件, 后缀名为xml", "提示", Messages.getInformationIcon());
            }
        }
    }

    /*@Override
    public void update(AnActionEvent e) {
        super.update(e);
        // 设置action的隐藏和可用状态
        getTemplatePresentation().setEnabledAndVisible(true);
    }*/

    private String askForName(Project project) {
        return Messages.showInputDialog(project,
                "你的名字是什么?", "请输入你的名字",
                Messages.getQuestionIcon());
    }

    private void sayHello(Project project, String userName) {
        Messages.showMessageDialog(project,
                String.format("你好, %s!\n 欢迎使用SVGTool.", userName), "提示",
                Messages.getInformationIcon());
    }
}

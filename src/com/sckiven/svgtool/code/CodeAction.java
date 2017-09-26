package com.sckiven.svgtool.code;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.List;

/**
 *
 * Created by kiven on 2016/10/26.
 */
public class CodeAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);
//        Messages.showMessageDialog(project, "您选择了CodeAction", "提示", Messages.getInformationIcon());

        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(anActionEvent.getDataContext());
        if (file == null) {
            Messages.showMessageDialog(project, "请选择文件或文件夹", "提示", Messages.getInformationIcon());
        } else {

            File file1 = new File(file.getPath());
            FileInfo fileInfo = new FileInfo(file1);
            if (file1.isDirectory()) {
                checkDir(fileInfo);
            } else {
                checkFile(fileInfo);
            }
            Messages.showMessageDialog(project, "空格计数 = " + fileInfo.spaceLineNumber + ", 非空行计数 = " + fileInfo.noSpaceLineNumber + ", 总计数 = " + (fileInfo.spaceLineNumber + fileInfo.noSpaceLineNumber), "提示", Messages.getInformationIcon());
        }
    }

    public void checkDir(FileInfo fileInfo) {
        if (fileInfo != null) {
            File[] paths = fileInfo.file.listFiles();
            if (paths != null && paths.length > 0) {

                FileInfo info;

                for (File file: paths) {
                    if (file != null) {
                        info = new FileInfo(file);
                        if (file.isDirectory()) {
                            checkDir(info);
                        } else {
                            String name = file.getName();
                            if (StringUtil.isEmpty(name)) {
                                continue;
                            } else {
                                if (name.endsWith(".java") || name.endsWith(".xml"))
                                    checkFile(info);
                                else
                                    continue;
                            }
                        }

                        fileInfo.spaceLineNumber += info.spaceLineNumber;
                        fileInfo.noSpaceLineNumber += info.noSpaceLineNumber;
                    }
                }
            }
        }
    }

    public void checkFile(FileInfo fileInfo) {
        LineNumberReader lineNumberReader = null;
        try {
            lineNumberReader = new LineNumberReader(new FileReader(fileInfo.file));
            if (lineNumberReader != null) {

                String lineRead = null;
                while ((lineRead = lineNumberReader.readLine()) != null) {
                    if (StringUtil.isEmptyOrSpaces(lineRead)) {
                        fileInfo.spaceLineNumber++;
                    } else {
                        fileInfo.noSpaceLineNumber++;
                    }
                }
            }

        } catch (Exception e) {
            System.out.print("ERROR: Read " + fileInfo.file.getPath());
        }
    }

    class FileInfo {
        File file;
        int spaceLineNumber;
        int noSpaceLineNumber;
        int createTime;
        int updateTime;

        public FileInfo(File file) {
            this.file = file;
        }
    }
}

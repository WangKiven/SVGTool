package com.sckiven.svgtool.svg;

import com.intellij.psi.PsiFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiven on 2016/10/16.
 */
public class SVGTool {
    /**
     * 提取svg文件
     * @param psiFiles
     * @return
     */
    public List<PsiFile> hasSVGXml(PsiFile[] psiFiles) {
        List<PsiFile> files = new ArrayList<PsiFile>(psiFiles == null? 0: psiFiles.length);
        if (psiFiles != null) {
            for (PsiFile psiFile : psiFiles) {
                if (isSVGXml(psiFile)) {
                    files.add(psiFile);
                }
            }
        }
        return files;
    }

    /**
     * 是否是svg文件
     */
    public boolean isSVGXml(PsiFile psiFile) {
        if (psiFile.getName().endsWith(".xml")) {

        }
        return false;
    }
}

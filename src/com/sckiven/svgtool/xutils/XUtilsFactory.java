package com.sckiven.svgtool.xutils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.sckiven.svgtool.common.android.Utils;
import com.sckiven.svgtool.xutils.info.IXUtils;
import com.sckiven.svgtool.xutils.info.XUtils1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class XUtilsFactory {
    /**
     * List of supported ButterKnifes.
     * Note: The ordering corresponds to the preferred ButterKnife versions.
     */
    private static IXUtils[] sSupportedXUtils = new IXUtils[]{
            new XUtils1()
    };

    private XUtilsFactory() {
        // no construction
    }

    /**
     * Find ButterKnife that is available for given {@link PsiElement} in the {@link Project}.
     * Note that it check if ButterKnife is available in the module.
     *
     * @param project    Project
     * @param psiElement Element for which we are searching for ButterKnife
     * @return ButterKnife
     */
    @Nullable
    public static IXUtils findXUtilsForPsiElement(@NotNull Project project, @NotNull PsiElement psiElement) {
        for (IXUtils butterKnife : sSupportedXUtils) {
            if (Utils.isClassAvailableForPsiFile(project, psiElement, butterKnife.getDistinctClassName())) {
                return butterKnife;
            }
        }
        // we haven't found any version of ButterKnife in the module, let's fallback to the whole project
        return findXUtilsForProject(project);
    }

    /**
     * Find ButterKnife that is available in the {@link Project}.
     *
     * @param project Project
     * @return ButterKnife
     * @since 1.3.1
     */
    @Nullable
    private static IXUtils findXUtilsForProject(@NotNull Project project) {
        for (IXUtils butterKnife : sSupportedXUtils) {
            if (Utils.isClassAvailableForProject(project, butterKnife.getDistinctClassName())) {
                return butterKnife;
            }
        }
        return null;
    }

    public static IXUtils[] getSupportedXUtils() {
        return sSupportedXUtils;
    }
}

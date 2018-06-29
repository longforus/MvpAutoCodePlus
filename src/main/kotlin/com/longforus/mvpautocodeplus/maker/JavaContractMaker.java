package com.longforus.mvpautocodeplus.maker;

import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * Created by XQ Yang on 2018/6/26  14:12.
 * Description :
 */
@Deprecated
class JavaContractMaker implements IMakeContract {

    @NotNull
    @Override
    public String getName() {
        return null;
    }

    @NotNull
    @Override
    public PsiDirectory getDir() {
        return null;
    }

    @Override
    public boolean isJava() {
        return false;
    }

    @Override
    public void checkExits() {

    }

    @NotNull
    @Override
    public PsiFile makeContract() {
        return null;
    }
}

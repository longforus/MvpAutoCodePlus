package com.longforus.mvpautocodeplus;

/**
 * Created by XQ Yang on 2018/6/28  11:02.
 * Description : 模版常量
 */

public interface TemplateCons {

    String CONTRACT_TP_CONTENT_JAVA =
        "#if (${PACKAGE_NAME} != \"\")package ${PACKAGE_NAME};#end\n" + "\n" + "import ${V};\n" + "import ${P};\n" + "import ${M};\n" + "/**\n" + " * Description : \n" +
            " * @author  ${USER}\n" + " * @date ${DATE}  ${TIME}\n" + " * \t\t\t\t\t\t\t\t - generate by MvpAutoCodePlus plugin.\n" + " */\n" + "\n" +
            "public interface I${NAME}Contract {\n" + "interface View extends IView${VG}{}\n" + "interface Presenter extends IPresenter${PG}{}\n" +
            "interface Model extends IModel${MG}{}\n" + "}\n";

    String CONTRACT_TP_CONTENT_KOTLIN =
        "#if (${PACKAGE_NAME} != \"\")package ${PACKAGE_NAME}#end\n" + "\n" + "import ${V}\n" + "import ${P}\n" + "import ${M}\n" + "/**\n" + " * Description : \n" +
            " * @author  ${USER}\n" + " * @date ${DATE}  ${TIME}\n" + " * \t\t\t\t\t\t\t\t - generate by MvpAutoCodePlus plugin.\n" + " */\n" + "\n" + "interface ${NAME} {\n" +
            "    interface View : IView${VG}{}\n" + "    interface Presenter : IPresenter${PG}{}\n" + "    interface Model : IModel${MG}{}\n" + "}\n";

    String COMMON_IMPL_TP_CONTENT_JAVA =
        "#if (${PACKAGE_NAME} != \"\")package ${PACKAGE_NAME};#end\n" + "\n" + "import ${CONTRACT};\n" + "#if (${IMPL} != \"\")import ${IMPL};#end\n" + "\n" + "\n" + "/**\n" +
            " * Description : \n" + " * @author  ${USER}\n" + " * @date ${DATE}  ${TIME}\n" + " * \t\t\t\t\t\t\t\t - generate by MvpAutoCodePlus plugin.\n" + " */\n" + "\n" +
            "public class ${NAME}${IMPL_TYPE} #if(${IMPL}!=\"\") extends ${IMPL}${VG}#end implements I${NAME}Contract.${TYPE} {\n" + "\n" + "}\n" + "\n";
    String COMMON_IMPL_TP_CONTENT_KOTLIN =
        "#if (${PACKAGE_NAME} != \"\")package ${PACKAGE_NAME}#end\n" + "\n" + "import ${CONTRACT}\n" + "#if (${IMPL} != \"\")import ${IMPL}#end\n" + "\n" + "/**\n" +
            " * Description : \n" + " * @author  ${USER}\n" + " * @date ${DATE}  ${TIME}\n" + " * \t\t\t\t\t\t\t\t - generate by MvpAutoCodePlus plugin.\n" + " */\n" + "\n" +
            "class ${NAME}${IMPL_TYPE} :#if (${IMPL_NP} != \"\") ${IMPL_NP}${VG}(),#end ${CONTRACT_NP}.${TYPE} {\n" + "\n" + "}\n" + "\n";
}

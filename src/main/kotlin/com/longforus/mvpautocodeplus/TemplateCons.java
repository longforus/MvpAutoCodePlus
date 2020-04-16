package com.longforus.mvpautocodeplus;

/**
 * Created by XQ Yang on 2018/6/28  11:02.
 * @describe 模版常量
 */

public interface TemplateCons {

    String CONTRACT_TP_CONTENT_JAVA =
        "#if (${PACKAGE_NAME} != \"\")package ${PACKAGE_NAME};#end\n" + "\n" + "import ${V};\n" + "import ${P};\n" + "import ${M};\n" + "/**\n" + " * @describe \n" +
            " * @author  ${USER}\n" + " * @date ${DATE}  ${TIME}\n" + " * \t\t\t\t\t\t\t\t - generate by MvpAutoCodePlus plugin.\n" + " */\n" + "\n" +
            "public interface I${NAME}Contract {\n" + "    interface View extends ${VN}${VG}{}\n" + "    interface ${P_OR_M} extends ${PN}${PG}{}\n" +
            "    interface Model extends ${MN}${MG}{}\n" + "}\n";
    String CONTRACT_TP_CONTENT_NO_MODEL_JAVA =
        "#if (${PACKAGE_NAME} != \"\")package ${PACKAGE_NAME};#end\n" + "\n" + "import ${V};\n" + "import ${P};\n" + "/**\n" + " * @describe \n" + " * @author  ${USER}\n" +
            " * @date ${DATE}  ${TIME}\n" + " * \t\t\t\t\t\t\t\t - generate by MvpAutoCodePlus plugin.\n" + " */\n" + "\n" + "public interface I${NAME}Contract {\n" +
            "    interface View extends ${VN}${VG}{}\n" + "    interface ${P_OR_M} extends ${PN}${PG}{}\n" + "}\n";

    String CONTRACT_TP_CONTENT_KOTLIN =
        "#if (${PACKAGE_NAME} != \"\")package ${PACKAGE_NAME}#end\n" + "\n" + "import ${V}\n" + "import ${P}\n" + "import ${M}\n" + "/**\n" + " * @describe \n" +
            " * @author  ${USER}\n" + " * @date ${DATE}  ${TIME}\n" + " * \t\t\t\t\t\t\t\t - generate by MvpAutoCodePlus plugin.\n" + " */\n" + "\n" + "interface ${NAME} {\n" +
            "    interface View : ${VN}${VG}{}\n" + "    interface ${P_OR_M} : ${PN}${PG}{}\n" + "    interface Model : ${MN}${MG}{}\n" + "}\n";
    String CONTRACT_TP_CONTENT_NO_MODEL_KOTLIN =
        "#if (${PACKAGE_NAME} != \"\")package ${PACKAGE_NAME}#end\n" + "\n" + "import ${V}\n" + "import ${P}\n" + "/**\n" + " * @describe \n" + " * @author  ${USER}\n" +
            " * @date ${DATE}  ${TIME}\n" + " * \t\t\t\t\t\t\t\t - generate by MvpAutoCodePlus plugin.\n" + " */\n" + "\n" + "interface ${NAME} {\n" +
            "    interface View : ${VN}${VG}{}\n" + "    interface ${P_OR_M} : ${PN}${PG}{}\n" + "}\n";

    String COMMON_IMPL_TP_CONTENT_JAVA =
        "#if (${PACKAGE_NAME} != \"\")package ${PACKAGE_NAME};#end\n" + "\n" + "import ${CONTRACT};\n" + "import ${PACKAGE_NAME}.R;\n" + "#if (${IMPL} != \"\")import ${IMPL};#end\n" + "\n" + "\n" + "/**\n" +
            " * @describe \n" + " * @author  ${USER}\n" + " * @date ${DATE}  ${TIME}\n" + " * \t\t\t\t\t\t\t\t - generate by MvpAutoCodePlus plugin.\n" + " */\n" + "\n" +
            "public class ${NAME}${IMPL_TYPE} #if(${IMPL}!=\"\") extends ${IMPL}${VG}#end implements I${NAME}Contract.${TYPE} {\n" + "\n" + "}\n" + "\n";

    //todo R文件的报名应该是清单里面的报名

    String COMMON_IMPL_TP_CONTENT_KOTLIN =
        "#if (${PACKAGE_NAME} != \"\")package ${PACKAGE_NAME}#end\n" + "\n" + "import ${CONTRACT}\n" + "import ${PACKAGE_NAME}.R\n" + "#if (${IMPL} != \"\")import ${IMPL}#end\n" + "\n" + "/**\n" +
            " * @describe \n" + " * @author  ${USER}\n" + " * @date ${DATE}  ${TIME}\n" + " * \t\t\t\t\t\t\t\t - generate by MvpAutoCodePlus plugin.\n" + " */\n" + "\n" +
            "class ${NAME}${IMPL_TYPE} :#if (${IMPL_NP} != \"\") ${IMPL_NP}${VG}(),#end ${CONTRACT_NP}.${TYPE} {\n" + "\n" + "}\n" + "\n";
}

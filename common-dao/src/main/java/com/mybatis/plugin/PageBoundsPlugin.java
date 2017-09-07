/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.mybatis.plugin;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.*;

/**
 * @author tjwang
 * @version $Id: PageBoundsPlugin.java, v 0.1 2017/4/21 0021 17:20 tjwang Exp $
 */
public class PageBoundsPlugin extends PluginAdapter {

    private static final String                        PAGE_BOUNDS       = "pageBounds";
    private static final String                        PAGE_BOUNDS_CLASS = "com.mybatis.domain.PageBounds";

    private FullyQualifiedJavaType                     pageBounds;
    private Map<FullyQualifiedTable, List<XmlElement>> elementsToAdd;

    public PageBoundsPlugin() {
        pageBounds = new FullyQualifiedJavaType(PAGE_BOUNDS_CLASS);
        elementsToAdd = new HashMap<>();
    }

    public static void generate() {
        String config = ShellRunner.class.getClassLoader().getResource("generatorConfig.xml").getFile();
        String[] arg = { "-configfile", config, "-overwrite" };
        ShellRunner.main(arg);
    }

    public static void main(String[] args) {
        generate();
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            copyAndAddMethod(method, interfaze);
        }
        return true;
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            copyAndSaveElement(element, introspectedTable.getFullyQualifiedTable());
        }
        return true;
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            copyAndAddMethod(method, interfaze);
        }
        return true;
    }

    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            copyAndSaveElement(element, introspectedTable.getFullyQualifiedTable());
        }
        return true;
    }

    /**
     * 将之前添加的元素添加到xml中。
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        List<XmlElement> elements = elementsToAdd.get(introspectedTable.getFullyQualifiedTable());
        if (elements != null) {
            for (XmlElement element : elements) {
                document.getRootElement().addElement(element);
            }
        }

        return true;
    }

    /**
     * 新增一个selectByExampleWithPageBounds方法
     *
     * @param method
     * @param interfaze
     */
    private void copyAndAddMethod(Method method, Interface interfaze) {
        Method newMethod = new Method(method);
        newMethod.setName(method.getName() + "WithPageBounds"); //$NON-NLS-1$
        newMethod.addParameter(new Parameter(pageBounds, "pageBounds")); //$NON-NLS-1$
        interfaze.addMethod(newMethod);
        interfaze.addImportedType(pageBounds);
    }

    /**
     * 在sqlmap中创建selectByExampleWithPageBounds对应的sql查询语句
     *
     * @param element
     * @param fqt
     */
    private void copyAndSaveElement(XmlElement element, FullyQualifiedTable fqt) {
        XmlElement newElement = new XmlElement(element);

        // remove old id attribute and add a new one with the new name
        for (Iterator<Attribute> iterator = newElement.getAttributes().iterator(); iterator.hasNext();) {
            Attribute attribute = iterator.next();
            if ("id".equals(attribute.getName())) { //$NON-NLS-1$
                iterator.remove();
                Attribute newAttribute = new Attribute("id", attribute.getValue() + "WithPageBounds"); //$NON-NLS-1$ //$NON-NLS-2$
                newElement.addAttribute(newAttribute);
                break;
            }
        }

        // 将新元素保存在本地，稍后将其添加到document中
        List<XmlElement> elements = elementsToAdd.get(fqt);
        if (elements == null) {
            elements = new ArrayList<>();
            elementsToAdd.put(fqt, elements);
        }
        elements.add(newElement);
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }
}
